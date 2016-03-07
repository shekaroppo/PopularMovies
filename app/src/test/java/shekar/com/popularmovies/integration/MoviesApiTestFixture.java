package shekar.com.popularmovies.integration;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import retrofit.Call;
import shekar.com.popularmovies.model.ResultsPage;

public class MoviesApiTestFixture extends BaseApiTestFixture {


    public MoviesApiTestFixture() {
        init();
    }

    @Test
    public void canGetMovies() {

        Call<ResultsPage> call = getApiService().getMovies("popular","a8534c68c7016cb0fd26d1fe8d724ae8");

        ResultsPage response = null;
        try {
            response = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
        assertResponse(response);
    }

    public void assertResponse(ResultsPage response) {
        Assert.assertNotNull(response.getTotal_results());
        Assert.assertTrue(response.getResults().size()> 0);
    }

}
