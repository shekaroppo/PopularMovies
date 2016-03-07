package shekar.com.popularmovies.services;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import shekar.com.popularmovies.model.ResultsPage;

public interface ApiService {

    @GET("movie/{sort_by}")
    Call<ResultsPage> getMovies(@Path("sort_by") String sort_by,@Query("api_key") String api_key);

}