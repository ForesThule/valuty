package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.mikepenz.fastadapter.IClickable;
import com.mikepenz.fastadapter.IExpandable;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.ISubItem;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.expandable.items.AbstractExpandableItem;
import com.mikepenz.fastadapter_extensions.drag.IDraggable;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Record;
import timber.log.Timber;

public class SubItem<Parent extends IItem & IExpandable & ISubItem & IClickable> extends AbstractExpandableItem<Parent, SubItem.ViewHolder, SubItem<Parent>> implements IDraggable<SubItem, IItem> {

    public String header;
    public StringHolder name;
    public StringHolder description;

    private boolean mIsDraggable = true;
    public List<Record> records = new ArrayList<>();

    public SubItem<Parent> withHeader(String header) {
        this.header = header;
        return this;
    }

    public SubItem<Parent> withName(String Name) {
        this.name = new StringHolder(Name);
        return this;
    }

    public SubItem<Parent> withName(@StringRes int NameRes) {
        this.name = new StringHolder(NameRes);
        return this;
    }

    public SubItem<Parent> withDescription(String description) {
        this.description = new StringHolder(description);
        return this;
    }

    public SubItem<Parent> withDescription(@StringRes int descriptionRes) {
        this.description = new StringHolder(descriptionRes);
        return this;
    }

    @Override
    public boolean isDraggable() {
        return mIsDraggable;
    }

    @Override
    public SubItem withIsDraggable(boolean draggable) {
        this.mIsDraggable = draggable;
        return this;
    }

    /**
     * defines the type defining this item. must be unique. preferably an id
     *
     * @return the type
     */
    @Override
    public int getType() {
        return R.id.fastadapter_sub_item_id;
    }

    /**
     * defines the layout which will be used for this item in the list
     *
     * @return the layout for this item
     */
    @Override
    public int getLayoutRes() {
        return R.layout.sample_item;
    }

    /**
     * binds the data of this item onto the viewHolder
     *
     * @param viewHolder the viewHolder of this item
     */
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        super.bindView(viewHolder, payloads);

        Timber.i("bindView: ");

        //get the context
        Context ctx = viewHolder.itemView.getContext();

        //set the background for the item
        UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(ctx, Color.RED, true));

        LineChartView lineChartView = viewHolder.lineChartView;

        List<Record>sortedList = new ArrayList<>(records);

        Collections.sort(sortedList,(o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        Timber.i("bindView: %s",sortedList);

        if (sortedList.size()>0) {
            String minimumValue = sortedList.get(0).getValue();
            float minFloat = Float.parseFloat(minimumValue.replace(",", "."));

            String maximumValue = sortedList.get(sortedList.size() - 1).getValue();
            float maxFloat = Float.parseFloat(maximumValue.replace(",", "."));


            Timber.i("bindView: minimumValue %s maximumValue %s", minimumValue, maximumValue);
//        lineChartView.setAxisBorderValues(minFloat,maxFloat);
            if (maxFloat>minFloat){
                lineChartView.setAxisBorderValues(minFloat, maxFloat);
            }


            LineSet set = new LineSet();

            try {
                for (Record record : records) {
                    set.addPoint(record.getDate(), Float.parseFloat(record.getValue().replace(",", ".")));
                }
            } catch (Exception e) {
                set.addPoint("LABEL", 100);
                set.addPoint("LABEL", 200);
                e.printStackTrace();
            }

            lineChartView.addData(set);
            lineChartView.show();
        }
    }

    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);
        holder.lineChartView.reset();
    }

    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    /**
     * our ViewHolder
     */
    protected static class ViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        @BindView(R.id.linechart)LineChartView lineChartView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
