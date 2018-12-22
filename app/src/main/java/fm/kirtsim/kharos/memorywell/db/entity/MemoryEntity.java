package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity
public class MemoryEntity {

    @PrimaryKey
    public long id;
    public String title;
    public String comment;
    public long dateTime;
    public String imagePath;

    public MemoryEntity() {
        title = "";
        comment = "";
        imagePath = "";
    }

    public MemoryEntity(MemoryEntity other) {
        this.id = other.id;
        this.title = other.title;
        this.comment = other.comment;
        this.dateTime = other.dateTime;
        this.imagePath = other.imagePath;
    }

    @Ignore
    public MemoryEntity(long id, String title, String comment, long dateTime, String imagePath) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.dateTime = dateTime;
        this.imagePath = imagePath;
    }



    @Override
    public int hashCode() {
        return Objects.hash(id, title, comment, dateTime, imagePath);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MemoryEntity))
            return false;
        if (this == obj)
            return true;
        MemoryEntity other = (MemoryEntity) obj;
        return id == other.id &&
                title.equals(other.title) &&
                comment.equals(other.comment) &&
                dateTime == other.dateTime &&
                imagePath.equals(other.imagePath);
    }
}
