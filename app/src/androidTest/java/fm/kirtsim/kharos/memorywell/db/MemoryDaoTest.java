package fm.kirtsim.kharos.memorywell.db;

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

import fm.kirtsim.kharos.memorywell.AssertUtil;
import fm.kirtsim.kharos.memorywell.DbUtil;
import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_DB_DELETE_COUNT;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_DB_UPDATE_COUNT;
import static fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks.getMockMemories;
import static fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks.memoriesWithinTimeRange;
import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
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
        List<Memory> expected = Lists.newArrayList(createMemoryWithId(100), createMemoryWithId(200));

        List<Long> ids = memoryDao.insert(expected);
        List<Memory> inserted = Lists.transform(ids, this::createMemoryWithId);

        assertMemoryListsEqual(expected, inserted);
    }

    @Test
    public void insert_duplicateIgnored_test() {
        List<Memory> forInsertion = Lists.newArrayList(createMemoryWithId(100), createMemoryWithId(200),
                createMemoryWithId(1));
        List<Long> ids = memoryDao.insert(forInsertion);
        List<Memory> expected = forInsertion.subList(0, 2);

        List<Memory> inserted = ids.stream().filter(id -> id > -1)
                .map(this::createMemoryWithId).collect(toList());

        assertMemoryListsEqual(expected, inserted);
    }

    @Test
    public void selectAll_test() {
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertMemoryListsEqual(getMockMemories(), selected);
    }

    @Test
    public void selectAll_emptyDb_test() {
        db.taggingDao().delete(TaggingMocks.getMockTaggings());
        memoryDao.delete(getMockMemories());
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertMemoryListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByIds_existingEntries_test() {
        List<Memory> expected = getMockMemories().subList(2, 4);
        List<Memory> selected = getValue(memoryDao.selectByIds(Lists.transform(expected, (m) -> m.id)));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByIds_notExistingEntry_test() {
        final long id = 1204;
        List<Memory> memories = getValue(memoryDao.selectByIds(Lists.newArrayList(id)));

        assertMemoryListsEqual(Lists.newArrayList(), memories);
    }

    @Test
    public void selectByIds_oneNotExisting_test() {
        List<Memory> expected = getMockMemories().subList(5, 7);
        List<Long> ids = Lists.newArrayList(Lists.transform(expected, (m) -> m.id));
        ids.add(12345L);
        List<Memory> selected = getValue(memoryDao.selectByIds(ids));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByTimeRange_selectAll_test() {
        List<Memory> selected = getValue(memoryDao.selectByTimeRange(1, 13));

        assertMemoryListsEqual(getMockMemories(), selected);
    }

    @Test
    public void selectByTimeRange_selectSingle_test() {
        final long from = 2, to = 2;
        List<Memory> expected = memoriesWithinTimeRange(from, to);
        List<Memory> selected = getValue(memoryDao.selectByTimeRange(from, to));

        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void selectByTimeRange_incorrectParams_test() {
        List<Memory> retMemories = getValue(memoryDao.selectByTimeRange(3, 1));

        assertMemoryListsEqual(Lists.newArrayList(), retMemories);
    }

    @Test
    public void update_test() {
        List<Memory> expected = getMockMemories();
        Memory updated = expected.get(5);
        updated.comment = "updated comment";
        updated.dateTime = 1001;
        updated.imagePath = "new/image/path";
        updated.title = "updated title";

        int updateCount = memoryDao.update(Lists.newArrayList(updated));
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT,1, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_notExistingId_test() {
        List<Memory> expected = getMockMemories();
        List<Memory> toUpdate = Lists.newArrayList(new Memory(100001, "a", "b", 1, ""));

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT,0, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_noChanges_test() {
        List<Memory> expected = getMockMemories();
        List<Memory> toUpdate = expected.subList(0, 1);

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT, 1, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void delete_test() {
        long unboundMemoryId = TaggingMocks.getUnboundMemoryIds().get(0);
        List<Memory> originalMemories = getMockMemories();
        List<Memory> expected = originalMemories.stream()
                .filter(m -> m.id != unboundMemoryId).collect(toList());
        List<Memory> toDelete = originalMemories.stream()
                .filter(m -> m.id == unboundMemoryId).collect(toList());

        int deleteCount = memoryDao.delete(toDelete);
        List<Memory> remaining = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT,1, deleteCount);
        assertMemoryListsEqual(expected, remaining);
    }

    @Test
    public void delete_idMatchOnly_test() {
        long unboundMemoryId = TaggingMocks.getUnboundMemoryIds().get(0);
        List<Memory> originalMemories = getMockMemories();
        List<Memory> expected = originalMemories.stream()
                .filter(m -> m.id != unboundMemoryId).collect(toList());
        List<Memory> toDelete = originalMemories.stream()
                .filter(m -> m.id == unboundMemoryId).collect(toList());
        toDelete.get(0).title = "DO NOT MATCH THE TITLE!";

        int deleteCount = memoryDao.delete(toDelete);
        List<Memory> remaining = getValue(memoryDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT,1, deleteCount);
        assertMemoryListsEqual(expected, remaining);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void delete_boundMemoryByTagging_test() {
        long boundMemoryId = TaggingMocks.getBoundMemoryIds().get(0);
        List<Memory> expected = getMockMemories();
        List<Memory> toDelete = getMockMemories().stream()
                .filter(m -> m.id == boundMemoryId).collect(toList());

        memoryDao.delete(toDelete);
    }

    private Memory createMemoryWithId(long id) {
        Memory memory = new Memory();
        memory.id = id;
        return memory;
    }

    private void assertMemoryListsEqual(List<Memory> expected, List<Memory> actual) {
        AssertUtil.assertListsEquals(expected, actual, (m1, m2) -> Long.compare(m1.id, m2.id));
    }
}
