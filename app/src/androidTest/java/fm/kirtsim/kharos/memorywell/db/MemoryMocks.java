package fm.kirtsim.kharos.memorywell.db;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Memory;

final class MemoryMocks {

    private static final List<Memory> memories = Lists.newArrayList(
            new Memory(1, "Memory 1", "Comment 1", 1, "path/im1"),
            new Memory(2, "Memory 2", "Comment 2", 2, "path/im2"),
            new Memory(5, "Memory 5", "Comment 5", 12, "path/im12"),
            new Memory(3, "Memory 3", "Comment 3", 3, "path/im3"),
            new Memory(4, "Memory 4", "Comment 4", 4, "path/im4"),
            new Memory(6, "Memory 6", "Comment 6", 6, "path/im6"),
            new Memory(7, "Memory 7", "Comment 7", 7, "path/im7"),
            new Memory(8, "Memory 8", "Comment 8", 8, "path/im8"),
            new Memory(9, "Memory 9", "Comment 9", 9, "path/im9"),
            new Memory(10, "Memory 10", "Comment 10", 10, "path/im10"),
            new Memory(12, "Memory 12", "Comment 12", 5, "path/im12"),
            new Memory(13, "Memory 13", "Comment 13", 13, "path/im13")
    );

    private MemoryMocks() {}

    public static int mockCount() {
        return memories.size();
    }

    public static List<Memory> getMockMemories() {
        return getFirstNMemories(memories.size());
    }

    public static List<Memory> getFirstNMemories(int n) {
        if (n > memories.size())
            throw new IllegalArgumentException("'n' is too large.");
        final List<Memory> mocks = Lists.newArrayListWithCapacity(memories.size());
        for (int index = 0; index < n; ++index)
            mocks.add(new Memory(memories.get(index)));
        return mocks;
    }

    static List<Memory> memoriesWithinTimeRange(long from, long to) {
        final List<Memory> mocks = Lists.newArrayList();
        for (Memory memory : memories)
            if (memory.id <= to && memory.id >= from)
                mocks.add(new Memory(memory));
        return mocks;
    }
}
