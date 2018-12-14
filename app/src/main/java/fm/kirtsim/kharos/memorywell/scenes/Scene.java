package fm.kirtsim.kharos.memorywell.scenes;

import android.os.Bundle;
import android.view.View;

public interface Scene {

    View getView();

    void saveState(Bundle state);

    Bundle saveState();

}
