package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.dao.MemoryDao;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.dao.TaggingDao;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagList;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;
import fm.kirtsim.kharos.memorywell.repository.mapper.IMemoryListResourceBuildCoordinator;

public final class MemoryRepository implements IMemoryRepository {

    private MemoryDao memoryDao;
    private TagDao tagDao;
    private TaggingDao taggingDao;
    private IMemoryListResourceBuildCoordinator memoryResourceCoordinator;

    public MemoryRepository(MemoryDao memoryDao, TagDao tagDao, TaggingDao taggingDao,
                            IMemoryListResourceBuildCoordinator memoryListResourceBuildCoordinator) {
        this.memoryDao = memoryDao;
        this.tagDao = tagDao;
        this.taggingDao = taggingDao;

    }

    @Override
    public LiveData<Resource<MemoryList>> listMemories() {
        MediatorLiveData<Resource<MemoryList>> memoryListLiveData = new MediatorLiveData<>();
        memoryListLiveData.addSource(memoryDao.selectAll(), memoryEntities ->
                memoryListLiveData.setValue(memoryResourceCoordinator.updateMemoryEntities(memoryEntities))
        );

        memoryListLiveData.addSource(taggingDao.selectAll(), taggingEntities ->
                memoryListLiveData.setValue(memoryResourceCoordinator.updateTaggingEntities(taggingEntities))
        );

        memoryListLiveData.addSource(tagDao.selectAll(), tagEntities ->
                memoryListLiveData.setValue(memoryResourceCoordinator.updateTagEntities(tagEntities))
        );

        return memoryListLiveData;
    }

    @Override
    public LiveData<Resource<MemoryList>> listMemoriesWithinTimeRange(long from, long to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Memory>> addNewMemory(Memory memory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Memory>> updateMemory(Memory memory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Memory>> deleteMemory(Memory memory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<MemoryList>> deleteMemories(MemoryList memory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<TagList>> listAllTags() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<TagList>> listTagsWithPrefix(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Tag>> addNewTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Tag>> deleteTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<TagList>> deleteTags(TagList tags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Tag>> updateTag(Tag tag) {
        throw new UnsupportedOperationException();
    }
}
