package fm.kirtsim.kharos.memorywell.db;

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

import fm.kirtsim.kharos.memorywell.AssertUtil;
import fm.kirtsim.kharos.memorywell.DbUtil;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fm.kirtsim.kharos.memorywell.db.mock.TagMocks;
import fm.kirtsim.kharos.memorywell.db.mock.TaggingMocks;

import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_DB_DELETE_COUNT;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_DB_UPDATE_COUNT;
import static fm.kirtsim.kharos.memorywell.AssertUtil.ERR_NO_EXCEPTION;
import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public final class TagEntityDaoTest {

    private MemoryDatabase db;
    private TagDao tagDao;

    @Before
    public void initBeforeEach() {
        db = DbUtil.setupDatabase(InstrumentationRegistry.getContext());
        tagDao = db.tagDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insert_idDuplicatesIgnored_test() {
        List<TagEntity> toInsert = Lists.newArrayList(new TagEntity(1, ""), new TagEntity(200, ""));
        List<TagEntity> expected = toInsert.subList(1, 2);

        List<Long> insertedIds = tagDao.insert(toInsert);
        List<TagEntity> insertedTags = insertedIds.stream().filter(id -> id > -1)
                .map(id -> new TagEntity(id, "")).collect(toList());

        assertTagListsEqual(expected, insertedTags);
    }

    @Test
    public void insert_nameDuplicateIgnored_test() {
        TagEntity duplicateTag = new TagEntity(100, TagMocks.getMockTags().get(0).name);
        List<TagEntity> toInsert = Lists.newArrayList(duplicateTag, new TagEntity(200, "new name"));
        List<TagEntity> expected = new ArrayList<>(toInsert.subList(1, 2));
        expected.forEach(tag -> tag.name = "");

        List<Long> ids = tagDao.insert(toInsert);
        List<TagEntity> inserted = ids.stream().filter(id -> id > -1)
                .map(id -> new TagEntity(id, "")).collect(toList());

        assertTagListsEqual(expected, inserted);
    }

    @Test
    public void selectAll_test() {
        List<TagEntity> selected = getValue(tagDao.selectAll());

        assertTagListsEqual(TagMocks.getMockTags(), selected);
    }

    @Test
    public void selectAll_emptyDb() {
        db.taggingDao().delete(TaggingMocks.getMockTaggings());
        tagDao.delete(TagMocks.getMockTags());
        List<TagEntity> selected = getValue(tagDao.selectAll());
        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByIds_test() {
        List<TagEntity> expected = TagMocks.getMockTags().subList(3, 6);
        List<TagEntity> selected = getValue(tagDao.selectByIds(expected.stream()
                .map(tag -> tag.id).collect(toList())));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByIds_missingTags_test() {
        List<TagEntity> expected = TagMocks.getMockTags().subList(3, 6);
        List<Long> ids = expected.stream().map(tag -> tag.id).collect(toList());
        ids.add(310L);
        ids.add(311L);

        List<TagEntity> selected = getValue(tagDao.selectByIds(expected.stream()
                .map(tag -> tag.id).collect(toList())));
        selected = selected.stream().filter(tag -> tag.id > -1).collect(toList());

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_test() {
        List<TagEntity> expected = TagMocks.getMockTags().subList(5, 10);
        List<String> names = expected.stream().map(tag -> tag.name.toLowerCase()).collect(toList());

        List<TagEntity> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_missingTags_test() {
        List<TagEntity> expected = TagMocks.getMockTags().subList(5, 10);
        List<String> names = expected.stream().map(tag -> tag.name.toLowerCase()).collect(toList());
        names.add("NO_TAG_NAME");
        names.add("EMAN_GAT_ON");

        List<TagEntity> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_noMatch_test() {
        List<String> names = Lists.newArrayList("ratatata", "voooa");

        List<TagEntity> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByNamesCaseSensitive_test() {
        List<TagEntity> expected = TagMocks.getMockTags();
        List<String> names = expected.stream().map(tag -> tag.name).collect(toList());

        List<TagEntity> selected = getValue(tagDao.selectByNamesCaseSensitive(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNamesCaseSensitive_caseMismatch_test() {
        List<String> names = Lists.newArrayList(TagMocks.tagNameWithInvertedCase());

        List<TagEntity> selected = getValue(tagDao.selectByNamesCaseSensitive(names));

        assertTagListsEqual(Lists.newArrayList(), selected);
    }


    @Test
    public void selectTagsWithNamesStarting_multipleMatch_test() {
        List<TagEntity> expected = Lists.newArrayList();
        String prefix = TagMocks.tagsMatchingNames3(expected);

        List<TagEntity> selected = getValue(tagDao.selectTagsWithNamesStarting(prefix));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectTagsWithNamesStarting_singleMatch_test() {
        List<TagEntity> expected = Lists.newArrayList(new TagEntity(0, ""));
        String prefix = TagMocks.tagWithUniqueNamePrefix(expected.get(0));

        List<TagEntity> selected = getValue(tagDao.selectTagsWithNamesStarting(prefix));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectTagsWithNameStarting_noMatch_test() {
        List<TagEntity> selected = getValue(tagDao.selectTagsWithNamesStarting(TagMocks.missingTagPrefix()));
        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void update_test() {
        List<TagEntity> expected = TagMocks.getMockTags();
        List<TagEntity> updated = expected.subList(0, 2);
        updated.get(0).name = "new_name_1";
        updated.get(1).name = "new_name_2";

        int updateCount = tagDao.update(updated);
        List<TagEntity> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT, updated.size(), updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void update_oneNotExisting_test() {
        List<TagEntity> expected = TagMocks.getMockTags();
        List<TagEntity> updated = Lists.newArrayList(expected.get(0), new TagEntity(10001, "blah_1"));
        updated.get(0).name = "new_name_1";

        int updateCount = tagDao.update(updated);
        List<TagEntity> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT,1, updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void update_noChanges_test() {
        List<TagEntity> expected = TagMocks.getMockTags();
        List<TagEntity> updated = Lists.newArrayList(expected.get(0));

        int updateCount = tagDao.update(updated);
        List<TagEntity> newSelected = getValue(tagDao.selectAll());

        assertEquals(ERR_DB_UPDATE_COUNT, 1, updateCount);
        assertTagListsEqual(expected, newSelected);
    }

    @Test
    public void delete_test() {
        List<Long> unboundTagIds = TaggingMocks.getUnboundTagIds().stream().limit(2).collect(toList());
        List<TagEntity> allTags = TagMocks.getMockTags();
        List<TagEntity> expected = allTags.stream().filter(t -> !unboundTagIds.contains(t.id)).collect(toList());
        List<TagEntity> toDelete = allTags.stream().filter(t -> unboundTagIds.contains(t.id)).collect(toList());

        int deleteCount = tagDao.delete(toDelete);
        List<TagEntity> remaining = getValue(tagDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT, toDelete.size(), deleteCount);
        assertTagListsEqual(expected, remaining);
    }

    @Test
    public void delete_onlyIdMatch_test() {
        long unboundTagId = TaggingMocks.getUnboundTagIds().get(0);
        List<TagEntity> allTags = TagMocks.getMockTags();
        List<TagEntity> expected = allTags.stream().filter(t -> t.id != unboundTagId).collect(toList());
        List<TagEntity> toDelete = allTags.stream().filter(t -> unboundTagId == t.id).collect(toList());
        toDelete.get(0).name = "different_name";

        int deleteCount = tagDao.delete(toDelete);
        List<TagEntity> remaining = getValue(tagDao.selectAll());

        assertEquals(ERR_DB_DELETE_COUNT, toDelete.size(), deleteCount);
        assertTagListsEqual(expected, remaining);
    }

    @Test
    public void delete_boundTagInTagging_boundGoesFirst_deleteNothing_test() {
        final long boundTagId = 2;
        db.taggingDao().delete(TaggingMocks.getMockTaggings());
        db.taggingDao().insert(new TaggingEntity(2,boundTagId));


        List<TagEntity> expected = TagMocks.getMockTags();
        TagEntity notBound = expected.stream().filter(tag -> tag.id == 1).findFirst().get();;
        TagEntity bound = expected.stream().filter(tag -> tag.id == 2).findFirst().get();
        assertNotNull(bound);
        assertNotNull(notBound);
        List<TagEntity> toDelete = Lists.newArrayList(notBound, bound);

        try {
            tagDao.delete(toDelete);
            fail(ERR_NO_EXCEPTION);
        } catch (SQLiteConstraintException ignored) {}
        List<TagEntity> remaining = getValue(tagDao.selectAll());

        assertTagListsEqual(expected, remaining);
    }

    private void assertTagListsEqual(List<TagEntity> expected, List<TagEntity> actual) {
        AssertUtil.assertListsEquals(expected, actual,
                (tag1, tag2) -> Long.compare(tag1.id, tag2.id));
    }

}
