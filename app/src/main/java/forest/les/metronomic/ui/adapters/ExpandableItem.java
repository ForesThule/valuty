package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.mikepenz.materialdrawer.holder.StringHolder;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Valute;

public class ExpandableItem<Parent extends IItem & IExpandable, SubItem extends IItem & ISubItem> extends AbstractExpandableItem<ExpandableItem<Parent, SubItem>, ExpandableItem.ViewHolder, SubItem> {

    public String header;
    public String name;
    public StringHolder description;
    public Valute valute;

    public ExpandableItem(Valute valute) {
        this.valute = valute;
        name = valute.name;
    }

    private OnClickListener<ExpandableItem> mOnClickListener;

    public ExpandableItem<Parent, SubItem> withHeader(String header) {
        this.header = header;
        return this;
    }

    public ExpandableItem<Parent, SubItem> withName(String Name) {
//        this.name = new StringHolder(Name);
        return this;
    }


    public ExpandableItem<Parent, SubItem> withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public ExpandableItem<Parent, SubItem> withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    public OnClickListener<ExpandableItem> getOnClickListener() {
        return mOnClickListener;
    }

    public ExpandableItem<Parent, SubItem> withOnClickListener(OnClickListener<ExpandableItem> mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        return this;
    }

    //we define a clickListener in here so we can directly animate
    final private OnClickListener<ExpandableItem<Parent, SubItem>> onClickListener = new OnClickListener<ExpandableItem<Parent, SubItem>>() {
        @Override
        public boolean onClick(View v, IAdapter adapter, ExpandableItem item, int position) {
            if (item.getSubItems() != null) {
                if (!item.isExpanded()) {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(180).start();
                } else {
                    ViewCompat.animate(v.findViewById(R.id.material_drawer_icon)).rotation(0).start();
                }
                return mOnClickListener == null || mOnClickListener.onClick(v, adapter, item, position);
            }
            return mOnClickListener != null && mOnClickListener.onClick(v, adapter, item, position);
        }
    };

    /**
     * we overwrite the item specific click listener so we can automatically animate within the item
     *
     * @return
     */
    @Override
    public OnClickListener<ExpandableItem<Parent, SubItem>> getOnItemClickListener() {
        return onClickListener;
    }

    @Override
    public boolean isSelectable() {
        //this might not be true for your application
        return getSubItems() == null;
    }

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.fastadapter_expandable_item_id;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.expandable_item;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        //get the context
        Context ctx = viewHolder.itemView.getContext();


        super.bindView(viewHolder, payloads);

        //bind our data
        //set the text for the name
        TextView name = viewHolder.name;
        TextView value = viewHolder.value;

        String nm = valute.name;

        double a = Double.parseDouble(valute.value.replace(",","."));

        String format = String.format("%,.2f", a);

        value.setText(format+" руб");

        String nominal = valute.nominal;


        if (this.name != null&&null!=nominal) {

            name.setText(String.format("%s %s",nominal,nm));
        }

        //set the background for the item
//        UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));
//        //set the text for the name
//        StringHolder.applyTo(name, viewHolder.name);
//        //set the text for the description or hide
//        StringHolder.applyToOrHide(description, viewHolder.description);
//
//        if (isExpanded()) {
//            ViewCompat.setRotation(viewHolder.icon, 0);
//        } else {
//            ViewCompat.setRotation(viewHolder.icon, 180);
//        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
//        holder.name.setText(null);
//        holder.description.setText(null);
//        //make sure all animations are stopped
//        holder.icon.clearAnimation();
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
//        public final View view;
//        @BindView(R.id.material_drawer_name)
//        public
//        TextView name;
//        @BindView(R.id.material_drawer_description)
//        public
//        TextView description;
//        @BindView(R.id.material_drawer_icon)
//        ImageView icon;



        protected View view;
        @BindView(R.id.tv_money_name)
        TextView name;
        @BindView(R.id.tv_money_value)
        TextView value;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
//            this.view = view;
        }
    }
}
