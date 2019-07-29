package ge.edu.freeuni.chatuna.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.model.HistoryModel;

@Dao
public interface ChatDao {
    List<HistoryModel> getHistory(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    int insertUser(User user);

}
