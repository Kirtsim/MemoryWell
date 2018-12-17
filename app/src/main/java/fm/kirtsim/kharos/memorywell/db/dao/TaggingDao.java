package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Tagging;

@Dao
public interface TaggingDao {

    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Tagging tagging);

    @Delete
    int delete(List<Tagging> taggings);

    @Query("select * from Tagging")
    LiveData<List<Tagging>> selectAll();

    @Query("select * from Tagging where memoryId = :memoryId and tagId = :tagId")
    LiveData<Tagging> selectSingle(long memoryId, long tagId);

    @Query("select * from Tagging where memoryId in (:memoryIds) order by memoryId")
    LiveData<List<Tagging>> selectByMemoryIds(List<Long> memoryIds);

    @Query("select * from Tagging where tagId in (:tagIds) order by tagId")
    LiveData<List<Tagging>> selectByTagIds(List<Long> tagIds);
}
