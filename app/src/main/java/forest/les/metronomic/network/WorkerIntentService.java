package forest.les.metronomic.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.anupcowkur.reservoir.Reservoir;
import com.orhanobut.hawk.Hawk;
import com.squareup.moshi.Json;

import org.greenrobot.eventbus.EventBus;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import forest.les.metronomic.ThisApp;
import forest.les.metronomic.data.Storage;
import forest.les.metronomic.events.EventDynamicCurse;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.events.EventValCurse;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.ValPeriodWrapper;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
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


    private CbrApi api;
    private Context ctx;

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "forest.les.metronomic.action.FOO";
    private static final String ACTION_BAZ = "forest.les.metronomic.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "forest.les.metronomic.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "forest.les.metronomic.extra.PARAM2";
    private Realm realm;

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
    public static void startActionFoo(Context context, String param1) {
        Intent intent = new Intent(context, WorkerIntentService.class);
        intent.setAction(ACTION_FOO);
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

    @Override
    protected void onHandleIntent(Intent intent) {


        ctx = this;

        realm = ThisApp.get(this).realm;

        if (null == api) {
            api = Helper.getCbrApi();
        }

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);

                handleActionFoo(param1);

            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();

    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1) {

        Timber.d(param1);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());

        api.getValutesFullData().enqueue(new Callback<Valuta>() {
            @Override
            public void onResponse(Call<Valuta> call, Response<Valuta> response) {


                realm.beginTransaction();

                Valuta valutaOnThatHour = realm.createObject(Valuta.class);

                realm.copyToRealm(valutaOnThatHour);

                realm.commitTransaction();


                Timber.i("onResponse");
                Timber.i(String.valueOf(response.body()));

                Valuta body = response.body();

                Observable.fromIterable(body.items)
                        .map(item -> {

                            Item result = item;
                            String trim = result.parentCode.trim();
                            result.parentCode = trim;
                            return result;
                        })
                        .subscribe(item -> Timber.i(item.toString()));

                Item item = body.items.get(1);
                String parentCode = item.parentCode;




                api.getPeriodRx("01/01/2016", "01/01/2017", parentCode)
                        .subscribeOn(Schedulers.io())
                        .map(valCursPeriod -> valCursPeriod.records)
                        .flatMap(records -> Observable.fromIterable(records)
                                .map(record -> record.date)

                        )

                        .subscribe(records -> Timber.i(String.valueOf(records)), t -> Timber.e(t));





//
//                api.getRatesOnPeriod("01/01/2016", "01/01/2017", parentCode)
//                        .enqueue(new Callback<ValCursPeriod>() {
//                            @Override
//                            public void onResponse(Call<ValCursPeriod> call, Response<ValCursPeriod> response) {
//
//                                Timber.i(response.body().toString());
//
//                                HashMap<Item, ValCursPeriod> itemValCursPeriodHashMap = new HashMap<>();
//
//                                itemValCursPeriodHashMap.put(item, response.body());
//
//                                ValPeriodWrapper valPeriodWrapper = new ValPeriodWrapper(itemValCursPeriodHashMap);
//
//                                Timber.i(itemValCursPeriodHashMap.toString());
//
//                                Hawk.put("dynamic", valPeriodWrapper);
//
//                                EventBus.getDefault().post(new EventDynamicCurse("OK"));
//                            }
//
//                            @Override
//                            public void onFailure(Call<ValCursPeriod> call, Throwable throwable) {
//
//                            }
//                        });

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
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
