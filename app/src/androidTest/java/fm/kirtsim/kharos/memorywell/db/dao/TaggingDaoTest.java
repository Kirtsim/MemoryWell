package fm.kirtsim.kharos.memorywell.db;

import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.List;

import fm.kirtsim.kharos.memorywell.AssertUtil;
import fm.kirtsim.kharos.memorywell.DbUtil;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_DB_DELETE_COUNT;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_MISSING_WORD;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_NO_EXCEPTION;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_NULL;
import static fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks.getMockTaggings;
import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TaggingEntityDaoTest {

    private static final String WORD_UNIQUE = "UNIQUE";
    private static final String WORD_FK = "FOREIGN KEY";

    private MemoryDatabase db;
    private TaggingDao taggingDao;

    @Before
    public void initBeforeEach() {
        db = DbUtil.setupDatabase(InstrumentationRegistry.getContext());
        taggingDao = db.taggingDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insert_existing_ignore_test() {
        TaggingEntity duplicate = getMockTaggings().get(5);

        Exception ex = assertThrowOnInsert(duplicate, SQLiteConstraintException.class);
        assertTrue(ERR_MISSING_WORD, ex.getMessage().contains(WORD_UNIQUE));
    }

    @Test
    public void insert_notExistingMemory_ignore_test() {
        TaggingEntity toInsert = new TaggingEntity(102, 1);

        Exception ex = assertThrowOnInsert(toInsert, SQLiteConstraintException.class);
        assertTrue(ERR_MISSING_WORD, ex.getMessage().contains(WORD_FK));
    }

    @Test
    public void insert_notExistingTags_ignore_test() {
        TaggingEntity toInsert = new TaggingEntity(1, 100);

        Exception ex = assertThrowOnInsert(toInsert, SQLiteConstraintException.class);
        assertTrue(ERR_MISSING_WORD, ex.getMessage().contains(WORD_FK));
    }

    @Test
    public void selectAll_test() {
        List<TaggingEntity> selected = getValue(taggingDao.selectAll());
        List<TaggingEntity> expected = getMockTaggings();

        assertListsEqual(expected, selected);
    }

    @Test
    public void selectSingle_test() {
        TaggingEntity expected = getMockTaggings().get(7);

        TaggingEntity selected = getValue(taggingDao.selectSingle(expected.memoryId, expected.tagId));

        assertNotNull(ERR_NULL, selected);
        assertEquals(expected, selected);
    }

    @Test
    public void selectSingle_notExisting_test() {
        TaggingEntity selected = getValue(taggingDao.selectSingle(100, 1001));

        assertNull(selected);
    }

    @Test
    public void selectByMemoryIds_test() {
        List<List<TaggingEntity>> testTaggings = TaggingMocks.getTestTagginsByMemoryIds();
        List<Long> memoryIds = testTaggings.stream().map(list -> list.get(0).memoryId).collect(toList());
        List<TaggingEntity> expected = testTaggings.stream().flatMap(Collection::stream).collect(toList());

        List<TaggingEntity> selected = getValue(taggingDao.selectByMemoryIds(memoryIds));

        assertListsEqual(expected, selected);
    }

    @Test
    public void selectByMemoryIds_notExistingId_test() {
        List<List<TaggingEntity>> testTaggings = TaggingMocks.getTestTagginsByMemoryIds();
        List<Long> memoryIds = testTaggings.stream().map(list -> list.get(0).memoryId).collect(toList());
        memoryIds.add(101L);
        List<TaggingEntity> expected = testTaggings.stream().flatMap(Collection::stream).collect(toList());

        List<TaggingEntity> selected = getValue(taggingDao.selectByMemoryIds(memoryIds));

        assertListsEqual(expected, selected);
    }

    @Test
    public void selectByTagIds_test() {
        List<List<TaggingEntity>> testTaggings = TaggingMocks.getTestTaggingsByTagIds();
        List<Long> tagIds = testTaggings.stream().map(list -> list.get(0).tagId).collect(toList());

        List<TaggingEntity> expected = testTaggings.stream().flatMap(Collection::stream).collect(toList());

        List<TaggingEntity> selected = getValue(taggingDao.selectByTagIds(tagIds));

        assertListsEqual(expected, selected);
    }

    @Test
    public void selectByTagIds_notExistingTagIds_test() {
        List<List<TaggingEntity>> testTaggings = TaggingMocks.getTestTaggingsByTagIds();
        List<Long> tagIds = testTaggings.stream().map(list -> list.get(0).tagId).collect(toList());
        tagIds.add(2012L);

        List<TaggingEntity> expected = testTaggings.stream().flatMap(Collection::stream).collect(toList());

        List<TaggingEntity> selected = getValue(taggingDao.selectByTagIds(tagIds));

        assertListsEqual(expected, selected);
    }

    @Test
    public void delete_test() {
        List<TaggingEntity> taggings = getMockTaggings();
        List<TaggingEntity> toDelete = taggings.stream().filter(t -> t.tagId == 1).collect(toList());
        List<TaggingEntity> expected = taggings.stream().filter(t -> t.tagId != 1).collect(toList());

        int deleteCount = taggingDao.delete(toDelete);
        List<TaggingEntity> remaining = getValue(taggingDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT, toDelete.size(), deleteCount);
        assertListsEqual(expected, remaining);
    }

    @Test
    public void delete_notExistingTagId_test() {
        List<TaggingEntity> toDelete = Lists.newArrayList(new TaggingEntity(1, 1001));
        List<TaggingEntity> expected = getMockTaggings();

        int deleteCount = taggingDao.delete(toDelete);
        List<TaggingEntity> remaining = getValue(taggingDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT,0, deleteCount);
        assertListsEqual(expected, remaining);
    }

    @Test
    public void delete_notExistingMemoryId_test() {
        List<TaggingEntity> toDelete = Lists.newArrayList(new TaggingEntity(1001, 1));
        List<TaggingEntity> expected = getMockTaggings();

        int deleteCount = taggingDao.delete(toDelete);
        List<TaggingEntity> remaining = getValue(taggingDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT, 0, deleteCount);
        assertListsEqual(expected, remaining);
    }

    private void assertListsEqual(List<TaggingEntity> expected, List<TaggingEntity> actual) {
        AssertUtil.assertListsEquals(expected, actual,
                (t1, t2) -> t1.memoryId == t2.memoryId ?
                        Long.compare(t1.tagId, t2.tagId) : Long.compare(t1.memoryId, t2.memoryId));
    }

    private <E extends Exception> E assertThrowOnInsert(TaggingEntity tagging, Class<E> exClass) {
        try {
            taggingDao.insert(tagging);
        } catch (Exception ex) {
            assertTrue(ex.getClass().isAssignableFrom(exClass));
            return (E) ex;
        }
        fail(ERR_NO_EXCEPTION);
        return null;
    }

}
