package fm.kirtsim.kharos.memorywell.model;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import fm.kirtsim.kharos.memorywell.model.Memory.Builder;
import fm.kirtsim.kharos.memorywell.model.exception.IdDuplicateException;
import fm.kirtsim.kharos.memorywell.model.exception.NameDuplicateException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

public class MemoryEntityTest {

    private static final Memory DEF_MEMORY = new Builder()
            .memoryId(-1)
            .title("")
            .comment("")
            .dateTime(0)
            .imagePath("").build();

    private static final class TestClass {}

    @Test
    public void hashCode_sameAttributes_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        Memory secondMemory = createBuilderWithTestData().build();

        assertEquals(firstMemory.hashCode(), secondMemory.hashCode());
    }

    @Test
    public void hashCode_diffAttribute_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        Memory idMemory= createBuilderWithTestData().memoryId(100).build();
        Memory titleMemory = createBuilderWithTestData().title("BLAH").build();
        Memory commentMemory = createBuilderWithTestData().comment("nonsense").build();
        Memory dateMemory = createBuilderWithTestData().dateTime(7777).build();
        Memory imagePathMemory = createBuilderWithTestData().imagePath("none/heh").build();
        Memory tagMemory = createBuilderWithTestData().addTag(new Tag(88, "jojo")).build();

        assertNotEquals("Id should differ.", firstMemory.hashCode(), idMemory.hashCode());
        assertNotEquals("Title should differ.", firstMemory.hashCode(), titleMemory.hashCode());
        assertNotEquals("Comment should differ.", firstMemory.hashCode(), commentMemory.hashCode());
        assertNotEquals("DateTime should differ.", firstMemory.hashCode(), dateMemory.hashCode());
        assertNotEquals("ImagePath should differ.", firstMemory.hashCode(), imagePathMemory.hashCode());
        assertNotEquals("Tags should differ.", firstMemory.hashCode(), tagMemory.hashCode());
    }

    @Test
    public void equals_allSame_returnTrue_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        Memory secondMemory = createBuilderWithTestData().build();

        assertEquals(firstMemory, secondMemory);
    }

    @Test
    public void equals_diffAttribute_returnFalse_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        Memory idMemory= createBuilderWithTestData().memoryId(100).build();
        Memory titleMemory = createBuilderWithTestData().title("BLAH").build();
        Memory commentMemory = createBuilderWithTestData().comment("nonsense").build();
        Memory dateMemory = createBuilderWithTestData().dateTime(7777).build();
        Memory imagePathMemory = createBuilderWithTestData().imagePath("none/heh").build();
        Memory tagMemory = createBuilderWithTestData().addTag(new Tag(88, "jojo")).build();

        assertNotEquals("Incorrect match in Id.", firstMemory, idMemory);
        assertNotEquals("Title should differ.", firstMemory, titleMemory);
        assertNotEquals("Comment should differ.", firstMemory, commentMemory);
        assertNotEquals("DateTime should differ.", firstMemory, dateMemory);
        assertNotEquals("ImagePath should differ.", firstMemory, imagePathMemory);
        assertNotEquals("Tags should differ.", firstMemory, tagMemory);
    }

    @Test
    public void equals_nullObject_returnFalse_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        assertNotSame(firstMemory, null);
    }

    @Test
    public void equals_differentClass_returnFalse_test() {
        Memory firstMemory = createBuilderWithTestData().build();
        assertNotSame(firstMemory, new TestClass());
    }


    @Test
    public void builder_defaultBuild_test() {
        Memory memory = new Builder().build();
        assertEquals(DEF_MEMORY, memory);
    }

    @Test
    public void builder_clearTags_test() {
        Memory memory = createBuilderWithTestData().addTag(new Tag(1, "l")).clearTags().build();
        assertTrue(memory.listTags().isEmpty());
    }

    @Test(expected = NameDuplicateException.class)
    public void builder_addTagWithDuplicateName_throw_test() throws IdDuplicateException, NameDuplicateException {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(2, "TAG");
        createBuilderWithTestData().clearTags().addTagOrThrow(tag).addTagOrThrow(tagCopy).build();
    }

    @Test
    public void builder_addTagWithDuplicateName_noThrow_test() {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(2, "TAG");
        Memory memory = createBuilderWithTestData().clearTags().addTag(tag).addTag(tagCopy).build();

        assertEquals(1, memory.listTags().size());
    }

    @Test(expected = IdDuplicateException.class)
    public void builder_addTagWithDuplicateId_throw_test() throws IdDuplicateException, NameDuplicateException {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(1, "TAG2");
        createBuilderWithTestData().clearTags().addTagOrThrow(tag).addTagOrThrow(tagCopy).build();
    }

    @Test
    public void builder_addTagWithDuplicateId_noThrow_test() {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(1, "TAG2");
        Memory memory = createBuilderWithTestData().clearTags().addTag(tag).addTag(tagCopy).build();

        assertEquals(1, memory.listTags().size());
    }

    @Test(expected = IdDuplicateException.class)
    public void builder_addDuplicateTag_throwIdException_test() throws IdDuplicateException, NameDuplicateException {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(1, "TAG");
        createBuilderWithTestData().clearTags().addTagOrThrow(tag).addTagOrThrow(tagCopy).build();
    }

    @Test
    public void builder_addDuplicateTag_noThrow_test() {
        Tag tag = new Tag(1, "TAG");
        Tag tagCopy = new Tag(1, "TAG");
        Memory memory = createBuilderWithTestData().clearTags().addTag(tag).addTag(tagCopy).build();

        assertEquals(1, memory.listTags().size());
    }

    @Test(expected = NullPointerException.class)
    public void builder_addNull_throw_test() throws IdDuplicateException, NameDuplicateException {
        createBuilderWithTestData().addTagOrThrow(null);
    }

    @Test
    public void builder_addNull_noThrow_test() {
        Memory memory = createBuilderWithTestData().clearTags().addTag(null).build();
        assertTrue(memory.listTags().isEmpty());
    }

    @Test
    public void builder_addMultipleTagsSomeDuplicateIds_test() {
        Tag tag1 = new Tag(1, "1");
        Tag tag1copy = new Tag(1, "1");
        Tag tag2 = new Tag(2, "2");

        Memory memory = new Builder().addTags(Lists.newArrayList(tag1, tag1copy, tag2)).build();

        assertEquals(2, memory.listTags().size());
    }

    @Test
    public void builder_removeTag_tagRemoved_test() {
        Tag tag = new Tag(1, "1");
        Tag tag2 = new Tag(2, "2");
        Tag tag2copy = new Tag(2, "2");

        Builder builder = new Builder().addTag(tag).addTag(tag2)
                .removeTag(tag);
        Memory memory = builder.build();
        assertEquals("TagEntity not removed with a copy obj.", 1, memory.listTags().size());

        memory = builder.removeTag(tag2copy).build();
        assertTrue("TagEntity not removed with original obj.", memory.listTags().isEmpty());
    }

    @Test
    public void builder_removeTag_notFullMatch_notRemoved_test() {
        Tag tag = new Tag(1, "1");
        Tag tag2 = new Tag(2, "2");
        Tag noMatchTag = new Tag(1, "2");

        Memory memory = new Builder().addTag(tag).addTag(tag2).removeTag(noMatchTag).build();
        assertEquals(2, memory.listTags().size());
    }

    @Test
    public void builder_removeTag_completeMismatch_test() {
        Tag tag = new Tag(1, "1");
        Tag tag2 = new Tag(2, "2");
        Tag noMatchTag = new Tag(3, "4");

        Memory memory = new Builder().addTag(tag).addTag(tag2).removeTag(noMatchTag).build();
        assertEquals(2, memory.listTags().size());
    }

    @Test
    public void builder_removeTag_null_notRemoved_test() {
        Memory memory = createBuilderWithTestData().build();
        Memory memoryWithRemovedTag = createBuilderWithTestData().removeTag(null).build();

        assertEquals(memory, memoryWithRemovedTag);

    }

    private Builder createBuilderWithTestData() {
        final long id = 1, dateTime = 120350;
        final String title = "MEMORY", comment = "brief comment", imagePath = "path/to/image";
        final List<Tag> tags = Lists.newArrayList(new Tag(1,"Tag1"),
                new Tag(2, "Tag2"), new Tag(3, "Tag3"));

        return new Builder().memoryId(1).title(title).comment(comment)
                .dateTime(dateTime).imagePath(imagePath).addTags(tags);
    }
}
