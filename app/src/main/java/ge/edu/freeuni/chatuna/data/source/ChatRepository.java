package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.data.source.local.ChatLocalDataSource;
import ge.edu.freeuni.chatuna.model.HistoryModel;
import ge.edu.freeuni.chatuna.model.MessageModel;

public class ChatRepository implements ChatDataSource {
    private volatile static ChatRepository INSTANCE = null;

    private ChatLocalDataSource chatLocalDataSource;

    private ChatRepository(@NonNull ChatLocalDataSource chatLocalDataSource) {
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
    public void getUserIdByName(String name, GetIdCallback callback) {
        chatLocalDataSource.getUserIdByName(name, new GetIdCallback() {
            @Override
            public void onIdLoaded(long id) {
                callback.onIdLoaded(id);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getSingleChatById(long userId, GetSingleChatCallback callback) {
        chatLocalDataSource.getSingleChatById(userId, new GetSingleChatCallback() {
            @Override
            public void onSingleChatLoaded(List<MessageModel> chat) {
                callback.onSingleChatLoaded(chat);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getHistory(long id, @NonNull GetHistoryCallback callback) {
        chatLocalDataSource.getHistory(id, new GetHistoryCallback() {
            @Override
            public void onHistoryLoaded(List<HistoryModel> histories) {
                callback.onHistoryLoaded(histories);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveUser(@NonNull final User user, @NonNull final InsertUserCallback callback) {
        chatLocalDataSource.saveUser(user, id -> callback.onUserInserted(id));
    }

    @Override
    public void saveMessage(@NonNull Message message) {
        chatLocalDataSource.saveMessage(message);
    }

    @Override
    public void deleteHistoryByPeerIds(long peerId) {
        chatLocalDataSource.deleteHistoryByPeerIds(peerId);
    }

    @Override
    public void deleteAll() {
        chatLocalDataSource.deleteAll();
    }
}
