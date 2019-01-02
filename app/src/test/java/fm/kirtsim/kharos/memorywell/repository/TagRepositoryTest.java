package fm.kirtsim.kharos.memorywell.repository;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
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

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;

import static fmShared.kirtsim.kharos.memorywell.db.mock.TagEntityMocks.getMockTags;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TagRepositoryTest {

    private ITagRepository repo;
    @Mock private TagDao tagDao;
    @Mock private Observer<Resource<List<TagEntity>>> selectionObserver;
    @Mock private Observer<Resource<Long>> additionObserver;
    @Mock private Observer<Resource<Integer>> deletionObserver;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void initBeforeEachTest() {
        MockitoAnnotations.initMocks(this);

        repo = new TagRepository(tagDao, Runnable::run);
    }

    @Test
    public void listAllTags_test() {
        MutableLiveData<List<TagEntity>> memoryLiveData = createLiveData(getMockTags());
        when(tagDao.selectAll()).thenReturn(memoryLiveData);

        repo.listAllTags().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockTags()));
    }

    @Test
    public void listAllTags_newMemoryAdded_test() {
        TagEntity newTag = new TagEntity(100, "blahTest");
        List<TagEntity> originalTags = getMockTags();
        List<TagEntity> newTags = getMockTags();
        newTags.add(newTag);
        MutableLiveData<List<TagEntity>> memoryLiveData = createLiveData(getMockTags());

        when(tagDao.selectAll()).thenReturn(memoryLiveData);
        when(tagDao.insert(any())).then(i -> updateMemoryLiveData(memoryLiveData, originalTags));

        repo.listAllTags().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockTags()));

        tagDao.insert(Lists.newArrayList(newTag));
        verify(selectionObserver).onChanged(Resource.success(newTags));
    }
    
    private List<Long> updateMemoryLiveData(MutableLiveData<List<TagEntity>> liveData, List<TagEntity> entities) {
        liveData.postValue(entities);
        return Lists.newArrayList();
    }

    @Test
    public void listTagsWithPrefix_test() {
        MutableLiveData<List<TagEntity>> memoryLiveData = createLiveData(getMockTags());

        when(tagDao.selectTagsWithNamesStarting(anyString())).thenReturn(memoryLiveData);
        repo.listTagsWithPrefix("blah");
        verify(selectionObserver).onChanged(Resource.success(getMockTags()));
    }

    @Test
    public void listTagsForMemoryId_test() {
        MutableLiveData<List<TagEntity>> memoryLiveData = createLiveData(getMockTags());

        when(tagDao.selectByMemoryId(anyLong())).thenReturn(memoryLiveData);
        repo.listTagsForMemoryId(3);
        verify(selectionObserver).onChanged(Resource.success(getMockTags()));
    }

    @Test
    public void addTag_liveDataUpdated_test() {
        MutableLiveData<List<TagEntity>> memoryLiveData = createLiveData(getMockTags());

        when(tagDao.selectAll()).thenReturn(memoryLiveData);
        when(tagDao.insert(any())).thenReturn(Lists.newArrayList(20L));

        repo.listAllTags().observeForever(selectionObserver);
        verify(selectionObserver).onChanged(Resource.success(getMockTags()));

        repo.addTag(new TagEntity()).observeForever(additionObserver);
        verify(additionObserver).onChanged(Resource.success(20L));
    }

    @Test
    public void addTag_SQLiteConstraintExceptionThrown_test() {
        when(tagDao.insert(any())).thenThrow(new SQLiteConstraintException(""));
        repo.addTag(new TagEntity()).observeForever(additionObserver);

        verify(additionObserver).onChanged(Resource.error("", -1L));
    }

    @Test
    public void removeTags_test() {
        int removeCount = 3;
        when(tagDao.delete(any())).thenReturn(removeCount);
        
        repo.removeTags(Lists.newArrayList()).observeForever(deletionObserver);
        verify(deletionObserver).onChanged(Resource.success(removeCount));
    }

    @Test
    public void removeTags_throwSQLiteConstraintException_test() {
        when(tagDao.delete(any())).thenThrow(new SQLiteConstraintException(""));

        repo.removeTags(Lists.newArrayList()).observeForever(deletionObserver);
        verify(deletionObserver).onChanged(Resource.error("", 0));
    }

    private static <T> MutableLiveData<List<T>> createLiveData(List<T> entities) {
        final MutableLiveData<List<T>> liveData = new MutableLiveData<>();
        liveData.setValue(entities);
        return liveData;
    }
}
