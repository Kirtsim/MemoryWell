package fm.kirtsim.kharos.memorywell.test;

import android.os.Bundle;

import junit.framework.Assert;

import fm.kirtsim.kharos.memorywell.controller.misc.FragmentAction;
import fm.kirtsim.kharos.memorywell.controller.misc.FragmentData;
import fm.kirtsim.kharos.memorywell.controller.fragments.BaseFragment;

public class FragmentDataMocks {

    public static final Class<? extends BaseFragment> DEF_CLASS = null;
    public static final String DEF_TAG = null;
    public static final String DEF_BS_NAME = null;
    public static final boolean DEF_TO_BS = false;
    public static final FragmentAction DEF_ACTION = FragmentAction.REPLACE;
    public static final Bundle DEF_DATA = null;
    public static final int DEF_CONT_ID = 0;

    public static final Class<? extends BaseFragment> TEST_CLASS = TestFragment.class;
    public static final String TEST_TAG = "TAG";
    public static final String TEST_BS_NAME = "NAME";
    public static final boolean TEST_TO_BS = true;
    public static final FragmentAction TEST_ACTION = FragmentAction.ADD;
    public static final Bundle TEST_DATA = new Bundle();
    public static final int TEST_CONT_ID = 1;

    public static final class TestFragment extends BaseFragment {}


    public static FragmentData.Builder builderFromTestData() {
        return new FragmentData.Builder()
                .fragmentClass(TEST_CLASS)
                .fragmentTag(TEST_TAG)
                .fragmentBackStackName(TEST_BS_NAME)
                .addFragmentToBackStack(TEST_TO_BS)
                .fragmentAction(TEST_ACTION)
                .fragmentData(TEST_DATA)
                .containerId(TEST_CONT_ID);
    }

    public static void assertWithTestData(FragmentData data) {
        Assert.assertEquals(TEST_CLASS, data.getFragmentClass());
        Assert.assertEquals(TEST_TAG, data.getTag());
        Assert.assertEquals(TEST_BS_NAME, data.getBackStackName());
        Assert.assertEquals(TEST_TO_BS, data.toBackStack());
        Assert.assertEquals(TEST_ACTION, data.getManagerAction());
        Assert.assertEquals(TEST_DATA, data.getFragmentData());
        Assert.assertEquals(TEST_CONT_ID, data.getContainerId());
    }
}
