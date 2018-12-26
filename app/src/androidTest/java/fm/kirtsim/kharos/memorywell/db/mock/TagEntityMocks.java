package fm.kirtsim.kharos.memorywell.db.mock;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;

public final class TagMocks {

    private static final List<TagEntity> tags = Lists.newArrayList(
            new TagEntity(1, "Stock_1"),
            new TagEntity(2, "Stockport_2"),
            new TagEntity(3, "Stockade_3"),
            new TagEntity(4, "String_4"),
            new TagEntity(12, "money_12"),
            new TagEntity(6, "monday_6"),
            new TagEntity(7, "movieStar_7"),
            new TagEntity(8, "TAG_8"),
            new TagEntity(9, "Tag_8"),
            new TagEntity(10, "taG_8"),
            new TagEntity(5, "zipper_5")
    );

    public static int tagCount() {
        return tags.size();
    }

    public static List<TagEntity> getMockTags() {
        final List<TagEntity> mocks = Lists.newArrayList();
        for (TagEntity tag : tags)
            mocks.add(new TagEntity(tag));
        return mocks;
    }

    public static String tagsMatchingNames3(List<TagEntity> outTags) {
        outTags.addAll(tags.subList(0, 3));
        return "Stock";
    }

    public static String tagWithUniqueNamePrefix(TagEntity tag) {
        final TagEntity uniqueTag = tags.get(tags.size() - 1);
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
