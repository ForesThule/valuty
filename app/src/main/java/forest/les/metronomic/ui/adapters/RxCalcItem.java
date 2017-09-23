package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RxCalcItem extends AbstractItem<RxCalcItem, RxCalcItem.ViewHolder> {
    private final Valute currentValute;
    public String name = "";
    public Valute realmValute;
    private List<Valute> currentValutesRates;
    private List<Item> currentItems;
    volatile Valute inputValute;
    volatile Valute outputValute;
    private List<String> spinnerDataList;
    private EditText editText;
    private TextView tv_output;
    private Spinner spinnerInput;
    private Spinner spinnerOut;
    private ArrayAdapter<String> adapterInput;
    private ArrayAdapter<String> adapterOut;


    public RxCalcItem(Valute valute) {

        currentValute = valute;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.calc_rxitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.rx_calc_item;
    }


    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        Context context = viewHolder.view.getContext();

        TextView valute_value = viewHolder.valute_value;
        TextView valute_name = viewHolder.valute_name;

        valute_name.setText(currentValute.name);
        valute_value.setText(currentValute.value);


    }

    private void setOutput() {
        if (!editText.getText().toString().equals("")) {

            tv_output.setText(calculate());
//                    calculate();
        } else {
            tv_output.setText(editText.getText().toString());

        }
    }

    public void setOutputValute(int position) {

        String s = spinnerDataList.get(position);

        Observable.fromIterable(currentValutesRates)
                .filter(valute -> valute.name.equals(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valute -> {
                    outputValute = valute;

                    setOutput();

                    Timber.i("OutValute = %s", outputValute);
                }, Throwable::printStackTrace);

    }

    private void setinputValute(int position) {

        String s = spinnerDataList.get(position);

        Observable.fromIterable(currentValutesRates)
                .filter(valute -> valute.name.equals(s))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valute -> {
                    inputValute = valute;

                    setOutput();

                    Timber.i("Input Valute = %s", inputValute);
                }, Throwable::printStackTrace);

    }

    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {
        super.unbindView(holder);

        holder.valute_name.setText(null);
        holder.valute_value.setText(null);

    }



    public String calculate() {

        String value = editText.getText().toString();

        Timber.i(inputValute.toString());

        Double doubleValue = Double.parseDouble(value);
        BigDecimal decValue = BigDecimal.valueOf(doubleValue);

        Double inValuteDouble = Double.parseDouble(inputValute.value.replace(",", "."));
        Double inNominalDouble = Double.parseDouble(inputValute.nominal);
        BigDecimal inValuteDec = BigDecimal.valueOf(inValuteDouble);
        BigDecimal inNominalDec = BigDecimal.valueOf(inNominalDouble);
        BigDecimal multiply = decValue.multiply(inValuteDec);
        BigDecimal inRubles = multiply.divide(inNominalDec, BigDecimal.ROUND_HALF_UP);


        Double outValuteDouble = Double.parseDouble(outputValute.value.replace(",", "."));
        Double outNominal = Double.parseDouble(outputValute.nominal);
        BigDecimal decOutNominal = BigDecimal.valueOf(outNominal);
        BigDecimal outValuteDec = BigDecimal.valueOf(outValuteDouble);
        BigDecimal outInRubles = outValuteDec.divide(decOutNominal, BigDecimal.ROUND_HALF_UP);


        BigDecimal divide = inRubles.divide(outInRubles, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

        Timber.i("calculate: %s %s", inValuteDec, outValuteDec);

        Timber.i("calculate: %s", divide);

        return divide.toString();

//////////////////////////////////////////////////////////

//        String s = editText.getText().toString();
//
//        int nom=0;
//        try {
//            nom = Integer.parseInt(inputValute.nominal);
//        } catch (Exception e){
//
//        }
//        int value = Integer.parseInt(inputValute.value);
//
//
//        int inputInRubles = value/nom;
//
//        Timber.i("calculate: inputInRubles %d",inputInRubles);
//
//        int typedValue = 0;
//
//        try {typedValue = Integer.parseInt(s);}
//        catch (Exception e){}


    }

    //Init the viewHolder for this Item
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    protected static class ViewHolder extends RecyclerView.ViewHolder {


        private View view;

        @BindView(R.id.valute_value)
        TextView valute_value;

        @BindView(R.id.valute_name)
        TextView valute_name;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }

    }

}