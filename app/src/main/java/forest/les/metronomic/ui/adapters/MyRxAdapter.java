package forest.les.metronomic.ui.adapters;

import android.support.annotation.LayoutRes;

import com.minimize.android.rxrecycleradapter.RxAdapter;

import java.util.List;

import forest.les.metronomic.model.Valute;

/**
 * Created by root on 05.10.17.
 */

public class MyRxAdapter extends RxAdapter {

    private final List <Valute> dataset;

    public MyRxAdapter(@LayoutRes int item_layout, List<Valute> dataSet) {
        super(item_layout, dataSet);
        this.dataset = dataSet;
    }

    public void changeImage(int index) {
        dataset.get(index).charcode = "<><><><><><><><><><><><";
        notifyItemChanged(index);

    }


}
