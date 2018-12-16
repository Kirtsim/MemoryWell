package fm.kirtsim.kharos.memorywell.db;

import android.arch.persistence.room.Room;
import android.content.Context;
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
import fm.kirtsim.kharos.memorywell.db.mock.TagMocks;

import static fm.kirtsim.kharos.memorywell.db.util.LiveDataTestUtil.getValue;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public final class TagDaoTest {

    private static final String ERR_NULL = "Null was returned.";
    private static final String ERR_SIZE = "Lists do not match in size.";
    private static final String ERR_LIST_ITEM = "The list item does't match.";


    private MemoryDatabase db;
    private TagDao tagDao;

    @Before
    public void initBeforeEach() {
        final Context context = InstrumentationRegistry.getContext();
        db = Room.inMemoryDatabaseBuilder(context, MemoryDatabase.class).build();
        tagDao = db.tagDao();

        tagDao.insert(TagMocks.getMocks());
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
        Tag duplicateTag = new Tag(100, TagMocks.getMocks().get(0).name);
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

        assertTagListsEqual(TagMocks.getMocks(), selected);
    }

    @Test
    public void selectAll_emptyDb() {
        tagDao.delete(TagMocks.getMocks());
        List<Tag> selected = getValue(tagDao.selectAll());
        assertTagListsEqual(Lists.newArrayList(), selected);
    }

    @Test
    public void selectByIds_test() {
        List<Tag> expected = TagMocks.getMocks().subList(3, 6);
        List<Tag> selected = getValue(tagDao.selectByIds(expected.stream()
                .map(tag -> tag.id).collect(toList())));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByIds_missingTags_test() {
        List<Tag> expected = TagMocks.getMocks().subList(3, 6);
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
        List<Tag> expected = TagMocks.getMocks().subList(5, 10);
        List<String> names = expected.stream().map(tag -> tag.name.toLowerCase()).collect(toList());

        List<Tag> selected = getValue(tagDao.selectByNames(names));

        assertTagListsEqual(expected, selected);
    }

    @Test
    public void selectByNames_missingTags_test() {
        List<Tag> expected = TagMocks.getMocks().subList(5, 10);
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
        List<Tag> expected = TagMocks.getMocks();
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

    private void assertTagListsEqual(List<Tag> expected, List<Tag> actual) {
        assertNotNull(ERR_NULL, actual);
        assertEquals(ERR_SIZE, expected.size(), actual.size());

        expected.sort((tag1, tag2) -> Long.compare(tag1.id, tag2.id));
        actual.sort((tag1, tag2) -> Long.compare(tag1.id, tag2.id));
        for (int i = 0; i < expected.size(); ++i)
            assertEquals(ERR_LIST_ITEM, expected.get(i), actual.get(i));

    }

}
