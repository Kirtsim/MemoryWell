package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.MemoryEntity;

@Dao
public interface MemoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<MemoryEntity> memories);

    @Update
    int update(List<MemoryEntity> memories);

    @Delete
    int delete(List<MemoryEntity> memories);

    @Query("select * from MemoryEntity where id = :id")
    LiveData<MemoryEntity> selectById(long id);

    @Query("select * from MemoryEntity")
    LiveData<List<MemoryEntity>> selectAll();

    @Query("select * from MemoryEntity where id in (:ids)")
    LiveData<List<MemoryEntity>> selectByIds(List<Long> ids);

    @Query("select * from MemoryEntity where dateTime >= :from and dateTime <= :until")
    LiveData<List<MemoryEntity>> selectByTimeRange(long from, long until);
}
