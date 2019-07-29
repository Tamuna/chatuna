package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.local.ChatDatabase;
import ge.edu.freeuni.chatuna.data.source.local.ChatLocalDataSource;

public class ChatRepository implements ChatDataSource {
    private volatile static ChatRepository INSTANCE = null;

    private ChatLocalDataSource chatLocalDataSource;

    private ChatRepository( @NonNull ChatLocalDataSource chatLocalDataSource) {
        this.chatLocalDataSource = chatLocalDataSource;
    }

    public static ChatRepository getInstance(ChatLocalDataSource toDosLocalDataSource) {
        if (INSTANCE == null) {
            synchronized (ChatRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChatRepository(toDosLocalDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void saveUser(@NonNull final User user, @NonNull final InsertUserCallback callback) {
        chatLocalDataSource.saveUser(user, new InsertUserCallback() {
            @Override
            public void onUserInserted(long id) {
                callback.onUserInserted(id);
            }
        });
    }

    @Override
    public void saveMessage(@NonNull Message message) {
        chatLocalDataSource.saveMessage(message);
    }
}
