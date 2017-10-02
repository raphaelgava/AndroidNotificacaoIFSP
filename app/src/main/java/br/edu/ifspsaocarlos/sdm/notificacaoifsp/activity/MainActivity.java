package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Date;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.CreateNotificationFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.FragmentFactory;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout.TemplateFragment;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Person;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.UserLogin;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.EnumUserType;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.ServiceState;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener,
        TemplateFragment.OnFragmentInteractionListener{

    private final int RESULT_OFFERING_ACTIVITY = 100;

    private static UserLogin actualUser;
    private static EnumUserType type;
    private static int userId;
    private static String auth;

    private android.support.v4.app.FragmentManager fragmentManager;
    private Intent serviceIntent;

    private FloatingActionButton fab;

    private final String TAG_FRAG_CREATE_NOTIFICATION = "tagNotification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.app_bar_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
               setFragTransactionStack(R.id.nav_notification, R.id.content_frame, true);

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
        setFragTransactionStack(R.id.nav_class_schedule, R.id.content_frame, true);

        /**
         * Verifica se esta cadastrado para só depois startar o que tem que startar. Sem isso ele
         * insere você na lista de usuários pois ele acaba percorrendo o fetUser duas vezes.
         */

        actualUser = checkUser(getIntent());
        if ( actualUser != null) {

            String toast = actualUser.getId() + " - " + actualUser.getGroup() + " - " + Boolean.toString(actualUser.getFlag());
            //Toast.makeText(MainActivity.this, toast,Toast.LENGTH_SHORT).show();

            userId = actualUser.getId();
            type = actualUser.getPersonType();
            auth = getString(R.string.json_token) + " " + actualUser.getToken();

            Log.d("TCC", toast);

            serviceIntent = new Intent(this, FetchJSONService.class);
            startService(serviceIntent);

            checkUserData(actualUser);
            //updateAdapter();
            //startMessagesService();
        }
        else{
            goBackToLogin();
        }
    }

    /*
    * Used to determine which type the user is at FetchJSONService
    */
    public static EnumUserType getPeronType(){
        return type;
    }

    public static int getUserId(){
        return userId;
    }

    public static String getAuth(){
        return auth;
    }

    private void checkUserData(UserLogin user){
        Person person = null;
        Realm realm = Realm.getDefaultInstance();

        if (user != null){
//            person = realm.where(Person.class).equalTo("pk", user.getId()).findFirst();
//            if (person == null) {
                Log.d("TCC", "Sending json get person data");
                ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_USER);
//            }
        }
    }

    private UserLogin checkUser(Intent intent) {
        UserLogin user;
        final UserLogin userN;
        Realm realm = Realm.getDefaultInstance();

        String token = intent.getStringExtra(getString(R.string.json_token));
        Log.d("TCC", token);
        if (token != null){
            user = realm.where(UserLogin.class).equalTo("token", token).findFirst();

            boolean flagContinue = false;
            if (user == null)
            {
                flagContinue = true;
            }
            else{
                Date userDate;
                userDate = user.getLastUpdate();
                if (userDate == null)
                {
                    flagContinue = true;
                }
                else {
                    /*
                     * Feito isso pois se o servidor for promovido a professor o aplicativo tem que atualizar a busca
                    * */
                    Date data_atual = new java.util.Date();
                    long diferenca = (data_atual.getTime() - userDate.getTime()) / (1000 * 60); //Tempo de atualização do Token (15 min)
                    if (diferenca > 14) {
                        flagContinue = true;
                    }
                }
            }

            if (flagContinue == true)
            {
                userN = new UserLogin();
                userN.setToken(token);
                userN.setId(intent.getIntExtra(getString(R.string.json_id), 0));
                userN.setGroup(intent.getStringExtra(getString(R.string.json_group)));
                userN.setFlag(intent.getBooleanExtra(getString(R.string.json_prof), false));
                userN.setLastUpdate(new java.util.Date());

                try {
                    //Symmetric
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(userN);
                    realm.commitTransaction();
                    return userN;
                }catch (Exception e){
                    Log.d("TCC", "Error to insert User: " + e.toString());
                    return null;
                }
/*
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        bgRealm.copyToRealmOrUpdate(userN);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("TCC", "User inserted");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.d("TCC", "Error to insert User: " + error.toString());
                    }
                });
                */
            }
            return user;
        }
        return null;
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
        if (lastFragEntry > 1) {
            lastFragEntry = lastFragEntry - 2;
            String lastFragTag = fragmentManager.getBackStackEntryAt(lastFragEntry).getName();
            //Toast.makeText(this.getApplicationContext(), lastFragTag, Toast.LENGTH_LONG).show();
            fragmentManager.popBackStack();
            if (lastFragTag.isEmpty() || lastFragTag.equals(Integer.toString(R.id.nav_class_schedule))){
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

    private void setFragTransactionStack(int fragType, int content, boolean flagAddStack){
        // Create new fragment and transaction
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragType == R.id.nav_class_schedule){
            if (fragmentManager.getBackStackEntryCount() >= 1) {
                fragmentManager.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

            fab.setVisibility(View.VISIBLE);
        }
        else{
            fab.setVisibility(View.INVISIBLE);
        }

        transaction.replace(content, FragmentFactory.CreateFragment(null, null, fragType));
        if (flagAddStack) {
            transaction.addToBackStack(Integer.toString(fragType));
            transaction.commit();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //https://developer.android.com/studio/write/vector-asset-studio.html#running -> mudar ícones
        if (id == R.id.nav_send) {
            CreateNotificationFragment myFragment = (CreateNotificationFragment) fragmentManager.findFragmentByTag(TAG_FRAG_CREATE_NOTIFICATION);
            if (myFragment != null && myFragment.isVisible()) {
                myFragment.submitForm();
            }else{
                Toast.makeText(getApplicationContext(), R.string.msg_not_creating_notification, Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_quit) {
            goBackToLogin();
        } else if (id == R.id.nav_offering){
            Log.d("TCC", "Sending json get person data");
            ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_OFERECIMENTO);

            Intent intent = new Intent(getApplicationContext(),OfferingListActivity.class);
            startActivityForResult(intent, RESULT_OFFERING_ACTIVITY);
        }else{
            setFragTransactionStack(id, R.id.content_frame, true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch (requestCode){
            case RESULT_OFFERING_ACTIVITY:
                if (resultCode == Activity.RESULT_OK){
                    //ArrayList<Remetente> array = (ArrayList<Remetente>) data.getSerializableExtra("remententList");

                    //for (int i = 0; i < array.size(); i++) {
                    //    Log.d("TCC", array.get(i).getDescription());
                    //}
                    Log.d("TCC", "Offering Activity Result ok");
                    setFragTransactionStack(R.id.nav_class_schedule, R.id.content_frame, true);
                }
                //String message=data.getStringExtra("MESSAGE");
                //textView1.setText(message);
                break;
            default:
                Log.d("TCC", "Activity Result wrong");
        }
    }

    private void goBackToLogin() {
        Intent intentNovo = new Intent(this, LoginActivity.class);
        intentNovo.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentNovo);
        finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("TCC", "onFragmentInteraction");
    }
}
