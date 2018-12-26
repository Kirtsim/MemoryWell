package fm.kirtsim.kharos.memorywell.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class BackgroundThreadPoster implements ThreadPoster {

    private final ExecutorService service;

    public BackgroundThreadPoster() {
        service = Executors.newCachedThreadPool();
    }

    @Override
    public void post(Runnable runnable) {
        service.execute(runnable);
    }
}
