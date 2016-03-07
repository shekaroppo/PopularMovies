package shekar.com.popularmovies;

import android.app.Application;

import shekar.com.popularmovies.injection.component.AppComponent;
import shekar.com.popularmovies.injection.component.DaggerAppComponent;
import shekar.com.popularmovies.injection.module.AppModule;

/**
 * Created by Shekar on 3/7/16.
 */
public class BaseApplication extends Application{
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        getComponent().inject(this);
    }

    public AppComponent getComponent() {
        if (component == null) {
            component = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        }
        return component;
    }

}
