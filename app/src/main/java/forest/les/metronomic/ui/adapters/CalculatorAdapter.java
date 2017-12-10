package forest.les.metronomic.ui.adapters;

import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.ui.MainActivity;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by a on 07.07.17.
 */

public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.ViewHolder> {

    private final MainActivity activity;
    private List<Valute> data = new ArrayList<>();
    private Valute currentValute = null;
    int editedIndex;

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {

            Timber.i("afterTextChanged: %s", s.toString());

            notifyOtherValutes(editedIndex);

        }
    };

    public CalculatorAdapter(MainActivity mainActivity) {
        activity = mainActivity;
    }

    private void notifyOtherValutes(int editedIndex) {
        activity.notifyCalculateAdapter(editedIndex);
    }


    public void setData(List<Valute> data) {

        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.calculator_adapter_layout, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TextView tv = holder.tv;
        EditText etValue = holder.etValue;
        Valute valute = data.get(position);


        etValue.setOnFocusChangeListener((v, hasFocus) -> {

            Timber.i("onBindViewHolder: %s", currentValute);
            if (hasFocus) {
                currentValute = valute;
                Timber.i("haz fokus: %s", currentValute);

//                RxTextView.textChanges(etValue).skipInitialValue()
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(charSequence -> {
//                            for (Valute datum : data) {
//                                datum.name = charSequence.toString();
//                            }
//                            Timber.i("%s: %s", Thread.currentThread(), charSequence.toString());
//                        });

                etValue.addTextChangedListener(watcher);
                editedIndex = position;
            } else {
                etValue.removeTextChangedListener(watcher);
            }
        });

        etValue.setText(valute.value);
        String name = valute.name;

        tv.setText(name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_calc_adapter)
        TextView tv;
        @BindView(R.id.et_value)
        EditText etValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
