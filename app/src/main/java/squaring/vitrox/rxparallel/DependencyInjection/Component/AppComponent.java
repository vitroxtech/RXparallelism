package squaring.vitrox.rxparallel.DependencyInjection.Component;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import squaring.vitrox.rxparallel.DependencyInjection.Module.ApplicationModule;
import squaring.vitrox.rxparallel.Network.GitApiService;
import squaring.vitrox.rxparallel.Network.ServiceModule;

@Singleton
@Component(modules = {ApplicationModule.class, ServiceModule.class})
public interface AppComponent {

    Context appContext();

    GitApiService gitapiService();

    OkHttpClient okHttpClient();

}
