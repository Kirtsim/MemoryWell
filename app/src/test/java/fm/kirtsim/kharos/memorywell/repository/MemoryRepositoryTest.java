package fm.kirtsim.kharos.memorywell.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

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
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fm.kirtsim.kharos.memorywell.repository.mapper.IMemoryListBuilder;
import fm.kirtsim.kharos.memorywell.repository.mapper.MemoryEntityDataMapper;
import fm.kirtsim.kharos.memorywell.repository.mapper.MemoryListBuilder;
import fm.kirtsim.kharos.memorywell.repository.mapper.TagEntityDataMapper;

import static fmShared.kirtsim.kharos.memorywell.db.mock.MemoryEntityMocks.getMockMemories;
import static fmShared.kirtsim.kharos.memorywell.db.mock.TagEntityMocks.getMockTags;
import static fmShared.kirtsim.kharos.memorywell.db.mock.TaggingEntityMocks.getMockTaggings;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class MemoryRepositoryTest {

    private ThreadPoster threadPoster;
    private IMemoryRepository repo;
    @Mock private MemoryDao memoryDao;
    @Mock private TaggingDao taggingDao;
    @Mock private TagDao tagDao;
    @Mock
    private Observer<Resource<MemoryList>> observer;
    private MemoryListBuilder memoryBuildCoordinator;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initBeforeEachTest() {
        MockitoAnnotations.initMocks(this);
        threadPoster = Runnable::run;
        memoryBuildCoordinator = new MemoryListBuilder(new MemoryEntityDataMapper(),
                new TagEntityDataMapper());

        final IMemoryListBuilder resCoordinator =
                new MemoryListBuilder(new MemoryEntityDataMapper(),
                new TagEntityDataMapper());

        repo = new MemoryRepository(memoryDao, tagDao, taggingDao, resCoordinator ,threadPoster);
    }

    @Test
    public void listMemories_test() {
        LiveData<List<MemoryEntity>> memoryLiveData = createLiveData(getMockMemories());
        when(memoryDao.selectAll()).thenReturn(memoryLiveData);

        LiveData<List<TaggingEntity>> taggingLiveData = createLiveData(getMockTaggings());
        when(taggingDao.selectAll()).thenReturn(taggingLiveData);

        LiveData<List<TagEntity>> tagLiveData = createLiveData(getMockTags());
        when(tagDao.selectAll()).thenReturn(tagLiveData);

        repo.listMemories().observeForever(observer);
        verify(observer).onChanged(memoryBuildCoordinator.includeMemories(getMockMemories()));
        verify(observer).onChanged(memoryBuildCoordinator.includeTaggings(getMockTaggings()));
        verify(observer).onChanged(memoryBuildCoordinator.includeTags(getMockTags()));
    }

    @Test
    public void listMemories_newMemoriesAdded_test() {
        List<MemoryEntity> originalMemories = getMockMemories();
        LiveData<List<MemoryEntity>> memoryLiveData = createLiveData(originalMemories);
        when(memoryDao.selectAll()).thenReturn(memoryLiveData);

        LiveData<List<TaggingEntity>> taggingLiveData = createLiveData(getMockTaggings());
        when(taggingDao.selectAll()).thenReturn(taggingLiveData);

        LiveData<List<TagEntity>> tagLiveData = createLiveData(getMockTags());
        when(tagDao.selectAll()).thenReturn(tagLiveData);


    }

    private static <T> MutableLiveData<List<T>>  createLiveData(List<T> entities) {
        final MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        liveData.setValue(entities);
        return liveData;
    }
}
