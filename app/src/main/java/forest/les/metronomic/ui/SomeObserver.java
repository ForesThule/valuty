package forest.les.metronomic.ui;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import timber.log.Timber;

public class SomeObserver implements LifecycleObserver {
    private Owner owner;

    public SomeObserver(Lifecycle lifecycle, Owner owner) {
        this.owner = owner;
        lifecycle.addObserver(this);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

        Timber.i("onCreate OBSERVER");
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        Timber.i("onStop OBSERVER");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onResume() {
        Timber.i("onResume OBSERVER");
    }
    enum Owner {
        ACTIVITY, FRAGMENT, PROCESS, SERVICE
    }
}