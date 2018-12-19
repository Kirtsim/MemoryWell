package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Memory;

@Dao
public interface MemoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<Memory> memories);

    @Update
    int update(List<Memory> memories);

    @Delete
    int delete(List<Memory> memories);

    @Query("select * from Memory where id = :id")
    LiveData<Memory> selectById(long id);

    @Query("select * from 'Memory'")
    LiveData<List<Memory>> selectAll();

    @Query("select * from Memory where id in (:ids)")
    LiveData<List<Memory>> selectByIds(List<Long> ids);

    @Query("select * from Memory where dateTime >= :from and dateTime <= :until")
    LiveData<List<Memory>> selectByTimeRange(long from, long until);
}
