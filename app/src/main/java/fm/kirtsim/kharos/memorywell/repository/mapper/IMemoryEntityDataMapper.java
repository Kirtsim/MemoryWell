package fm.kirtsim.kharos.memorywell.repository.mapper;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;

public interface IMemoryEntityDataMapper {

    Memory mapEntity(MemoryEntity entity, List<Tag> tags);

}
