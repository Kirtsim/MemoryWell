package fm.kirtsim.kharos.memorywell.db;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
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

import java.util.Comparator;
import java.util.List;

import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;
import fm.kirtsim.kharos.memorywell.db.entity.Tagging;
import fm.kirtsim.kharos.memorywell.db.mock.TagMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

import static fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks.getMockMemories;
import static fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks.memoriesWithinTimeRange;
import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class MemoryDaoTest {

    private static final String NULL_ERROR = "Null value was returned.";
    private static final String SIZE_ERROR = "Size mismatch.";
    private static final String LIST_ITEM_MISMATCH = "List item mismatch.";
    private static final String DELETE_ERROR = "The number of deleted items is incorrect: ";

    private static final Comparator<Memory> idComparator = (m1, m2) -> Long.compare(m1.id, m2.id);
    private static final Comparator<Memory> titleComparator = (m1, m2) -> m1.title.compareTo(m2.title);

    private MemoryDao memoryDao;
    private MemoryDatabase db;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initializeBeforeEach() {
        Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class)
                .allowMainThreadQueries()
                .build();
        memoryDao = db.memoryDao();
        memoryDao.insert(getMockMemories());

        db.tagDao().insert(TagMocks.getMockTags());
        for (Tagging tagging : TaggingMocks.getMockTaggings())
            db.taggingDao().insert(tagging);
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

        assertEquals(1, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_notExistingId_test() {
        List<Memory> expected = getMockMemories();
        List<Memory> toUpdate = Lists.newArrayList(new Memory(100001, "a", "b", 1, ""));

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertEquals(0, updateCount);
        assertMemoryListsEqual(expected, selected);
    }

    @Test
    public void update_noChanges_test() {
        List<Memory> expected = getMockMemories();
        List<Memory> toUpdate = expected.subList(0, 1);

        int updateCount = memoryDao.update(Lists.newArrayList(toUpdate));
        List<Memory> selected = getValue(memoryDao.selectAll());

        assertEquals(1, updateCount);
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

        assertEquals(DELETE_ERROR,1, deleteCount);
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

        assertEquals(DELETE_ERROR,1, deleteCount);
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
        assertMemoryListsEqual(expected, actual, idComparator);
    }

    private void assertMemoryListsEqual(List<Memory> expected, List<Memory> actual,
                                        Comparator<Memory> sortComparator) {
        assertNotNull(NULL_ERROR, actual);
        assertEquals(SIZE_ERROR, expected.size(), actual.size());
        expected.sort(sortComparator);
        for (int i = 0; i < expected.size(); ++i)
            assertEquals(LIST_ITEM_MISMATCH, expected.get(i), actual.get(i));
    }
}
