package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.MemoryList;
import fm.kirtsim.kharos.memorywell.db.Resource;
import fm.kirtsim.kharos.memorywell.db.entity.Memory;

@Dao
public interface MemoryDao {

    @Insert
    void insert(List<Memory> memories);

    @Update
    void update(List<Memory> memories);

    @Delete
    void delete(List<Memory> memories);

    @Query("select * from Memory")
    LiveData<Resource<MemoryList>> selectAll();

    @Query("select * from Memory where id in (:ids)")
    LiveData<Resource<MemoryList>> selectByIds(List<Long> ids);

    @Query("select * from Memory where dateTime >= :from and dateTime <= :until")
    LiveData<Resource<MemoryList>> selectByTimeRange(long from, long until);
}
