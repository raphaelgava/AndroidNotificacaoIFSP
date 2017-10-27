package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.NotificationAdapter;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import io.realm.Realm;

public class NotificationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private EditText edtResearch;
    private Button btnEndNotification;
    private final Realm realm = Realm.getDefaultInstance();
    private boolean loadNotification;
    private Handler handler;
    private TextView txtEmpty;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notification);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_notification);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (loadNotification == false) {
                    handler.post(new Runnable() {
                        public void run() {
                            createList();
                            edtResearch.setEnabled(true);
                        }
                    });
                }
                // Stop refresh animation
                swipeLayout.setRefreshing(false);
            }
        });
        swipeLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent,
                R.color.colorBackgroundCard, R.color.colorCell);

        txtEmpty = (TextView) findViewById(R.id.txtNotificationEmpty);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerListNotification);
        /*
          O erro: No adapter attached; skipping layout. Acontece pois não esta setando o adapter
          antes do layoutmanager.
         */
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(false);

        edtResearch = (EditText) findViewById(R.id.edtFindNotification);
        edtResearch.setEnabled(false);
        edtResearch.clearFocus();
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
                notificationAdapter.getFilter().filter(arg0.toString());
            }
        });

        btnEndNotification = (Button) findViewById(R.id.btnEndNotification) ;
        btnEndNotification.requestFocus();
        btnEndNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//to show the back button on the actionbar

        loadNotification = true;
        handler = new Handler();

        new Thread(){
            public void run() {
                while (loadNotification) {
                    try {
                        Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                        if (FetchJSONService.isBuscandoDadosTerminou() == true){
                            handler.post(new Runnable() {
                                public void run() {
                                    createList();
                                    edtResearch.setEnabled(true);
                                }
                            });

                            loadNotification = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
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
        String dateMask = getString(R.string.mask_date);
        SimpleDateFormat formatDate = new SimpleDateFormat(dateMask);
        Calendar c = Calendar.getInstance();
        Date today;
        try {
            today = formatDate.parse(formatDate.format(c.getTime()));

            ArrayList<Notification> list = new ArrayList(realm.where(Notification.class).greaterThanOrEqualTo("eventDate", today).findAll());

            if (list.size() > 0) {
                txtEmpty.setVisibility(View.GONE);
                notificationAdapter = new NotificationAdapter(list);
                recyclerView.setAdapter(notificationAdapter);
            }
        }catch (Exception e){
            Log.d("TCC", "Error: " + e.toString());
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (loadNotification == false) {
            handler.post(new Runnable() {
                public void run() {
                    createList();
                    edtResearch.setEnabled(true);
                }
            });
        }
    }


}
