package forest.les.metronomic;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import io.realm.Realm;
import timber.log.Timber;

/**
 * Created by root on 05.06.17.
 */

public class ThisApp extends Application {

    public static CbrApi api;
    public Realm realm;

    @Override
    public void onCreate() {

        super.onCreate();

        realm = Realm.getDefaultInstance();

        Hawk.init(this).build();

        api = Helper.getCbrApi();

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
}
