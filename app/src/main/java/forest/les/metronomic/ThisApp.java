package forest.les.metronomic;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;

import forest.les.metronomic.network.api.CbrApi;
import forest.les.metronomic.util.Helper;
import timber.log.Timber;

/**
 * Created by root on 05.06.17.
 */

public class ThisApp extends Application {

    private static CbrApi api;

    @Override
    public void onCreate() {

        super.onCreate();

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
