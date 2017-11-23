package forest.les.metronomic;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;


import java.util.List;
import java.util.concurrent.TimeUnit;

import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.network.api.BitcoinApi;
import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;

import timber.log.Timber;

/**
 * Created by root on 05.06.17.
 */

public class ThisApp extends Application {

    public static CbrApi api;
    private static BitcoinApi btcApi;

    private List<ValCurs> valCurses;


    @Override
    public void onCreate() {

        super.onCreate();

        // Don't do this! This is just so cold launches take some time
//        SystemClock.sleep(TimeUnit.SECONDS.toMillis(3));

//        RealmConfiguration config = new RealmConfiguration
//                .Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build();
//
//        Realm.setDefaultConfiguration(config);

        api = Helper.getCbrApi();

        btcApi = Helper.getBtcApi();


        if (BuildConfig.DEBUG) {

            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return super.createStackElementTag(element) +
                            ":timber: line=" + element.getLineNumber() +
                            " method: " + element.getMethodName();
                }
            });

        }
    }

    public static CbrApi getApi() {
        return api;
    }

    public static ThisApp get(Context ctx) {
        return (ThisApp) ctx.getApplicationContext();
    }

    public List<ValCurs> getValCurses() {
        return valCurses;
    }
}
