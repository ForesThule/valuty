package forest.les.metronomic.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;

public class DynamicItem extends AbstractItem<DynamicItem, DynamicItem.ViewHolder> {
    public String name = "";
    public Valute realmValute;



    public DynamicItem(Valute valute) {

        realmValute = valute;

    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.dynamic_sampleitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.dynamic_layout;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);


        //set the text for the description or hide

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);


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

        @BindView(R.id.valute_first)
        ExpandableListView expandableListViewFrs;

        @BindView(R.id.valute_second)
        ExpandableListView expandableListViewSec;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }
    }
}