package forest.les.metronomic.ui;

import android.arch.lifecycle.LifecycleActivity;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.stephentuso.welcome.WelcomeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.events.EventDynamicCurse;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.realm.RealmRecord;
import forest.les.metronomic.model.realm.RealmValCursPeriod;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.ui.adapters.CalcItem;
import forest.les.metronomic.ui.adapters.SampleIDynamicItem;
import forest.les.metronomic.ui.adapters.SampleItem;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends LifecycleActivity {


    SomeObserver someObserver = new SomeObserver(getLifecycle(), SomeObserver.Owner.ACTIVITY);


    //    private PlayConfig playConfig;
//    private ActivityMainBinding binding;
    private List<IItem> iItems;
    private FastItemAdapter adapter;

    private RecyclerView recycler;

    BottomNavigationView navigation;
    private WelcomeHelper welcomeScreen;
    private List<Valute> currentRateList;
    private Valuta currentValuteData;


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

//        realm  = ThisApp.get(this).realm;

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        welcomeScreen = new WelcomeHelper(this, SplashActivity.class);
        welcomeScreen.forceShow();


//        final MyViewModel viewModel = ViewModelProviders.of(this).get(MyViewModel.class);
//
//        viewModel.getProgressState().observe(this,aBoolean -> {
//
//        });

        navigation = (BottomNavigationView) findViewById(R.id.nav_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new FastItemAdapter();

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//
        navigation.setSelectedItemId(R.id.money_rate_menu_item);

        android.app.ActionBar supportActionBar = getActionBar();


//
        recycler.setAdapter(adapter);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.addItemDecoration(new DividerItemDecoration(this, 1));

        getActualData();

    }

    private void getActualData() {

        CbrApi cbrApi = Helper.getCbrApi();

        cbrApi.getValutesFullData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valuta -> {

                    currentValuteData = valuta;
                    Timber.i(valuta.name);
                }, Throwable::printStackTrace);


        cbrApi.getCurrentRates(Helper.getActualTime())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(valCurs -> {
                    ValCurs valCurs1 = valCurs;
                    List<Valute> valutes = valCurs1.valute;

                    Valute rub = new Valute();
                    rub.value = "1";
                    rub.nominal = "1";
                    rub.name = "Российский рубль";
                    rub.charcode = "RUB";
                    valutes.add(rub);

                    valCurs1.valute = valutes;

                    return valCurs1;

                })
                .subscribe(valCurs -> {

                    currentRateList = valCurs.valute;

                    Timber.i(valCurs.name);
                }, Throwable::printStackTrace);

    }


    private void showDynamicRates() {

        CbrApi cbrApi = Helper.getCbrApi();
//
        cbrApi.getValutesFullData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(valuta -> {
                    List<Item> items = valuta.getItems();
                    Timber.i(String.valueOf(items));
                    return valuta;
                })
                .subscribe(
                        valuta -> {},
                        Throwable::printStackTrace
                );
//
        String valuteCode = "R01235";

        cbrApi.getPeriodRx("01.01.2010","01.01.2011", valuteCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valCursPeriod -> {
                    Timber.i(String.valueOf(valCursPeriod.records));
                },Throwable::printStackTrace);


        ArrayList items = new ArrayList();
        items.add(new CalcItem(currentRateList));
        adapter.setNewList(items);

    }

    private void calcuLate() {

        adapter.clear();

        ArrayList items = new ArrayList();
        items.add(new CalcItem(currentRateList));
        adapter.setNewList(items);

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

            Calendar instance = Calendar.getInstance();
            int day = instance.get(Calendar.DAY_OF_MONTH);
            int month = instance.get(Calendar.MONTH);
            int year = instance.get(Calendar.YEAR);

            String actualTime = Helper.getActualTime();

            CbrApi cbrApi = Helper.getCbrApi();

            Timber.i("time %s", actualTime);

            cbrApi.getCurrentRates(actualTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(valutes -> {
                        Timber.i(valutes.valute.toString());

                        currentRateList = valutes.valute;

                        Valute rub = new Valute();
                        rub.value = "1";
                        rub.nominal = "1";
                        rub.name = "Ruble";
                        rub.charcode = "RUB";

                        currentRateList.add(rub);


                        showValuteRates(currentRateList);
                    });

        } else {
            showValuteRates(currentRateList);
        }
    }

    @Subscribe
    public void showDynamic(EventDynamicCurse e) {

        RealmValCursPeriod valCurs = e.valCurs;


        Timber.i(valCurs.getName());


        for (RealmRecord record : valCurs.records) {

//            Timber.i("RECORD %s %s",record.getValue(),record.date);
        }


        SampleIDynamicItem sampleIDynamicItem = new SampleIDynamicItem(this, valCurs);
        sampleIDynamicItem.name = "DYNAMIC";

        adapter.clear();
        List list = new ArrayList();
        list.add(sampleIDynamicItem);
        adapter.setNewList(list);
    }


    public void showValuteRates(List<Valute> currentRateList) {

        Timber.i("SHOW VAL CURSES");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());


        iItems = new ArrayList<>();


        Observable.fromIterable(currentRateList)
                .filter(valute -> {

                    Currency instance;
                    try {
                        instance = Currency.getInstance(valute.charcode);
                    } catch (Exception e){

                        return false;
                    }
                    return Currency.getAvailableCurrencies().contains(instance);
                })
                .map(valute -> {
//                    Currency.getInstance(valute.charcode);
                    Valute newVal = valute;
                    Timber.i("VALUTE CONTROL: %s",newVal);
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
                    if (e.realmValute.charcode.equals("EUR")){
                        iItems.set(1,e);

                    } else
                        if (e.realmValute.charcode.equals("USD")){
                        iItems.set(0,e);

                    }  else {
                        iItems.add(e);

                    }

                },Throwable::printStackTrace);

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


}
