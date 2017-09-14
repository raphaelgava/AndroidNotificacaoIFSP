package br.edu.ifspsaocarlos.sdm.notificacaoifsp.service;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Person;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.EnumParser;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by rapha on 9/13/2017.
 */

public class ParserJSON {
    private static ParserJSON instance;

    private ParserJSON(){

    }

    public static ParserJSON getInstance(){
        if (instance == null){
            instance = new ParserJSON();
        }
        return instance;
    }

    public boolean parserAndSave(JSONObject json, EnumParser state){
        boolean flagOK = false;
        switch (state){
            case ENUM_STUDENT:
            case ENUM_EMPLOYEE:
            case ENUM_PROFESSOR:
                //flagOK = saveUser(json, state);
                flagOK = saveUser(json);
                break;
        }
        return flagOK;
    }

    //private boolean saveUser(JSONObject json, EnumParser state){
    private boolean saveUser(JSONObject json){
        Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        //RealmObject object = null;
        Person object = null;
        /*
        switch (state){
            case ENUM_STUDENT:
                object = gson.fromJson(json.toString(), Student.class);
                break;
            case ENUM_EMPLOYEE:
                object = gson.fromJson(json.toString(), Employee.class);
                break;
            case ENUM_PROFESSOR:
                object = gson.fromJson(json.toString(), Professor.class);
                break;
        }
        */
        object = gson.fromJson(json.toString(), Person.class);


        if (object != null){
            object.setType(MainActivity.getPeronType());

            final RealmObject finalObject = object;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e("TCC", "User saved");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.e("TCC", "Erro: " + error.toString());
                }
            });
            return true;
        }
        return false;
    }

}
