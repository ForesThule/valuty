package forest.les.metronomic.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.github.piasy.rxandroidaudio.PlayConfig;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import forest.les.metronomic.R;
import forest.les.metronomic.data.Storage;
import forest.les.metronomic.databinding.ActivityMainBinding;
import forest.les.metronomic.events.EventValCurse;
import forest.les.metronomic.model.ValPeriodWrapper;
import forest.les.metronomic.network.WorkerIntentService;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private PlayConfig playConfig;
    private  ActivityMainBinding binding;
    private List<IItem> iItems;
    private FastItemAdapter adapter;

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

        ButterKnife.bind(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        binding.navigation.setSelectedItemId(R.id.dynamic_menu_item);

        ActionBar supportActionBar = getSupportActionBar();

        supportActionBar.setTitle("VALUTY");


        RecyclerView recycler = binding.recycler;

        adapter = new FastItemAdapter();

        recycler.setAdapter(adapter);

        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.addItemDecoration(new DividerItemDecoration(this,1));


    }

    private void setDynamicRates() {

        SampleIDynamicItem sampleIDynamicItem = new SampleIDynamicItem();

        ValPeriodWrapper periodDataAsync = Storage.getPeriodDataAsync(this);

        Timber.i(String.valueOf(periodDataAsync));

    }

    private void calcuLate() {



    }

    private void initRecycler() {
        RecyclerView recycler = binding.recycler;

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

        String formatDate = String.format("%02d/%02d/%d", day, month, year);
        System.out.println(formatDate);

        WorkerIntentService.startActionFoo(this,formatDate);


    }

    public PlayConfig getPlayConfig() {
        return PlayConfig
                .res(this, R.raw.click)
                .looping(false)
                .build();

    }

    private void play(String play) {

        binding = DataBindingUtil.setContentView(this, R.layout.l2);


//
//        PlayConfig build = getPlayConfig();
////        RxAudioPlayer rxPlayer = getRxAudioPlayer();
//
//
//        int resID=getResources().getIdentifier("click", "raw", getPackageName());
//
//        MediaPlayer mediaPlayer=MediaPlayer.create(this,resID);
//
//
//        int period = 1000 / 220;
//
//        if (!mediaPlayer.isPlaying()) {
//
//            Observable.interval(period, TimeUnit.MILLISECONDS)
////                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnNext(aLong -> {
//
//                        mediaPlayer.start();
//                    })
//                    .subscribe(
//                            aLong -> {},
//                            Throwable::printStackTrace,
//                            () -> {}
//                    );
//
//        }


//        rxPlayer.play(build)
//                .subscribeOn(io)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Boolean>() {
//                    @Override
//                    public void onSubscribe(final Disposable disposable) {
//                        System.out.println("disposable = " + disposable);
//                    }
//
//                    @Override
//                    public void onNext(final Boolean aBoolean) {
//
//                        System.out.println("aBoolean = " + aBoolean);
//                        // prepared
//                    }
//
//                    @Override
//                    public void onError(final Throwable throwable) {
//
//                        System.out.println("throwable = " + throwable);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("mediaPlayer = " + mediaPlayer);
//                        // play finished
//                        // NOTE: if looping, the Observable will never finish, you need stop playing
//                        // onDestroy, otherwise, memory leak will happen!
//                    }
//                });
    }

    private void stop() {

        binding = DataBindingUtil.setContentView(this, R.layout.l1);


        //        getRxAudioPlayer().stopPlay();
    }

    @Subscribe
    public void showValCurses(EventValCurse valCurseEvent) {
//
//        ValCurs valCurs = valCurseEvent.valCurs;
//
//        iItems = new ArrayList<>();
//
//
//        Observable.fromIterable(valCurs.valute)
//                .filter(valute -> {
//
//                    return Currency.getAvailableCurrencies().contains(Currency.getInstance(valute.charcode));
//
//                })
//                .map(valute -> {
//                    Valute newVal = valute;
//                    newVal.currency = Currency.getInstance(valute.charcode);
//                    return newVal;
//                })
//                .map(valute -> {
//
//                    SampleItem sampleItem = new SampleItem();
//
//                    sampleItem.name = valute.currency.getDisplayName();
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
//
//
//        for (Valute valute : valCurs.valute) {
//
//        }
//        Timber.d("valCurseEvent: %s", valCurs);

    }

}
