package fm.kirtsim.kharos.memorywell.repository.mapper;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.Status;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;
import fm.kirtsim.kharos.memorywell.db.entity.MemoryList;
import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;
import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;
import fm.kirtsim.kharos.memorywell.model.Memory;
import fm.kirtsim.kharos.memorywell.model.Tag;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public final class MemoryListResourceBuildCoordinatorTest {

    private static final IMemoryEntityDataMapper memoryMapper = new MemoryEntityDataMapper();
    private static final ITagEntityDataMapper tagMapper = new TagEntityDataMapper();

    private IMemoryListResourceBuildCoordinator buildCoordinator;


    @Before
    public void initBeforeEachTest() {
        buildCoordinator = new MemoryListResourceBuildCoordinator(memoryMapper, tagMapper);
    }


    @Test
    public void updateMemoryEntities_noTagsAndTaggings_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> entities = Lists.newArrayList(entity1, entity2);

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1,emptyList),
                memoryMapper.mapEntity(entity2, emptyList));

        Resource<MemoryList> result=  buildCoordinator.updateMemoryEntities(entities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateMemoryEntities_noTaggings_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);

        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        buildCoordinator.updateTagEntities(tagEntities);

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1,emptyList),
                memoryMapper.mapEntity(entity2, emptyList));

        Resource<MemoryList> result=  buildCoordinator.updateMemoryEntities(memoryEntities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateMemoryEntities_noTags_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);

        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        buildCoordinator.updateTaggingEntities(taggingEntities);

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1,emptyList),
                memoryMapper.mapEntity(entity2, emptyList));

        Resource<MemoryList> result=  buildCoordinator.updateMemoryEntities(memoryEntities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateMemoryEntities_AllEntitiesPresent_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);

        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        buildCoordinator.updateTaggingEntities(taggingEntities);

        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        buildCoordinator.updateTagEntities(tagEntities);
        List<Tag> tags = tagEntities.stream().map(tagMapper::mapEntity).collect(toList());

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1, tags),
                memoryMapper.mapEntity(entity2, emptyList));

        Resource<MemoryList> result=  buildCoordinator.updateMemoryEntities(memoryEntities);

        assertEquals(Status.SUCCESS, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }






    @Test
    public void updateTagEntities_noMemoriesAndTaggings_test() {
        List<TagEntity> entities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));

        final List<Memory> emptyList = Lists.newArrayList();

        Resource<MemoryList> result=  buildCoordinator.updateTagEntities(entities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(emptyList, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTagEntities_noTaggings_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);
        buildCoordinator.updateMemoryEntities(memoryEntities);

        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        Resource<MemoryList> result=  buildCoordinator.updateTagEntities(tagEntities);

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1,emptyList),
                memoryMapper.mapEntity(entity2, emptyList));



        assertEquals(Status.LOADING, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTagEntities_noMemories_test() {
        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        buildCoordinator.updateTaggingEntities(taggingEntities);

        final List<Memory> emptyList = Lists.newArrayList();
        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        Resource<MemoryList> result=  buildCoordinator.updateTagEntities(tagEntities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(emptyList, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTagEntities_AllEntitiesPresent_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);
        buildCoordinator.updateMemoryEntities(memoryEntities);

        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        buildCoordinator.updateTaggingEntities(taggingEntities);

        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        Resource<MemoryList> result = buildCoordinator.updateTagEntities(tagEntities);

        List<Tag> tags = tagEntities.stream().map(tagMapper::mapEntity).collect(toList());
        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1, tags),
                memoryMapper.mapEntity(entity2, emptyList));

        assertEquals(Status.SUCCESS, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }




    @Test
    public void updateTaggingEntities_noMemoriesAndTags_test() {
        List<TaggingEntity> entities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));

        final List<Memory> emptyList = Lists.newArrayList();

        Resource<MemoryList> result=  buildCoordinator.updateTaggingEntities(entities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(emptyList, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTaggingEntities_noTags_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);
        buildCoordinator.updateMemoryEntities(memoryEntities);

        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));

        Resource<MemoryList> result = buildCoordinator.updateTaggingEntities(taggingEntities);

        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1,emptyList),
                memoryMapper.mapEntity(entity2, emptyList));

        assertEquals(Status.LOADING, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTaggingEntities_noMemories_test() {
        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        buildCoordinator.updateTaggingEntities(taggingEntities);

        final List<Memory> emptyList = Lists.newArrayList();
        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        Resource<MemoryList> result =  buildCoordinator.updateTagEntities(tagEntities);

        assertEquals(Status.LOADING, result.status());
        assertEquals(emptyList, result.data().memories);
        assertEquals("", result.message());
    }

    @Test
    public void updateTaggingEntities_AllEntitiesPresent_test() {
        MemoryEntity entity1 = new MemoryEntity(1, "t1", "c1", 11, "p1");
        MemoryEntity entity2 = new MemoryEntity(2, "t2", "c2", 12, "p2");
        List<MemoryEntity> memoryEntities = Lists.newArrayList(entity1, entity2);
        buildCoordinator.updateMemoryEntities(memoryEntities);

        List<TagEntity> tagEntities = Lists.newArrayList(new TagEntity(1, "1" ), new TagEntity(2, "2"));
        buildCoordinator.updateTagEntities(tagEntities);

        List<TaggingEntity> taggingEntities = Lists.newArrayList(new TaggingEntity(1, 1 ),
                new TaggingEntity(1, 2));
        Resource<MemoryList> result = buildCoordinator.updateTaggingEntities(taggingEntities);

        List<Tag> tags = tagEntities.stream().map(tagMapper::mapEntity).collect(toList());
        final List<Tag> emptyList = Lists.newArrayList();
        List<Memory> expectedMemories = Lists.newArrayList(memoryMapper.mapEntity(entity1, tags),
                memoryMapper.mapEntity(entity2, emptyList));

        assertEquals(Status.SUCCESS, result.status());
        assertEquals(expectedMemories, result.data().memories);
        assertEquals("", result.message());
    }



}
