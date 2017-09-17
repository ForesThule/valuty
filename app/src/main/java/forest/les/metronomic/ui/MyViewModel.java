package forest.les.metronomic.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by root on 06.08.17.
 */

public class MyViewModel extends ViewModel {

    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();


    public void doSomeThing(){
        showProgress.postValue(true);

        showProgress.postValue(false);
    }

    public MutableLiveData<Boolean> getProgressState(){
        return showProgress;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
