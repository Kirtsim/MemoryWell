package fmShared.kirtsim.kharos.memorywell.util;

import com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class AssertUtil {

    public static final String ERR_NULL = "The object is null.";
    public static final String ERR_LIST_SIZE = "The size of the list is incorrect: ";
    public static final String ERR_LIST_ITEM = "An item in the list does not match: ";
    public static final String ERR_DB_DELETE_COUNT = "The count of deleted items is incorrect: ";
    public static final String ERR_DB_UPDATE_COUNT = "The count of updated items is incorrect: ";
    public static final String ERR_NO_EXCEPTION = "An exception was expected.";
    public static final String ERR_MISSING_WORD = "A word was missing from a string.";

    private AssertUtil() {}

    public static <O> void assertListsEquals(List<O> expected, List<O> actual,
                                             Comparator<O> comparator ) {
        assertNotNull(ERR_NULL, actual);
        assertEquals(ERR_LIST_SIZE, expected.size(), actual.size());
        sortList(expected, comparator);
        sortList(actual, comparator);
        for (int i = 0; i < expected.size(); ++i)
            assertEquals(ERR_LIST_ITEM, expected.get(i), actual.get(i));
    }

    private static <O> void sortList(List<O> list, Comparator<O> comparator) {
        try {
            list.sort(comparator);
        } catch (UnsupportedOperationException ex) {
            list = Lists.newArrayList(list);
        }
        list.sort(comparator);
    }

}
