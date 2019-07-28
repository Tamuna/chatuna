package ge.edu.freeuni.chatuna.data.source.local;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.ChatDataSource;
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
    public void saveUser(@NonNull User user, @NonNull InsertUserCallback callback) {
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                final int id = chatDao.insertUser(user);

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
}
