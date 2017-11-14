package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Local;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import io.realm.Realm;

public class NotificationActivity extends FragmentActivity {

    private Button botao, btnGoMap;
    private TextView texto;
    //    private Offering offer;

    private Notification obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Gson gson = new Gson();
        Bundle bundle = i.getExtras();
        obj = gson.fromJson(bundle.getString("notificacao"), Notification.class);
        if (obj != null) {
            Log.d("TCC", obj.toString());

            this.setTitle(obj.getTitulo());

            //setTheme(android.R.style.Theme_Holo_Light_Dialog);
            setContentView(R.layout.layout_dialog);

            this.setFinishOnTouchOutside(false);

            btnGoMap = (Button) findViewById(R.id.btnDialogMap);
            btnGoMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent notificationIntent = new Intent(v.getContext(), MapsActivity.class);

//                    Gson gson = new Gson();
//                    Realm realm = Realm.getDefaultInstance();
//                    realm.beginTransaction();
////                object.setChecked(true);
//                    Notification newObject = realm.copyToRealmOrUpdate(obj);
//                    String json = gson.toJson(realm.copyFromRealm(newObject));
//                    notificationIntent.putExtra("notificacao", json);
////                realm.commitTransaction();
//                    realm.cancelTransaction();

                    LatLng position;
                    if (obj.getId_local() != null) {
                        Realm realm = Realm.getDefaultInstance();
                        Local local = realm.where(Local.class).equalTo("pk", obj.getId_local()).findFirst();
                        if (local != null) {
                            if (!local.getPosition().isEmpty()) {
                                String[] latlong = local.getPosition().split(",");
                                position = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));

                                String titulo = obj.getTitulo();
                                String desc = local.getDescricao();
                                Bundle args = new Bundle();
                                args.putParcelable("from_position", position);
                                args.putString("titulo", titulo);
                                args.putString("descricao", desc);
                                notificationIntent.putExtra("notificacao", args);
                            }
                        }
                    }


                    //Não pode inserir essa opção aqui pois se cancelar o pedido de GPS vai fechar a app!!!
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    try {
                        v.getContext().startActivity(notificationIntent);
                    }catch (Exception e){
                        Log.d("TCC", "Error in maps activity: " + e.toString());
                    }
                }
            });

            if (obj.getId_local() != null) {
                btnGoMap.setVisibility(View.VISIBLE);
            }else{
                btnGoMap.setVisibility(View.GONE);
            }

            texto = (TextView) findViewById(R.id.txtParametro);
            texto.bringToFront();
            texto.setText(obj.getDescricao());
            texto.setGravity(Gravity.CENTER_VERTICAL);

            botao = (Button) findViewById(R.id.btnDialog);
            botao.bringToFront();
            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


/*
            //setTheme(android.R.style.Theme_Holo_Light_Dialog);
            setContentView(R.layout.layout_dialog);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            this.setFinishOnTouchOutside(false);

            position = new LatLng(-34, 151);
            offer = (Offering) getIntent().getSerializableExtra("oferecimento");

            texto = (TextView) findViewById(R.id.txtParametro);
            texto.bringToFront();
            if (offer != null) {
                texto.setText(offer.getDescricao() + ": esse texto é apenas para eu saber como será distribuido na tela independente do tamanho pois o texto pode ser bem grande vindo do servidor. Ainda assim faltou texto por isso estou adicionando mais algumas palavras de teste. Parte 22309837542:esse texto é apenas para eu saber como será distribuido na tela independente do tamanho pois o texto pode ser bem grande vindo do servidor. Ainda assim faltou texto por isso estou adicionando mais algumas palavras de teste.");
            }

            botao = (Button) findViewById(R.id.btnDialog);
            botao.bringToFront();
            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //enableMyLocation();
*/
            updateObjectRealm(obj);
        } else {
            finish();
        }
    }

    private void updateObjectRealm(Notification obj){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        obj.setChecked(true);
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

}
