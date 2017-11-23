package forest.les.metronomic.network.api;

import java.util.List;

import forest.les.metronomic.model.BitcTickerMsg;
import forest.les.metronomic.model.BitcTickerMsgWrapper;
import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.btc.Example;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 05.06.17.
 */

public interface BitcoinApi {

    @GET("ru/ticker")
    Observable<Example> getData();
}
