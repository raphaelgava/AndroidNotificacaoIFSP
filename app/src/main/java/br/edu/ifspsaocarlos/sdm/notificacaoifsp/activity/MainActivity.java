package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.Uteis.EnumFragments;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.ChangeUserDataFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.CreateNotificationFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.GridNotificationsFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.TemplateFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        TemplateFragment.OnFragmentInteractionListener{

    private String token;
    private android.support.v4.app.FragmentManager fragmentManager;
    private Intent serviceIntent;

    private FloatingActionButton fab;

    private final String TAG_FRAG_CREATE_NOTIFICATION = "tagNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setContentView(R.layout.activity_main);
        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.bringToFront();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
               setFragTransactionStack(EnumFragments.FRAG_NOTIFICATION, R.id.content_frame, true);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,
                GridNotificationsFragment.newInstance(null, null)).commit();

        //fragmentManager = getSupportFragmentManager();
        //Fragment frag = GridNotificationsFragment.newInstance(this, getBundleCurrentFragment());
        //Fragment frag = GridNotificationsFragment.newInstance("GridNotificationsFragment", null);
        //fragmentManager.beginTransaction().replace(R.id.content_frame, frag).commit();

        token = getIntent().getStringExtra(getString(R.string.json_token));

        if (token != null) {
            String toast = getIntent().getIntExtra(getString(R.string.json_id), 0) + " - " + getIntent().getStringExtra(getString(R.string.json_group));
            Toast.makeText(MainActivity.this, toast,
                    Toast.LENGTH_SHORT).show();
            serviceIntent = new Intent(this, FetchJSONService.class);
            startService(serviceIntent);

            Log.d("TCC", token);
        }
        else{
            //Snackbar.make(drawer, "Token not received.", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void onDestroy(){
        if (serviceIntent != null) {
            stopService(serviceIntent);
        }
        super.onDestroy();
    }

    // TODO: 6/29/2017 ACERTAR TAG PARA DESCOBRIR SE E DA GRID PARA PODER DEIXAR VSIBLE O BOTAO DE NOTIFICACAO!!!! 

    @Override
    public void onBackPressed() {
        int lastFragEntry = fragmentManager.getBackStackEntryCount();
        if (lastFragEntry > 0) {
            String lastFragTag = fragmentManager.getBackStackEntryAt(lastFragEntry - 1).getName();
            Toast.makeText(this.getApplicationContext(), lastFragTag, Toast.LENGTH_LONG).show();
            fragmentManager.popBackStack();
            if (lastFragTag.isEmpty() || lastFragTag.equals(EnumFragments.FRAG_GRID.toString())){
                fab.setVisibility(View.VISIBLE);
            }
        } else {
            moveTaskToBack(true); // faz com que não volte para a tela de login!!!!
            //this.finish();
        }
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true); // faz com que não volte para a tela de login!!!!
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intentNovo = new Intent(this, AboutActivity.class);
            //Não pode inserir essa opção pois se não, fecha a aplicação!!!
            //intentNovo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentNovo);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFragTransactionStack(EnumFragments frag, int content, boolean flagAddStack){
        // Create new fragment and transaction
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(content,
                ChangeUserDataFragment.newInstance(null, null));
        if (flagAddStack) {
            transaction.addToBackStack(frag.toString());
            transaction.commit();
//            fragmentManager.beginTransaction().replace(R.id.content_frame,
//                    ChangeUserDataFragment.newInstance(null, null)).commit();
        }
        fab.setVisibility(View.INVISIBLE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //https://developer.android.com/studio/write/vector-asset-studio.html#running -> mudar ícones
        if (id == R.id.nav_class_schedule) {
            setFragTransactionStack(EnumFragments.FRAG_GRID, R.id.content_frame, false);
            fab.setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_user_data) {
            setFragTransactionStack(EnumFragments.FRAG_USER, R.id.content_frame, true);
        } else if (id == R.id.nav_notification) {
            setFragTransactionStack(EnumFragments.FRAG_NOTIFICATION, R.id.content_frame, true);
        } else if (id == R.id.nav_send) {
            CreateNotificationFragment myFragment = (CreateNotificationFragment) fragmentManager.findFragmentByTag(TAG_FRAG_CREATE_NOTIFICATION);
            if (myFragment != null && myFragment.isVisible()) {
                myFragment.submitForm();
            }else{
                Toast.makeText(getApplicationContext(), R.string.msg_not_creating_notification, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_quit) {
            Intent intentNovo = new Intent(this, LoginActivity.class);
            intentNovo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentNovo);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("TCC", "onFragmentInteraction");
    }
}
