package fm.kirtsim.kharos.memorywell.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.entity.Tag;
import fm.kirtsim.kharos.memorywell.db.entity.Tagging;
import fm.kirtsim.kharos.memorywell.db.mock.MemoryMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TagMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class TagDaoTest {

    private static final String ERR_NULL = "Null was returned.";
    private static final String ERR_SIZE = "Lists do not match in size.";
    private static final String ERR_LIST_ITEM = "The list item does't match.";
    private static final String ERR_UPDATE_COUNT = "The count of updated items is incorrect: ";
    private static final String ERR_DELETE_COUNT = "The count of deleted items is incorrect: ";
    private static final String ERR_EXCEPTION = "An exception was expected to be thrown.";

    private MemoryDatabase db;
    private TagDao tagDao;

    @Before
    public void initBeforeEach() {
        final Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class).build();
        tagDao = db.tagDao();

        tagDao.insert(TagMocks.getMockTags());
        db.memoryDao().insert(MemoryMocks.getMockMemories());
        for (Tagging tagging : TaggingMocks.getMockTaggings())
            db.taggingDao().insert(tagging);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insert_idDuplicatesIgnored_test() {
        List<Tag> toInsert = Lists.newArrayList(new Tag(1, ""), new Tag(200, ""));
        List<Tag> expected = toInsert.subList(1, 2);

        List<Long> insertedIds = tagDao.insert(toInsert);
        List<Tag> insertedTags = insertedIds.stream().filter(id -> id > -1)
                .map(id -> new Tag(id, "")).collect(toList());

        assertTagListsEqual(expected, insertedTags);
    }

    @Test
    public void insert_nameDuplicateIgnored_test() {
        Tag duplicateTag = new Tag(100, TagMocks.getMockTags().get(0).name);
        List<Tag> toInsert = Lists.newArrayList(duplicateTag, new Tag(200, "new name"));
        List<Tag> expected = new ArrayList<>(toInsert.subList(1, 2));
        expected.forEach(tag -> tag.name = "");

        List<Long> ids = tagDao.insert(toInsert);
        List<Tag> inserted = ids.stream().filter(id -> id > -1)
                .map(id -> new Tag(id, "")).collect(toList());

        assertTagListsEqual(expected, inserted);
    }

    @Test
    public void selectAll_test() {
        List<Tag> selected = getValue(tagDao.selectAll());

        assertTagListsEqual(TagMocks.getMockTags(), selected);
    }

    @Test
    public void selectAll_emptyDb() {
        db.taggingDao().delete(TaggingMocks.getMockTaggings());
        tagDao.delete(TagMocks.getMockTags());
        List<Tag> selected = getValue(tagDao.selectAll());
        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByIds_test() {
        List<Tag> expected = TagMocks.getMockTags().subList(3, 6);
        List<Tag> selected = getValue(tagDao.selectByIds(expected.stream()
                .map(tag -> tag.id).collect(toList())));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByIds_missingTags_test() {
        List<Tag> expected = TagMocks.getMockTags().subList(3, 6);
        List<Long> ids = expected.stream().map(tag -> tag.id).collect(toList());
        ids.add(310L);
        ids.add(311L);

        List<Tag> selected = getValue(tagDao.selectByIds(expected.stream()
                .map(tag -> tag.id).collect(toList())));
        selected = selected.stream().filter(tag -> tag.id > -1).collect(toList());

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_test() {
        List<Tag> expected = TagMocks.getMockTags().subList(5, 10);
        List<String> names = expected.stream().map(tag -> tag.name.toLowerCase()).collect(toList());

        List<Tag> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_missingTags_test() {
        List<Tag> expected = TagMocks.getMockTags().subList(5, 10);
        List<String> names = expected.stream().map(tag -> tag.name.toLowerCase()).collect(toList());
        names.add("NO_TAG_NAME");
        names.add("EMAN_GAT_ON");

        List<Tag> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_noMatch_test() {
        List<String> names = Lists.newArrayList("ratatata", "voooa");

        List<Tag> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByNamesCaseSensitive_test() {
        List<Tag> expected = TagMocks.getMockTags();
        List<String> names = expected.stream().map(tag -> tag.name).collect(toList());

        List<Tag> selected = getValue(tagDao.selectByNamesCaseSensitive(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNamesCaseSensitive_caseMismatch_test() {
        List<String> names = Lists.newArrayList(TagMocks.tagNameWithInvertedCase());

        List<Tag> selected = getValue(tagDao.selectByNamesCaseSensitive(names));

        assertTagListsEqual(Lists.newArrayList(), selected);
    }


    @Test
    public void selectTagsWithNamesStarting_multipleMatch_test() {
        List<Tag> expected = Lists.newArrayList();
        String prefix = TagMocks.tagsMatchingNames3(expected);

        List<Tag> selected = getValue(tagDao.selectTagsWithNamesStarting(prefix));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectTagsWithNamesStarting_singleMatch_test() {
        List<Tag> expected = Lists.newArrayList(new Tag(0, ""));
        String prefix = TagMocks.tagWithUniqueNamePrefix(expected.get(0));

        List<Tag> selected = getValue(tagDao.selectTagsWithNamesStarting(prefix));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectTagsWithNameStarting_noMatch_test() {
        List<Tag> selected = getValue(tagDao.selectTagsWithNamesStarting(TagMocks.missingTagPrefix()));
        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void update_test() {
        List<Tag> expected = TagMocks.getMockTags();
        List<Tag> updated = expected.subList(0, 2);
        updated.get(0).name = "new_name_1";
        updated.get(1).name = "new_name_2";

        int updateCount = tagDao.update(updated);
        List<Tag> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_UPDATE_COUNT, updated.size(), updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void update_oneNotExisting_test() {
        List<Tag> expected = TagMocks.getMockTags();
        List<Tag> updated = Lists.newArrayList(expected.get(0), new Tag(10001, "blah_1"));
        updated.get(0).name = "new_name_1";

        int updateCount = tagDao.update(updated);
        List<Tag> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_UPDATE_COUNT,1, updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void update_noChanges_test() {
        List<Tag> expected = TagMocks.getMockTags();
        List<Tag> updated = Lists.newArrayList(expected.get(0));

        int updateCount = tagDao.update(updated);
        List<Tag> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_UPDATE_COUNT, 1, updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void delete_test() {
        List<Long> unboundTagIds = TaggingMocks.getUnboundTagIds().stream().limit(2).collect(toList());
        List<Tag> allTags = TagMocks.getMockTags();
        List<Tag> expected = allTags.stream().filter(t -> !unboundTagIds.contains(t.id)).collect(toList());
        List<Tag> toDelete = allTags.stream().filter(t -> unboundTagIds.contains(t.id)).collect(toList());

        int deleteCount = tagDao.delete(toDelete);
        List<Tag> remaining = getValue(tagDao.selectAll());

        assertEquals(ERR_DELETE_COUNT, toDelete.size(), deleteCount);
        assertTagListsEqual(expected, remaining);
    }

    @Test
    public void delete_onlyIdMatch_test() {
        long unboundTagId = TaggingMocks.getUnboundTagIds().get(0);
        List<Tag> allTags = TagMocks.getMockTags();
        List<Tag> expected = allTags.stream().filter(t -> t.id != unboundTagId).collect(toList());
        List<Tag> toDelete = allTags.stream().filter(t -> unboundTagId == t.id).collect(toList());
        toDelete.get(0).name = "different_name";

        int deleteCount = tagDao.delete(toDelete);
        List<Tag> remaining = getValue(tagDao.selectAll());

        assertEquals(ERR_DELETE_COUNT, toDelete.size(), deleteCount);
        assertTagListsEqual(expected, remaining);
    }

    @Test
    public void delete_boundTagInTagging_boundGoesFirst_deleteNothing_test() {
        final long boundTagId = 2;
        db.taggingDao().delete(TaggingMocks.getMockTaggings());
        db.taggingDao().insert(new Tagging(2,boundTagId));


        List<Tag> expected = TagMocks.getMockTags();
        Tag notBound = expected.stream().filter(tag -> tag.id == 1).findFirst().get();;
        Tag bound = expected.stream().filter(tag -> tag.id == 2).findFirst().get();
        assertNotNull(bound);
        assertNotNull(notBound);
        List<Tag> toDelete = Lists.newArrayList(notBound, bound);

        try {
            tagDao.delete(toDelete);
            fail(ERR_EXCEPTION);
        } catch (SQLiteConstraintException ignored) {}
        List<Tag> remaining = getValue(tagDao.selectAll());

        assertTagListsEqual(expected, remaining);
    }

    private void assertTagListsEqual(List<Tag> expected, List<Tag> actual) {
        assertNotNull(ERR_NULL, actual);
        assertEquals(ERR_SIZE, expected.size(), actual.size());

        expected.sort((tag1, tag2) -> Long.compare(tag1.id, tag2.id));
        actual.sort((tag1, tag2) -> Long.compare(tag1.id, tag2.id));
        for (int i = 0; i < expected.size(); ++i)
            assertEquals(ERR_LIST_ITEM, expected.get(i), actual.get(i));

    }

}
