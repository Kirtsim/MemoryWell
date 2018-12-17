package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.Objects;

@Entity(primaryKeys = {"memoryId", "tagId"},
foreignKeys = {
        @ForeignKey(entity = Memory.class,
                parentColumns = "id",
                childColumns = "memoryId"),
        @ForeignKey(entity = Tag.class,
                parentColumns = "id",
                childColumns = "tagId")
})
public class Tagging {

    public long memoryId;
    public long tagId;

    public Tagging() {
    }

    @Ignore
    public Tagging(long memoryId, long tagId) {
        this.memoryId = memoryId;
        this.tagId = tagId;
    }

    @Ignore
    public Tagging(Tagging other) {
        this(other.memoryId, other.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryId, tagId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tagging))
            return false;
        if (this == obj)
            return true;
        Tagging other = (Tagging) obj;
        return memoryId == other.memoryId && tagId == other.tagId;
    }
}
