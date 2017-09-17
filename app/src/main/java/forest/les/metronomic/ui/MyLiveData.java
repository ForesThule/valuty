package forest.les.metronomic.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import forest.les.metronomic.model.ValCurs;

/**
 * Created by root on 06.08.17.
 */

public class MyLiveData extends MediatorLiveData<ValCurs> {

    @Override
    protected void onActive() {
        super.onActive();
    }

    @Override
    protected void onInactive() {
        super.onInactive();


    }
    
}
