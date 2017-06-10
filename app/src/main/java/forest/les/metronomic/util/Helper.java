package forest.les.metronomic.util;

import forest.les.metronomic.BuildConfig;
import forest.les.metronomic.network.api.CbrApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


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


}
