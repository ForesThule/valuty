package forest.les.metronomic.network.api;

import java.util.Arrays;
import java.util.List;

import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.btc.CoinDescData;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 07.12.17.
 */

public interface ApiCoinDesc {

//    https://api.coindesk.com/v1/bpi/historical/close.json

    @GET("v1/bpi/historical/close.json")
    Single<CoinDescData> getBitcoinRAtesByMonth();



}
