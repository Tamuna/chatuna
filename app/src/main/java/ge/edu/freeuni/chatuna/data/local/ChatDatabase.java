package ge.edu.freeuni.chatuna.data.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.utils.DateTypeConverter;

@androidx.room.Database(entities = {User.class, Message.class}, version = 1, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class ChatDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "app_database";

    private static ChatDatabase INSTANCE;

    private static final Object lock = new Object();

    public abstract ChatDatabase toDoDao();

    public static ChatDatabase getInstance(Context context){
        synchronized (lock){
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        ChatDatabase.class,
                        DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

}
