package forest.les.metronomic.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.materialize.holder.StringHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;

public class SampleItem extends AbstractItem<SampleItem, SampleItem.ViewHolder> {
    public String name = "";
    public double value;

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


        if (this.value != 0) {
            value.setText(Double.toString(this.value));
        }
        if (this.name != null) {

            name.setText(this.name);
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

        TextView name;

        @BindView(R.id.tv_money_value) TextView value;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

            name = (TextView) view.findViewById(R.id.tv_money_name);
            value = (TextView) view.findViewById(R.id.tv_money_value);
        }
    }
}