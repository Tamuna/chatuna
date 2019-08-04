package ge.edu.freeuni.chatuna.data.source.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ge.edu.freeuni.chatuna.data.Message;
import ge.edu.freeuni.chatuna.data.User;
import ge.edu.freeuni.chatuna.model.HistoryModel;
import ge.edu.freeuni.chatuna.model.MessageModel;

@Dao
public interface ChatDao {
    @Query("SELECT * FROM messages")
    List<Message> getHistory();

    @Query("SELECT m.message_text AS messageText, m.create_date AS createDate, null AS senderName," +
            " CASE WHEN m.sender_user_id == :userId THEN 0 ELSE 1 END AS isSent FROM messages m " +
            "WHERE m.sender_user_id == :userId OR m.receiver_user_id == :userId ORDER BY m.create_date")
    List<MessageModel> getSingleChatById(long userId);

    @Query("SELECT * FROM users u WHERE u.is_me = 1")
    User getSelf();

    @Query("SELECT id FROM users WHERE :name = name")
    long getUserIdByName(String name);

    @Query("SELECT name FROM users WHERE id = :id")
    String getUsernameById(Long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    @Query("DELETE FROM messages WHERE sender_user_id == :peerId OR receiver_user_id == :peerId")
    void deleteHistoryByPeerIds(long peerId);

    @Query("DELETE FROM messages")
    void deleteAll();

}