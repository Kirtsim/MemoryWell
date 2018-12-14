package fm.kirtsim.kharos.memorywell.scenes;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

public class BaseScene implements Scene {

    private View view;

    @Override
    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void saveState(Bundle state) {
    }

    @Override
    public Bundle saveState() {
        Bundle state = new Bundle();
        saveState(state);
        return state;
    }

    protected <V extends View> V findViewById(@IdRes int id) {
        if (view != null)
            return view.findViewById(id);
        return null;
    }
}
