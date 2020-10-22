package com.app.erladmin.injection.component;

import android.app.Application;
import android.content.SharedPreferences;

import com.app.erladmin.injection.module.AppModule;
import com.app.erladmin.injection.module.NetworkModule;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * This is primary parent component initializing AppModule and Network Module.
 * This is parrent component for dependent child component i.e. ServiceComponent.
 * Please refer signature of ServiceComponent.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface NetworkComponent {

    Retrofit provideRetrofit();
    Application provideAppContext();
    SharedPreferences provideSharedPreference();
    Gson provideGson();

}
