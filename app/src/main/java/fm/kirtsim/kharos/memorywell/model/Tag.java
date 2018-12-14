package fm.kirtsim.kharos.memorywell.model;

import java.util.Objects;

public class Tag {

    private final long id;
    private final String name;

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Tag))
            return false;
        if (this == obj)
            return true;
        Tag other = (Tag) obj;
        return this.id == other.id && this.name.equals(other.name);
    }
}
