package fm.kirtsim.kharos.memorywell.db.entity;

import java.util.List;

import fm.kirtsim.kharos.memorywell.model.Memory;

public class MemoryList {

    public final List<Memory> memories;

    public MemoryList(List<Memory> memories) {
        this.memories = memories;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MemoryList))
            return false;
        if (this == obj)
            return true;
        MemoryList other = (MemoryList) obj;
        if (other.memories.size() != this.memories.size())
            return false;
        for (int i = 0; i < memories.size(); ++i) {
            Memory otherMemory = other.memories.get(i);
            Memory memory = memories.get(i);
            if (memory == null && otherMemory != null)
                return false;
            if (memory != null && !memory.equals(otherMemory))
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String memoryStr = "";
        if (memories.size() > 0)
            memoryStr += "<" + memories.get(0) + ">";
        if (memories.size() > 1)
            memoryStr += " ... <" + memories.get(memories.size() - 1) + ">";
        return "MemoryList [" + memoryStr + "]";
    }
}
