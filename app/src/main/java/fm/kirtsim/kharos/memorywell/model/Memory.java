package fm.kirtsim.kharos.memorywell.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import fm.kirtsim.kharos.memorywell.model.exception.IdDuplicateException;
import fm.kirtsim.kharos.memorywell.model.exception.NameDuplicateException;

public class Memory {

    private final long id;
    private final String title;
    private final String comment;
    private final long dateTime;
    private final String imagePath;
    private Map<Long, Tag> tagsById;
    private Map<String, Tag> tagsByName;

    private Memory(long id, String title, String comment, long dateTime, String imagePath,
                   Map<Long, Tag> tagsById, Map<String, Tag> tagsByName) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.dateTime = dateTime;
        this.imagePath = imagePath;
        this.tagsById = tagsById;
        this.tagsByName = tagsByName;
    }

    public long id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String comment() {
        return comment;
    }

    public long dateTime() {
        return dateTime;
    }

    public String imagePath() {
        return imagePath;
    }

    public List<Tag> listTags() {
        return Lists.newArrayList(tagsById.values());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, comment, dateTime, imagePath, tagsById.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Memory))
            return false;
        if (this == obj)
            return true;
        Memory other = (Memory) obj;
        return id == other.id &&
                title.equals(other.title) &&
                comment.equals(other.comment) &&
                dateTime == other.dateTime &&
                imagePath.equals(other.imagePath) &&
                tagsById.equals(other.tagsById);
    }

    @Override
    public String toString() {

        return "Memory: id(" + id + "), title(" + title + "), comment(" + comment + ") + date(" +
                dateTime + "), imgPath(" + imagePath + ")";
    }

    public final static class Builder {
        private long id;
        private String title;
        private String comment;
        private long dateTime;
        private String imagePath;
        private Map<Long, Tag> tagsById;
        private Map<String, Tag> tagsByName;

        public Builder() {
            id = -1;
            title = "";
            comment = "";
            imagePath = "";
            tagsById = Maps.newConcurrentMap();
            tagsByName = Maps.newConcurrentMap();
        }

        public Builder memoryId(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            if (title != null)
                this.title = title;
            return this;
        }

        public Builder comment(String comment) {
            if (comment != null)
                this.comment = comment;
            return this;
        }

        public Builder dateTime(long dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public Builder imagePath(String path) {
            if (path != null)
                this.imagePath = path;
            return this;
        }

        public Builder addTag(Tag tag) {
            try {
                addTagOrThrow(tag);
            } catch(IdDuplicateException | NameDuplicateException | NullPointerException ignored) {}
            return this;
        }

        public Builder addTagOrThrow(Tag tag) throws IdDuplicateException, NameDuplicateException {
            if (tag == null)
                throw new NullPointerException();
            throwIfTagClashes(tag);
            tagsById.put(tag.id(), tag);
            tagsByName.put(tag.name(), tag);
            return this;
        }

        private void throwIfTagClashes(Tag tag) throws IdDuplicateException, NameDuplicateException {
            if (tagsById.containsKey(tag.id()))
                throw new IdDuplicateException();
            if (tagsByName.containsKey(tag.name()))
                throw new NameDuplicateException();
        }

        public Builder addTags(List<Tag> tags) {
            for (Tag tag : tags) {
                addTag(tag);
            }
            return this;
        }

        public Builder removeTag(Tag tag) {
            if (tag != null && doesTagExist(tag)) {
                tagsById.remove(tag.id());
                tagsByName.remove(tag.name());
            }
            return this;
        }

        private boolean doesTagExist(Tag tag) {
            Tag retTag = tagsById.get(tag.id());
            if (retTag == null)
                return false;
            return retTag.name().equals(tag.name());
        }

        public Builder clearTags() {
            tagsById.clear();
            tagsByName.clear();
            return this;
        }

        public Memory build() {
            return new Memory(id, title, comment, dateTime, imagePath, tagsById, tagsByName);
        }
    }
}
