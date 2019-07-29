package ge.edu.freeuni.chatuna.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "message_text")
    private String messageText;

    @ColumnInfo(name = "create_date")
    private Date createDate;

    @ColumnInfo(name = "user_id")
    private int userId;

    @Ignore
    public Message(String messageText, Date createDate, int userId) {
        setMessageText(messageText);
        setCreateDate(createDate);
        setUserId(userId);
    }

    public Message(int id, String messageText, Date createDate, int userId) {
        setId(id);
        setMessageText(messageText);
        setCreateDate(createDate);
        setUserId(userId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
