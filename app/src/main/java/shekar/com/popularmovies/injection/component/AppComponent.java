package shekar.com.popularmovies.injection.component;


import javax.inject.Singleton;

import dagger.Component;
import shekar.com.popularmovies.BaseApplication;
import shekar.com.popularmovies.injection.module.AppModule;
import shekar.com.popularmovies.injection.module.DataModule;
import shekar.com.popularmovies.services.ApiService;
import shekar.com.popularmovies.ui.movielist.MoviesListActivity;
import shekar.com.popularmovies.utils.NetworkConnectionUtils;

/**
 * Created by Shekar on 7/3/15.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class
})
public interface AppComponent {
    void inject(BaseApplication baseApplication);

    void inject(MoviesListActivity moviesListActivity);

    ApiService getApiService();

    NetworkConnectionUtils getNetworkConnectionUtils();
}
