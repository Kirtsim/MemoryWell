package fm.kirtsim.kharos.memorywell.concurrency;

public interface ThreadPoster {

    void post(Runnable runnable);

}
