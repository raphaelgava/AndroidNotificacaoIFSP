package br.edu.ifspsaocarlos.sdm.notificacaoifsp.application;

import android.support.multidex.MultiDexApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by rapha on 6/30/2016.
 */
public class MyApplication extends MultiDexApplication {
//    String currentMessagingUser;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        // The Realm file will be located in package's "files" directory.
        //RealmConfiguration realmConfig = new RealmConfiguration.Builder().build();
        final RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("notificacao.realm")
                                                    .schemaVersion(1) //Versão atual do BD! Essa tem que ser o último oldVersion da classe RealmMigrations
                                                    .migration(new RealmMigrations()).build();
        Realm.setDefaultConfiguration(realmConfig);
        // This will automatically trigger the migration if needed
        Realm.getInstance(realmConfig);
    }

    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

/*
    public String getCurrentMessagingUser() {
        return currentMessagingUser;
    }

    public void setCurrentMessagingUser(String currentMessagingUser) {
        this.currentMessagingUser = currentMessagingUser;
    }
    */
}

