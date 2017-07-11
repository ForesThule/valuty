package forest.les.metronomic.ui;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.time.RadialPickerLayout;
import com.borax12.materialdaterangepicker.time.TimePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.ValPeriodWrapper;
import forest.les.metronomic.model.realm.RealmRecord;
import forest.les.metronomic.model.realm.RealmValCursPeriod;
import im.dacer.androidcharts.LineView;
import io.reactivex.Observable;
import io.realm.RealmList;

import static com.squareup.timessquare.CalendarPickerView.SelectionMode.RANGE;

public class SampleIDynamicItem extends AbstractItem<SampleIDynamicItem, SampleIDynamicItem.ViewHolder>  implements TimePickerDialog.OnTimeSetListener, com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener{
    public String name = "";
    public double value;

    public MainActivity mainActivity;

    public SampleIDynamicItem(MainActivity context, RealmValCursPeriod valCurs) {
        this.valCurs = valCurs;
        mainActivity = context;
    }

    private RealmValCursPeriod valCurs;

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.fastadapter_dynamic_sampleitem_id;
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

        LineChart lineView = viewHolder.lineChart;
        Context context = lineView.getContext();


        RealmList<RealmRecord> records = valCurs.records;

        List<Entry> entries = new ArrayList<Entry>();

        for (int i = 0, recordsSize = records.size(); i < recordsSize; i++) {
            RealmRecord data = records.get(i);

            // turn your data into Entry objects
            entries.add(new Entry(i, Float.parseFloat(data.getValue().replace(",","."))));
        }


        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

        lineView.setData(new LineData(dataSet));
        lineView.invalidate();
        
//        dataSet.setColor();
//        dataSet.setValueTextColor(...); // styling, ..



//
//        lineView.setDrawDotLine(true); //optional
//        lineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //optional
//
//        lineView.setColorArray(new int[]{Color.BLACK,Color.GREEN,Color.GRAY,Color.CYAN});
//
//        ArrayList<String> bottomTextList = new ArrayList<>();
//
//
//        ArrayList<ArrayList<Float>> allGraphList = new ArrayList<>();
//
//        ArrayList<Float> valuteGraph = new ArrayList<>();
//
//        RealmList<RealmRecord> records = valCurs.records;
//        for (RealmRecord record : records) {
//            bottomTextList.add(record.date);
//            valuteGraph.add(Float.parseFloat(record.getValue().replace(",",".")));
//
//        }
//
//
//        allGraphList.add(valuteGraph);
//        lineView.setBottomTextList(bottomTextList);
//        lineView.setFloatDataList(allGraphList); //or lineView.setFloatDataList(floatDataLists)


//        Calendar nextYear = Calendar.getInstance();
//        nextYear.add(Calendar.YEAR, 1);
//
//        CalendarPickerView calendar = viewHolder.calendarPickerView;
//        Date today = new Date();
//
//        calendar.init(today, nextYear.getTime())
//                .inMode(RANGE)
//                .withSelectedDate(today);

//        Calendar now = Calendar.getInstance();
//        com.borax12.materialdaterangepicker.date.DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
//                (view, year, monthOfYear, dayOfMonth, yearEnd, monthOfYearEnd, dayOfMonthEnd) -> {
//
//                },
//                now.get(Calendar.YEAR),
//                now.get(Calendar.MONTH),
//                now.get(Calendar.DAY_OF_MONTH)
//        );
//        dpd.show(mainActivity.getFragmentManager(), "Datepickerdialog");
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

    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;


    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int hourOfDayEnd, int minuteEnd) {

        String time = "You picked the following time: "+hourOfDay+"h"+minute+"m";
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;

//        @BindView(R.id.line_view) LineView lineView;
        @BindView(R.id.chart)
LineChart lineChart;
        @BindView(R.id.calendar_picker_start) CalendarPickerView calendarPickerView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}