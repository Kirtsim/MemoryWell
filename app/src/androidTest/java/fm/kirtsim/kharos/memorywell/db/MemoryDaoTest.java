package fm.kirtsim.kharos.memorywell.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;

@RunWith(AndroidJUnit4.class)
public class MemoryDaoTest {

    private MemoryDao memoryDao;
    private MemoryDatabase db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class).build();
        memoryDao = db.memoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void selectAll_test() {
        Memory firstMemory= new Memory();
        firstMemory.id = 1;
        Memory secondMemory = new Memory();
        secondMemory.id = 2;

        memoryDao.insert(Lists.newArrayList(firstMemory, secondMemory));

        LiveData<MemoryList>

    }

}
