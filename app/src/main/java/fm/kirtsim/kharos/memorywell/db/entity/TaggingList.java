package fm.kirtsim.kharos.memorywell.db.entity;

import java.util.List;

public class TaggingList {

    public final List<TaggingEntity> taggings;

    public TaggingList(List<TaggingEntity> taggings) {
        this.taggings = taggings;
    }
}
