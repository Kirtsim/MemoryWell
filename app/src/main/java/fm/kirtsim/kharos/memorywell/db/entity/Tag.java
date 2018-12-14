package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Tag {

    @PrimaryKey
    public long id;
    public String name;

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof fm.kirtsim.kharos.memorywell.model.Tag))
            return false;
        Tag other = (Tag) obj;
        return this.id == other.id && this.name.equals(other.name);
    }
}
