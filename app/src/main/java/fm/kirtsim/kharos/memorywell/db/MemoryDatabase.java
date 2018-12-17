package fm.kirtsim.kharos.memorywell.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;
import fm.kirtsim.kharos.memorywell.db.entity.Tag;
import fm.kirtsim.kharos.memorywell.db.entity.Tagging;

@Database(entities = {Memory.class, Tag.class, Tagging.class}, version = 1)
public abstract class MemoryDatabase extends RoomDatabase {

    public abstract MemoryDao memoryDao();

    public abstract TagDao tagDao();

    public abstract TaggingDao taggingDao();
}
