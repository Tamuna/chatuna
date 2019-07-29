package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.source.local.ChatLocalDataSource;

public class ChatRepository {
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
}
