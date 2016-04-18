package shekar.com.popularmovies.integration;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import retrofit.Call;
import shekar.com.popularmovies.model.MovieResults;
import shekar.com.popularmovies.model.ReviewResults;
import shekar.com.popularmovies.model.TrailersResults;

public class MoviesApiTestFixture extends BaseApiTestFixture {


    public MoviesApiTestFixture() {
        init();
    }

    @Test
    public void canGetMovies() {

        Call<MovieResults> call = getApiService().getMovies("popular","a8534c68c7016cb0fd26d1fe8d724ae8");

        MovieResults response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        assertResponse(response);
    }

    @Test
    public void canGetTrailers() {

        Call<TrailersResults> call = getApiService().getTrailer("244786","a8534c68c7016cb0fd26d1fe8d724ae8");

        TrailersResults response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        assertResponse(response);
    }

    @Test
    public void canGetReviews() {

        Call<ReviewResults> call = getApiService().getReviews("244786","a8534c68c7016cb0fd26d1fe8d724ae8");

        ReviewResults response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        assertResponse(response);
    }


    public void assertResponse(MovieResults response) {
        Assert.assertNotNull(response.getTotal_results());
        Assert.assertTrue(response.getResults().size()> 0);
    }

    public void assertResponse(TrailersResults response) {
        Assert.assertNotNull(response.results);
        Assert.assertTrue(response.results.size()> 0);
    }

    public void assertResponse(ReviewResults response) {
        Assert.assertNotNull(response.results);
        Assert.assertTrue(response.results.size()> 0);
    }

}
