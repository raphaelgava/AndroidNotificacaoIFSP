package br.edu.ifspsaocarlos.sdm.notificacaoifsp.service;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Local;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Person;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Remetente;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.TipoNotificacao;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.MyGsonBuilder;
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

    public boolean saveUser(JSONObject json){
        Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        Person object = null;

        object = gson.fromJson(json.toString(), Person.class);

//        Offering teste = new Offering();
//        teste.setDescricao("ola");
//        Offering teste2 = new Offering();
//        teste.setDescricao("ola");
//
//        realm.beginTransaction();
//        realm.insertOrUpdate(teste);
//        realm.commitTransaction();
//
//        realm.beginTransaction();
//        realm.insertOrUpdate(teste2);
//        realm.commitTransaction();
//
//        ArrayList<Offering> list = new ArrayList(realm.where(Offering.class).equalTo("descricao", "ola").findAll());
//        Log.d("TCC0", list.toString());


        if (object != null){
            object.setType(MainActivity.getPeronType());

            final RealmObject finalObject = object;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);
                    //bgRealm.insertOrUpdate(finalObject);//faster than copyToRealmOrUpdate
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

    public boolean saveTipoNotificacao(JSONArray json){
        Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        TipoNotificacao object = null;

        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject obj = json.getJSONObject(i);
                object = gson.fromJson(obj.toString(), TipoNotificacao.class);

                if (object != null){
                    final RealmObject finalObject = object;
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            bgRealm.copyToRealmOrUpdate(finalObject);
                            //bgRealm.insertOrUpdate(finalObject);//faster than copyToRealmOrUpdate
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
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean saveLocal(JSONArray json){
        Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        Local object = null;

        try {
            for (int i = 0; i < json.length(); i++) {
                JSONObject obj = json.getJSONObject(i);
                object = gson.fromJson(obj.toString(), Local.class);

                if (object != null){
                    final RealmObject finalObject = object;
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            bgRealm.copyToRealmOrUpdate(finalObject);
                            //bgRealm.insertOrUpdate(finalObject);//faster than copyToRealmOrUpdate
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
                }
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean saveOffering(JSONObject json){
        //Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        Offering object = null;


        Gson gson = MyGsonBuilder.getInstance().myGson();
        object = gson.fromJson(json.toString(), Offering.class);
        object.setId_user(MainActivity.getUserId());//feito isso apenas pra trazer na busca das ofertas as ofertas relacionadas ao curso do usuario

        if (object != null){
            final RealmObject finalObject = object;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e("TCC", "Offering saved");
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

    public boolean saveNotificacao(JSONObject json){
        //Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        Notification object = null;

        Gson gson = MyGsonBuilder.getInstance().myGson();
        object = gson.fromJson(json.toString(), Notification.class);
        object.setId_user(MainActivity.getUserId());//feito isso apenas pra trazer na busca das ofertas as ofertas relacionadas ao curso do usuario

        if (object != null){
            Notification old = realm.where(Notification.class).equalTo("pk", object.getPk()).findFirst();
            if (old != null) {
                //Se algo na notificação foi alterado, então irá notificar novamente
                if (object.getDescricao().equals(old.getDescricao()) &&
                        object.getTitulo().equals(old.getTitulo()) &&
                        object.getDataHoraString().equals(old.getDataHoraString()) &&
                        object.getId_local() == old.getId_local()
                        ) {
                    object.setLastShow(old.getLastShow());
                    object.setChecked(old.isChecked());
                }
            }
            object.setDatahora(object.getDataHoraString());

            final RealmObject finalObject = object;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e("TCC", "Notification saved");
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

    public boolean saveRemetent(JSONObject json){
        //Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();
        Remetente object = null;


        Gson gson = MyGsonBuilder.getInstance().myGson();
        object = gson.fromJson(json.toString(), Remetente.class);
        //object.setId_user(MainActivity.getUserId());//feito isso apenas pra trazer na busca das ofertas as ofertas relacionadas ao curso do usuario

        if (object != null){
            final RealmObject finalObject = object;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e("TCC", "Remetent saved");
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
