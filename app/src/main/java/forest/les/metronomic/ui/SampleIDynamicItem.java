package forest.les.metronomic.ui;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.ValPeriodWrapper;
import im.dacer.androidcharts.LineView;
import io.reactivex.Observable;

public class SampleIDynamicItem extends AbstractItem<SampleIDynamicItem, SampleIDynamicItem.ViewHolder> {
    public String name = "";
    public double value;

    private ValPeriodWrapper valPeriodWrapper;

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_sampleitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.sample_dynamic_item;
    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
    	super.bindView(viewHolder, payloads);

        LineView lineView = viewHolder.lineView;

        lineView.setDrawDotLine(false); //optional
        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
//        LineView.setBottomTextList(strList);
        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();


        lineView.setDataList(arrayLists); //or lineView.setFloatDataList(floatDataLists)


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

        @BindView(R.id.line_view) LineView lineView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}