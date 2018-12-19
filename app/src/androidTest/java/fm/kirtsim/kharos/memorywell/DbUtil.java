package fm.kirtsim.kharos.memorywell;

import android.arch.persistence.room.Room;
import android.content.Context;

import fm.kirtsim.kharos.memorywell.db.MemoryDatabase;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.Tagging;
import fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TagMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

public final class DbUtil {

    private DbUtil() {}

    public static MemoryDatabase setupDatabase(Context context) {
        MemoryDatabase db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class)
                .allowMainThreadQueries().build();
        db.memoryDao().insert(MemoryMocks.getMockMemories());
        db.tagDao().insert(TagMocks.getMockTags());

        final TaggingDao taggingDao = db.taggingDao();
        for (Tagging tagging : TaggingMocks.getMockTaggings())
            taggingDao.insert(tagging);

        return db;
    }

}
