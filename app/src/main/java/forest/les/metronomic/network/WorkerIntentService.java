package forest.les.metronomic.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import forest.les.metronomic.ThisApp;
import forest.les.metronomic.events.EventDynamicCurse;
import forest.les.metronomic.events.EventRealmValCurse;
import forest.les.metronomic.model.BitcTickerMsg;
import forest.les.metronomic.model.BitcTickerMsgWrapper;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.Record;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.realm.RealmItem;
import forest.les.metronomic.model.realm.RealmRecord;
import forest.les.metronomic.model.realm.RealmString;
import forest.les.metronomic.model.realm.RealmValCurs;
import forest.les.metronomic.model.realm.RealmValCursPeriod;
import forest.les.metronomic.model.realm.RealmValuta;
import forest.les.metronomic.model.realm.RealmValute;
import forest.les.metronomic.network.api.BitcoinApi;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WorkerIntentService extends IntentService {


    private static final String EXTRA_PARAM3 = "extra.param.3";
    private static final String ACTION_GET_DYNAMIC = "action.get.dynamic";
    private CbrApi api;
    private Context ctx;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CURRENT_CURS = "forest.les.metronomic.action.FOO";
    private static final String ACTION_BAZ = "forest.les.metronomic.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "forest.les.metronomic.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "forest.les.metronomic.extra.PARAM2";
    private Realm realm;
    private BitcoinApi btcApi;

    public WorkerIntentService() {
        super("WorkerIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void getCurrentCurs(Context context, String param1) {
        Intent intent = new Intent(context, WorkerIntentService.class);
        intent.setAction(ACTION_CURRENT_CURS);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, WorkerIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void getDynamicCurs(Context context, String param1, String param2, String param3) {
        Intent intent = new Intent(context, WorkerIntentService.class);
        intent.setAction(ACTION_GET_DYNAMIC);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        intent.putExtra(EXTRA_PARAM3, param3);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        ctx = this;

        realm = ThisApp.get(this).realm;

        if (null == api) {
            api = Helper.getCbrApi();
        }

        if (null == btcApi) {
            btcApi = Helper.getBtcApi();

        }

        if (intent != null) {

            final String action = intent.getAction();

            switch (action) {
                case ACTION_CURRENT_CURS: {

                    final String param1 = intent.getStringExtra(EXTRA_PARAM1);

                    getValCurse(param1);

                    break;
                }
                case ACTION_BAZ: {

                    final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                    final String param2 = intent.getStringExtra(EXTRA_PARAM2);

                    break;
                }
                case ACTION_GET_DYNAMIC: {

                    final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                    final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                    final String param3 = intent.getStringExtra(EXTRA_PARAM3);

                    getPeriodCurse(param1, param2, param3);

                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void getValCurse(String param1) {


        Timber.d(param1);

        api.getRatesOnData(param1).enqueue(new Callback<ValCurs>() {
            @Override
            public void onResponse(Call<ValCurs> call, Response<ValCurs> response) {


                Timber.i("ON RESPONSE %s", response.body());

                ValCurs body = response.body();

                ValCurs valCurs = response.body();

                RealmValCurs realmValCurs = new RealmValCurs();

                String date = valCurs.date;

                realmValCurs.setDate(date);

                String name = valCurs.name;

                realmValCurs.setName(name);

                RealmList<RealmValute> valutes = new RealmList<>();


                for (Valute valute : valCurs.valute) {

                    RealmValute realmValute = new RealmValute();

                    realmValute.setName(valute.name);
                    realmValute.setNominal(valute.nominal);
                    realmValute.setValue(valute.value);
                    realmValute.setDate(param1);
                    realmValute.setCharcode(valute.charcode);


                    RealmResults<RealmValuta> all = realm.where(RealmValuta.class).findAll();

                    ArrayList<RealmValuta> realmValutas = new ArrayList<>(all);

                    for (RealmValuta realmValuta : realmValutas) {

                        for (RealmItem realmItem : realmValuta.getItems()) {


                            if (valute.charcode.equals(realmItem.getIsoCharcode())) {
                                realmValute.setParentCode(realmItem.parentCode.trim());

                            }
                        }
                    }


                    valutes.add(realmValute);
                }
                realmValCurs.setValutes(valutes);

                realm.beginTransaction();


                realm.copyToRealmOrUpdate(realmValCurs);

//                realm.delete(RealmValCurs.class);


                RealmResults<RealmValCurs> realmValcurss = realm.where(RealmValCurs.class).findAll();

                ArrayList<RealmValCurs> realmValCurses = new ArrayList<>(realmValcurss);

                for (RealmValCurs curs : realmValCurses) {

                    for (RealmValute realmValute : curs.getValutes()) {

                        Timber.i("REALM CURS DATE: %s", realmValute);

                        EventBus.getDefault().post(new EventRealmValCurse(realmValCurs));

                    }
                }

                RealmResults<RealmValuta> basicDataList = realm.where(RealmValuta.class).findAll();

                Timber.i("basicDataList %s", basicDataList.size());

                if (basicDataList.size() == 0) {

                    getBasicData();
                }

                realm.commitTransaction();

                Timber.i("onResponse");

            }

            @Override
            public void onFailure(Call<ValCurs> call, Throwable throwable) {

                Timber.d("ERROR %s", throwable.getMessage());
            }
        });


    }

    private void getBasicData() {

        api.getValutesFullData().enqueue(new Callback<Valuta>() {
            @Override
            public void onResponse(Call<Valuta> call, Response<Valuta> response) {

                realm.beginTransaction();

                Valuta valuta = response.body();

                RealmResults<RealmValuta> all = realm.where(RealmValuta.class).findAll();

                System.out.println("ALL" + all.size());

                RealmValuta realmValuta = new RealmValuta();
                realmValuta.setName(valuta.name);
                realmValuta.setID(valuta.ID);

                RealmList<RealmItem> items1 = new RealmList<>();
                for (Item item : valuta.items) {

                    Timber.i("PARENT CODE %s %s", item.engName, item.getParentCode());
                    RealmItem realmItem = new RealmItem();
                    realmItem.setName(item.name);
                    realmItem.setEngName(item.engName);
                    realmItem.setId(item.id);
                    realmItem.setISO_Num_Code(item.ISO_Num_Code);
                    realmItem.setIsoCharcode(item.isoCharcode);
                    realmItem.setNominal(item.nominal);
                    realmItem.setParentCode(item.parentCode);

                    items1.add(realmItem);
                }

                realmValuta.setItems(items1);

                realm.copyToRealmOrUpdate(realmValuta);
//                realm.delete(RealmValuta.class);
                realm.commitTransaction();

                List<Item> items = valuta.getItems();
//                for (int i = 0; i < items.size(); i++) {
//                    Item item = items.get(i);
//
//                    if (i==2){
//                        getPeriodCurse("01/01/2000", "01/02/2017", item.parentCode);
//
//                    }
//
//
//                }
            }

            @Override
            public void onFailure(Call<Valuta> call, Throwable throwable) {
                Timber.d("ERROR %s", throwable.getMessage());

            }
        });

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void getPeriodCurse(String param1, String param2, String param3) {

//        RealmResults<RealmValCurs> all = realm.where(RealmValCurs.class).findAll();
//        Timber.i("REALM_VAL_CURS COUNT: %d", all.size());


        api.getPeriodRx(param1, param2, param3.trim())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(valCursPeriod -> {

//                    Observable.fromIterable(valCursPeriod.records)
//                            .subscribe(record -> {
//
//                                handleValCursPeriod(record);
//                            });

                    RealmValCursPeriod realmValCursPeriod = new RealmValCursPeriod();

                    realmValCursPeriod.setName(valCursPeriod.name);
                    realmValCursPeriod.date1 = valCursPeriod.date1;
                    realmValCursPeriod.date2 = valCursPeriod.date2;
                    List<Record> records = valCursPeriod.records;
                    RealmList<RealmRecord> realmRecords = new RealmList<>();

                    for (Record record : records) {

                        RealmRecord realmRecord = new RealmRecord();
                        realmRecord.date = record.date;
                        realmRecord.setValue(record.value);
                        realmRecord.setID(record.getID());

                        realmRecords.add(realmRecord);

                    }

                    realmValCursPeriod.records = realmRecords;

//                    Observable.fromIterable(valCursPeriod.records)
//
//                            .map(record -> {
//
//                                RealmRecord realmRecord = new RealmRecord();
//                                realmRecord.setDate(record.date);
//                                realmRecord.value = record.value;
//                                realmRecord.nominal = record.nominal;
//                                realmRecord.setID(record.getID());
//
//                                return realmRecord;
//
//                            }).subscribe(realmValCursPeriod.records::add);


                    return realmValCursPeriod;
                })
//                .flatMap(valCursPeriod -> Observable.fromIterable(valCursPeriod.records))
                .subscribe(valCursPeriod -> {

//                    realm.beginTransaction();
//                    realm.copyToRealmOrUpdate(valCursPeriod);
//                    realm.commitTransaction();

                    Timber.i("subscribe");
                    EventBus.getDefault().post(new EventDynamicCurse(valCursPeriod));

                    Timber.i("%s - %s", valCursPeriod.getDate1(), valCursPeriod.getName());

                }, Throwable::getMessage, () -> {
                    Timber.i("onComp");
                });

        getBitcoinCurse();

    }

    private void handleValCursPeriod(Record valCursPeriod) {


        Timber.i("handleValCursPeriod = %s", valCursPeriod);

    }

    private void getBitcoinCurse( ) {
        Timber.i("GET BTC CURS");
        btcApi.getData()
                .subscribe(aDouble ->  {
                    Timber.i("BTC %s",aDouble);
                });


//        btcApi.getData().enqueue(new Callback<BitcTickerMsgWrapper>() {
//            @Override
//            public void onResponse(Call<BitcTickerMsgWrapper> call, Response<BitcTickerMsgWrapper> response) {
//                Timber.i("RESP %s",response.body());
//            }
//
//            @Override
//            public void onFailure(Call<BitcTickerMsgWrapper> call, Throwable throwable) {
//
//                Timber.i("ERR %s",throwable.getMessage());
//
//            }
//        });
    }
}
