package ge.edu.freeuni.chatuna.data.source.local;

import androidx.annotation.NonNull;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.ChatDataSource;
import ge.edu.freeuni.chatuna.model.HistoryModel;
import ge.edu.freeuni.chatuna.utils.AppExecutors;

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
    public void getHistory(long id, @NonNull final GetHistoryCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<HistoryModel> histories = chatDao.getHistory(id);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (histories.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onHistoryLoaded(histories);
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
    public void deleteHistoryByPeerIds(long hostId, long peerId) {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                chatDao.deleteHistoryByPeerIds(hostId, peerId);
            }
        };

        appExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void deleteAll() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                chatDao.deleteAll();
            }
        };

        appExecutors.diskIO().execute(deleteRunnable);
    }
}
