package forest.les.metronomic.ui;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.orhanobut.hawk.Hawk;

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
import forest.les.metronomic.ThisApp;
import forest.les.metronomic.events.EventDynamicCurse;
import forest.les.metronomic.events.EventRealmValCurse;
import forest.les.metronomic.events.EventValCurse;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.ValPeriodWrapper;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.realm.RealmString;
import forest.les.metronomic.model.realm.RealmValCurs;
import forest.les.metronomic.model.realm.RealmValuta;
import forest.les.metronomic.network.WorkerIntentService;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

//    private PlayConfig playConfig;
//    private ActivityMainBinding binding;
    private List<IItem> iItems;
    private FastItemAdapter adapter;

    private RecyclerView recycler;

    BottomNavigationView navigation;

    private Realm realm;



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

        realm  = ThisApp.get(this).realm;

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        navigation = (BottomNavigationView) findViewById(R.id.nav_main);
        recycler = (RecyclerView) findViewById(R.id.recycler);


//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.money_rate_menu_item);

        ActionBar supportActionBar = getSupportActionBar();

        supportActionBar.setTitle("VALUTY");


        adapter = new FastItemAdapter();

        recycler.setAdapter(adapter);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.addItemDecoration(new DividerItemDecoration(this,1));


    }


    private void setDynamicRates() {

        SampleIDynamicItem sampleIDynamicItem = new SampleIDynamicItem();
        sampleIDynamicItem.name = "DYNAMIC";

        adapter.clear();
        adapter.add(sampleIDynamicItem);

    }

    private void calcuLate() {



    }

    private void initRecycler() {

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(adapter);

    }

    public void navigate(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dynamic_menu_item:
                setDynamicRates();
                break;
            case R.id.calc_menu_item:
                calcuLate();
                break;
            case R.id.money_rate_menu_item:
                getCurrentValuteCurses();
                break;
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.dynamic_menu_item:
                setDynamicRates();
                return true;
            case R.id.calc_menu_item:
                calcuLate();
                return true;
            case R.id.money_rate_menu_item:
                getCurrentValuteCurses();
                return true;
        }
        return false;
    };

    private void getCurrentValuteCurses() {

        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DAY_OF_MONTH);
        int month = instance.get(Calendar.MONTH);
        int year = instance.get(Calendar.YEAR);

//        String formatDate = String.currentHour("%02d/%02d/%d", day, month, year);
//        System.out.println(formatDate);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentHour = simpleDateFormat.format(new Date());

        RealmResults<RealmValCurs> date = realm.where(RealmValCurs.class)
                .findAll();


        if (date.size()>0) {

            System.out.println("REALM HAVE VALCURS AT THESE DAY ");
            realm.beginTransaction();

            Timber.i("DATE %s",date.size());

            for (RealmValCurs realmValCurs : date) {
                System.out.println(realmValCurs.name);
            }

            realm.commitTransaction();

            for (RealmValCurs realmValCurs : date) {
                Timber.i("realmValCurs-----> %s", realmValCurs);
                Timber.i("realmValCurs-----> %s", realmValCurs.name);
                Timber.i("realmValCurs-----> %s", realmValCurs.valutes);

                EventBus.getDefault().post(new EventRealmValCurse(realmValCurs));
            }

        } else {

            System.out.println("REALM does not HAVE AT THESE HOUR ");

            WorkerIntentService.getCurrentCurs(this,currentHour);

        }
    }


//    public PlayConfig getPlayConfig() {
//        return PlayConfig
//                .res(this, R.raw.click)
//                .looping(false)
//                .build();
//
//    }



    private void stop() {



        //        getRxAudioPlayer().stopPlay();
    }


    @Subscribe
    public void showDynamic(EventDynamicCurse e){

        Hawk.init(this);
        Hawk.get("dynamic");

        while (Hawk.get("dynamic")) {

            Timber.i("while");
        }

        Timber.i(e.valCurs);
    }

    @Subscribe
    public void showValCurses(EventValCurse valCurseEvent) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());

        if (Hawk.contains(format)){
            ValCurs valCurs = Hawk.get(format);

            Timber.i(format);

        }

        ValCurs valCurs = valCurseEvent.valCurs;

        iItems = new ArrayList<>();


//        Observable.fromIterable(valCurs.valute)
//                .filter(valute -> {
//
//                    Currency instance;
//                    try {
//                        instance = Currency.getInstance(valute.charcode);
//                    } catch (Exception e){
//
//                        return false;
//                    }
//                    return Currency.getAvailableCurrencies().contains(instance);
//                })
//                .map(valute -> {
//                    Valute newVal = valute;
//                    Currency.getInstance(valute.charcode);
//                    return newVal;
//                })
//                .map(valute -> {
//
//                    SampleItem sampleItem = new SampleItem(valute);
//
//                    String replace = valute.value.replace(",", ".");
//                    sampleItem.value = Double.parseDouble(replace);
//
//                    return sampleItem;
//                })
//                .doOnComplete(() -> adapter.set(iItems))
//                .subscribe(e -> {
//
//                    iItems.add(e);
//                });


        for (Valute valute : valCurs.valute) {

        }
        Timber.d("valCurseEvent: %s", valCurs);

    }

    @Subscribe
    public void showRealmValCurses(EventRealmValCurse valCurseEvent) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());

        if (Hawk.contains(format)){
            ValCurs valCurs = Hawk.get(format);

            Timber.i(format);

        }

        RealmValCurs valCurs = valCurseEvent.valCurs;

        iItems = new ArrayList<>();


        Observable.fromIterable(valCurs.getValutes())
                .filter(valute -> {

                    Currency instance;
                    try {
                        instance = Currency.getInstance(valute.getCharcode());
                    } catch (Exception e){

                        return false;
                    }
                    return Currency.getAvailableCurrencies().contains(instance);
                })
                .map(valute -> {

                    SampleItem sampleItem = new SampleItem(valute);

                    return sampleItem;
                })
                .doOnComplete(() -> adapter.set(iItems))
                .subscribe(e -> {

                    iItems.add(e);
                });

        Timber.d("valCurseEvent: %s", valCurs);

    }

}
