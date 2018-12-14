package fm.kirtsim.kharos.memorywell.scene;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class BaseObservableSceneTest {

    private static final class Listener {
        int id;

        Listener(int id) { this.id = id; }
    }

    private static final Listener LISTENER_1 = new Listener(1);
    private static final Listener LISTENER_2 = new Listener(2);
    private static final Listener LISTENER_3 = new Listener(3);

    private BaseObservableSceneTestable<Listener> scene;

    private int startingListenerCount;

    @Before
    public void initBeforeEach() {
        scene = new BaseObservableSceneTestable<>();
        registerListeners();
    }

    private void registerListeners() {
        scene.registerListener(LISTENER_1);
        scene.registerListener(LISTENER_2);
        scene.registerListener(LISTENER_3);
        startingListenerCount = 3;
    }

    @Test
    public void registerListener_listenerAdded_test() {
        Listener newListener = new Listener(4);

        scene.registerListener(newListener);
        assertEquals(startingListenerCount + 1, scene.listenerCount());
    }

    @Test
    public void registerListener_addNull_listenerNotAdded_test() {
        scene.registerListener(null);
        assertEquals(startingListenerCount, scene.listenerCount());
    }

    @Test
    public void unregisterListener_providedNull_nothingRemoved_test() {
        scene.unregisterListener(null);
        assertEquals(startingListenerCount, scene.listenerCount());
    }

    @Test
    public void unregisterListener_listenerRemoved_test() {
        scene.unregisterListener(LISTENER_1);
        assertEquals(startingListenerCount -1, scene.listenerCount());
    }

    @Test
    public void clearListeners_allCleared_test() {
        scene.clearListeners_testable();
        assertEquals(0, scene.listenerCount());
    }

    @Test
    public void hasListener_listenerPresent_returnTrue_test() {
        assertTrue(scene.hasListener_testable(LISTENER_1));
    }

    @Test
    public void hasListener_listenerNotPresent_returnFalse_test() {
        Listener listener = new Listener(100);
        assertFalse(scene.hasListener_testable(listener));
    }

    @Test
    public void hasListener_noListeners_returnFalse_test() {
        scene.clearListeners_testable();
        assertFalse(scene.hasListener_testable(LISTENER_1));
    }

    @Test
    public void hasListener_passingNull_returnFalse_test() {
        scene.clearListeners_testable();
        assertFalse(scene.hasListener_testable(null));
    }
}
