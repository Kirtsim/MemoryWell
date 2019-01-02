package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.TagEntity;

@Dao
public interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<TagEntity> tags);

    @Delete
    int delete(List<TagEntity> tags);

    @Update
    int update(List<TagEntity> tags);

    @Query("select * from TagEntity")
    LiveData<List<TagEntity>> selectAll();

    @Query("select * from TagEntity where id in (:ids)")
    LiveData<List<TagEntity>> selectByIds(List<Long> ids);

    @Query("select distinct id, name from TagEntity join TaggingEntity on(id=tagId) where memoryId = :memoryId")
    LiveData<List<TagEntity>> selectByMemoryId(long memoryId);

    @Query("select * from TagEntity where lower(name) in (:tagNames)")
    LiveData<List<TagEntity>> selectByNames(List<String> tagNames);

    @Query("select * from TagEntity where name in (:tagNames)")
    LiveData<List<TagEntity>> selectByNamesCaseSensitive(List<String> tagNames);

    @Query("select * from TagEntity where name like :subName || '%'")
    LiveData<List<TagEntity>> selectTagsWithNamesStarting(String subName);
}
