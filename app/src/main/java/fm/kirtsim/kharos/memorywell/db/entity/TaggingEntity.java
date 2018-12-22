package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

import java.util.Objects;

@Entity(primaryKeys = {"memoryId", "tagId"},
foreignKeys = {
        @ForeignKey(entity = MemoryEntity.class,
                parentColumns = "id",
                childColumns = "memoryId"),
        @ForeignKey(entity = TagEntity.class,
                parentColumns = "id",
                childColumns = "tagId")
})
public class TaggingEntity {

    public long memoryId;
    public long tagId;

    public TaggingEntity() {
    }

    @Ignore
    public TaggingEntity(long memoryId, long tagId) {
        this.memoryId = memoryId;
        this.tagId = tagId;
    }

    @Ignore
    public TaggingEntity(TaggingEntity other) {
        this(other.memoryId, other.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoryId, tagId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaggingEntity))
            return false;
        if (this == obj)
            return true;
        TaggingEntity other = (TaggingEntity) obj;
        return memoryId == other.memoryId && tagId == other.tagId;
    }
}
