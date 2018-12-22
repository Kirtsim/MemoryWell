package fm.kirtsim.kharos.memorywell.repository.mapper;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;


public class MemoryEntityDataMapper implements IMemoryEntityDataMapper{

    @Override
    public Memory mapEntity(MemoryEntity entity, List<Tag> tags) {
        Memory.Builder builder = new Memory.Builder();
        builder.memoryId(entity.id)
                .title(entity.title)
                .comment(entity.comment)
                .imagePath(entity.imagePath)
                .dateTime(entity.dateTime);
        builder.addTags(tags);
        return builder.build();
    }

}
