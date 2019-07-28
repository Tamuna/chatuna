package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.source.local.ChatLocalDataSource;

public class ChatRepository {
    private volatile static ChatRepository INSTANCE = null;

    private ChatLocalDataSource chatLocalDataSource;

    private ChatRepository( @NonNull ChatLocalDataSource chatLocalDataSource) {
        this.chatLocalDataSource = chatLocalDataSource;
    }
}
