package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.model.HistoryModel;
import ge.edu.freeuni.chatuna.model.MessageModel;

public interface ChatDataSource {
    interface InsertUserCallback {
        void onUserInserted(long id);
    }

    interface GetHistoryCallback {
        void onHistoryLoaded(List<HistoryModel> histories);

        void onDataNotAvailable();
    }

    interface GetSingleChatCallback {
        void onSingleChatLoaded(List<MessageModel> chat);

        void onDataNotAvailable();
    }

    interface GetIdCallback {
        void onIdLoaded(long id);

        void onDataNotAvailable();
    }

    void getUserIdByName(String name, GetIdCallback callback);

    void getSingleChatById(long userId, GetSingleChatCallback callback);

    void getHistory(long id, @NonNull final GetHistoryCallback callback);

    void saveUser(@NonNull User user, @NonNull InsertUserCallback callback);

    void saveMessage(@NonNull Message message);

    void deleteHistoryByPeerIds(long peerId);

    void deleteAll();
}
