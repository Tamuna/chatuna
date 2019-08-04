package ge.edu.freeuni.chatuna.data.source.local;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.ChatDataSource;
import ge.edu.freeuni.chatuna.model.HistoryModel;
import ge.edu.freeuni.chatuna.model.MessageModel;
import ge.edu.freeuni.chatuna.utils.AppExecutors;
import ge.edu.freeuni.chatuna.utils.DateUtils;

public class ChatLocalDataSource implements ChatDataSource {
    private static volatile ChatLocalDataSource INSTANCE;

    private ChatDao chatDao;

    private AppExecutors appExecutors;

    private ChatLocalDataSource(@NonNull AppExecutors appExecutors,
                                @NonNull ChatDao chatDao) {
        this.appExecutors = appExecutors;
        this.chatDao = chatDao;
    }

    public static ChatLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                   @NonNull ChatDao chatDao) {
        if (INSTANCE == null) {
            synchronized (ChatLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChatLocalDataSource(appExecutors, chatDao);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getUserIdByName(final String name, final GetIdCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final long id = chatDao.getUserIdByName(name);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onIdLoaded(id);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getSelf(final GetSelfCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final User user = chatDao.getSelf();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            callback.onSelfLoaded(user);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getSingleChatById(long userId, GetSingleChatCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<MessageModel> chat = chatDao.getSingleChatById(userId);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (chat.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onSingleChatLoaded(chat);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getHistory(long id, @NonNull final GetHistoryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Message> histories = chatDao.getHistory();
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        Map<Long, Integer> counts = new HashMap<>();
                        Map<Long, Date> maxDate = new HashMap<>();

                        for (int i = 0; i < histories.size(); i++) {
                            Long currentPeerId;
                            if (histories.get(i).getSenderUserId() == id) {
                                currentPeerId = histories.get(i).getReceiverUserId();
                            } else {
                                currentPeerId = histories.get(i).getSenderUserId();
                            }
                            if (counts.containsKey(currentPeerId)) {
                                counts.put(currentPeerId, counts.get(currentPeerId) + 1);
                                maxDate.put(currentPeerId, DateUtils.max(maxDate.get(currentPeerId), histories.get(i).getCreateDate()));
                            } else {
                                counts.put(currentPeerId, 1);
                                maxDate.put(currentPeerId, histories.get(i).getCreateDate());
                            }
                        }

                        List<HistoryModel> result = new ArrayList<>();
                        for (Long id : maxDate.keySet()) {
                            String name = chatDao.getUsernameById(id);
                            result.add(new HistoryModel(name, counts.get(id), maxDate.get(id).toString()));
                        }

                        if (result.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onHistoryLoaded(result);
                        }
                    }
                });
            }
        };

        appExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveUser(@NonNull User user, @NonNull InsertUserCallback callback) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                final long id = chatDao.insertUser(user);


                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onUserInserted(id);
                    }
                });
            }
        };

        appExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void saveMessage(@NonNull Message message) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                chatDao.insertMessage(message);
            }
        };

        appExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void deleteHistoryByPeerIds(long peerId) {
        Runnable deleteRunnable = () -> chatDao.deleteHistoryByPeerIds(peerId);

        appExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteAll() {
        Runnable deleteRunnable = () -> chatDao.deleteAll();

        appExecutors.diskIO().execute(deleteRunnable);
    }
}
