package fm.kirtsim.kharos.memorywell.scene;

import fm.kirtsim.kharos.memorywell.scenes.BaseObservableScene;

public class BaseObservableSceneTestable<Listener> extends BaseObservableScene<Listener> {

    public boolean hasListener_testable(Listener listener) {
        return hasListener(listener);
    }

    public void clearListeners_testable() {
        clearListeners();
    }

}
