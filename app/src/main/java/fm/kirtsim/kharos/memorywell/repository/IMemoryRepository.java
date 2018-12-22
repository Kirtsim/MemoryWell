package fm.kirtsim.kharos.memorywell.repository;

import android.arch.lifecycle.LiveData;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagList;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;

public interface IMemoryRepository {

    LiveData<Resource<MemoryList>> listMemories();

    LiveData<Resource<MemoryList>> listMemoriesWithinTimeRange(long from, long to);

    LiveData<Resource<Memory>> addNewMemory(Memory memory);

    LiveData<Resource<Memory>> updateMemory(Memory memory);

    LiveData<Resource<Memory>> deleteMemory(Memory memory);

    LiveData<Resource<MemoryList>> deleteMemories(MemoryList memory);


    LiveData<Resource<TagList>> listAllTags();

    LiveData<Resource<TagList>> listTagsWithPrefix(String prefix);

    LiveData<Resource<Tag>> addNewTag(Tag tag);

    LiveData<Resource<Tag>> deleteTag(Tag tag);

    LiveData<Resource<TagList>> deleteTags(TagList tags);

    LiveData<Resource<Tag>> updateTag(Tag tag);

}
