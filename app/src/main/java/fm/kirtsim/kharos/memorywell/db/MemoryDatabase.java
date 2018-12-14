package fm.kirtsim.kharos.memorywell.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;

@Database(entities = {Memory.class}, version = 1)
public abstract class MemoryDatabase extends RoomDatabase {

    public abstract MemoryDao memoryDao();

}
