package fm.kirtsim.kharos.memorywell.controller.fragments;

import android.support.v4.app.Fragment;

import fm.kirtsim.kharos.memorywell.controller.AppActivity;
import fm.kirtsim.kharos.memorywell.controller.misc.FragmentData;

public abstract class BaseFragment extends Fragment {

    protected void startFragment(FragmentData fragmentData) {
        getAppActivity().startFragment(fragmentData);
    }

    protected AppActivity getAppActivity() {
        return (AppActivity) getActivity();
    }

}
