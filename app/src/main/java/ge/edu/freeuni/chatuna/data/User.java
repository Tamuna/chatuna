package ge.edu.freeuni.chatuna.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import ge.edu.freeuni.chatuna.utils.DateTypeConverter;

@Entity(tableName = "users")
@TypeConverters({DateTypeConverter.class})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    private String name;

    @Ignore
    public User(String name){
        setId(-1);
        setName(name);
    }

    public User(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
