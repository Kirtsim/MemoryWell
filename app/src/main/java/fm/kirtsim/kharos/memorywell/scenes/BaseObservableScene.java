package fm.kirtsim.kharos.memorywell.scenes;

import com.google.common.collect.Sets;

import java.util.Set;

public class BaseObservableScene<Listener> extends BaseScene implements ObservableScene<Listener> {

    private final Set<Listener> listeners = Sets.newConcurrentHashSet();

    @Override
    public void registerListener(Listener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(Listener listener) {
        if (listener != null)
            listeners.remove(listener);
    }

    @Override
    public int listenerCount() {
        return listeners.size();
    }

    protected void clearListeners() {
        listeners.clear();
    }

    protected boolean hasListener(Listener listener) {
        if (listener != null)
            return listeners.contains(listener);
        return false;
    }
}
