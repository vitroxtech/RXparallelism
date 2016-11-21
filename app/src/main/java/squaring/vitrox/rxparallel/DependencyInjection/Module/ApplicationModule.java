package squaring.vitrox.rxparallel.DependencyInjection.Module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import squaring.vitrox.rxparallel.App;

/**
 * Created by miguelgomez on 6/7/16.
 */
@Module
public class ApplicationModule {

    private final App mApp;

    public ApplicationModule(App app) {
        mApp = app;
    }

    @Provides
    @Singleton
    public Context appContext() {
        return mApp;
    }
}
