package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;
import androidx.room.Query;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.model.HistoryModel;

public interface ChatDataSource {
    interface InsertUserCallback {
        void onUserInserted(long id);
    }

    interface GetHistoryCallback {
        void onHistoryLoaded(List<HistoryModel> histories);

        void onDataNotAvailable();
    }

    interface GetIdCallback {
        void onIdLoaded(long id);

        void onDataNotAvailable();
    }

    void getUserIdByName(String name, GetIdCallback callback);

    void getHistory(long id, @NonNull final GetHistoryCallback callback);

    void saveUser(@NonNull User user, @NonNull InsertUserCallback callback);

    void saveMessage(@NonNull Message message);

    void deleteHistoryByPeerIds(long hostId, long peerId);

    void deleteAll();
}
