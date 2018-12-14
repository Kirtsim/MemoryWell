package fm.kirtsim.kharos.memorywell.db;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Memory;

public class MemoryList {

    public final List<Memory> memories;

    public MemoryList(List<Memory> memories) {
        this.memories = memories;
    }
}
