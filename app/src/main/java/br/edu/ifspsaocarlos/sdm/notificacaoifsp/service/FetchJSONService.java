package br.edu.ifspsaocarlos.sdm.notificacaoifsp.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MapsActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.AddedOffering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Person;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.EnumParser;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.MyGsonBuilder;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.ServiceState;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Para funcionar tem que estar registrado no manifest!!
 */
public class FetchJSONService extends Service implements Runnable {
    private boolean appAberta;
    private static boolean novoComandoLiberado;

    private ServiceState machine;
    private RequestQueue queue;
    private static Stack<AddedOffering> stackOffering;
    private final Realm realm = Realm.getDefaultInstance();
    private Handler handler;
    private static Stack<Notification> stackNotification;
    private NotificationManager manager;
    private boolean flagLocalNotification;

    public FetchJSONService() {
        machine = ServiceState.getInstance();
        stackOffering = new Stack<AddedOffering>();
        stackNotification = new Stack<Notification>();
        flagLocalNotification = false;
    }

    public static void setOffering(AddedOffering offer){
        stackOffering.add(offer);
        ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_INSERT_STUDENT_OFFERING);
    }

    public static void setNotification(Notification notification){
        stackNotification.add(notification);
        ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_INSERT_NOTIFICATION);
    }

    public IBinder onBind(Intent intent) {
// TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onCreate() {
        super.onCreate();
        appAberta = true;
        novoComandoLiberado = false;
        Log.e("", "onCreate");
        queue = Volley.newRequestQueue(FetchJSONService.this);

        // Add as notification
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        handler = new Handler();
        new Thread(this).start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    private int contTime;

    public void run() {
        Log.e("", "run");
        contTime = 0;
        while (appAberta) {
            try {
                Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                handler.post(new Runnable() {
                    public void run() {
                        if (machine.hasItemEnabled() == true) {
                            ServiceState.EnumServiceState state = machine.popState();
                            switch (state) {
                                case ENUM_NOTIFICATION:
                                    buscarNotificacao();
                                    machine.pushState(ServiceState.EnumServiceState.ENUM_LOCAL);
                                    flagLocalNotification = true;
                                    break;
                                case ENUM_SHOW_NOTIFICATION:
                                    showNotification();
                                    break;
                                case ENUM_USER:
                                    buscarUsuario();
                                    break;
                                case ENUM_OFERECIMENTO:
                                    buscarOferecimento();
                                    break;
                                case ENUM_REMETENTE:
                                    buscarRemetente();
                                    break;
                                case ENUM_REMOVE_STUDENT_OFFERING:
                                    atualizarAlunoOferecimento(true);
                                    break;
                                case ENUM_INSERT_STUDENT_OFFERING:
                                    atualizarAlunoOferecimento(false);
                                    break;
                                case ENUM_INSERT_NOTIFICATION:
                                    atualizarNotificacao(false);
                                    break;
                                case ENUM_TIPO_NOTIFICACAO:
                                    buscarTipoNotificacao();
                                    break;
                                case ENUM_LOCAL:
                                    buscaLocal();
                                    if (flagLocalNotification == true) {
                                        machine.pushState(ServiceState.EnumServiceState.ENUM_SHOW_NOTIFICATION);
                                        flagLocalNotification = false;
                                    }
                                    break;
//                                default:

                            }
                            ServiceState.finishLastPop();
                            contTime = 0;
                        }
                        else{
                            if (isBuscandoDadosTerminou() == true) {
                                contTime++;
                                if (contTime == 5) {
                                    contTime = 0;
                                    machine.pushState(ServiceState.EnumServiceState.ENUM_NOTIFICATION);
                                }
                            }
                        }
                    }
                });

//                if (!primeiraBusca && novoNumeroContatos != ultimoNumeroContatos) {
//                    NotificationManager nm = (NotificationManager)
//                            getSystemService(NOTIFICATION_SERVICE);
//                    Intent intent = new Intent(this, NovoContatoActivity.class);
//                    intent.putExtra("mensagem_da_notificacao",
//                            getString(R.string.contatos_atualizados));
//                    PendingIntent p = PendingIntent.getActivity(this, 0, intent, 0);
//                    Notification.Builder builder = new Notification.Builder(this);
//                    builder.setSmallIcon(R.drawable.ic_contato);
//                    builder.setTicker(getString(R.string.novo_contato_adicionado));
//                    builder.setContentTitle(getString(R.string.novo_contato));
//                    builder.setContentText(getString(R.string.clique_aqui));
//                    builder.setWhen(System.currentTimeMillis());
//                    builder.setContentIntent(p);
//                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),
//                            R.drawable.ic_mensageiro));
//                    Notification notification = builder.build();
//                    notification.vibrate = new long[] {100, 250};
//                    nm.notify(R.mipmap.ic_launcher, notification);
//                }
//                ultimoNumeroContatos = novoNumeroContatos;
//                primeiraBusca = false;
            }
            catch (InterruptedException ie) {
                Log.e("TCC", "Erro na thread de recuperação de contato");
            }
        }
    }

    private void atualizarAlunoOferecimento(boolean flagRemove) {
        if (stackOffering.isEmpty() == false){
            final AddedOffering offer = stackOffering.pop();

            boolean flagOk = true;

            String url = getString(R.string.url_base);

            novoComandoLiberado = false;
            switch (MainActivity.getPeronType()){
                case ENUM_STUDENT:
                    url += getString(R.string.url_oferecimento); break;
                default:
                    Log.e("TCC", "Tipo pessoa desconhecida");
                    flagOk = false;
            }
            if (flagOk == true) {
                url += Integer.toString(offer.getPk()) + "/"; //PUT to update is necessary this last slash!!!

                try {
                    Gson gson = new Gson();
//                    Gson gson = MyGsonBuilder.getInstance().myGson();

                    //Person managedModel = realm.copyFromRealm(person);
//                    FATAL EXCEPTION: Thread-276  - PROBLEMA TRANSAÇÃO DO REALM
//                    Process: br.edu.ifspsaocarlos.sdm.notificacaoifsp, PID: 31614
//                    java.lang.IllegalArgumentException: Only valid managed objects can be copied from Realm.
//
//                    FATAL EXCEPTION: main - PROBLEMA DE REFERENCIA CIRCULAR (ADAPTER DO GSON)
//                    Process: br.edu.ifspsaocarlos.sdm.notificacaoifsp, PID: 15165
//                    java.lang.StackOverflowError
//                    at java.lang.String._getChars(String.java:908)
//
//                    FATAL EXCEPTION: main - PROBLEMA DA STRING DO JSON
//                    Process: br.edu.ifspsaocarlos.sdm.notificacaoifsp, PID: 16561
//                    java.lang.IllegalStateException: Nesting problem.
//                    at com.google.gson.stream.JsonWriter.beforeName(JsonWriter.java:616)

                    //realm.beginTransaction();
                    final Offering bla = realm.where(Offering.class).equalTo("pk", offer.getPk()).findFirst();

                    String json = gson.toJson(realm.copyFromRealm(bla));
                    JSONObject user = new JSONObject(json);

                    if (flagRemove){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    AddedOffering deletedObj = new AddedOffering(bla);
                                    AddedOffering other = realm.copyToRealmOrUpdate(deletedObj);
                                    other.deleteFromRealm();
                                }catch (Exception e){
                                    Log.d("TCC", "ERROR: " + e.toString());
                                }
                            }
                        });
                        MainActivity.setFragTransactionStack(R.id.nav_class_schedule, R.id.content_frame, null, true);
                    }

                    try {
                        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                                new Response.Listener<JSONObject>() {
                                    public void onResponse(JSONObject s) {
                                        //realm.commitTransaction();
                                        Log.e("TCC", "Updated offer." + s.toString());
                                    }
                                }, new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError volleyError) {
                                stopTransaction();
                                Log.e("TCC", "Error during update offer." + volleyError.toString());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                // Basic Authentication
                                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                                headers.put("Content-Type", "application/json");
                                headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                                return headers;
                            }
                        };

                        queue.add(jsonObjectRequest);
                    } catch (Exception e) {
                        stopTransaction();
                        Log.e("TCC", "Erro na leitura de mensagens");
                    }
                } catch (JSONException e) {
                    stopTransaction();
                    Log.e("TCC", "ERROR: " + e.toString());
                }
            }
        }
    }

    private void atualizarNotificacao(boolean flagRemove) {
        if (stackNotification.isEmpty() == false){
            final Notification objeto = stackNotification.pop();

            boolean flagOk = true;

            String url = getString(R.string.url_base);

            novoComandoLiberado = false;
            switch (MainActivity.getPeronType()){
                case ENUM_EMPLOYEE:
                case ENUM_PROFESSOR:
                    url += getString(R.string.url_notificacao); break;
                default:
                    Log.e("TCC", "Tipo pessoa desconhecida");
                    flagOk = false;
            }
            if (flagOk == true) {
                //url += Integer.toString(objeto.getPk()) + "/"; //PUT to update is necessary this last slash!!!

                try {
                    //Gson gson = new Gson();
                    Gson gson = MyGsonBuilder.getInstance().myGson();

                    final Notification bla = realm.where(Notification.class).equalTo("pk", objeto.getPk()).findFirst();

                    String json = gson.toJson(realm.copyFromRealm(bla));
                    JSONObject user = new JSONObject(json);
                    Log.d("TCC", "JSON notification: " + user.toString());

                    if (flagRemove){
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                try {
                                    Notification other = realm.copyToRealmOrUpdate(bla);
                                    other.deleteFromRealm();
                                }catch (Exception e){
                                    Log.d("TCC", "ERROR: " + e.toString());
                                }
                            }
                        });
                        //MainActivity.setFragTransactionStack(R.id.nav_class_schedule, R.id.content_frame, null, true);
                    }

                    try {
                        RequestQueue queue = Volley.newRequestQueue(this.getApplicationContext());
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                                new Response.Listener<JSONObject>() {
                                    public void onResponse(JSONObject s) {
                                        //realm.commitTransaction();
                                        novoComandoLiberado = true; //para não ficar travado caso de algum erro
                                        Log.e("TCC", "Updated notification." + s.toString());
                                    }
                                }, new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError volleyError) {
                                stopTransaction();
                                novoComandoLiberado = true; //para não ficar travado caso de algum erro
                                Log.e("TCC", "Error during update notification." + volleyError.toString());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> headers = new HashMap<>();
                                // Basic Authentication
                                //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                                headers.put("Content-Type", "application/json");
                                headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                                return headers;
                            }
                        };

                        queue.add(jsonObjectRequest);
                    } catch (Exception e) {
                        stopTransaction();
                        Log.e("TCC", "Erro na leitura de mensagens");
                        novoComandoLiberado = true; //para não ficar travado caso de algum erro
                    }
                } catch (JSONException e) {
                    stopTransaction();
                    Log.e("TCC", "ERROR: " + e.toString());
                    novoComandoLiberado = true; //para não ficar travado caso de algum erro
                }
            }
        }
    }

    private void stopTransaction() {
        if(realm.isInTransaction()) {
            realm.cancelTransaction();
        }
    }

    private void buscarTipoNotificacao() {

        String url = getString(R.string.url_base) + getString(R.string.url_tipo_notificacao);

        try {

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray s) {
                            ParserJSON.getInstance().saveTipoNotificacao(s);
                            //Toast.makeText(FetchJSONService.this, "Finalmente: " + s.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("TCC", "TipoNotificacao: " + s.toString());
                            novoComandoLiberado = true; //para não ficar travado caso de algum erro
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(FetchJSONService.this, "Erro na recuperação do TipoNotificacao: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                            novoComandoLiberado = true; //para não ficar travado caso de algum erro
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                    return headers;
                }
            };
            jsonObjectRequest.setTag("MyTAG");

            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            novoComandoLiberado = true; //para não ficar travado caso de algum erro
        }
    }

    private void buscaLocal() {

        String url = getString(R.string.url_base) + getString(R.string.url_local);

        try {

            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray s) {
                            ParserJSON.getInstance().saveLocal(s);
                            //Toast.makeText(FetchJSONService.this, "Finalmente: " + s.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("TCC", "Local: " + s.toString());
                            novoComandoLiberado = true; //para não ficar travado caso de algum erro
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(FetchJSONService.this, "Erro na recuperação do Local: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                            novoComandoLiberado = true; //para não ficar travado caso de algum erro
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                    return headers;
                }
            };
            jsonObjectRequest.setTag("MyTAG");

            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            novoComandoLiberado = true; //para não ficar travado caso de algum erro
        }
    }

    private void buscarUsuario() {

        String url = getString(R.string.url_base);

        boolean flagOk = true;

        EnumParser parser = null;
        switch (MainActivity.getPeronType()){
            case ENUM_STUDENT: url += getString(R.string.url_student); parser = EnumParser.ENUM_STUDENT; break;
            case ENUM_EMPLOYEE: url += getString(R.string.url_employee); parser = EnumParser.ENUM_EMPLOYEE; break;
            case ENUM_PROFESSOR: url += getString(R.string.url_professor); parser = EnumParser.ENUM_PROFESSOR; break;
            default:
                Log.e("TCC", "Tipo pessoa desconhecida");
                flagOk = false;
        }

        if (flagOk == true) {
            novoComandoLiberado = false;
            url += Integer.toString(MainActivity.getUserId());
            try {

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject s) {
                                ParserJSON.getInstance().saveUser(s);
                                //Toast.makeText(FetchJSONService.this, "Finalmente: " + s.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("TCC", "User: " + s.toString());
                                novoComandoLiberado = true;
                                
                                /* // TODO: 9/12/2017 caso queira pegar um array de dentro do json!!! 
                                JSONArray jsonArray;
                                try {
                                    jsonArray = s.getJSONArray("contatos");
                                    novoNumeroContatos = s.length();
                                    Toast.makeText(FetchJSONService.this, "Finalmente: " + s.toString(), Toast.LENGTH_SHORT).show();
                                } catch (JSONException je) {
                                    Toast.makeText(FetchJSONService.this, "Erro na conversão " +
                                            "de objeto JSON!", Toast.LENGTH_SHORT).show();
                                }
                                */
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(FetchJSONService.this, "Erro na recuperação do usuário: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                                novoComandoLiberado = true;
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        // Basic Authentication
                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                        headers.put("Content-Type", "application/json");
                        headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                        return headers;
                    }
                };
                
                
                /* 
                // TODO: 9/12/2017 SE FOR UTILIZAR ARRAY TEM QUE UTILIZAR ESSA FORMA. CASO CONTÁRIO OCORRERÁ UMA EXCEÇÃO: com.android.volley.ParseError: org.json.JSONException

                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray s) {
                                //JSONArray jsonArray;
                                //jsonArray = s.getJSONArray("contatos");
                                novoNumeroContatos = s.length();
                                Toast.makeText(FetchJSONService.this, "Finalmente: " + s.toString(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(FetchJSONService.this, "Erro na recuperação do usuário: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        // Basic Authentication
                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                        headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                        return headers;
                    }
                };
                */
                jsonObjectRequest.setTag("MyTAG");

                queue.add(jsonObjectRequest);
            } catch (Exception e) {
                e.printStackTrace();
                novoComandoLiberado = true; //para não ficar travado caso de algum erro
            }
        }
    }

    // TODO: 10/19/2017 Buscar oferecimento pois se o professor retirar o aluno/inserir aluno não será atualizado na grade
    // TODO: 10/19/2017 Buscar oferecimento para exibir a grade do professor!
    private void buscarOferecimento() {
        boolean flagOk = true;

        String url = getString(R.string.url_base);

        Person p = null;
        switch (MainActivity.getPeronType()){
            case ENUM_STUDENT:
                Realm realm = Realm.getDefaultInstance();
                p = realm.where(Person.class).equalTo("pk", MainActivity.getUserId()).findFirst();
            case ENUM_PROFESSOR:
                url += getString(R.string.url_oferecimento); break;
            default:
                Log.e("TCC", "Tipo pessoa desconhecida");
                flagOk = false;
        }

        if (flagOk == true) {
            novoComandoLiberado = false;
            //url += Integer.toString(MainActivity.getUserId());
            try {
                HashMap<String, String> params = new HashMap<String, String>();
                Calendar cd = Calendar.getInstance();
                int year = cd.get(Calendar.YEAR);
                int month = cd.get(Calendar.MONTH);
                int semester;
                if (month <= 6){
                    semester = 1;
                }
                else{
                    semester = 2;
                }

                if (p != null)
                    url += "?ano=" + year + "&semestre=" + semester + "&turma=" + p.getPkTurma() + "&pk=" + p.getPk();
                else
                    url += "?ano=" + year + "&semestre=" + semester;

                StringRequest sr = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            byte[] u = s.toString().getBytes("ISO-8859-1");
                            s = new String(u, "UTF-8");

                            JSONArray jsonArray = new JSONArray(s);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                ParserJSON.getInstance().saveOffering(jsonObj);
                            }
                            //Toast.makeText(FetchJSONService.this, "Finalmente: " + s, Toast.LENGTH_SHORT).show();
                            Log.d("TCC", "Response Offering");
                        } catch (JSONException e) {
                            Toast.makeText(FetchJSONService.this, "Erro na conversão " +
                                    "de objeto JSON!", Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        novoComandoLiberado = true; //para não ficar travado caso de algum erro
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        novoComandoLiberado = true; // para não deixar a página sem carregar nada caso de erro
                        Toast.makeText(FetchJSONService.this, "Erro na recuperação da offerta: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
//                    //Só funciona para POST
//                    @Override
//                    protected Map<String,String> getParams(){
//                        // Posting parameters to login url
//                        Map<String, String> params = new HashMap<String, String>();
//
//                        params.put("ano", Integer.toString(year));
//                        params.put("semestre", Integer.toString(semester));
//                        return params;
//                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        // Basic Authentication
                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                        headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                        return headers;
                    }
                };

                queue.add(sr);
            } catch (Exception e) {
                e.printStackTrace();
                novoComandoLiberado = true; //para não ficar travado caso de algum erro
            }
        }
    }

    private void buscarRemetente() {
        boolean flagOk = true;

        String url = getString(R.string.url_base);

        novoComandoLiberado = false;

        if (flagOk == true) {
            url += getString(R.string.url_remetente);
            try {
                StringRequest sr = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            byte[] u = s.toString().getBytes("ISO-8859-1");
                            s = new String(u, "UTF-8");

                            JSONArray jsonArray = new JSONArray(s);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                ParserJSON.getInstance().saveRemetent(jsonObj);
                            }
                            //Toast.makeText(FetchJSONService.this, "Finalmente: " + s, Toast.LENGTH_SHORT).show();
                            Log.d("TCC", "Response Remetent");
                        } catch (JSONException e) {
                            Toast.makeText(FetchJSONService.this, "Erro na conversão " +
                                    "de objeto JSON!", Toast.LENGTH_SHORT).show();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        novoComandoLiberado = true; //para não ficar travado caso de algum erro
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        novoComandoLiberado = true; // para não deixar a página sem carregar nada caso de erro
                        Toast.makeText(FetchJSONService.this, "Erro na recuperação do remetente: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
//                    //Só funciona para POST
//                    @Override
//                    protected Map<String,String> getParams(){
//                        // Posting parameters to login url
//                        Map<String, String> params = new HashMap<String, String>();
//
//                        params.put("ano", Integer.toString(year));
//                        params.put("semestre", Integer.toString(semester));
//                        return params;
//                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        // Basic Authentication
                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                        headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                        return headers;
                    }
                };

                queue.add(sr);
            } catch (Exception e) {
                e.printStackTrace();
                novoComandoLiberado = true; //para não ficar travado caso de algum erro
            }
        }
    }


    private void buscarNotificacao() {
        RequestQueue queue = Volley.newRequestQueue(FetchJSONService.this);
        String url = getString(R.string.url_base) + getString(R.string.url_notificacao);
        try {
            novoComandoLiberado = false;

            url += "?pk=" + MainActivity.getUserId() + "&user=" + MainActivity.getPeronType().ordinal();

            StringRequest sr = new StringRequest(Request.Method.GET, url , new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        byte[] u = s.toString().getBytes("ISO-8859-1");
                        s = new String(u, "UTF-8");

                        JSONArray jsonArray = new JSONArray(s);
                        for(int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            ParserJSON.getInstance().saveNotificacao(jsonObj);
                        }
                        //Toast.makeText(FetchJSONService.this, "Finalmente: " + s, Toast.LENGTH_SHORT).show();
                        Log.d("TCC", "Response Notification");
                    } catch (JSONException e) {
                        Toast.makeText(FetchJSONService.this, "Erro na conversão " +
                                "de objeto JSON!", Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    novoComandoLiberado = true; //para não ficar travado caso de algum erro
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    novoComandoLiberado = true; // para não deixar a página sem carregar nada caso de erro
                    Toast.makeText(FetchJSONService.this, "Erro na recuperação da offerta: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    // Basic Authentication
                    //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);

                    headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                    return headers;
                }
            };

            queue.add(sr);
        } catch (Exception e) {
            e.printStackTrace();
            novoComandoLiberado = true; //para não ficar travado caso de algum erro
        }
    }


    //private void showNotification(List<Notification> messageList) {
    private void showNotification() {
//        Setting date in gson
//        Gson gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd")
//                .create();
//        String dateMask = getString(R.string.mask_date);
//        SimpleDateFormat formatDate = new SimpleDateFormat(dateMask);
        Calendar c = Calendar.getInstance();

        RealmResults<Notification> stepEntryResults = realm.where(Notification.class).equalTo("id_user", MainActivity.getUserId()).findAll();
        List<Notification> messageList = realm.copyFromRealm(stepEntryResults);

//        StatusBarNotification status[] = new StatusBarNotification[0];
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            status = manager.getActiveNotifications();
//        }
//        boolean flagNotificar;

        for (Notification noti : messageList) {
//            flagNotificar = true;
//            for (int i = 0; i < status.length; i++ ){
//                if (status[i].getId() == noti.getPk()){
//                    flagNotificar = false;
//                    break;
//                }
//            }
//            if (flagNotificar == true)
            {
                //            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                //                    .setSmallIcon(R.drawable.ic_email_black)
                //                    //.setWhen(System.currentTimeMillis())
                //                    //.setAutoCancel(true)
                //                    .setContentTitle("New notification")
                //                    .setContentText("TESTING NOTIFICATION");
                long[] v = {500,1000}; //vibrate
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);//sound
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(R.drawable.ic_email_black)
                                .setAutoCancel(true)
                                .setVibrate(v)
                                .setSound(uri)
                                .setContentTitle(noti.getTitulo())
                                .setContentText(noti.getDescricao());

                //            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                //            //resultIntent.putExtra("", messageList.get(0).getPk());
                //
                //            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                //            stackBuilder.addParentStack(MainActivity.class);
                //            //stackBuilder.addParentStack(MessageActivity.class);
                //            stackBuilder.addNextIntent(resultIntent);
                //
                //            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                //            mBuilder.setContentIntent(resultPendingIntent);
                //
                //
                //            mNotificationManager.notify(id, mBuilder.build());

                Intent notificationIntent = new Intent(this, MapsActivity.class);

                Gson gson = new Gson();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                Notification object = realm.copyToRealmOrUpdate(noti);
                String json = gson.toJson(realm.copyFromRealm(object));
                notificationIntent.putExtra("notificacao", json);
                Realm.getDefaultInstance().cancelTransaction();

                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);

                //            // Add as notification
                //            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //manager.notify(0, builder.build());// 0  para setar tudo na mesma notificação
                manager.notify(noti.getPk(), builder.build());
            }
        }


        /*
        // check first use
        //if (!isFirstUse)
        {
            Integer id = messageList.get(0).getPk();
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancel(id);

            Realm realm = Realm.getDefaultInstance();

            //if (contact != null)
            {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_email_black)
                        //.setWhen(System.currentTimeMillis())
                        //.setAutoCancel(true)
                        .setContentTitle("New notification")
                        .setContentText("TESTING NOTIFICATION");

                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                //resultIntent.putExtra("", messageList.get(0).getPk());

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(MainActivity.class);
                //stackBuilder.addParentStack(MessageActivity.class);
                stackBuilder.addNextIntent(resultIntent);

                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);


                mNotificationManager.notify(id, mBuilder.build());
            }
        }
        */
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("", "onDestroy");

        queue.cancelAll("MyTAG");

        appAberta = false;
        stopSelf();
    }

    public static boolean isBuscandoDadosTerminou() {
        return novoComandoLiberado;
    }
}



//
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.app.TaskStackBuilder;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.R;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.activity.MessageActivity;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.application.MyApplication;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.model.Contact;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.model.ContactMessage;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.model.Message;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.util.BigMessage;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.util.Constants;
//import br.edu.ifspsaocarlos.sdm.mensageirosdm.util.Helpers;
//import io.realm.Realm;
//import io.realm.RealmQuery;
//import io.realm.RealmResults;
//
//public class FetchJSONService extends Service {
//    private MyAsyncTask task;
//    private ServiceState machine;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        task = new MyAsyncTask();
//        task.execute();
//
//        machine = ServiceState.getInstance();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        return Service.START_NOT_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//
//        task.cancel(true);
//        super.onDestroy();
//    }
//
//    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
//        private Context context;
//        private RequestQueue requestQueue;
//        private int requestSize;
//
//        public MyAsyncTask() {
//            super();
//            this.context = getApplication();
//
////            userId = Helpers.getUserId(context);
////            isFirstUse = Helpers.isFirstUse(context);
//
//            requestQueue = Volley.newRequestQueue(context);
//            requestSize = 0;
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            if (machine.hasItemEnabled() == true){
//                    ServiceState.EnumServiceState state = machine.popState();
//                    switch (state){
//                        case ENUM_NOTIFICATION:
//                            //buscarNotificacao();
//                            break;
//                        case ENUM_USER:
//                            buscarUsuario();
//                            break;
//                        case ENUM_OFERECIMENTO:
//                            break;
//                    }
//                }
//
//
//
//            // loop para esperar todas as requests finalizarem antes de começar o próximo burst
//            while (!isCancelled() && (requestSize != 0)) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            requestQueue.cancelAll("TAGS");
//
//            return null;
//        }
//
//        private void buscarUsuario() {
//            RequestQueue queue = Volley.newRequestQueue(FetchJSONService.this);
//            String url = getString(R.string.url_base);
//
//            UserLogin user = MainActivity.getUser();
//
//            boolean flagOk = true;
//            switch (user.getPersonType()){
//                case ENUM_STUDENT: url += getString(R.string.url_student); break;
//                case ENUM_EMPLOYEE: url += getString(R.string.url_employee); break;
//                case ENUM_PROFESSOR: url += getString(R.string.url_professor); break;
//                default:
//                    Log.e("TCC", "Tipo pessoa desconhecida");
//                    flagOk = false;
//            }
//
//
//            if (flagOk == true) {
//                try {
//                    HashMap<String, String> params = new HashMap<String, String>();
//                    params.put(getString(R.string.url_login_username), Integer.toString(user.getId()));
//                    JSONObject object = new JSONObject(params);
//
//                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object,
//                            new Response.Listener<JSONObject>() {
//                                public void onResponse(JSONObject s) {
//                                    JSONArray jsonArray;
//                                    try {
//                                        jsonArray = s.getJSONArray("contatos");
//                                        int novoNumeroContatos = jsonArray.length();
//                                    } catch (JSONException je) {
//                                        Toast.makeText(FetchJSONService.this, "Erro na conversão " +
//                                                "de objeto JSON!", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }, new Response.ErrorListener() {
//                        public void onErrorResponse(VolleyError volleyError) {
//                            Toast.makeText(FetchJSONService.this, "Erro na recuperação do número " +
//                                    "de contatos!", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    queue.add(jsonObjectRequest);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Void s) {
//            task = new MyAsyncTask();
//            task.execute();
//        }
//    }
//}


//public class FetchJSONService extends Service {
//    private MyAsyncTask task;
////    private MyApplication myApplication;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
////        myApplication = ((MyApplication) getApplication());
//        task = new MyAsyncTask();
//        task.execute();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        return Service.START_NOT_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//
//        task.cancel(true);
//        super.onDestroy();
//    }
//
//    class MyAsyncTask extends AsyncTask<Void, Void, Void> {
//        private String userId;
//        private boolean isFirstUse;
//
//        private Context context;
//        private RequestQueue requestQueue;
//        private int requestSize;
//
//        public MyAsyncTask() {
//            super();
//            this.context = getApplication();
//
////            userId = Helpers.getUserId(context);
////            isFirstUse = Helpers.isFirstUse(context);
//
//            requestQueue = Volley.newRequestQueue(context);
//            requestSize = 0;
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            Realm realm = Realm.getDefaultInstance();
//            RealmQuery<Contact> queryContacts = realm.where(Contact.class);
//            RealmResults<Contact> resultContacts = queryContacts.findAll();
//            requestSize = resultContacts.size() * 2;
//
//
//            // loop de requisição de mensagens de cada contato
//            for (Contact contact : resultContacts.subList(0, resultContacts.size())) {
//                try {
//                    ContactMessage conMessage = realm.where(ContactMessage.class).equalTo("id", contact.getId()).findFirst();
//                    if (conMessage != null) {
//                        fetchMessages(conMessage.getLastFromContact(), conMessage.getId(), userId);
//                        fetchMessages(conMessage.getLastToContact(), userId, conMessage.getId());
//                    } else {
//                        fetchMessages("0", contact.getId(), userId);
//                        fetchMessages("0", userId, contact.getId());
//                    }
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            realm.close();
//
//            // loop para esperar todas as requests finalizarem antes de começar o próximo burst
//            while (!isCancelled() && (requestSize != 0)) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            requestQueue.cancelAll(Constants.REQUEST_TAG);
//            Helpers.updateFirstUse(context);
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void s) {
//            task = new MyAsyncTask();
//            task.execute();
//        }
//
//        private void fetchMessages(String idMessage, String idContact, String idUser) {
//            // verifica se é null pois pode não estar no realm ainda!
//            if (idMessage == null) {
//                idMessage = "0";
//            }
//            // incrementa o id da última mensagem para que exiba somente mensagens novas
//            Integer idMessageRequest = Integer.parseInt(idMessage);
//            idMessageRequest++;
//
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append(Constants.SERVER_URL);
//            stringBuilder.append(Constants.MENSAGEM_PATH);
//            stringBuilder.append("/" + idMessageRequest + "/");
//            stringBuilder.append(idContact);
//            stringBuilder.append("/");
//            stringBuilder.append(idUser);
//
//            JsonObjectRequest request = new JsonObjectRequest
//                    (Request.Method.GET, stringBuilder.toString(), null, new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject json) {
//                            requestSize--;
//                            parseMessageList(json);
//                        }
//                    }, new Response.ErrorListener() {
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            requestSize--;
//                        }
//                    });
//
//            request.setTag(Constants.REQUEST_TAG);
//            requestQueue.add(request);
//        }
//
//        private void parseMessageList(JSONObject jsonRoot) {
//            List<Message> messageList = new ArrayList<>();
//
//            try {
//                JSONArray jsonArray = jsonRoot.getJSONArray("mensagens");
//                Gson gson = new Gson();
//
//                BigMessage big = new BigMessage();
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    Message message = gson.fromJson(jsonArray.getJSONObject(i).toString(), Message.class);
//                    switch (big.bigMessageValidation(message)) {
//                        case BigMessage.BIG_MESSAGE_ENDED:
//                            messageList.add(big.getBigMessage());
//                            break;
//
//                        case BigMessage.BIG_MESSAGE_NOT_DETECTED:
//                            messageList.add(message);
//                            break;
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            saveMessages(messageList);
//        }
//
//        private void saveMessages(final List<Message> messageList) {
//            if (messageList != null) {
//                if (messageList.size() > 0) {
//
//                    // Usado para atualizar indice
//                    final Message mensagem = messageList.get(messageList.size() - 1);
//                    Realm realm = Realm.getDefaultInstance();
//                    realm.executeTransactionAsync(new Realm.Transaction() {
//                        @Override
//                        public void execute(Realm bgRealm) {
//                            bgRealm.copyToRealmOrUpdate(messageList);
//                        }
//                    }, new Realm.Transaction.OnSuccess() {
//                        @Override
//                        public void onSuccess() {
//                            updateMessagesIndex(mensagem);
//
//                            if (!userId.equals(messageList.get(0).getOrigem_id()))
//                                showNotification(messageList);
//                        }
//                    }, new Realm.Transaction.OnError() {
//                        @Override
//                        public void onError(Throwable error) {
//                        }
//                    });
//                }
//            }
//        }
//
//        private void updateMessagesIndex(Message mensagem) {
//            ContactMessage conMenssage;
//            final ContactMessage conMenssageNew;
//
//            Realm realm = Realm.getDefaultInstance();
//            if (!mensagem.getOrigem_id().equals(userId)) {
//                conMenssage = realm.where(ContactMessage.class).equalTo("id", mensagem.getOrigem_id()).findFirst();
//            } else {
//                conMenssage = realm.where(ContactMessage.class).equalTo("id", mensagem.getDestino_id()).findFirst();
//            }
//
//            conMenssageNew = new ContactMessage();
//            if (conMenssage != null) {
//                conMenssageNew.setId(conMenssage.getId());
//                conMenssageNew.setLastToContact(conMenssage.getLastToContact());
//                conMenssageNew.setLastFromContact(conMenssage.getLastFromContact());
//            }
//
//            if (!mensagem.getOrigem_id().equals(userId)) {
//                conMenssageNew.setId(mensagem.getOrigem_id());
//                conMenssageNew.setLastFromContact(mensagem.getId());
//            } else {
//                conMenssageNew.setId(mensagem.getDestino_id());
//                conMenssageNew.setLastToContact(mensagem.getId());
//            }
//
//            realm.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm bgRealm) {
//                    bgRealm.copyToRealmOrUpdate(conMenssageNew);
//                }
//            }, new Realm.Transaction.OnSuccess() {
//                @Override
//                public void onSuccess() {
//                }
//            }, new Realm.Transaction.OnError() {
//                @Override
//                public void onError(Throwable error) {
//                }
//            });
//        }
//
//        private void showNotification(List<Message> messageList) {
//            String currentMessagingUser = myApplication.getCurrentMessagingUser();
//
//            // check first use
//            if (!isFirstUse) {
//
//                // verifica se o usuario esta conversando com o usuario da notificação
//                if (!messageList.get(0).getOrigem_id().equals(currentMessagingUser)) {
//
//                    Integer id = Integer.parseInt(messageList.get(0).getOrigem_id());
//                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    mNotificationManager.cancel(id);
//
//                    Realm realm = Realm.getDefaultInstance();
//                    Contact contact = realm.where(Contact.class).equalTo("id", messageList.get(0).getOrigem_id()).findFirst();
//
//                    if (contact != null) {
//                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
//                                .setSmallIcon(R.drawable.ic_send_white_24dp)
//                                .setWhen(System.currentTimeMillis())
//                                .setAutoCancel(true)
//                                .setContentTitle("Nova mensagem")
//                                .setContentText(contact.getNome_completo());
//
//                        Intent resultIntent = new Intent(getApplicationContext(), MessageActivity.class);
//                        resultIntent.putExtra(Constants.SENDER_USER_KEY, messageList.get(0).getOrigem_id());
//
//                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//                        stackBuilder.addParentStack(MessageActivity.class);
//                        stackBuilder.addNextIntent(resultIntent);
//
//                        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//                        mBuilder.setContentIntent(resultPendingIntent);
//
//
//                        mNotificationManager.notify(id, mBuilder.build());
//                    }
//                }
//            }
//        }
//    }
//}