package fm.kirtsim.kharos.memorywell.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.database.sqlite.SQLiteConstraintException;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import fm.kirtsim.kharos.memorywell.concurrency.ThreadPoster;
import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.repository.mapper.MemoryEntityDataMapper;
import fm.kirtsim.kharos.memorywell.repository.mapper.MemoryListBuilder;
import fm.kirtsim.kharos.memorywell.repository.mapper.TagEntityDataMapper;

import static fmShared.kirtsim.kharos.memorywell.db.mock.MemoryEntityMocks.getMockMemories;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class MemoryRepositoryTest {

    private ThreadPoster threadPoster;
    private IMemoryRepository repo;
    @Mock private MemoryDao memoryDao;
    @Mock private Observer<Resource<List<MemoryEntity>>> selectionObserver;
    @Mock private Observer<Resource<List<Long>>> additionObserver;
    @Mock private Observer<Resource<Integer>> deletionObserver;
    private MemoryListBuilder memoryBuildCoordinator;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initBeforeEachTest() {
        MockitoAnnotations.initMocks(this);
        threadPoster = Runnable::run;
        memoryBuildCoordinator = new MemoryListBuilder(new MemoryEntityDataMapper(),
                new TagEntityDataMapper());

        repo = new MemoryRepository(memoryDao, threadPoster);
    }

    @Test
    public void listAllMemories_test() {
        MutableLiveData<List<MemoryEntity>> memoryLiveData = createLiveData(getMockMemories());
        when(memoryDao.selectAll()).thenReturn(memoryLiveData);

        repo.listAllMemories().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockMemories()));
    }

    @Test
    public void listAllMemories_newMemoryAdded_test() {
        List<MemoryEntity> memories = getMockMemories();
        MemoryEntity newMemory = new MemoryEntity(100, "blah", "", 1000, "/p");
        MutableLiveData<List<MemoryEntity>> memoryLiveData = createLiveData(getMockMemories());

        when(memoryDao.selectAll()).thenReturn(memoryLiveData);
        memories.add(newMemory);
        when(memoryDao.insert(any())).then(i -> updateMemoryLiveData(memoryLiveData, memories));

        repo.listAllMemories().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockMemories()));

        memoryDao.insert(Lists.newArrayList(memories));
        verify(selectionObserver).onChanged(Resource.success(memories));
    }

    @Test
    public void listMemoriesWithinTimeRange_test() {
        LiveData<List<MemoryEntity>> liveData = createLiveData(Lists.newArrayList());
        when(memoryDao.selectByTimeRange(anyLong(), anyLong())).thenReturn(liveData);

        repo.listMemoriesWithinTimeRange(1, 2).observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(Lists.newArrayList()));
    }

    private List<Long> updateMemoryLiveData(MutableLiveData<List<MemoryEntity>> liveData, List<MemoryEntity> entities) {
        liveData.postValue(entities);
        return Lists.newArrayList();
    }

    @Test
    public void addMemory_liveDataUpdated_test() {
        MutableLiveData<List<MemoryEntity>> memoryLiveData = createLiveData(getMockMemories());

        when(memoryDao.selectAll()).thenReturn(memoryLiveData);
        when(memoryDao.insert(any())).thenReturn(Lists.newArrayList(20L));

        repo.listAllMemories().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockMemories()));

        repo.addMemory(new MemoryEntity()).observeForever(additionObserver);
        verify(additionObserver).onChanged(Resource.success(Lists.newArrayList(20L)));
    }

    @Test
    public void addMemory_SQLiteConstraintExceptionThrown_test() {
        when(memoryDao.insert(any())).thenThrow(new SQLiteConstraintException(""));
        repo.addMemory(new MemoryEntity()).observeForever(additionObserver);

        verify(additionObserver).onChanged(Resource.error("", Lists.newArrayList()));
    }

    @Test
    public void removeMemories_test() {
        int removeCount = 3;
        when(memoryDao.delete(any())).thenReturn(removeCount);

        repo.removeMemories(Lists.newArrayList()).observeForever(deletionObserver);
        verify(deletionObserver).onChanged(Resource.success(removeCount));
    }

    @Test
    public void removeMemories_throwSQLiteConstraintException_test() {
        when(memoryDao.delete(any())).thenThrow(new SQLiteConstraintException(""));

        repo.removeMemories(Lists.newArrayList()).observeForever(deletionObserver);
        verify(deletionObserver).onChanged(Resource.error("", 0));
    }

    private static <T> MutableLiveData<List<T>>  createLiveData(List<T> entities) {
        final MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        liveData.setValue(entities);
        return liveData;
    }
}
