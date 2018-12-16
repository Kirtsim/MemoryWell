package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index(value = {"name"}, unique = true)})
public class Tag {

    @PrimaryKey
    public long id;
    public String name;

    public Tag() {
        this.name = "";
    }

    @Ignore
    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Tag(Tag other) {
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
        if (!(obj instanceof Tag))
            return false;
        Tag other = (Tag) obj;
        return this.id == other.id && this.name.equals(other.name);
    }
}
