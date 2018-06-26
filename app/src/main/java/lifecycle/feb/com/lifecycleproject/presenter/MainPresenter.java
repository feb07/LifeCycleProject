package lifecycle.feb.com.lifecycleproject.presenter;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lilichun on 18/6/26.
 */

public class MainPresenter extends BasePresenter {
    private final String TAG = MainPresenter.class.getName();
    private Context context;

    public MainPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {
        super.onCreate(owner);
        Log.d(TAG, "LifeCycle------onCreate------");
    }

    @Override
    public void onDestory(@NotNull LifecycleOwner owner) {
        super.onDestory(owner);
        Log.d(TAG, "LifeCycle------onDestory------");
    }
}
