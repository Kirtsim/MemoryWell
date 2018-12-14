package fm.kirtsim.kharos.memorywell.scene;

import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fm.kirtsim.kharos.memorywell.R;
import fm.kirtsim.kharos.memorywell.test.TestActivity;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class BaseSceneTest1 {

    private static final int VIEW_1_ID = R.id.test_textView1;
    private static final int VIEW_2_ID = R.id.test_textView2;
    private static final int VIEW_3_ID = R.id.test_textView3;

    private BaseSceneTestable scene;

    @Rule
    public ActivityTestRule<TestActivity> activityRule =
            new ActivityTestRule<>(TestActivity.class);


    @BeforeClass
    public static void initForAll() {

    }

    @Before
    public void initBeforeEach() {
        scene = new BaseSceneTestable();
        scene.setView(activityRule.getActivity().getRootView());
    }

    @Test
    public void findViewById_rootViewNull_test() {
        scene.setView(null);
        assertNull(scene.findViewById_access(VIEW_1_ID));
    }

    @Test
    public void findViewById_childViewNotNull_test() {
        assertNotNull(scene.findViewById_access(VIEW_1_ID));
        assertNotNull(scene.findViewById_access(VIEW_2_ID));
        assertNotNull(scene.findViewById_access(VIEW_3_ID));
    }

    @Test
    public void saveState_returnsBundle_test() {
        assertNotNull(scene.saveState());
    }

    @Test
    public void saveState_givenBundleUnchanged_test() {
        Bundle state = new Bundle(0);
        scene.saveState(state);
        assertEquals(0, state.size());
    }

    @Test
    public void getView_returnsView_test() {
        assertNotNull(scene.getView());
    }

    @Test
    public void setView_toNull_returnsNull_test() {
        scene.setView(null);
        assertNull(scene.getView());
    }

}
