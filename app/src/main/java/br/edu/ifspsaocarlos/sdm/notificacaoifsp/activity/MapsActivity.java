package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Local;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import io.realm.Realm;

public class MapsActivity extends FragmentActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private Button botao, btnGoMark;
    private TextView texto;
    //    private Offering offer;
    private LatLng position;

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
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            this.setFinishOnTouchOutside(false);

            btnGoMark = (Button) findViewById(R.id.btnGoMark);

            position = new LatLng(-21.969539, -47.878139); //IFSP
            if (obj.getId_local() != null) {
                Realm realm = Realm.getDefaultInstance();
                Local local = realm.where(Local.class).equalTo("pk", obj.getId_local()).findFirst();
                if (local != null) {
                    if (!local.getPosition().isEmpty()) {
                        String[] latlong = local.getPosition().split(",");
                        position = new LatLng(Double.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
                    }
                }
            }else{
                btnGoMark.setVisibility(View.GONE);
                mapFragment.getView().setVisibility(View.GONE);
            }

            btnGoMark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mMap != null) {
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                        //mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

                        //CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(position, 16);
                        //mMap.animateCamera(yourLocation);
                        CameraPosition yourLocation = new CameraPosition.Builder()
                                .target(position)      // Sets the center of the map to Mountain View
                                .zoom(ZOOM)                   // Sets the zoom
                                .bearing(BEARING)                // Sets the orientation of the camera to east
                                .tilt(TILT)                   // Sets the tilt of the camera to 30 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(yourLocation));
                    }
                }
            });

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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private final int ZOOM  = 15; // Sets the zoom
    private final int BEARING = 0; // Sets the orientation of the camera to east
    private final int TILT = 30; // Sets the tilt of the camera to 30 degrees

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(position).title("Marker"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        CameraPosition yourLocation = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to Mountain View
                .zoom(ZOOM)                   // Sets the zoom
                .bearing(BEARING)                // Sets the orientation of the camera to east
                .tilt(TILT)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(yourLocation));

        mMap.setOnMyLocationButtonClickListener(this);


        enableMyLocation();

//        if (flagShowReCenter) {
//            // Access to the location has been granted to the app.
//            mMap.setMyLocationEnabled(true);
//            mMap.getUiSettings().setMyLocationButtonEnabled(true);
//            //Toast.makeText(this, "MyLocations", Toast.LENGTH_SHORT).show();
//            Log.d("TCC", "Center maps is visible");
//        }
    }

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int MY_LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (mMap != null) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_PERMISSION_REQUEST_CODE);
                //Toast.makeText(getApplicationContext(), "Teste 4", Toast.LENGTH_SHORT).show();
                Log.d("TCC", "Requesting GPS permission");
                /*
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(getApplicationContext(), "Teste 3", Toast.LENGTH_SHORT).show();

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_LOCATION_PERMISSION_REQUEST_CODE);
                    Toast.makeText(getApplicationContext(), "Teste 4", Toast.LENGTH_SHORT).show();

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
                */
            } else {
                buttonReCenterMaps();
            }
        }
    }

    private void buttonReCenterMaps() {
        //Toast.makeText(getApplicationContext(), "Teste 5", Toast.LENGTH_SHORT).show();
        //Snackbar.make(getCurrentFocus(), "Checking GPS", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
            Log.d("TCC", "GPS is enabled in your devide");
            if (mMap != null) {
                // Access to the location has been granted to the app.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                //Toast.makeText(this, "MyLocations", Toast.LENGTH_SHORT).show();
                Log.d("TCC", "Center maps is visible");
            }
        }else{
            showGPSDisabledAlertToUser();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        enableMyLocation();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                //callGPSSettingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buttonReCenterMaps();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    //Toast.makeText(getApplicationContext(), "Teste 2", Toast.LENGTH_SHORT).show();
                    Log.d("TCC", "Permission denied");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
