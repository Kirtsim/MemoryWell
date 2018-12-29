package fm.kirtsim.kharos.memorywell.repository.mapper;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;

public interface IMemoryListBuilder {

    IMemoryListBuilder includeMemories(List<MemoryEntity> memoryEntities);

    IMemoryListBuilder includeTaggings(List<TaggingEntity> taggingEntities);

    IMemoryListBuilder includeTags(List<TagEntity> tagEntities);

    MemoryList buildMemoryList();
}
