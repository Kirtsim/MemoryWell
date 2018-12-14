package fm.kirtsim.kharos.memorywell.controller;

import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import fm.kirtsim.kharos.memorywell.R;
import fm.kirtsim.kharos.memorywell.controller.misc.FragmentAction;
import fm.kirtsim.kharos.memorywell.controller.misc.FragmentData;

import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.builderFromTestData;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class AppActivityTest {

    private static final int DELAY_MS = 500;
    private static final int CONTAINER_ID = R.id.fragment_container;

    private AppActivity activity;
    private FragmentManager manager;
    private FragmentData firstFragmentData;

    @Rule
    public ActivityTestRule<AppActivity> activityRule =
            new ActivityTestRule<>(AppActivity.class);

    @Before
    public void initBeforeEach() {
        this.activity = activityRule.getActivity();
        this.manager = activity.getSupportFragmentManager();
        firstFragmentData = builderFromTestData().fragmentTag("FR1").containerId(CONTAINER_ID).build();
    }


    @Test
    public void startFragment_fragmentNull_notLaunched_test() {
        final FragmentData fragmentData = builderFromTestData().fragmentClass(null).build();
        activity.startFragment(fragmentData);
        assertNull(manager.findFragmentByTag(fragmentData.getTag()));
    }

    @Test
    public void startFragment_addFragmentAndAddToBackStack_fragmentAdded_test() {
        FragmentData secondFragmentData = FragmentData.Builder
                .copyFromFragmentData(firstFragmentData).fragmentTag("FR2").build();
        activity.startFragment(firstFragmentData);
        SystemClock.sleep(DELAY_MS);
        activity.startFragment(secondFragmentData);
        SystemClock.sleep(DELAY_MS);

        assertEquals(2, manager.getBackStackEntryCount());
        assertNotNull(manager.findFragmentByTag(firstFragmentData.getTag()));
        assertNotNull(manager.findFragmentByTag(secondFragmentData.getTag()));
    }

    @Test
    public void startFragment_replaceFragmentAddToBackStack_test() {
        FragmentData secondFragmentData = FragmentData.Builder
                .copyFromFragmentData(firstFragmentData)
                .fragmentTag("FR2")
                .fragmentAction(FragmentAction.REPLACE).build();
        activity.startFragment(firstFragmentData);
        SystemClock.sleep(DELAY_MS);
        activity.startFragment(secondFragmentData);
        SystemClock.sleep(DELAY_MS);

        assertEquals(2, manager.getBackStackEntryCount());
        assertNotNull(manager.findFragmentByTag(firstFragmentData.getTag()));
        assertNotNull(manager.findFragmentByTag(secondFragmentData.getTag()));
    }

    @Test
    public void startFragment_replaceFragmentNotToBackStack_test() {
        FragmentData secondFragmentData = FragmentData.Builder
                .copyFromFragmentData(firstFragmentData)
                .fragmentTag("FR2")
                .addFragmentToBackStack(false)
                .fragmentAction(FragmentAction.REPLACE).build();
        activity.startFragment(firstFragmentData);
        SystemClock.sleep(DELAY_MS);
        activity.startFragment(secondFragmentData);
        SystemClock.sleep(DELAY_MS);

        assertEquals(1, manager.getBackStackEntryCount());
        assertNotNull(manager.findFragmentByTag(firstFragmentData.getTag()));
        assertNotNull(manager.findFragmentByTag(secondFragmentData.getTag()));
    }

    @Test
    public void startFragment_addFragmentNotToBackStack_test() {
        FragmentData secondFragmentData = FragmentData.Builder
                .copyFromFragmentData(firstFragmentData)
                .fragmentTag("FR2")
                .addFragmentToBackStack(false)
                .fragmentAction(FragmentAction.ADD).build();
        activity.startFragment(firstFragmentData);
        SystemClock.sleep(DELAY_MS);
        activity.startFragment(secondFragmentData);
        SystemClock.sleep(DELAY_MS);

        assertEquals(1, manager.getBackStackEntryCount());
        assertNotNull(manager.findFragmentByTag(firstFragmentData.getTag()));
        assertNotNull(manager.findFragmentByTag(secondFragmentData.getTag()));
    }

    // TODO: couple of more tests or at least for replacing a fragment

}
