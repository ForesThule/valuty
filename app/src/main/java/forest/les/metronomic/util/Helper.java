package forest.les.metronomic.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import forest.les.metronomic.BuildConfig;
import forest.les.metronomic.model.Valute;
import forest.les.metronomic.network.api.BitcoinApi;
import forest.les.metronomic.network.api.CbrApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import timber.log.Timber;


/**
 * Created by root on 05.06.17.
 */

public class Helper {


    public static CbrApi getCbrApi(){


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
        OkHttpClient.Builder client = new OkHttpClient.Builder();
//
        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(interceptor);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.cbr.ru/") //Базовая часть адреса
                .addConverterFactory(SimpleXmlConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client.build())
                .build();

        return retrofit.create(CbrApi.class);
    };

    public static BitcoinApi getBtcApi(){


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//
        OkHttpClient.Builder client = new OkHttpClient.Builder();
//
        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(interceptor);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://blockchain.info/") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client.build())
                .build();

        return retrofit.create(BitcoinApi.class);
    };

    public static String getActualTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return  simpleDateFormat.format(new Date());
    }

    public static String calculate(String input, Valute inpValute, Valute outValute) {

//        Timber.v("calculate() called with: " + "input = [" + input + "], inpValute = [" + inpValute + "], outValute = [" + outValute + "]");

        String value = input;

        if (value.equals("")){
            return "";
        }

//        Timber.i(inpValute.toString());

        Double doubleValue = Double.parseDouble(value);
        BigDecimal decValue = BigDecimal.valueOf(doubleValue);

        Double inValuteDouble = Double.parseDouble(inpValute.value.replace(",", "."));
        Double inNominalDouble = Double.parseDouble(inpValute.nominal);
        BigDecimal inValuteDec = BigDecimal.valueOf(inValuteDouble);
        BigDecimal inNominalDec = BigDecimal.valueOf(inNominalDouble);
        BigDecimal multiply = decValue.multiply(inValuteDec);
        BigDecimal inRubles = multiply.divide(inNominalDec, BigDecimal.ROUND_HALF_UP);


        Double outValuteDouble = Double.parseDouble(outValute.value.replace(",", "."));
        Double outNominal = Double.parseDouble(outValute.nominal);
        BigDecimal decOutNominal = BigDecimal.valueOf(outNominal);
        BigDecimal outValuteDec = BigDecimal.valueOf(outValuteDouble);
        BigDecimal outInRubles = outValuteDec.divide(decOutNominal, BigDecimal.ROUND_HALF_UP);


        BigDecimal divide = inRubles.divide(outInRubles, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);

//        Timber.i("calculate: %s %s", inValuteDec, outValuteDec);
//
//        Timber.i("calculate: %s", divide);

        return divide.toString();
    }



}
