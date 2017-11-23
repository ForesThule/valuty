package forest.les.metronomic.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.commons.utils.FastAdapterUIUtils;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.materialize.holder.StringHolder;
import com.mikepenz.materialize.util.UIUtils;

import org.reactivestreams.Subscription;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.ui.MainActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class RxCalcItem extends AbstractItem<RxCalcItem, RxCalcItem.ViewHolder> {
    public final Valute currentValute;
    private final MainActivity activity;
    private final long numcode;
    //    public String name = "";

    volatile Valute inputValute;
    volatile Valute outputValute;
    private StringHolder title;
    private StringHolder value;

    public EditText valute_value;
    private TextView valute_name;

    private InputMethodManager imm;

    private Disposable subscribe;
    private CompositeDisposable compositeDisposable;


    public RxCalcItem(MainActivity activity, Valute valute) {

        compositeDisposable = new CompositeDisposable();
        this.activity = activity;
        this.currentValute = valute;

        this.title = new StringHolder(valute.name);
        this.value = new StringHolder(valute.value);

        numcode = Long.parseLong(currentValute.numcode);
        withIdentifier(numcode);
    }

    public RxCalcItem withName(String name) {
        title.setText(name);
        return this;
    }

    public RxCalcItem withValue(String value) {
        this.value.setText(value);
        return this;
    }

//    @Override
//    public RxCalcItem withSetSelected(boolean selected) {
//        Timber.i("withSetSelected: %s",selected);
//
//        return this;
//    }

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


//    @Override
//    public RxCalcItem withSetSelected(boolean selected) {
//        Timber.i("onSelect: valute: %s select: %s",currentValute.name,selected);
//        if (selected){
//            RxTvalute_value
//        }
//        return super.withSetSelected(selected);
//
//    }

    //The logic to bind your data to the view
    @Override
    public void bindView(ViewHolder viewHolder, List<Object> payloads) {

        Timber.i("bindView: %s", currentValute.name);
        //call super so the selection is already handled for you
        super.bindView(viewHolder, payloads);

        Context context = viewHolder.view.getContext();

        UIUtils.setBackground(viewHolder.view, FastAdapterUIUtils.getSelectableBackground(context, Color.RED, true));


        valute_value = viewHolder.valute_value;
        valute_name = viewHolder.valute_name;

        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        setvalues();

        if (isSelected()) {

            valute_value.setEnabled(true);
            valute_value.setText("");

            valute_value.requestFocus();

//            if (!imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
//                imm.showSoftInput(valute_value,InputMethodManager.SHOW_FORCED);

//            valute_value.addTextChangedListener(textWatcher);


            subscribe = RxTextView.textChanges(valute_value)
                    .filter(cs -> !cs.toString().equals(""))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .delay(500, TimeUnit.MILLISECONDS)
                    .subscribe(charSequence -> {

                        withValue(charSequence.toString());

                        Timber.i("INPUT: %s", charSequence.toString());

                        Timber.i("%s", subscribe.toString());

                        Timber.i("%s %d",title.getText(),compositeDisposable.size());

                        activity.updateCalculatorList(viewHolder.getAdapterPosition(), charSequence.toString(), currentValute);

                    }, Throwable::printStackTrace);

            compositeDisposable.add(subscribe);

            Timber.i("%s %s", currentValute.name, isSelected());


        } else {

            valute_value.clearFocus();
            valute_value.setEnabled(false);


        }

    }

    private void setvalues() {
        title.applyTo(valute_name);
        value.applyTo(valute_value);
    }


    //reset the view here (this is an optional method, but recommended)
    @Override
    public void unbindView(ViewHolder holder) {

        super.unbindView(holder);
//        Timber.i("unbindView: %s", currentValute.name);


        if (!isSelected()) {
            compositeDisposable.clear();

            Timber.i("unbindView: is Selected %s", currentValute.name);

        }


//        holder.valute_value.removeTextChangedListener(textWatcher);

        holder.valute_name.setText(null);
        holder.valute_value.setText(null);
        holder.valute_value.clearFocus();


//        holder.valute_value.setEnabled(false);

    }


    public String calculate() {

        String value = valute_value.getText().toString();

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

    public StringHolder getTitle() {
        return title;
    }

    public StringHolder getValue() {
        return value;
    }

    public static class SelectClckEvent extends ClickEventHook<RxCalcItem> {
        @Override
        public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
            if (viewHolder instanceof RxCalcItem.ViewHolder) {
                View view = ((ViewHolder) viewHolder).valute_value;

                Timber.i("onBind EventHook:");

                return view;
            }
            return null;
        }


        @Override
        public void onClick(View v, int position, FastAdapter<RxCalcItem> fastAdapter, RxCalcItem item) {

            Timber.i("onClick: ");

            if (!item.isSelected()) {
                Set<Integer> selections = fastAdapter.getSelections();
                if (!selections.isEmpty()) {
                    int selectedPosition = selections.iterator().next();
                    fastAdapter.deselect();
                    Timber.i("onClick: notifyAdapter from hook");
                    fastAdapter.notifyItemChanged(selectedPosition);
                }
                fastAdapter.select(position);
            }
        }
    }

    //Init the viewHolder for this Item
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    //The viewHolder used for this item. This viewHolder is always reused by the RecyclerView so scrolling is blazing fast
    public static class ViewHolder extends RecyclerView.ViewHolder {


        public View view;

        @BindView(R.id.valute_value)
        EditText valute_value;

        @BindView(R.id.valute_name)
        TextView valute_name;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;

        }

    }

}