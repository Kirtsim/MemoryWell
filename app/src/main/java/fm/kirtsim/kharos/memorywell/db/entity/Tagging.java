package fm.kirtsim.kharos.memorywell.db.entity;

import android.arch.persistence.room.Entity;

@Entity(primaryKeys = {"memoryId", "tagId"})
public class Tagging {

    public long memoryId;
    public long tagId;

}
