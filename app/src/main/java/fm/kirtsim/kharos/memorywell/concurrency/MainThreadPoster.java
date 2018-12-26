package fm.kirtsim.kharos.memorywell.concurrency;

import android.os.Handler;
import android.os.Looper;

public final class MainThreadPoster implements ThreadPoster {

    private final Handler handler;

    public MainThreadPoster() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
