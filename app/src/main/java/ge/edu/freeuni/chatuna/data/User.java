package ge.edu.freeuni.chatuna.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "is_me")
    private boolean isMe;

    @Ignore
    public User(String name){
        setName(name);
    }

    @Ignore
    public User(String name, boolean isMe){
        setName(name);
        setMe(isMe);
    }

    public User(long id, String name, boolean isMe) {
        setId(id);
        setName(name);
        setMe(isMe);
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
