package forest.les.metronomic.data;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import forest.les.metronomic.ThisApp;
import forest.les.metronomic.model.Item;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.ValPeriodWrapper;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.util.Cv;
import forest.les.metronomic.util.Helper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import timber.log.Timber;

import static android.R.attr.data;

/**
 * Created by root on 10.06.17.
 */

public class Storage{





    public static void getRealmData(Context context){


    }


    public synchronized static ValPeriodWrapper getPeriodDataAsync(Context ctx) {

        String jsonString = getSpecificStorage(ctx, Cv.PREFS_VALUTA_DATA)
                .getString(Cv.REPO_VALUTA_DATA, "");

        String replace = jsonString.replace("\\", "");
        Timber.i(replace);

        ValPeriodWrapper facts = null;

        try {
            facts = new Gson().fromJson(jsonString, ValPeriodWrapper.class);
        } catch (Exception ex) {

            ex.printStackTrace();

        }

        if (facts == null) {

            facts = new ValPeriodWrapper(new HashMap<>());
        }
        return facts;
    }

    public synchronized static void savePeriodData(Context ctx, HashMap <Item, ValCursPeriod> period) {

        String jsonString = new Gson().toJson(new ValPeriodWrapper(period));

        getSpecificStorage(ctx, Cv.PREFS_VALUTA_DATA)
                .edit()
                .putString(Cv.REPO_VALUTA_DATA, jsonString)
                .apply();
    }

    public synchronized static void clearData(Context ctx) {

        getSpecificStorage(ctx, Cv.PREFS_VALUTA_DATA)
                .edit()
                .putString(Cv.REPO_VALUTA_DATA, null)
                .apply();
    }

    private static SharedPreferences getSpecificStorage(Context ctx, String prefsName) {

        ThisApp thisApp = ThisApp.get(ctx);

        String key = "VALUTY_APP";

        // TODO: 10.06.17 for multiuser
//        String key = String.format("%s%s%s", prefsName, thisApp.getCurrentUser(), thisApp.getCurrentPwd());

        return ctx.getSharedPreferences(key, Context.MODE_PRIVATE);
    }


    public static String getActualValuteData(Context ctx) {

        String jsonString = getSpecificStorage(ctx, Cv.PREFS_VALUTA_DATA)
                .getString(Cv.REPO_VALUTA_DATA, "");

        return jsonString;
    }
}
