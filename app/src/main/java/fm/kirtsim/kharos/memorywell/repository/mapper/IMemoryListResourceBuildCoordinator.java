package fm.kirtsim.kharos.memorywell.repository.mapper;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;

public interface IMemoryListResourceBuildCoordinator {

    Resource<MemoryList> updateMemoryEntities(List<MemoryEntity> memoryEntities);

    Resource<MemoryList> updateTaggingEntities(List<TaggingEntity> taggingEntities);

    Resource<MemoryList> updateTagEntities(List<TagEntity> tagEntities);

}
