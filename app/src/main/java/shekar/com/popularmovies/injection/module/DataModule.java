package shekar.com.popularmovies.injection.module;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import shekar.com.popularmovies.BaseApplication;
import shekar.com.popularmovies.R;
import shekar.com.popularmovies.services.ApiService;

/**
 * Created by Shekar on 7/03/16.
 */

@Module
public class DataModule {

    @Provides
    @Singleton
    Retrofit provideRestAdapter(BaseApplication context) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(httpLoggingInterceptor);
        return  new Retrofit.Builder()
                .baseUrl(context.getString(R.string.server_endpoint))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(Retrofit restAdapter) {
        return restAdapter.create(ApiService.class);
    }
}
