package forest.les.metronomic.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;

public class SampleItem extends AbstractItem<SampleItem, SampleItem.ViewHolder> {
    public String name = "";
    public Valute realmValute;



    public SampleItem(Valute valute) {

        realmValute = valute;

    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_sampleitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.sample_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        //bind our data
        //set the text for the name
        TextView name = viewHolder.name;
        TextView value = viewHolder.value;
        TextView charcode = viewHolder.charcode;

        charcode.setText(realmValute.charcode);

        String nm = realmValute.name;

        double a = Double.parseDouble(realmValute.value.replace(",","."));

        String format = String.format("%,.2f", a);

        value.setText(format+" руб");

        String nominal = realmValute.nominal;


        if (this.name != null&&null!=nominal) {

            name.setText(String.format("За %s %s",nominal,nm));
        }
        //set the text for the description or hide

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.name.setText(null);
        holder.value.setText(null);
    }

    //Init the viewHolder for this Item
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        @BindView(R.id.tv_money_name)
        TextView name;

        @BindView(R.id.tv_money_value)
        TextView value;

        @BindView(R.id.tv_money_charcode)
        TextView charcode;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}