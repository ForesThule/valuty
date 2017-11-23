package forest.les.metronomic.ui;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;
import timber.log.Timber;

/**
 * Created by root on 05.10.17.
 */

public class AnotherAdapter extends RecyclerView.Adapter<AnotherAdapter.ViewHolder>{

    private MainActivity activity;
    public List<Valute> data;

    public AnotherAdapter(MainActivity activity, List<Valute> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public AnotherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false));
    }
    @Override
    public void onBindViewHolder(AnotherAdapter.ViewHolder holder, int position) {
        holder.textView.setText(data.get(position).name);
        EditText editText = holder.editText;
        editText.setText(data.get(position).value);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Timber.i("afterTextChanged: %s",s.toString());
                activity.updateAnotherAdapter(position);



            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.editTextViewItem) EditText editText;
        @BindView(R.id.textViewItem) TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}


