package fm.kirtsim.kharos.memorywell.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fm.kirtsim.kharos.memorywell.db.entity.Tag;

@Dao
public interface TagDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insert(List<Tag> tags);

    @Delete
    void delete(List<Tag> tags);

    @Update
    void update(List<Tag> tags);

    @Query("select * from Tag")
    LiveData<List<Tag>> selectAll();

    @Query("select * from Tag where id in (:ids)")
    LiveData<List<Tag>> selectByIds(List<Long> ids);

    @Query("select * from Tag where lower(name) in (:tagNames)")
    LiveData<List<Tag>> selectByNames(List<String> tagNames);

    @Query("select * from Tag where name in (:tagNames)")
    LiveData<List<Tag>> selectByNamesCaseSensitive(List<String> tagNames);

    @Query("select * from Tag where name like :subName || '%'")
    LiveData<List<Tag>> selectTagsWithNamesStarting(String subName);
}
