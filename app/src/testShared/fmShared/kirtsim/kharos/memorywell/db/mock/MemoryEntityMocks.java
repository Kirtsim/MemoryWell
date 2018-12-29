package fmShared.kirtsim.kharos.memorywell.db.mock;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;

public final class MemoryEntityMocks {

    private static final List<MemoryEntity> memories = Lists.newArrayList(
            new MemoryEntity(1, "MemoryEntity 1", "Comment 1", 1, "path/im1"),
            new MemoryEntity(2, "MemoryEntity 2", "Comment 2", 2, "path/im2"),
            new MemoryEntity(5, "MemoryEntity 5", "Comment 5", 12, "path/im12"),
            new MemoryEntity(3, "MemoryEntity 3", "Comment 3", 3, "path/im3"),
            new MemoryEntity(4, "MemoryEntity 4", "Comment 4", 4, "path/im4"),
            new MemoryEntity(6, "MemoryEntity 6", "Comment 6", 6, "path/im6"),
            new MemoryEntity(7, "MemoryEntity 7", "Comment 7", 7, "path/im7"),
            new MemoryEntity(8, "MemoryEntity 8", "Comment 8", 8, "path/im8"),
            new MemoryEntity(9, "MemoryEntity 9", "Comment 9", 9, "path/im9"),
            new MemoryEntity(10, "MemoryEntity 10", "Comment 10", 10, "path/im10"),
            new MemoryEntity(12, "MemoryEntity 12", "Comment 12", 5, "path/im12"),
            new MemoryEntity(13, "MemoryEntity 13", "Comment 13", 13, "path/im13")
    );

    private MemoryEntityMocks() {}

    public static int mockCount() {
        return memories.size();
    }

    public static List<MemoryEntity> getMockMemories() {
        return getFirstNMemories(memories.size());
    }

    public static List<MemoryEntity> getFirstNMemories(int n) {
        if (n > memories.size())
            throw new IllegalArgumentException("'n' is too large.");
        final List<MemoryEntity> mocks = Lists.newArrayListWithCapacity(memories.size());
        for (int index = 0; index < n; ++index)
            mocks.add(new MemoryEntity(memories.get(index)));
        return mocks;
    }

    public static List<MemoryEntity> memoriesWithinTimeRange(long from, long to) {
        final List<MemoryEntity> mocks = Lists.newArrayList();
        for (MemoryEntity memory : memories)
            if (memory.id <= to && memory.id >= from)
                mocks.add(new MemoryEntity(memory));
        return mocks;
    }
}
