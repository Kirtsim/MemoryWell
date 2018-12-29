package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;

import fmShared.kirtsim.kharos.memorywell.util.AssertUtil;
import fmShared.kirtsim.kharos.memorywell.db.util.DbUtil;
import fm.kirtsim.kharos.memorywell.db.MemoryDatabase;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fmShared.kirtsim.kharos.memorywell.db.mock.TaggingEntityMocks;

import static fmShared.kirtsim.kharos.memorywell.util.AssertUtil.ERR_DB_DELETE_COUNT;
import static fmShared.kirtsim.kharos.memorywell.util.AssertUtil.ERR_DB_UPDATE_COUNT;
import static fmShared.kirtsim.kharos.memorywell.db.mock.MemoryEntityMocks.getMockMemories;
import static fmShared.kirtsim.kharos.memorywell.db.mock.MemoryEntityMocks.memoriesWithinTimeRange;
import static fmShared.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class MemoryDaoTest {

    private MemoryDatabase db;
    private MemoryDao memoryDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initializeBeforeEach() {
        db = DbUtil.setupDatabase(InstrumentationRegistry.getContext());
        memoryDao = db.memoryDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insert_test() {
        List<MemoryEntity> expected = Lists.newArrayList(createMemoryWithId(100), createMemoryWithId(200));

        List<Long> ids = memoryDao.insert(expected);
        List<MemoryEntity> inserted = Lists.transform(ids, this::createMemoryWithId);

        assertMemoryListsEqual(expected, inserted);
    }

    @Test
    public void insert_duplicateIgnored_test() {
        List<MemoryEntity> forInsertion = Lists.newArrayList(createMemoryWithId(100), createMemoryWithId(200),
                createMemoryWithId(1));
        List<Long> ids = memoryDao.insert(forInsertion);
        List<MemoryEntity> expected = forInsertion.subList(0, 2);

        List<MemoryEntity> inserted = ids.stream().filter(id -> id > -1)
                .map(this::createMemoryWithId).collect(toList());

        assertMemoryListsEqual(expected, inserted);
    }

    @Test
    public void selectAll_test() {
        List<MemoryEntity> selected = getValue(memoryDao.selectAll());

        assertMemoryListsEqual(getMockMemories(), selected);
    }

    @Test
    public void selectAll_emptyDb_test() {
        db.taggingDao().delete(TaggingEntityMocks.getMockTaggings());
        memoryDao.delete(getMockMemories());
        List<MemoryEntity> selected = getValue(memoryDao.selectAll());

        assertMemoryListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByIds_existingEntries_test() {
        List<MemoryEntity> expected = getMockMemories().subList(2, 4);
        List<MemoryEntity> selected = getValue(memoryDao.selectByIds(Lists.transform(expected, (m) -> m.id)));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByIds_notExistingEntry_test() {
        final long id = 1204;
        List<MemoryEntity> memories = getValue(memoryDao.selectByIds(Lists.newArrayList(id)));

        assertMemoryListsEqual(Lists.newArrayList(), memories);
    }

    @Test
    public void selectByIds_oneNotExisting_test() {
        List<MemoryEntity> expected = getMockMemories().subList(5, 7);
        List<Long> ids = Lists.newArrayList(Lists.transform(expected, (m) -> m.id));
        ids.add(12345L);
        List<MemoryEntity> selected = getValue(memoryDao.selectByIds(ids));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByTimeRange_selectAll_test() {
        List<MemoryEntity> selected = getValue(memoryDao.selectByTimeRange(1, 13));

        assertMemoryListsEqual(getMockMemories(), selected);
    }

    @Test
    public void selectByTimeRange_selectSingle_test() {
        final long from = 2, to = 2;
        List<MemoryEntity> expected = memoriesWithinTimeRange(from, to);
        List<MemoryEntity> selected = getValue(memoryDao.selectByTimeRange(from, to));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByTimeRange_incorrectParams_test() {
        List<MemoryEntity> retMemories = getValue(memoryDao.selectByTimeRange(3, 1));

        assertMemoryListsEqual(Lists.newArrayList(), retMemories);
    }

    @Test
    public void update_test() {
        List<MemoryEntity> expected = getMockMemories();
        MemoryEntity updated = expected.get(5);
        updated.comment = "updated comment";
        updated.dateTime = 1001;
        updated.imagePath = "new/image/path";
        updated.title = "updated title";

        int updateCount = memoryDao.update(Lists.newArrayList(updated));
        List<MemoryEntity> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT,1, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_notExistingId_test() {
        List<MemoryEntity> expected = getMockMemories();
        List<MemoryEntity> toUpdate = Lists.newArrayList(new MemoryEntity(100001, "a", "b", 1, ""));

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<MemoryEntity> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT,0, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_noChanges_test() {
        List<MemoryEntity> expected = getMockMemories();
        List<MemoryEntity> toUpdate = expected.subList(0, 1);

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<MemoryEntity> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT, 1, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void delete_test() {
        long unboundMemoryId = TaggingEntityMocks.getUnboundMemoryIds().get(0);
        List<MemoryEntity> originalMemories = getMockMemories();
        List<MemoryEntity> expected = originalMemories.stream()
                .filter(m -> m.id != unboundMemoryId).collect(toList());
        List<MemoryEntity> toDelete = originalMemories.stream()
                .filter(m -> m.id == unboundMemoryId).collect(toList());

        int deleteCount = memoryDao.delete(toDelete);
        List<MemoryEntity> remaining = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT,1, deleteCount);
        assertMemoryListsEqual(expected, remaining);
    }

    @Test
    public void delete_idMatchOnly_test() {
        long unboundMemoryId = TaggingEntityMocks.getUnboundMemoryIds().get(0);
        List<MemoryEntity> originalMemories = getMockMemories();
        List<MemoryEntity> expected = originalMemories.stream()
                .filter(m -> m.id != unboundMemoryId).collect(toList());
        List<MemoryEntity> toDelete = originalMemories.stream()
                .filter(m -> m.id == unboundMemoryId).collect(toList());
        toDelete.get(0).title = "DO NOT MATCH THE TITLE!";

        int deleteCount = memoryDao.delete(toDelete);
        List<MemoryEntity> remaining = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT,1, deleteCount);
        assertMemoryListsEqual(expected, remaining);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void delete_boundMemoryByTagging_test() {
        long boundMemoryId = TaggingEntityMocks.getBoundMemoryIds().get(0);
        List<MemoryEntity> expected = getMockMemories();
        List<MemoryEntity> toDelete = getMockMemories().stream()
                .filter(m -> m.id == boundMemoryId).collect(toList());

        memoryDao.delete(toDelete);
    }

    private MemoryEntity createMemoryWithId(long id) {
        MemoryEntity memory = new MemoryEntity();
        memory.id = id;
        return memory;
    }

    private void assertMemoryListsEqual(List<MemoryEntity> expected, List<MemoryEntity> actual) {
        AssertUtil.assertListsEquals(expected, actual, (m1, m2) -> Long.compare(m1.id, m2.id));
    }
}
