package io.sdevaraj.goals;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Called when the application is initially loaded. Used for setting the configurations.
 */
public class AppBucketDrops extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
