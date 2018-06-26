package lifecycle.feb.com.lifecycleproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import lifecycle.feb.com.lifecycleproject.presenter.IPresenter;
import lifecycle.feb.com.lifecycleproject.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity {
    private IPresenter mPresenter;
    private final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity------onCreate------");
        mPresenter = new MainPresenter(this);
        getLifecycle().addObserver(mPresenter);//添加LifecycleObserver
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity------onDestroy------");
    }
}
