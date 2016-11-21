package squaring.vitrox.rxparallel.Network;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import squaring.vitrox.rxparallel.Common.Config;
import squaring.vitrox.rxparallel.DependencyInjection.Module.ApplicationModule;

@Module(includes = ApplicationModule.class)
public class ServiceModule {

    @Provides
    @Singleton
    public GitApiService apiService(OkHttpClient client) {

        String demoUrl = Config.BASE_URL;
        //Jackson Mapper and deserializer
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return new Retrofit.Builder()
                .baseUrl(demoUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .client(client)
                .build()
                .create(GitApiService.class);
    }

    @Provides
    @Singleton
    public OkHttpClient okHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient();
        return okHttpClient;
    }
}