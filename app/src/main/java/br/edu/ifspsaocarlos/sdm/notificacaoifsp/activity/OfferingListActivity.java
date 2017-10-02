package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.OfferingAdapter;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Oferecimento;

/**
 * Created by rapha on 9/25/2017.
 */

public class OfferingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OfferingAdapter offeringAdapter;
    private EditText edtResearch;
    private Button btnEndOffering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_offers);

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
                data.putExtra("offeringList", array);
                setResult(RESULT_OK, data);
                finish();            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to show the back button on the actionbar

//        fetchUsers();
//        startMessagesService();
        createList();
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

    private ArrayList<Oferecimento> array;
    private void createList(){
        array = new ArrayList<Oferecimento>();
        char my = 0x61;
        /*
        for (int i = 0; i < 4; i++){
            Remetente r = new Remetente();
            r.setCode(i);
            r.setDescription(my + "Item " + i);
            my++;
            array.add(r);
        }

        remetentAdapter = new RemetentAdapter(array);
        recyclerView.setAdapter(remetentAdapter);
        */
    }

/*
    private void fetchUsers() {
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, Constants.SERVER_URL + Constants.CONTATO_PATH, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject json) {
                        parseUserList(json);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Helpers.showDialog(MainActivity.this, R.string.dialog_content_error_fetching_user);
                    }
                });

        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }

    private void parseUserList(JSONObject jsonRoot) {
        List<Contact> contactList = new ArrayList<>();

        try {
            JSONArray jsonArray = jsonRoot.getJSONArray("contatos");
            Gson gson = new Gson();

            for (int i = 0; i < jsonArray.length(); i++) {
                Contact contact = gson.fromJson(jsonArray.getJSONObject(i).toString(), Contact.class);

                if (!contact.getId().equals(Helpers.getUserId(this)))
                    contactList.add(contact);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        saveContacts(contactList);
    }

    private void saveContacts(final List<Contact> contactList) {
        if (contactList != null) {
            Realm realm = Realm.getDefaultInstance();
            //        realm.executeTransaction(new Realm.Transaction() {
            //            @Override
            //            public void execute(Realm realm) {
            //                realm.copyToRealmOrUpdate(contactList);
            //            }
            //        });
            //        updateAdapter();

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(contactList);
//                    bgRealm.commitTransaction();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    updateAdapter();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.d("SDM", "onError: " + error.toString());
                    updateAdapter();
                }
            });
        }
    }

    private void updateAdapter() {
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<Contact> query = realm.where(Contact.class);
        RealmResults<Contact> result = query.findAll();

        contactAdapter = new ContactAdapter(result.subList(0, result.size()));
        recyclerView.setAdapter(contactAdapter);
    }
    */
}
