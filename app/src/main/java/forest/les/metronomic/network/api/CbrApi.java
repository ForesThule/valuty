package forest.les.metronomic.network.api;

import forest.les.metronomic.model.ValCurs;
import forest.les.metronomic.model.ValCursPeriod;
import forest.les.metronomic.model.Valuta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by root on 05.06.17.
 */

public interface CbrApi {

    @GET("scripts/XML_daily_eng.asp")
    Call<ValCurs> getRatesOnData(@Query("date_req") String date_req);

    @GET("/scripts/XML_valFull.asp")
    Call<Valuta> getValutesFullData();

    @GET("/scripts/XML_dynamic.asp?date_req1=01/04/2011&date_req2=27/04/2011&VAL_NM_RQ=R01235")
    Call<ValCursPeriod> getRatesOnPeriod(@Query("date_req1") String date_req1, @Query("date_req2") String date_req2, @Query("VAL_NM_RQ") String VAL_NM_RQ);




}
