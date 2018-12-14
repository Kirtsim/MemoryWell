package fm.kirtsim.kharos.memorywell.scene;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import fm.kirtsim.kharos.memorywell.R;


public class TestActivity extends AppCompatActivity {

    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView();
        setContentView(view);
    }

    private void inflateView() {
        LayoutInflater inflater = getLayoutInflater();
        view = inflater.inflate(R.layout.scene_test, null, false);
    }

    public View getRootView() {
        return view;
    }

}
