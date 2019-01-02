package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;

public interface ITagRepository {

    LiveData<Resource<List<TagEntity>>> listAllTags();

    LiveData<Resource<List<TagEntity>>> listTagsWithPrefix(String prefix);

    LiveData<Resource<List<TagEntity>>> listTagsForMemoryId(long memoryId);

    LiveData<Resource<Long>> addTag(TagEntity tag);

    LiveData<Resource<Integer>> removeTags(List<TagEntity> tag);
}
