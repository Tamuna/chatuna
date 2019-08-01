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

    @ColumnInfo(name = "sender_user_id")
    private long senderUserId;

    @ColumnInfo(name = "receiver_user_id")
    private long receiverUserId;

    @Ignore
    public Message(String messageText, Date createDate, long senderUserId, long receiverUserId) {
        setMessageText(messageText);
        setCreateDate(createDate);
        setSenderUserId(senderUserId);
        setReceiverUserId(receiverUserId);
    }

    public Message(long id, String messageText, Date createDate, long senderUserId, long receiverUserId) {
        setId(id);
        setMessageText(messageText);
        setCreateDate(createDate);
        setSenderUserId(senderUserId);
        setReceiverUserId(receiverUserId);
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

    public long getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(long userId) {
        this.senderUserId = userId;
    }

    public long getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(long receiverUserId) {
        this.receiverUserId = receiverUserId;
    }
}
