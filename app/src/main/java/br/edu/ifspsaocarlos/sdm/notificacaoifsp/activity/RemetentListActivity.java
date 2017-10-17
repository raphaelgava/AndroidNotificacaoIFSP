package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.RemetentAdapter;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.RealmInteger;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Remetente;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import io.realm.Realm;

public class RemetentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RemetentAdapter remetentAdapter;
    private EditText edtResearch;
    private Button btnEndRemetent;
    private final Realm realm = Realm.getDefaultInstance();
    private HashSet<Remetente> array;
    private boolean loadRemetent;
    private Handler handler;
    private Notification notificationObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_remetent);

        array = new HashSet<Remetente>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        /*
          O erro: No adapter attached; skipping layout. Acontece pois não esta setando o adapter
          antes do layoutmanager.
         */
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        edtResearch = (EditText) findViewById(R.id.edtFind);
        edtResearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                remetentAdapter.getFilter().filter(arg0.toString());
            }
        });

        btnEndRemetent = (Button) findViewById(R.id.btnEndRemetent) ;
        btnEndRemetent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                //Type listType = new TypeToken<ArrayList<Offering>>() {}.getType();
                ArrayList<Remetente> end = new ArrayList<Remetente>(array);

                try {
                    Gson gson = new Gson();
                    for (int i = 0; i < end.size(); i++) {
                        String json = gson.toJson(realm.copyFromRealm(end.get(i)));
                        data.putExtra(Integer.toString(i), json);
                    }

                    setResult(RESULT_OK, data);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to show the back button on the actionbar

        loadRemetent = true;
        handler = new Handler();

        new Thread(){
            public void run() {
                while (loadRemetent) {
                    try {
                        Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                        if (FetchJSONService.isBuscandoDadosTerminou() == true){
                            handler.post(new Runnable() {
                                public void run() {
                                    createList();
                                }
                            });

                            loadRemetent = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        Intent i = getIntent();
        Gson gson = new Gson();
        Bundle bundle = i.getExtras();

        notificationObject = gson.fromJson(bundle.getString("notificacao"), Notification.class);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// back button on action bar
                // app icon in action bar clicked; goto parent activity.
                setResult(RESULT_CANCELED, null);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createList(){

        ArrayList<Remetente> list = new ArrayList(realm.where(Remetente.class).equalTo("is_active", true).findAll());
        //ArrayList<Offering> list = new ArrayList(realm.where(Offering.class).findAll());

        if (notificationObject != null){
            if (notificationObject.getSize() > 0) {
                Realm.getDefaultInstance().beginTransaction();
                for (RealmInteger valor : notificationObject.getRemetente()) {
                    if (valor != null) {
                        for (Remetente remet : list) {
                            if (valor.getPk() == remet.getPk()) {

                                remet.setChecked(true);
                                break;
                            }
                        }
                    }
                }
                Realm.getDefaultInstance().commitTransaction();
            }
        }

        remetentAdapter = new RemetentAdapter(list, array);
        recyclerView.setAdapter(remetentAdapter);
    }

}
