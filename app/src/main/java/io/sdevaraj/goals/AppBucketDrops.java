package io.sdevaraj.goals;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.sdevaraj.goals.adapters.Filter;

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

    /**
     * Saves the shared preference to the preferences file.
     */
    public static void save(Context context, int filterOption) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt("filter", filterOption);
        edit.apply();
    }

    /**
     * Loads the shared preference from the preferences file.
     */
    public static int load(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("filter", Filter.NONE);
    }
}
