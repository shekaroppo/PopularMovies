package shekar.com.popularmovies.services;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import shekar.com.popularmovies.model.MovieResults;
import shekar.com.popularmovies.model.ReviewResults;
import shekar.com.popularmovies.model.TrailersResults;

public interface ApiService {

    @GET("movie/{sort_by}")
    Call<MovieResults> getMovies(@Path("sort_by") String sort_by, @Query("api_key") String api_key);

    @GET("movie/{id}/videos")
    Call<TrailersResults> getTrailer(@Path("id") String id, @Query("api_key") String api_key);

    @GET("movie/{id}/reviews")
    Call<ReviewResults> getReviews(@Path("id") String id, @Query("api_key") String api_key);
}