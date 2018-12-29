package fm.kirtsim.kharos.memorywell.repository.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;

public class MemoryListBuilder implements IMemoryListBuilder {

    private IMemoryEntityDataMapper memoryMapper;
    private ITagEntityDataMapper tagMapper;

    private Map<Long, MemoryEntity> memoryEntities;
    private Map<Long, List<Long>> tagIdsByMemoryId;
    private Map<Long, Tag> tags;

    public MemoryListBuilder(IMemoryEntityDataMapper memoryMapper,
                             ITagEntityDataMapper tagMapper) {
        this.memoryMapper = memoryMapper;
        this.tagMapper = tagMapper;
        memoryEntities = Maps.newConcurrentMap();
        tagIdsByMemoryId = Maps.newConcurrentMap();
        tags = Maps.newConcurrentMap();
    }

    @Override
    public IMemoryListBuilder includeMemories(List<MemoryEntity> memoryEntities) {
        this.memoryEntities.clear();
        for (MemoryEntity entity : memoryEntities) {
            this.memoryEntities.put(entity.id, entity);
        }
        return this;
    }

    @Override
    public IMemoryListBuilder includeTaggings(List<TaggingEntity> taggingEntities) {
        tagIdsByMemoryId.clear();
        for (TaggingEntity entity : taggingEntities) {
            List<Long> tagIds = tagIdsByMemoryId.get(entity.memoryId);
            if (tagIds == null) {
                tagIds = Lists.newArrayList();
                tagIdsByMemoryId.put(entity.memoryId, tagIds);
            }
            tagIds.add(entity.tagId);
        }
        return this;
    }

    @Override
    public IMemoryListBuilder includeTags(List<TagEntity> tagEntities) {
        tags.clear();
        for (TagEntity entity : tagEntities) {
            Tag tag = tagMapper.mapEntity(entity);
            tags.put(tag.id(), tag);
        }
        return this;
    }

    @Override
    public MemoryList buildMemoryList() {
        if (memoryEntities.isEmpty())
            return new MemoryList(Lists.newArrayList());

        final List<Memory> memories = Lists.newArrayList();
        if (tagIdsByMemoryId.isEmpty() || tags.isEmpty()) {
            final List<Tag> emptyList = Lists.newArrayList();
            for (MemoryEntity entity : memoryEntities.values()) {
                memories.add(memoryMapper.mapEntity(entity, emptyList));
            }
            return new MemoryList(memories);
        }

        for (Map.Entry<Long, MemoryEntity> entityEntry : memoryEntities.entrySet()) {
            final long id = entityEntry.getKey();
            MemoryEntity entity = entityEntry.getValue();

            List<Long> tagIds = tagIdsByMemoryId.get(id);
            List<Tag> tags = Lists.newArrayList();
            if (tagIds != null) {
                for (Long tagId : tagIds) {
                    Tag tag = this.tags.get(tagId);
                    if (tag != null)
                        tags.add(tag);
                }
            }
            memories.add(memoryMapper.mapEntity(entity, tags));
        }
        return new MemoryList(memories);
    }
}
