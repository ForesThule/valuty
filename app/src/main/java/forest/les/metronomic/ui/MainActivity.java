package forest.les.metronomic.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemFilter;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.expandable.ExpandableExtension;
import com.mikepenz.fastadapter.listeners.ItemFilterListener;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.materialize.MaterializeBuilder;
import com.stephentuso.welcome.WelcomeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.data.Repository;
import forest.les.metronomic.model.Record;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.btc.Example;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.ui.adapters.CalcItem;
import forest.les.metronomic.ui.adapters.ExpandableItem;
import forest.les.metronomic.ui.adapters.RxCalcItem;
import forest.les.metronomic.ui.adapters.SampleItem;
import forest.les.metronomic.ui.adapters.SubItem;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ItemTouchCallback, ItemFilterListener<SampleItem> {


//    SomeObserver someObserver = new SomeObserver(getLifecycle(), SomeObserver.Owner.ACTIVITY);


    //    private PlayConfig playConfig;
//    private ActivityMainBinding binding;
    private List<IItem> iItems;
    public FastItemAdapter adapter;
    private RecyclerView recycler;
    private WelcomeHelper welcomeScreen;

    private List<Valute> currentRateList;
    private List<Valute> currentRateListMod;
    private List<RxCalcItem> currentRateListRx = new ArrayList<>();

    private Valuta currentValuteData;
    //    @BindView(R.id.nav_main)
    BottomNavigationView navigation;
    private AnotherAdapter anotherAdapter;
    private CalcItem currentCalcItem;
    private ArrayList currentCalcItemHolder;

    public Valute currentValute;

    private ActionModeHelper mActionModeHelper;
    private SimpleDragCallback dragCallback;
    private ItemTouchHelper touchHelper;
    private ItemFilter itemFilter;
    private CbrApi cbrApi;
    private Observable<Example> btcObservable;
    private ExpandableExtension<IItem> expandableExtension;
    private ValCursPeriod valCursPeriod = new ValCursPeriod();
    private List<ValCursPeriod> periodList = new ArrayList<>();


//    @Bind(R.id.tollbar_et)
//    EditText editText;

//    @BindView(R.id.linearLayout)
//    ListView linearLayout;


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);

        setContentView(R.layout.main);
        ButterKnife.bind(this);

        new MaterializeBuilder().withActivity(this).build();


//        Repository repository = new Repository();
//        repository.getdata();

//        welcomeScreen = new WelcomeHelper(this, SplashActivity.class);
//        welcomeScreen.forceShow();


        init();

        getActualData();
    }

    //
    public void filterAdapter() {


//        itemFilter.withFilterPredicate((item, constraint) -> {
//            Timber.i("filter: ");
//            SampleItem i =(SampleItem) item;
//            String s = i.valute.charcode.toLowerCase();
//
//
//            String anObject0 = constraint.toString().toLowerCase();
//            String anObject1 = constraint.toString().toLowerCase();
//            String anObject2 = constraint.toString().toLowerCase();
//            return s.equals("USD".toLowerCase())||s.equals("EUR".toLowerCase())|| s.equals("RUB".toLowerCase());
//        });
//

        Timber.i("filterAdapter: ");


        adapter.filter("RUB");

    }

    private void init() {

        Valute rub = new Valute();
        rub.value = "1";
        rub.nominal = "1";
        rub.name = "Российский рубль";
        rub.charcode = "RUB";

        setCurrentValute(rub);

        navigation = (BottomNavigationView) findViewById(R.id.nav_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new FastItemAdapter();

        mActionModeHelper = new ActionModeHelper(adapter, R.menu.navigation, new ActionBarCallBack());

        mActionModeHelper.withAutoDeselect(true);

//        mActionModeHelper.withSupportSubItems(false);

        dragCallback = new SimpleDragCallback(this);
        touchHelper = new ItemTouchHelper(dragCallback);
        touchHelper.attachToRecyclerView(recycler);

//        adapter.withPositionBasedStateManagement(false);

        adapter.setHasStableIds(true);
        adapter.withSelectable(true);
        adapter.withMultiSelect(false);

        expandableExtension = new ExpandableExtension<>();
        adapter.addExtension(expandableExtension);


//        adapter.getItemFilter().performFiltering("sss");

        //configure the itemAdapter
        itemFilter = adapter.getItemFilter();

        adapter.getItemFilter().withItemFilterListener(this);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.money_rate_menu_item);

        android.app.ActionBar supportActionBar = getActionBar();

        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(this, 1));
        recycler.setItemAnimator(new DefaultItemAnimator());
    }

    @NonNull
    private void getDisposable(EditText valute_value) {


    }

    private void setCurrentValute(Valute valute) {

        currentValute = valute;
    }


    private void getActualData() {

        cbrApi = Helper.getCbrApi();
        btcObservable = Helper.getBtcApi().getData();

        Observable<Valuta> valutaFullObservable = cbrApi.getValutesFullData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<ValCurs> currentObservable = cbrApi.getCurrentRates(Helper.getActualTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


//        Single<ValCursPeriod> singlePeriod =

        Observable.zip(valutaFullObservable, currentObservable, btcObservable, (valuta, valCurs, example) -> {

            List<Valute> currentRateList = valCurs.valute;

            Valute rub = new Valute();
            rub.value = "1";
            rub.nominal = "1";
            rub.numcode = "99999";
            rub.name = "Российский рубль";
            rub.charcode = "RUB";
            currentRateList.add(3, rub);

            Valute btc = new Valute();
            btc.value = example.getRUB().getLast().toString();
            btc.nominal = "1";
            btc.numcode = "66666";
            btc.name = "Bitcoin";
            btc.charcode = "BTC";
            currentRateList.add(4, btc);

            Timber.i("getActualData: %s", btc);

            return currentRateList;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::getPeriodsRates)
                .subscribe(v -> {
                    currentRateList = v;
                    Timber.i(String.valueOf(v));
                }, Throwable::printStackTrace);
    }

    private void getPeriodsRates() {
        Observable.fromIterable(currentRateList)
                .flatMap(valute -> cbrApi.getPeriodRx(Helper.getMonthAgoTime(), Helper.getActualTime(), valute.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                )
                .doOnComplete(() -> showValuteRates(currentRateList))
                .subscribe(valCursPeriod1 -> {
                    Timber.i("getPeriodsRates: %s",valCursPeriod1.ID);
                    periodList.add(valCursPeriod1);
                },throwable -> Timber.e(throwable,"getPeriodsRates: %s",throwable.getLocalizedMessage()));
    }


    private void calcuLate() {
        adapter.clear();

        currentRateListMod = new ArrayList<>(currentRateList);

        Observable.fromIterable(currentRateListMod)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> adapter.setNewList(currentRateListRx))
                .map(valute -> {
                    RxCalcItem rxCalcItem = new RxCalcItem(this, valute);
                    Timber.i(String.valueOf(rxCalcItem.getIdentifier()));
                    return rxCalcItem;
                })
                .map(rxCalcItem -> {
                    rxCalcItem.withSelectable(true);
                    return rxCalcItem;
                })
                .subscribe(currentRateListRx::add, Throwable::printStackTrace);
    }


    private void initRecycler() {

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(adapter);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
//            case R.id.dynamic_menu_item:
//                showDynamicRates();
//                return true;
            case R.id.calc_menu_item:
                calcuLate();
                return true;
            case R.id.money_rate_menu_item:
                getCurrentValuteCurses();
                return true;
        }
        return false;
    };


    public void getCurrentValuteCurses() {
        if (null == currentRateList) {
            getActualData();
        } else {
            showValuteRates(currentRateList);
        }
    }

    public void showValuteRates(List<Valute> currentRateList) {

        Timber.i("SHOW VAL CURSES");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());


        iItems = new ArrayList<>();


        Observable.fromIterable(currentRateList)
                .map(valute -> {
//                    Currency.getInstance(valute.charcode);
                    Valute newVal = valute;
                    Timber.i("show valute rates: %s", newVal.name);
                    return newVal;
                })
                .map(valute -> {

                    String replace = valute.value.replace(",", ".");
                    valute.value = replace;

                    ExpandableItem sampleItem = new ExpandableItem(valute);
                    List<SubItem> subItems = new ArrayList<>();

                    String id = valute.id;

                    SubItem subItem = new SubItem();

                    for (ValCursPeriod valCursPeriod : periodList) {
                        if (valCursPeriod.ID.equals(id)) {
                            List<Record> records = valCursPeriod.records;
                            subItem.records = records;
                        }
                    }
                    subItems.add(subItem);
                    sampleItem.withSubItems(subItems);

//                    SampleItem sampleItem = new SampleItem(valute);
//                    List<DynamicItem> subitems = new ArrayList<>();
//
//                    DynamicItem e = new DynamicItem(valute);
//                    DynamicItem dynamicItem = e.withParent(sampleItem);
//                    subitems.add(dynamicItem);
//
//                    sampleItem.withSubItems(subitems);
//                    sampleItem.withIsExpanded(true);
//
//                    Timber.i("showValuteRates: %s",sampleItem);
////                    sampleItem.value = Double.parseDouble(replace);

                    return sampleItem;
                })
                .doOnComplete(() -> {
                    adapter.setNewList(iItems);
                    filterAdapter();
                })
                .subscribe(e -> {
//                    if (e.valute.charcode.equals("EUR")) {
//                        iItems.set(1, e);
//                    } else if (e.valute.charcode.equals("USD")) {
//                        iItems.set(0, e);
//                    }


//                    else {
                    iItems.add(e);
//                    }
                }, throwable -> Timber.e("showValuteRates: %s",throwable.getLocalizedMessage()));
    }


    @Subscribe
    public String calculate(String input, Valute inpValute, Valute outValute) {

        String value = input;

        Timber.i(inpValute.toString());

        Double doubleValue = Double.parseDouble(value);
        BigDecimal decValue = BigDecimal.valueOf(doubleValue);

        Double inValuteDouble = Double.parseDouble(inpValute.value.replace(",", "."));
        Double inNominalDouble = Double.parseDouble(inpValute.nominal);
        BigDecimal inValuteDec = BigDecimal.valueOf(inValuteDouble);
        BigDecimal inNominalDec = BigDecimal.valueOf(inNominalDouble);
        BigDecimal multiply = decValue.multiply(inValuteDec);
        BigDecimal inRubles = multiply.divide(inNominalDec, BigDecimal.ROUND_HALF_UP);


        Double outValuteDouble = Double.parseDouble(outValute.value.replace(",", "."));
        Double outNominal = Double.parseDouble(outValute.nominal);
        BigDecimal decOutNominal = BigDecimal.valueOf(outNominal);
        BigDecimal outValuteDec = BigDecimal.valueOf(outValuteDouble);
        BigDecimal outInRubles = outValuteDec.divide(decOutNominal, BigDecimal.ROUND_HALF_UP);


        BigDecimal divide = inRubles.divide(outInRubles, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

        Timber.i("calculate: %s %s", inValuteDec, outValuteDec);

        Timber.i("calculate: %s", divide);

        return divide.toString();
    }

    //change all valute item list by input valute and value
    public void updateCalculatorList(int skipPosition, String value, Valute currVal) {

        Observable.fromIterable(currentRateListRx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(rxCalcItem -> !rxCalcItem.isSelected())
                .subscribe(rxCalcItem -> {
                    String calculate = Helper.calculate(value, currVal, rxCalcItem.currentValute);
                    Timber.i("updateCalculatorList: %s", calculate);
                    rxCalcItem.withValue(calculate);


                    adapter.notifyAdapterItemRangeChanged(0, skipPosition);
                    adapter.notifyAdapterItemRangeChanged(skipPosition + 1, currentRateListRx.size() - skipPosition);

//                    adapter.notifyAdapterItemRangeChanged(skipPosition+1,currentRateListRx.size());

                }, Throwable::printStackTrace);


    }


//    }


    void getBitcois() {

        Helper.getBtcApi().getData()
                .subscribe(example -> {
                    Timber.i("getBitcois: %s", example);
                });
    }


    @Subscribe

    public void updateAnotherAdapter(int pos) {

        List<Valute> data = anotherAdapter.data;
        for (int i = 0; i < data.size(); i++) {
            if (i != pos) {
                Valute valute = data.get(i);
                valute.name = String.format("%d%d%d", i, i, i);
                if (recycler.isComputingLayout()) {

                    anotherAdapter.notifyItemChanged(i);
                }

            }
        }
    }

    public void observEditText(Observable<CharSequence> editTextObservable) {

    }


    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap(adapter.getAdapterItems(), oldPosition, newPosition); // change position
        adapter.notifyAdapterItemMoved(oldPosition, newPosition);
        return true;
    }

    @Override
    public void itemTouchDropped(int oldPosition, int newPosition) {

    }

    @Override
    public void itemsFiltered(CharSequence constraint, List<SampleItem> results) {
//
//        for (SampleItem result : results) {
//
//            Timber.i("itemsFiltered: %s",result);
//
//        }


    }

    @Override
    public void onReset() {

    }

    /**
     * Our ActionBarCallBack to showcase the CAB
     */
    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            Timber.i("onActionItemClicked:ActionBarCallBack ");
            //logic if an item was clicked
            //return false as we want default behavior to go on
            return false;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }

}
