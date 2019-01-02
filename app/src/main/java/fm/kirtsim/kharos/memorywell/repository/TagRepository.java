package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import fm.kirtsim.kharos.memorywell.concurrency.ThreadPoster;
import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.dao.TagDao;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;

public class TagRepository implements ITagRepository {

    private TagDao tagDao;
    private ThreadPoster threadPoster;

    public TagRepository(TagDao tagDao, ThreadPoster threadPoster) {
        this.tagDao = tagDao;
        this.threadPoster = threadPoster;
    }

    @Override
    public LiveData<Resource<List<TagEntity>>> listAllTags() {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<List<TagEntity>>> listTagsWithPrefix(String prefix) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<List<TagEntity>>> listTagsForMemoryId(long memoryId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Long>> addTag(TagEntity tag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LiveData<Resource<Integer>> removeTags(List<TagEntity> tag) {
        throw new UnsupportedOperationException();
    }
}
