package fm.kirtsim.kharos.memorywell.db.util;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public final class LiveDataTestUtil {

    public static final <V> V getValue(LiveData<V> liveData) {
        final Object[] value = new Object[1];

        final CountDownLatch latch = new CountDownLatch(1);
        final Observer<V> observer = new Observer<V>() {
            @Override
            public void onChanged(@Nullable V data) {
                value[0] = data;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);
        try {
            latch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (V) value[0];
    }
}
