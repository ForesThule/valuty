package forest.les.metronomic.network.api;

import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.Valuta;
import forest.les.metronomic.model.realm.RealmValCursPeriod;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 05.06.17.
 */

public interface CbrApi {

    @GET("scripts/XML_daily_eng.asp")
    Call<ValCurs> getRatesOnData(@Query("date_req") String date_req);

    @GET("scripts/XML_daily_eng.asp")
    Observable<ValCurs> getCurrentRates(@Query("date_req") String date_req);

    @GET("/scripts/XML_valFull.asp")
    Observable<Valuta> getValutesFullData();

    @GET("/scripts/XML_dynamic.asp")
    Call<ValCursPeriod> getRatesOnPeriod(@Query("date_req1") String date_req1, @Query("date_req2") String date_req2, @Query("VAL_NM_RQ") String VAL_NM_RQ);

    @GET("/scripts/XML_dynamic.asp")
    Single<ValCursPeriod> getPeriodRx(@Query("date_req1") String date_req1, @Query("date_req2") String date_req2, @Query("VAL_NM_RQ") String VAL_NM_RQ);

}
