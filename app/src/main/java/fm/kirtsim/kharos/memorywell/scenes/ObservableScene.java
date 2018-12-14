package fm.kirtsim.kharos.memorywell.scenes;

public interface ObservableScene<Listener> extends Scene {

    void registerListener(Listener listener);

    void unregisterListener(Listener listener);

    int listenerCount();

}
