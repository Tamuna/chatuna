package ge.edu.freeuni.chatuna.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "messages")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "message_text")
    private String messageText;

    @ColumnInfo(name = "create_date")
    private Date createDate;

    @ColumnInfo(name = "user_id")
    private long userId;

    @Ignore
    public Message(String messageText, Date createDate, long userId) {
        setMessageText(messageText);
        setCreateDate(createDate);
        setUserId(userId);
    }

    public Message(long id, String messageText, Date createDate, long userId) {
        setId(id);
        setMessageText(messageText);
        setCreateDate(createDate);
        setUserId(userId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
