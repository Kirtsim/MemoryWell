package fm.kirtsim.kharos.memorywell.db.mock;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Tag;

public final class TagMocks {

    private static final List<Tag> tags = Lists.newArrayList(
            new Tag(1, "Stock_1"),
            new Tag(2, "Stockport_2"),
            new Tag(3, "Stockade_3"),
            new Tag(4, "String_4"),
            new Tag(12, "money_12"),
            new Tag(6, "monday_6"),
            new Tag(7, "movieStar_7"),
            new Tag(8, "TAG_8"),
            new Tag(9, "Tag_8"),
            new Tag(10, "taG_8"),
            new Tag(5, "zipper_5")
    );

    public static int tagCount() {
        return tags.size();
    }

    public static List<Tag> getMockTags() {
        final List<Tag> mocks = Lists.newArrayList();
        for (Tag tag : tags)
            mocks.add(new Tag(tag));
        return mocks;
    }

    public static String tagsMatchingNames3(List<Tag> outTags) {
        outTags.addAll(tags.subList(0, 3));
        return "Stock";
    }

    public static String tagWithUniqueNamePrefix(Tag tag) {
        final Tag uniqueTag = tags.get(tags.size() - 1);
        tag.id = uniqueTag.id;
        tag.name = uniqueTag.name;

        return uniqueTag.name.substring(0,3);
    }

    public static String missingTagPrefix() {
        return "Xena";
    }

    public static String tagNameWithInvertedCase() {
        return "MOVIEsTAR_7";
    }
}
