package lifecycle.feb.com.lifecycleproject.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;

import org.jetbrains.annotations.NotNull;

/**
 * Created by lilichun on 18/6/26.
 */

public class BasePresenter implements IPresenter {

    @Override
    public void onCreate(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onDestory(@NotNull LifecycleOwner owner) {

    }

    @Override
    public void onLifeCycleChanged(@NotNull LifecycleOwner owner, @NotNull Lifecycle.Event event) {

    }
}
