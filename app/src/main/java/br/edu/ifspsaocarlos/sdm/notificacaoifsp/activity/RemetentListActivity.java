package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.RemetentAdapter;

public class RemetentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RemetentAdapter remetentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_remetent);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
        /**
         * O erro: No adapter attached; skipping layout. Acontece pois não esta setando o adapter
         * antes do layoutmanager.
         */
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

//        fetchUsers();
//        startMessagesService();
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
