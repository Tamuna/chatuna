package ge.edu.freeuni.chatuna.data.source;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;

public interface ChatDataSource {
    interface InsertUserCallback {
        void onUserInserted(int id);
    }

    void saveUser(@NonNull User user, @NonNull InsertUserCallback callback);

    void saveMessage(@NonNull Message message);
}
