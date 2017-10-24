package forest.les.metronomic.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.listeners.ClickEventHook;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.drag.ItemTouchCallback;
import com.mikepenz.fastadapter_extensions.drag.SimpleDragCallback;
import com.mikepenz.materialize.MaterializeBuilder;
import com.stephentuso.welcome.WelcomeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.data.Repository;
import forest.les.metronomic.events.EventDynamicCurse;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.btc.Example;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.ui.adapters.CalcItem;
import forest.les.metronomic.ui.adapters.RxCalcItem;
import forest.les.metronomic.ui.adapters.SampleItem;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.HEAD;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ItemTouchCallback{


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


        Repository repository = new Repository();
        repository.getdata();

//        welcomeScreen = new WelcomeHelper(this, SplashActivity.class);
//        welcomeScreen.forceShow();


        init();

        getActualData();
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

        mActionModeHelper.withSupportSubItems(false);

        dragCallback = new SimpleDragCallback(this);
        touchHelper = new ItemTouchHelper(dragCallback);
        touchHelper.attachToRecyclerView(recycler);

        adapter.withPositionBasedStateManagement(false);

        adapter.setHasStableIds(true);
        adapter.withSelectable(true);
        adapter.withMultiSelect(false);
//        adapter.withItemEvent(new RxCalcItem.SelectClckEvent());

        adapter.withOnPreClickListener(new FastAdapter.OnClickListener<IItem>() {
            @Override
            public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
                //we handle the default onClick behavior for the actionMode. This will return null if it didn't do anything and you can handle a normal onClick
                Timber.i("withOnPreClickListener: ");
                Boolean res = mActionModeHelper.onClick(item);
                return res != null ? res : false;
            }
        });

        adapter.withSelectionListener((item, selected) -> {
            if (item instanceof RxCalcItem) {
//                IItem headerItem = ((RxCalcItem) item).getParent();
//                if (headerItem != null) {
                int pos = adapter.getAdapterPosition(item);
                // Important: notify the header directly, not via the notifyadapterItemChanged!
                // we just want to update the view and we are sure, nothing else has to be done
                adapter.notifyItemChanged(pos);
//                }
            }
        });


//        adapter.withItemEvent(new ClickEventHook() {
//            @Nullable
//            @Override
//            public View onBind(@NonNull RecyclerView.ViewHolder viewHolder) {
//                //return the views on which you want to bind this event
//                if (viewHolder instanceof RxCalcItem.ViewHolder) {
//                    return viewHolder.itemView;
//                }
//                return null;
//            }
//            @Override
//            public void onClick(View v, int position, FastAdapter fastAdapter, IItem item) {
//                Timber.v("onClick() called with: " + "v = [" + v + "], position = [" + position + "], fastAdapter = [" + fastAdapter + "], item = [" + item + "]");
//                RxCalcItem item1 = (RxCalcItem) item;
//
////                item1.valute_value.setText("");
//
//
//
//
//                RxTextView.textChanges(item1.valute_value)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(charSequence -> {
//                            Timber.i("onClick: %s",charSequence.toString());
//
//                            updateCalculatorList(position,charSequence.toString(),item1.currentValute);
//
//                            adapter.notifyAdapterItemRangeChanged(0,position-1);
//                            adapter.notifyAdapterItemRangeChanged(position+1,currentRateListRx.size());
//                        });
//
////                getDisposable(item1.valute_value);
//
//                Timber.i("onClick: data - %s", item1);
//            }
//        });
//        adapter.withSelectionListener((item, selected) -> {
//
//        });


//        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onChanged() {
//
//                Timber.i("onChanged: ");
//            }
//
//            @Override
//            public void onItemRangeChanged(int positionStart, int itemCount) {
//
//                Timber.i("registerAdapterDataObserver: %d",positionStart);
//
////                super.onItemRangeChanged(positionStart, itemCount);
//            }
//        });

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


        CbrApi cbrApi = Helper.getCbrApi();

        Observable<Valuta> valutaFullObservable = cbrApi.getValutesFullData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<ValCurs> currentObservable = cbrApi.getCurrentRates(Helper.getActualTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observable<Example> btcObservable = Helper.getBtcApi().getData();

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
                .subscribe(v -> {
                    currentRateList = v;
                    showValuteRates(currentRateList);
                    Timber.i(String.valueOf(v));
                }, Throwable::printStackTrace);

    }


//    private void showDynamicRates() {
//
//
//        CbrApi cbrApi = Helper.getCbrApi();
//////
////        Single<Valuta> map = cbrApi.getValutesFullData()
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .map(valuta -> {
////                    List<Item> items = valuta.getItems();
////                    Timber.i(String.valueOf(items));
////                    return valuta;
////                });
////                .subscribe(
////                        valuta -> {
////                        },
////                        Throwable::printStackTrace
////                );
////
//        String valuteCode = "R01235";
//
//        Single<ValCursPeriod> valCursPeriodSingle = cbrApi.getPeriodRx("01.01.2010", "01.01.2011", valuteCode)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
////                .subscribe(valCursPeriod -> {
////                    Timber.i(String.valueOf(valCursPeriod.records));
////                }, Throwable::printStackTrace);
//
////        Observable.merge();
//
//        ArrayList items = new ArrayList();
//        items.add(new CalcItem(currentRateList));
//        adapter.setNewList(items);
//
//    }

    private void calcuLate() {
        adapter.clear();

//        LinAdapter linAdapter = new LinAdapter(this,currentRateList);
//        linearLayout.setAdapter(linAdapter);

//        recycler.setVisibility(View.GONE);


//        anotherAdapter = new AnotherAdapter(this,currentRateList);
//        recycler.setAdapter(anotherAdapter);


//        List<String> dataSet = new ArrayList<>();
//        dataSet.add("This");
//        dataSet.add("is");
//        dataSet.add("an");
//        dataSet.add("example");
//        dataSet.add("of RX!");
//
//        RxDataSource<Valute> rxDataSource = new RxDataSource<>(currentRateList);
//
//        rxDataSource
////                .map(String::toLowerCase)
////                .repeat(10)
//                //cast call this method with the binding Layout
//                .<ItemLayoutBinding>bindRecyclerView(recycler, R.layout.item_layout)
//                .subscribe(viewHolder -> {
//
//                    ItemLayoutBinding b = viewHolder.getViewDataBinding();
//
//                    int adapterPosition = viewHolder.getAdapterPosition();
//
//                    TextView textViewItem = b.textViewItem;
//                    textViewItem.setText(viewHolder.getItem().name);
//
//                    EditText editTextViewItem = b.editTextViewItem;
//                    editTextViewItem.setText(viewHolder.getItem().value);
//
//                    RxTextView.afterTextChangeEvents(b.editTextViewItem)
//                            .subscribe(event -> {
//
//                                        Timber.i("calcuLate: %s", event.editable().toString());
//
//
//                                        RxDataSource<Valute> valuteRxDataSource = rxDataSource.elementAt(viewHolder.getAdapterPosition());
//
////                                        currentRateList.get(viewHolder.getAdapterPosition())
////                                                .value = "RRRRRRRRRR";
////
////                                        rxDataSource.updateDataSet(currentRateList);
////
////                                        rxDataSource.getRxAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
//                                    },
//                                    Throwable::printStackTrace);
//                });


        currentRateListMod = new ArrayList<>(currentRateList);
//        Collections.copy(currentRateListMod,currentRateList);


//

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


//


//        currentCalcItemHolder = new ArrayList();
//        currentCalcItem = new CalcItem(currentRateList);
//
//            //we make sure the new items are displayed properly

//        currentCalcItemHolder.add(currentCalcItem);
//        adapter.setNewList(currentCalcItemHolder);


    }

    private void initRecycler() {

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(adapter);

    }

//    public void navigate(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.dynamic_menu_item:
//                showDynamicRates();
//                break;
//            case R.id.calc_menu_item:
//                calcuLate();
//                break;
//            case R.id.money_rate_menu_item:
//                getCurrentValuteCurses();
//                break;
//        }
//    }

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

    @Subscribe
    public void showDynamic(EventDynamicCurse e) {
//
//        RealmValCursPeriod valCurs = e.valCurs;
//
//
//        Timber.i(valCurs.getName());
//
//
//        for (RealmRecord record : valCurs.records) {
//
////            Timber.i("RECORD %s %s",record.getValue(),record.date);
//        }
//
//
//        SampleIDynamicItem sampleIDynamicItem = new SampleIDynamicItem(this, valCurs);
//        sampleIDynamicItem.name = "DYNAMIC";
//
//        adapter.clear();
//        List list = new ArrayList();
//        list.add(sampleIDynamicItem);
//        adapter.setNewList(list);
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
                    Timber.i("VALUTE CONTROL: %s", newVal);
                    return newVal;
                })
                .map(valute -> {

                    String replace = valute.value.replace(",", ".");

                    valute.value = replace;

                    SampleItem sampleItem = new SampleItem(valute);

//                    sampleItem.value = Double.parseDouble(replace);

                    return sampleItem;
                })
                .doOnComplete(() -> adapter.set(iItems))
                .subscribe(e -> {
                    if (e.realmValute.charcode.equals("EUR")) {
                        iItems.set(1, e);

                    } else if (e.realmValute.charcode.equals("USD")) {
                        iItems.set(0, e);

                    } else {
                        iItems.add(e);

                    }

                }, Throwable::printStackTrace);

    }

//    @Subscribe
//    public void showRealmValCurses(EventRealmValCurse valCurseEvent) {
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
//        String format = simpleDateFormat.format(new Date());
//
//        RealmValCurs valCurs = valCurseEvent.valCurs;
//
//        iItems = new ArrayList<>();
//
//
//        Observable.fromIterable(valCurs.getValutes())
//                .filter(valute -> {
//
//                    Currency instance;
//                    try {
//                        instance = Currency.getInstance(valute.getCharcode());
//                    } catch (Exception e) {
//
//                        return false;
//                    }
//                    return Currency.getAvailableCurrencies().contains(instance);
//                })
//                .filter(realmValute -> realmValute.getCharcode().equals("USD") || realmValute.getCharcode().equals("EUR"))
//                .map(realmValute -> {
//
//
//                    Timber.i("REALM VALUTE %s", realmValute.getCharcode());
//
//                    return realmValute;
//                })
////                .filter(realmValute -> {
////                    String charcode = realmValute.getCharcode();
////                    String s = charcode.toLowerCase();
////                    return s.equals("eur");
////                })
////                .filter(realmValute -> {
////                    String charcode = realmValute.getCharcode();
////                    String s = charcode.toLowerCase();
////                    return s.equals("usd");
////                })
//
//
//                .map(valute -> {
//
//                    SampleItem sampleItem = new SampleItem(valute);
//
//                    return sampleItem;
//                })
//                .doOnComplete(() -> adapter.set(iItems))
//                .subscribe(e -> {
//
//                    String charcode = e.realmValute.getCharcode().toLowerCase();
//
//                    if (charcode.equals("USD")) {
//
//                        iItems.add(0, e);
//
//                    }
//
//                    if (charcode.equals("EUR")) {
//                        iItems.add(1, e);
//                    }
//
//                    iItems.add(e);
//                });
//
//        Timber.d("valCurseEvent: %s", valCurs);
//
//    }


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
    public void updateCalculatorList(int skipPosition, String value, Valute currVal){

        Observable.fromIterable(currentRateListRx)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(rxCalcItem -> !rxCalcItem.isSelected())
                .subscribe(rxCalcItem -> {
                    String calculate = Helper.calculate(value, currVal, rxCalcItem.currentValute);
                    Timber.i("updateCalculatorList: %s",calculate);
                    rxCalcItem.withValue(calculate);


                    adapter.notifyAdapterItemRangeChanged(0,skipPosition);
                    adapter.notifyAdapterItemRangeChanged(skipPosition+1,currentRateListRx.size()-skipPosition);

//                    adapter.notifyAdapterItemRangeChanged(skipPosition+1,currentRateListRx.size());

                }, Throwable::printStackTrace);




//        for (int i = 0; i < currentRateListRx.size(); i++) {

//            if (i==2){
//
//                RxCalcItem rxCalcItem = currentRateListRx.get(i);
//                Valute outValute = rxCalcItem.currentValute;
//                rxCalcItem.withValue(Helper.calculate(value,currVal,outValute));
//                adapter.notifyAdapterItemChanged(i);
//
//            }


//            List<RxCalcItem> adapterItems = adapter.getAdapterItems();
//            Observable.fromIterable(adapterItems)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .filter(rxCalcItem -> !rxCalcItem.isSelected())
//                    .doOnComplete(() -> {adapter.notifyDataSetChanged();})
//                    .doOnNext(rxCalcItem -> {
//                        Timber.i("do on next: %s",rxCalcItem.getTitle().getText());
//                        String calculate = Helper.calculate(value, currVal, rxCalcItem.currentValute);
//                        Timber.i("Calculate %s", calculate);
//                        rxCalcItem.withValue(calculate);
//                    })
//                    .subscribe(rxCalcItem ->{
//
//                        String name = rxCalcItem.currentValute.name;
//                        String val = rxCalcItem.getValue().getText();
//
//
//                        Timber.i("updateCalculatorList: %s %s", name, val);
//
//                        int position = adapter.getPosition(rxCalcItem);
//
//                        Timber.i("position %d", position);
//
////                        adapter.notifyItemChanged();
////                        adapter.not
//                   },Throwable::printStackTrace);
        }


//    }


    void getBitcois() {

        Helper.getBtcApi().getData()
                .subscribe(example -> {
                    Timber.i("getBitcois: %s", example);
                });
    }



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
//        Disposable subscribe = editTextObservable.subscribe();
//
//        CompositeDisposable compositeDisposable = new CompositeDisposable();
//        compositeDisposable.add(subscribe);
//        compositeDisposable.
    }


    @Override
    public boolean itemTouchOnMove(int oldPosition, int newPosition) {
        Collections.swap(adapter.getAdapterItems(), oldPosition, newPosition); // change position
        adapter.notifyAdapterItemMoved(oldPosition, newPosition);
        return true;
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
