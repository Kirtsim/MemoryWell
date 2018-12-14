package fm.kirtsim.kharos.memorywell.controller.misc;

import android.support.v4.app.FragmentTransaction;

import fm.kirtsim.kharos.memorywell.controller.fragments.BaseFragment;

public enum FragmentAction {
    ADD((fragment, txn, data) -> {
        txn.add(data.getContainerId(), fragment, data.getTag());
    }),

    REPLACE((fragment, txn, data) -> {
        txn.replace(data.getContainerId(), fragment, data.getTag());
    });

    private final FragmentHandler fragmentHandler;

    private FragmentAction(FragmentHandler fragmentHandler) {
        this.fragmentHandler = fragmentHandler;
    }

    public void processFragment(BaseFragment fragment, FragmentTransaction transaction,
                                FragmentData data) {
        fragmentHandler.handleFragment(fragment, transaction, data);
    }

    private interface FragmentHandler {
        void handleFragment(BaseFragment fragment, FragmentTransaction transaction,
                            FragmentData data);
    }
}
