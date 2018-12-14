package fm.kirtsim.kharos.memorywell.controller.misc;

import android.os.Bundle;
import android.support.annotation.IdRes;

import fm.kirtsim.kharos.memorywell.controller.fragments.BaseFragment;

public class FragmentData {

    private final Class<? extends BaseFragment> clazz;
    private final String tag;
    private final String backStackName;
    private final boolean useBackStack;
    private final FragmentAction managerAction;
    private final Bundle data;
    @IdRes private final int containerId;

    public FragmentData(Class<? extends BaseFragment> class_, String tag, String bsName,
                        boolean backStack, FragmentAction action, Bundle data,
                        @IdRes int containerId) {
        this.clazz = class_;
        this.tag = tag;
        this.backStackName = bsName;
        this.useBackStack = backStack;
        this.managerAction = action;
        this.data = data;
        this.containerId = containerId;
    }

    public Class<? extends BaseFragment> getFragmentClass() {
        return clazz;
    }

    public String getTag() {
        return tag;
    }

    public String getBackStackName() {
        return backStackName;
    }

    public boolean toBackStack() {
        return useBackStack;
    }

    public FragmentAction getManagerAction() {
        return managerAction;
    }

    public Bundle getFragmentData() {
        return data;
    }

    @IdRes
    public int getContainerId() {
        return containerId;
    }

    public static final class Builder {
        private Class<? extends BaseFragment> clazz;
        private String tag;
        private String backStackName;
        private boolean useBackStack;
        private FragmentAction managerAction;
        private Bundle data;
        @IdRes private int containerId;

        public Builder() {
            managerAction = FragmentAction.REPLACE;
        }

        public Builder(FragmentData otherData) {
            copyFromFragmentData(otherData);
        }

        public Builder fragmentClass(Class<? extends BaseFragment> clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder fragmentTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder fragmentBackStackName(String name) {
            this.backStackName = name;
            return this;
        }

        public Builder addFragmentToBackStack(boolean add) {
            this.useBackStack = add;
            return this;
        }

        public Builder fragmentAction(FragmentAction action) {
            this.managerAction = action;
            return this;
        }

        public Builder fragmentData(Bundle data) {
            this.data = data;
            return this;
        }

        public Builder containerId(@IdRes int containerId) {
            this.containerId = containerId;
            return this;
        }

        public static Builder copyFromFragmentData(FragmentData data) {
            Builder builder = new Builder();
            builder.clazz = data.getFragmentClass();
            builder.tag = data.getTag();
            builder.backStackName = data.getBackStackName();
            builder.useBackStack = data.toBackStack();
            builder.managerAction = data.getManagerAction();
            builder.data = data.getFragmentData();
            builder.containerId = data.getContainerId();
            return builder;
        }

        public FragmentData build() {
            return new FragmentData(
                    this.clazz,
                    this.tag,
                    this.backStackName,
                    this.useBackStack,
                    this.managerAction,
                    this.data,
                    this.containerId);
        }
    }
}
