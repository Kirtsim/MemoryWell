package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Memory {

    @PrimaryKey
    public long id;
    public String title;
    public String comment;
    public long dateTime;
    public String imagePath;

    public Memory() {
        title = "";
        comment = "";
        imagePath = "";
    }

    public Memory(long id, String title, String comment, long dateTime, String imagePath) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.dateTime = dateTime;
        this.imagePath = imagePath;
    }
}
