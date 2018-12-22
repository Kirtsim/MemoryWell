package fm.kirtsim.kharos.memorywell.db.entity;

import java.util.List;

import fm.kirtsim.kharos.memorywell.model.Memory;

public class MemoryList {

    public final List<Memory> memories;

    public MemoryList(List<Memory> memories) {
        this.memories = memories;
    }
}
