package shekar.com.popularmovies.injection.module;

import dagger.Module;
import dagger.Provides;
import shekar.com.popularmovies.BaseApplication;
import shekar.com.popularmovies.utils.NetworkConnectionUtils;

/**
 * Created by Shekar on 7/3/15.
 */

@Module
public class AppModule {
    BaseApplication application;

    public AppModule(BaseApplication app) {
        application = app;
    }

    @Provides
    BaseApplication provideApplication() {
        return application;
    }

    @Provides
    NetworkConnectionUtils provideNetworkConnectionUtils(BaseApplication context) {
        return new NetworkConnectionUtils(context);
    }
}
