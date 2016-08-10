package cn.j1angvei.cnbetareader.di.module;

import android.app.Application;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import cn.j1angvei.cnbetareader.data.remote.CnbetaApi;
import cn.j1angvei.cnbetareader.data.remote.interceptor.AddHeaderInterceptor;
import cn.j1angvei.cnbetareader.data.remote.interceptor.JsonpInterceptor;
import dagger.Module;
import dagger.Provides;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wayne on 2016/6/15.
 */
@Module
public class ApplicationModule {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String BASE_URL = "http://www.cnbeta.com";
    private Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    String provideBaseUrl() {
        return BASE_URL;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat(DATE_FORMAT)
                .create();
    }

    @Provides
    @Singleton
    CookieJar provideCookieJar(Application application) {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(application));
    }

    @Provides
    @Singleton
    OkHttpClient provideOkhttpClient(CookieJar cookieJar) {
        return new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(new AddHeaderInterceptor())
                .addInterceptor(new JsonpInterceptor())
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build();
    }

    @Provides
    @Singleton
    CnbetaApi provideApi(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .build()
                .create(CnbetaApi.class);
    }

}
