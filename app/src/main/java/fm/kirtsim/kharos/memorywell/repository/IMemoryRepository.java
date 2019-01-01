package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;

public interface IMemoryRepository {

    LiveData<Resource<List<MemoryEntity>>> listAllMemories();

    LiveData<Resource<List<MemoryEntity>>> listMemoriesWithinTimeRange(long from, long to);

    LiveData<Resource<List<Long>>> addMemory(MemoryEntity memory);

    LiveData<Resource<MemoryEntity>> updateMemory(MemoryEntity memory);

    LiveData<Resource<Integer>> removeMemories(List<MemoryEntity> memory);

}
