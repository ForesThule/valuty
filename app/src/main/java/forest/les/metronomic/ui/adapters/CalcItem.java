package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
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

public class CalcItem extends AbstractItem<CalcItem, CalcItem.ViewHolder> {
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


    public CalcItem(List<Valute> currentValutesRates) {

        this.currentValutesRates = currentValutesRates;
    }

    //The unique ID for this type of item
    @Override
    public int getType() {
        return R.id.calc_sampleitem_id;
    }

    //The layout to be used for this type of item
    @Override
    public int getLayoutRes() {
        return R.layout.calculator;
    }


    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        Context context = viewHolder.view.getContext();

        tv_output = viewHolder.tv_output;
        RecyclerView rvCalc = viewHolder.rvCalc;

        spinnerInput = viewHolder.spinnerInput;
        spinnerOut = viewHolder.spinnerOutput;

        spinnerDataList = new ArrayList<>();
//        ArrayAdapter<String> adapterInput = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, data);
//
//
//        MaterialSpinnerAdapter adapterInput = new MaterialSpinnerAdapter(context,data);
//
//        spinnerInput.setAdapter(adapterInput);


        CbrApi cbrApi = Helper.getCbrApi();

//        currentValutesRates

        Observable.fromIterable(currentValutesRates)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(valute -> {
                    try {
                        Currency instance = Currency.getInstance(valute.charcode);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }

                })
                .subscribe(valute -> {

                            Currency instance = Currency.getInstance(valute.charcode);

                            String name = valute.name;
                            String displayName = instance.getDisplayName(Locale.getDefault());
                            String symbol = instance.getSymbol(Locale.getDefault());


                            spinnerDataList.add(name);

                            adapterInput.notifyDataSetChanged();

                        },
                        Throwable::printStackTrace);

        cbrApi.getValutesFullData().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(valuta -> valuta.items)
                .subscribe(items -> {
                    currentItems = items;
                    Timber.i("ITEMS: %s", currentItems);
                }, Throwable::printStackTrace);


//        Consumer<? super CharSequence> text = RxTextView.text(tv_output);


        adapterInput = new ArrayAdapter<String>(context, R.layout.dropdown_item, spinnerDataList);
//        adapterOut = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerDataList);

        adapterInput.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        adapterOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInput.setAdapter(adapterInput);
        spinnerOut.setAdapter(adapterInput);

//        RxAdapterView.itemSelections(spinnerInput)
//                .subscribeOn(AndroidSchedulers.mainThread())
//
//                .subscribe(integer -> {
////                    setinputValute(integer);
//                    calculate(editText.getText().toString(),inputValute,outputValute);
//                    Log.v("spinner input", integer.toString());
//                });
//        RxAdapterView.itemSelections(spinnerOut)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .subscribe(integer -> {
////                    setOutputValute(integer);
//                    calculate(editText.getText().toString(),inputValute,outputValute);
//
//                    Log.v("spinner out", integer.toString());
//                });

        Timber.i("bindView: %s", spinnerInput);
        Timber.i("bindView: %s", spinnerOut);

        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.i("onItemSelected: %d", position);

                setinputValute(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Timber.i("onItemSelected: OUTPUT %d", position);

                setOutputValute(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editText = viewHolder.editText;


        RxTextView.textChanges(editText)
                .map(charSequence -> charSequence.toString())
//                .filter(s -> !s.equals(""))
                .map(s -> s.equals("") ? "" : calculate())
//                .onErrorReturn(throwable -> neuw String(""))
                .subscribe(
                        aLong -> {
                            if (aLong.equals("")) {
                                Timber.i("EMPTY TEXT");
                                tv_output.setText("");
                                tv_output.invalidate();

                            } else {
                                String message = aLong;
                                Timber.i(message);

                                tv_output.setText(message);
                            }


//                    RxTextView.text(tv_output)
//                            .accept(String.valueOf(charSequence))

                        },
                        Throwable::printStackTrace);


        FastItemAdapter adapter = new FastItemAdapter();
        rvCalc.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
        rvCalc.setAdapter(adapter);


        Observable.fromIterable(currentValutesRates)
                .map(RxCalcItem::new)
                .subscribe(adapter::add);


//        adapterInput.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        spinnerInput.setOnItemSelectedListener((view, position, id, item) -> {
//
//            Timber.i("SpinnerBindingAdapter ITEM CLICK");
//        });


        //set the text for the description or hide

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

        holder.editText.setText(null);

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

        @BindView(R.id.et_value_output)
        TextView tv_output;

        @BindView(R.id.spin_valutes_input)
        Spinner spinnerInput;


        @BindView(R.id.spin_valutes_output)
        Spinner spinnerOutput;

        @BindView(R.id.et_value)
        EditText editText;

        @BindView(R.id.rv_calc)
        RecyclerView rvCalc;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }

    }

}