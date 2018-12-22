package fm.kirtsim.kharos.memorywell.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class TagEntityTest {

    private static final long TEST_ID = 1;
    private static final String TEST_NAME = "TEST_TAG";

    private static final class TestClass {}

    @Test
    public void hashCode_allSame_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag secondTag = new Tag(TEST_ID, TEST_NAME);

        assertEquals(firstTag.hashCode(), secondTag.hashCode());
    }

    @Test
    public void hashCode_diffAttribute_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag idTag = new Tag(500, TEST_NAME);
        Tag nameTag = new Tag(TEST_ID, "diff_name");

        assertNotSame("Assertion for ID-tag.", firstTag.hashCode(), idTag.hashCode());
        assertNotSame("Assertion for Name-tag", firstTag.hashCode(), nameTag.hashCode());
    }

    @Test
    public void equals_allEqual_returnTrue_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag secondTag = new Tag(TEST_ID, TEST_NAME);

        assertEquals(firstTag, secondTag);
    }

    @Test
    public void equals_idDiff_returnFalse_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag secondTag = new Tag(3, TEST_NAME);
        assertNotSame(firstTag, secondTag);
    }

    @Test
    public void equals_nameDiff_returnFalse_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag secondTag = new Tag(TEST_ID, "DIFFERENT_NAME");
        assertNotSame(firstTag, secondTag);
    }

    @Test
    public void equals_allDiff_returnFalse_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        Tag secondTag = new Tag(3, "DIFFERENT_NAME");
        assertNotSame(firstTag, secondTag);
    }

    @Test
    public void equals_compareToNull_returnFalse_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        assertNotSame(firstTag, null);
    }

    @Test
    public void equals_diffClass_returnFalse_test() {
        Tag firstTag = new Tag(TEST_ID, TEST_NAME);
        TestClass obj = new TestClass();
        assertNotSame(firstTag, obj);
    }

//    @Test
//    public void setTest() {
//        Set<TagEntity> tags1 = Sets.newConcurrentHashSet();
//        Set<TagEntity> tags2 = Sets.newConcurrentHashSet();
//        tags1.add(new TagEntity(1, "a"));
//        tags1.add(new TagEntity(2, "b"));
//
//        tags2.add(new TagEntity(1, "a"));
//        tags2.add(new TagEntity(2, "b"));
//
//        assertEquals(tags1.hashCode(), tags2.hashCode());
//    }
}
