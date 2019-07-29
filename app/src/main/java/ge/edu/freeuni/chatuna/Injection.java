package ge.edu.freeuni.chatuna;

import android.content.Context;

import androidx.annotation.NonNull;

import ge.edu.freeuni.chatuna.data.source.ChatRepository;
import ge.edu.freeuni.chatuna.data.source.local.ChatDatabase;
import ge.edu.freeuni.chatuna.data.source.local.ChatLocalDataSource;
import ge.edu.freeuni.chatuna.utils.AppExecutors;

public class Injection {

    public static ChatRepository provideChatRepository(@NonNull Context context) {
        ChatDatabase database = ChatDatabase.getInstance(context);
        return ChatRepository.getInstance(
                ChatLocalDataSource.getInstance(new AppExecutors(), database.chatDao()));
    }
}
