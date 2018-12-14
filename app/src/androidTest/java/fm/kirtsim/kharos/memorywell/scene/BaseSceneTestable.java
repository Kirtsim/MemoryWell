package fm.kirtsim.kharos.memorywell.scene;

import android.support.annotation.IdRes;
import android.view.View;

import fm.kirtsim.kharos.memorywell.scenes.BaseScene;

public class BaseSceneTestable extends BaseScene {

    public <V extends View> V findViewById_access(@IdRes int id) {
        return findViewById(id);
    }

}
