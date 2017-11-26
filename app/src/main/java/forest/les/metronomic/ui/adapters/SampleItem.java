package forest.les.metronomic.ui.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;
import timber.log.Timber;

public class SampleItem extends AbstractItem<SampleItem, SampleItem.ViewHolder> implements IExpandable<SampleItem,DynamicItem>{
    public String name = "";
    public Valute realmValute;

    private List<DynamicItem> subItems;
    private OnClickListener<SampleItem> mOnClickListener;


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
//
//    @Override
//    public SampleItem withOnItemClickListener(OnClickListener<SampleItem> onItemClickListener) {
////        return super.withOnItemClickListener(onItemClickListener);
//        mOnClickListener = onItemClickListener;
//        return this;
//    }

    //we define a clickListener in here so we can directly animate
    final private OnClickListener<SampleItem> onClickListener = (v, adapter, item, position) -> {
        Timber.i("OnClickListener: ");
        if (item.getSubItems() != null) {
                if (!item.isExpanded()) {

                    Timber.i("expand %s ",item.isExpanded());
//                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(180).start();
                } else {
//                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(0).start();
                    Timber.i("expand %s ",item.isExpanded());

                }
            return mOnClickListener == null || mOnClickListener.onClick(v, adapter, item, position);
        }
        return mOnClickListener != null && mOnClickListener.onClick(v, adapter, item, position);
    };
//
//    public OnClickListener<SampleItem> getOnClickListener() {
//        return mOnClickListener;
//    }
//    public SampleItem withOnClickListener(OnClickListener<SampleItem> mOnClickListener) {
//        this.mOnClickListener = mOnClickListener;
//        return this;
//    }

    @Override
    public OnClickListener<SampleItem> getOnItemClickListener() {
        return onClickListener;
    }

    /**
     * we overwrite the item specific click listener so we can automatically animate within the item
     *
     * @return
     */

    @Override
    public boolean isExpanded() {
        return false;
    }

    @Override
    public SampleItem withIsExpanded(boolean collapsed) {
        return null;
    }

    @Override
    public SampleItem withSubItems(List<DynamicItem> subItems) {
        return null;
    }

    @Override
    public List<DynamicItem> getSubItems() {
        return null;
    }

    @Override
    public boolean isAutoExpanding() {
        return false;
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

        TextView name;

        @BindView(R.id.tv_money_value)
        TextView value;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

            name = (TextView) view.findViewById(R.id.tv_money_name);
            value = (TextView) view.findViewById(R.id.tv_money_value);
        }
    }
}