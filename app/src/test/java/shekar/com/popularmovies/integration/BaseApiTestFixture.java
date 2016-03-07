package shekar.com.popularmovies.integration;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import shekar.com.popularmovies.services.ApiService;

public class BaseApiTestFixture {

    private ApiService mApiService;

    protected ApiService getApiService() {
        if (mApiService == null) {
            throw new IllegalStateException("Initialize API Service using init()");
        }
        return mApiService;
    }
    public void init() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(httpLoggingInterceptor);
        mApiService = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(ApiService.class);
    }
}
