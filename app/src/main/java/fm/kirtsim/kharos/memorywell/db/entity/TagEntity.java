package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class TagEntity {

    @PrimaryKey
    public long id;
    public String name;

    public TagEntity() {
        this.name = "";
    }

    @Ignore
    public TagEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public TagEntity(TagEntity other) {
        this.id = other.id;
        this.name = other.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TagEntity))
            return false;
        TagEntity other = (TagEntity) obj;
        return this.id == other.id && this.name.equals(other.name);
    }
}
