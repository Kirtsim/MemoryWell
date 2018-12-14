package fm.kirtsim.kharos.memorywell.controller;

import org.junit.Test;

import fm.kirtsim.kharos.memorywell.controller.misc.FragmentData;
import fm.kirtsim.kharos.memorywell.controller.misc.FragmentData.Builder;

import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_ACTION;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_BS_NAME;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_CLASS;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_CONT_ID;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_DATA;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_TAG;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.DEF_TO_BS;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.assertWithTestData;
import static fm.kirtsim.kharos.memorywell.test.FragmentDataMocks.builderFromTestData;
import static org.junit.Assert.assertEquals;

public class FragmentDataTest {

    @Test
    public void defaultDataValues_test() {
        FragmentData data = new Builder().build();
        assertEquals(DEF_CLASS, data.getFragmentClass());
        assertEquals(DEF_TAG, data.getTag());
        assertEquals(DEF_BS_NAME, data.getBackStackName());
        assertEquals(DEF_TO_BS, data.toBackStack());
        assertEquals(DEF_ACTION, data.getManagerAction());
        assertEquals(DEF_DATA, data.getFragmentData());
        assertEquals(DEF_CONT_ID, data.getContainerId());
    }

    @Test
    public void builder_createWithImplicitConstructor_test() {
        FragmentData data = builderFromTestData().build();
        assertWithTestData(data);
    }

    @Test
    public void builder_createWithCopyConstructor_test() {
        FragmentData data = builderFromTestData().build();
        FragmentData copy = new Builder(data).build();
        assertWithTestData(copy);
    }

}
