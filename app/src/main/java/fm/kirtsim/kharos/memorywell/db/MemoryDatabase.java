package fm.kirtsim.kharos.memorywell.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;

@Database(entities = {MemoryEntity.class, TagEntity.class, TaggingEntity.class}, version = 1)
public abstract class MemoryDatabase extends RoomDatabase {

    public abstract MemoryDao memoryDao();

    public abstract TagDao tagDao();

    public abstract TaggingDao taggingDao();
}
