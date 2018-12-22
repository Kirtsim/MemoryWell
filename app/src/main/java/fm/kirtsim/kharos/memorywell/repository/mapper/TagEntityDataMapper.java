package fm.kirtsim.kharos.memorywell.repository.mapper;

import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.model.Tag;

public final class TagEntityDataMapper implements ITagEntityDataMapper {

    @Override
    public Tag mapEntity(TagEntity entity)  {
        return new Tag(entity.id, entity.name);
    }

}
