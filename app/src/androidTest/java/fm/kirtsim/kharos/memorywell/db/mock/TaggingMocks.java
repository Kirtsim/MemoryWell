package fm.kirtsim.kharos.memorywell.db.mock;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Tagging;

import static java.util.stream.Collectors.toList;

public final class TaggingMocks {

    private static final class Holder {
        long memoryId;
        List<Long> tagIds;

        Holder(long memoryId, List<Long> tagIds) {
            this.memoryId = memoryId;
            this.tagIds = tagIds;
        }
    }

    private static final long[] tagIdsInMultipleTaggings = { 1, 3, 5 };

    private static final List<Holder> taggings = Lists.newArrayList(
            new Holder(1, tagIds(1, 2, 3, 4, 5)),
            new Holder(2, tagIds(6, 7, 8)),
            new Holder(3, tagIds(1, 3, 5)),
            new Holder(4, tagIds(1, 6, 9)),
            new Holder(5, tagIds(9))
    );

    private static List<Long> tagIds(long ... ids) {
        List<Long> tagIds = Lists.newArrayList();
        for (Long id : ids)
            tagIds.add(id);
        return tagIds;
    }

    public static List<Tagging> getMockTaggings() {
        List<Tagging> mocks = Lists.newArrayList();
        for(Holder holder : taggings)
            addGeneratedTaggings(holder, mocks);
        return mocks;
    }

    private static void addGeneratedTaggings(Holder holder, List<Tagging> taggings) {
        for (Long tagId : holder.tagIds)
            taggings.add(new Tagging(holder.memoryId, tagId));
    }

    public static List<List<Tagging>> getTestTagginsByMemoryIds() {
        List<List<Tagging>> mocks = Lists.newArrayList(Lists.newArrayList(), Lists.newArrayList());
        addGeneratedTaggings(taggings.get(0), mocks.get(0));
        addGeneratedTaggings(taggings.get(1), mocks.get(1));
        return mocks;
    }

    public static List<List<Tagging>> getTestTaggingsByTagIds() {
        List<List<Tagging>> mocks = Lists.newArrayListWithCapacity(tagIdsInMultipleTaggings.length);
        for (Long tagId : tagIdsInMultipleTaggings) {
            List<Tagging> testTaggins = taggings.stream().filter(h -> h.tagIds.contains(tagId))
                    .map(h -> new Tagging(h.memoryId, tagId)).collect(toList());
            mocks.add(testTaggins);
        }
        return mocks;
    }

    public static List<Long> getUnboundMemoryIds() {
        final List<Long> boundIds = taggings.stream().map(h -> h.memoryId).collect(toList());
        List<Long> allIds = MemoryMocks.getMockMemories().stream().map(m -> m.id).collect(toList());

        return allIds.stream().filter(id -> !boundIds.contains(id)).collect(toList());
    }

    public static List<Long> getBoundMemoryIds() {
        final List<Long> boundIds = taggings.stream().map(h -> h.memoryId).collect(toList());
        List<Long> allIds = MemoryMocks.getMockMemories().stream().map(m -> m.id).collect(toList());

        return allIds.stream().filter(boundIds::contains).collect(toList());
    }

    public static List<Long> getUnboundTagIds() {
        List<Long> boundIds = taggings.stream().flatMap(h -> h.tagIds.stream()).distinct().collect(toList());
        List<Long> allTagIds = TagMocks.getMockTags().stream().map(t -> t.id).collect(toList());

        return allTagIds.stream().filter(id -> !boundIds.contains(id)).collect(toList());
    }

    public static List<Long> getBoundTagIds() {
        List<Long> boundIds = taggings.stream().flatMap(h -> h.tagIds.stream()).distinct().collect(toList());
        List<Long> allTagIds = TagMocks.getMockTags().stream().map(t -> t.id).collect(toList());

        return allTagIds.stream().filter(boundIds::contains).collect(toList());

    }
}
