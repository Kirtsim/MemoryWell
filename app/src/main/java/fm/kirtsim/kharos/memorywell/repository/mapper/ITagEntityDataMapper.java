package fm.kirtsim.kharos.memorywell.repository.mapper;

import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.model.Tag;

public interface ITagEntityDataMapper {

    Tag mapEntity(TagEntity entity);

}
