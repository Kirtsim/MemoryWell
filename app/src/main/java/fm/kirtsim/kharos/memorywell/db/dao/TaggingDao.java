package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.TaggingEntity;

@Dao
public interface TaggingDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(TaggingEntity tagging);

    @Delete
    int delete(List<TaggingEntity> taggings);

    @Query("select * from TaggingEntity")
    LiveData<List<TaggingEntity>> selectAll();

    @Query("select * from TaggingEntity where memoryId = :memoryId and tagId = :tagId")
    LiveData<TaggingEntity> selectSingle(long memoryId, long tagId);

    @Query("select * from TaggingEntity where memoryId in (:memoryIds) order by memoryId")
    LiveData<List<TaggingEntity>> selectByMemoryIds(List<Long> memoryIds);

    @Query("select * from TaggingEntity where tagId in (:tagIds) order by tagId")
    LiveData<List<TaggingEntity>> selectByTagIds(List<Long> tagIds);
}
