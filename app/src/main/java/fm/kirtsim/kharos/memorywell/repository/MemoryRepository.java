package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.database.sqlite.SQLiteConstraintException;

import com.google.common.collect.Lists;

import java.util.List;

import fm.kirtsim.kharos.memorywell.concurrency.ThreadPoster;
import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;

public final class MemoryRepository implements IMemoryRepository {

    private MemoryDao memoryDao;
    private LiveData<Resource<List<MemoryEntity>>> allMemories;
    private ThreadPoster threadPoster;

    public MemoryRepository(MemoryDao memoryDao, ThreadPoster threadPoster) {
        this.memoryDao = memoryDao;
        this.threadPoster = threadPoster;
    }

    @Override
    public LiveData<Resource<List<MemoryEntity>>> listAllMemories() {
        if (allMemories == null)
            allMemories = Transformations.map(memoryDao.selectAll(), Resource::success);
        return allMemories;
    }

    @Override
    public LiveData<Resource<List<MemoryEntity>>> listMemoriesWithinTimeRange(long from, long to) {
        return Transformations.map(memoryDao.selectByTimeRange(from, to), Resource::success);
    }

    @Override
    public LiveData<Resource<List<Long>>> addMemory(MemoryEntity memory) {
        MutableLiveData<Resource<List<Long>>> liveData = new MutableLiveData<>();
        threadPoster.post(() -> {
            try {
                List<Long> ids = memoryDao.insert(Lists.newArrayList(memory));
                liveData.postValue(Resource.success(ids));
            } catch (SQLiteConstraintException ex) {
                liveData.postValue(Resource.error(ex.getMessage(), Lists.newArrayList()));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<Resource<MemoryEntity>> updateMemory(MemoryEntity memory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Integer>> removeMemories(List<MemoryEntity> memories) {
        MutableLiveData<Resource<Integer>> liveData = new MutableLiveData<>();
        threadPoster.post(() -> {
            try {
                int count = memoryDao.delete(memories);
                liveData.postValue(Resource.success(count));
            } catch (SQLiteConstraintException ex) {
                liveData.postValue(Resource.error(ex.getMessage(), 0));
            }
        });
        return liveData;
    }
}
