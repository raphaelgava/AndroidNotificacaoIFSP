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
import java.util.Calendar;
import java.util.HashSet;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.OfferingAdapter;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import io.realm.Realm;

/**
 * Created by rapha on 9/25/2017.
 */

public class OfferingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OfferingAdapter offeringAdapter;
    private EditText edtResearch;
    private Button btnEndOffering;
    private boolean loadOffering;
    private final Realm realm = Realm.getDefaultInstance();
    private Handler handler;
    private HashSet<Offering> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

        array = new HashSet<Offering>();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListOffering);
        /*
          O erro: No adapter attached; skipping layout. Acontece pois não esta setando o adapter
          antes do layoutmanager.
         */
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        edtResearch = (EditText) findViewById(R.id.edtFindOffering);
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
                offeringAdapter.getFilter().filter(arg0.toString());
            }
        });

        btnEndOffering = (Button) findViewById(R.id.btnEndOffer) ;
        btnEndOffering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                //Type listType = new TypeToken<ArrayList<Offering>>() {}.getType();
                ArrayList<Offering> end = new ArrayList<Offering>(array);

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

        loadOffering = true;
        handler = new Handler();

        new Thread(){
            public void run() {
                while (loadOffering) {
                    try {
                        Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                        if (FetchJSONService.isBuscandoDadosTerminou() == true){
                            handler.post(new Runnable() {
                                public void run() {
                                createList();
                                }
                            });

                            loadOffering = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    /*
        @Override
        protected void onPostCreate(Bundle savedInstanceState) {
            super.onPostCreate(savedInstanceState);

        }
    */

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

        ArrayList<Offering> list = new ArrayList(realm.where(Offering.class).equalTo("ano", year).equalTo("semestre", semester).equalTo("is_active", true).equalTo("id_user", MainActivity.getUserId()).findAll());
        //ArrayList<Offering> list = new ArrayList(realm.where(Offering.class).findAll());

        offeringAdapter = new OfferingAdapter(list, array);
        recyclerView.setAdapter(offeringAdapter);
    }
}
