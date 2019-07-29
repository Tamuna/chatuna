package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

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

    void getHistory(long id, @NonNull final GetHistoryCallback callback);

    void saveUser(@NonNull User user, @NonNull InsertUserCallback callback);

    void saveMessage(@NonNull Message message);
}
