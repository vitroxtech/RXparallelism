package squaring.vitrox.rxparallel;

import android.app.Application;
import android.support.annotation.VisibleForTesting;

import squaring.vitrox.rxparallel.DependencyInjection.Component.AppComponent;
import squaring.vitrox.rxparallel.DependencyInjection.Component.DaggerAppComponent;
import squaring.vitrox.rxparallel.DependencyInjection.Module.ApplicationModule;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @VisibleForTesting
    public void setAppComponent(AppComponent appComponent) {
        mAppComponent = appComponent;
    }
}
