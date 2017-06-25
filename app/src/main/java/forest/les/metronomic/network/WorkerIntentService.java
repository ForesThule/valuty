package forest.les.metronomic.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import forest.les.metronomic.ThisApp;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.Record;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.model.realm.RealmString;
import forest.les.metronomic.model.realm.RealmValCurs;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
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

                getValCurse(param1);

            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh");
        String format = simpleDateFormat.format(new Date());

        api.getRatesOnData(format).enqueue(new Callback<ValCurs>() {
            @Override
            public void onResponse(Call<ValCurs> call, Response<ValCurs> response) {



                ValCurs body = response.body();
                for (Valute valute : body.valute) {
                    Timber.i("vALUTE: %s", valute.toString());

                }


                realm.beginTransaction();

                ValCurs valCurs = response.body();

                RealmValCurs realmValCurs = new RealmValCurs();

                String date = valCurs.date;
                RealmString realmDate = new RealmString();
                realmDate.setString(date);
                realmValCurs.setDate(realmDate);


                String name = valCurs.name;
                RealmString realmName = new RealmString();
                realmName.setString(name);
                realmValCurs.setName(realmName);

                RealmList<RealmString> valutes = new RealmList<>();

                for (Valute valute : valCurs.valute) {
                    RealmString realmString = new RealmString();
                    realmString.setString(valute.charcode);
                    valutes.add(realmString);
                }
                realmValCurs.setValutes(valutes);

                realm.copyToRealm(realmValCurs);

                realm.commitTransaction();


                RealmResults<RealmValCurs> all = realm.where(RealmValCurs.class).findAll();

                for (RealmValCurs curs : all) {

                    Timber.i("REALM CURS: %s",curs);
                }


                api.getValutesFullData().enqueue(new Callback<Valuta>() {
                    @Override
                    public void onResponse(Call<Valuta> call, Response<Valuta> response) {

                        Timber.i("VALUTA %s", response.body());
                    }

                    @Override
                    public void onFailure(Call<Valuta> call, Throwable throwable) {
                        Timber.d("ERROR %s", throwable.getMessage());

                    }
                });

//                api.getPeriodRx("01/01/2017", "01/02/2017", )


                Timber.i("onResponse");

            }

            @Override
            public void onFailure(Call<ValCurs> call, Throwable throwable) {

                Timber.d("ERROR %s", throwable.getMessage());
            }
        });


    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void getPeriodCurse(String param1, String param2) {


    }
}
