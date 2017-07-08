package omar.apps923.downloadmanager;


import android.content.Context;
import android.content.ContextWrapper;

import com.activeandroid.ActiveAndroid;

import omar.apps923.downloadmanager.helpers.Prefs;
import omar.modules923.multidownload.DownloadConfiguration;
import omar.modules923.multidownload.DownloadManager;

public class Application extends android.app.Application {

    private static Context sContext;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
         ActiveAndroid.initialize(this);

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(false)
                .build();


        initDownloader();


    }
    private void initDownloader() {
         DownloadConfiguration configuration = new DownloadConfiguration();
         configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
         DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    public static Context getContext() {
        return sContext;
    }

}