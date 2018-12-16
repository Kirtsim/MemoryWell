package fm.kirtsim.kharos.memorywell.db.util;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;

import java.lang.reflect.Method;
import java.util.HashMap;

import fm.kirtsim.kharos.memorywell.BuildConfig;

public class DebugUtil {

    public static void setInMemoryRoomDatabases(RoomDatabase database) {
        setInMemoryRoomDatabases(database.getOpenHelper().getWritableDatabase());
    }

    public static void setInMemoryRoomDatabases(SupportSQLiteDatabase... database) {
        if (BuildConfig.DEBUG) {
            try {
                Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
                Class[] argTypes = new Class[]{HashMap.class};
                HashMap<String, SupportSQLiteDatabase> inMemoryDatabases = new HashMap<>();
                // set your inMemory databases
                inMemoryDatabases.put("InMemoryOne.db", database[0]);
                Method setRoomInMemoryDatabase = debugDB.getMethod("setInMemoryRoomDatabases", argTypes);
                setRoomInMemoryDatabase.invoke(null, inMemoryDatabases);
            } catch (Exception ignore) {

            }
        }
    }

}
