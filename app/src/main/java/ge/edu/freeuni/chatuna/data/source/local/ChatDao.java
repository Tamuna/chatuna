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
    @Query("SELECT count(*) AS messageCount, u.name as senderName, m_date AS date FROM messages " +
            "m JOIN users u ON m.sender_user_id = u.id or m.receiver_user_id = u.id JOIN (SELECT " +
            "CASE WHEN m1.sender_user_id == :id THEN m1.receiver_user_id ELSE m1.sender_user_id END " +
            "AS m_user_id, max(m1.create_date) AS m_date FROM messages AS m1 WHERE m1.sender_user_id " +
            "!= :id or m1.sender_user_id GROUP BY CASE WHEN m1.sender_user_id == :id THEN " +
            "m1.receiver_user_id ELSE m1.sender_user_id END) max_dates WHERE CASE WHEN m.sender_user_id == :id" +
            " THEN m.receiver_user_id ELSE m.sender_user_id END and max_dates.m_user_id = u.id GROUP " +
            "BY CASE WHEN m.sender_user_id == :id THEN m.receiver_user_id ELSE m.sender_user_id END")
    List<HistoryModel> getHistory(long id);

    @Query("SELECT id FROM users WHERE :name = name")
    long getUserIdByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

}