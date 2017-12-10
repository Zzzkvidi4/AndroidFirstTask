package zzzkvidi4.com.testandroidapplication1.syncronization;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Roman on 10.12.2017.
 * In package zzzkvidi4.com.testandroidapplication1.syncronization.
 */

public interface MindBlowerAPI {
    String MIND_BLOWER_SERVER_URL = "https://mind-blower.herokuapp.com";

    @POST("token/obtain/")
    Call<User> getUserToken(@Body UserInfo userInfo);

    @GET("score/{game_id}/{difficulty_id}/")
    Call<TopResults> getTopResults(@Path("game_id") int game_id, @Path("difficulty_id") int difficulty_id, @Header("Authorization") String auth);

    @POST("score/{game_id}/{difficulty_id}/")
    Call<TopResults> postGameResult(@Path("game_id") int game_id, @Path("difficulty_id") int difficulty_id, @Body GameScore score, @Header("Authorization") String auth);
}
