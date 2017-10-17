package br.edu.ifspsaocarlos.sdm.notificacaoifsp.util;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.RealmInteger;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.internal.IOException;

/**
 * Created by rapha on 10/9/2017.
 */

public class MyGsonBuilder {
    //http://w3cgeek.com/realm-and-expected-begin_object-but-was-number-at-path-0-location-coordinates0.html

    private static MyGsonBuilder instance;


    public static MyGsonBuilder getInstance(){
        if (instance == null){
            instance = new MyGsonBuilder();
        }
        return instance;
    }

    private MyGsonBuilder(){
    }

    public Gson myGson(){
        Gson gson = new GsonBuilder()
                //.excludeFieldsWithoutExposeAnnotation()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                //para excluir um campo na hora de serializar!
//                .addSerializationExclusionStrategy(new ExclusionStrategy() {
//                    @Override
//                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getName().toLowerCase().contains("id_local");
//                    }
//
//                    @Override
//                    public boolean shouldSkipClass(Class<?> aClass) {
//                        return false;
//                    }
//                })
                .create();
            return gson;
    }


    public class JsonAdapter extends TypeAdapter<RealmList<RealmInteger>> {
        @Override
        public void write(JsonWriter out, RealmList<RealmInteger> value) throws IOException {
            // Ignore
            Log.d("TCC", "Antes: " + out.toString());

            try {
                if (value != null) {
                    out.beginArray();
                    for (RealmInteger inter : value){
                        out.value(inter.getPk());
                    }
                    out.endArray();
                }
                else{
                    out.nullValue();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            Log.d("TCC", "Depois: " + out.toString());
        }

        @Override
        public RealmList<RealmInteger> read(JsonReader in) throws IOException {
            RealmList<RealmInteger> list = new RealmList<RealmInteger>();
            try {
                in.beginArray();

                while (in.hasNext()) {
                    int valor = in.nextInt();
                    list.add(new RealmInteger(valor));
                }
                in.endArray();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
