package fmShared.kirtsim.kharos.memorywell.db.util;

import android.arch.persistence.room.Room;
import android.content.Context;

import fm.kirtsim.kharos.memorywell.db.MemoryDatabase;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fmShared.kirtsim.kharos.memorywell.db.mock.MemoryEntityMocks;
import fmShared.kirtsim.kharos.memorywell.db.mock.TagEntityMocks;
import fmShared.kirtsim.kharos.memorywell.db.mock.TaggingEntityMocks;

public final class DbUtil {

    private DbUtil() {}

    public static MemoryDatabase setupDatabase(Context context) {
        MemoryDatabase db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class)
                .allowMainThreadQueries().build();
        db.memoryDao().insert(MemoryEntityMocks.getMockMemories());
        db.tagDao().insert(TagEntityMocks.getMockTags());

        final TaggingDao taggingDao = db.taggingDao();
        for (TaggingEntity tagging : TaggingEntityMocks.getMockTaggings())
            taggingDao.insert(tagging);

        return db;
    }

}
