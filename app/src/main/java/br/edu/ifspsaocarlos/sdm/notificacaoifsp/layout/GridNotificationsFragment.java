package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.CustomGrid;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.AddedOffering;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import io.realm.Realm;
//todo inserir mapa no frame ade acezso ao item
//todo estudar OnFragmentInteractionListener (verificar se cada fragmento tem que ter o seu e se o main tem que implementar todos)



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridNotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridNotificationsFragment extends TemplateFragment{

    public static final int NUMBER_COLUMN = 5; //Number of column = number of classes days
    private static Bundle bundle;
    private static Bundle actualBundle;
    private static boolean flagSalvar;

    public GridNotificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {        
        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.v("TAG","Landscape !!!");
        }
        else {
            Log.v("TAG","Portrait !!!");
        }
        
        //// TODO: 6/15/2017 testar e verificar se a transição ficou lenta!!!
        configurarAdapter();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment grid_notifications.
     */
    // TODO: Rename and change types and number of parameters
    //public static GridNotificationsFragment newInstance(String param1, String param2) {
    public static GridNotificationsFragment newInstance(Context context, Bundle args){
        GridNotificationsFragment fragment = new GridNotificationsFragment();

        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        //mainBundle();

        flagSalvar = false;
        if (args != null) {
            bundle = args;
            flagSalvar = true;
        }
        return fragment;
    }

    private static void mainBundle() {
        Realm realm = Realm.getDefaultInstance();
        ArrayList<AddedOffering> list = new ArrayList(realm.where(AddedOffering.class).findAll());

        Intent data = new Intent();

        try {
            Gson gson = new Gson();
            for (int i = 0; i < list.size(); i++) {
                Offering offer = list.get(i).getOffer();

                String json = gson.toJson(realm.copyFromRealm(offer));
                data.putExtra(Integer.toString(i), json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        actualBundle = data.getExtras();
    }

    private RecyclerView mRecyclerView;
    private CustomGrid mListaApdapter;
    private View thisView;
    private LinearLayout week;

    private int configurarLegenda(){
        //week = (LinearLayout) thisView.findViewById(R.id.gridWeek);
        week.removeAllViews();

        Rect rectWeek = new Rect();
        week.getGlobalVisibleRect(rectWeek);
        //GridLayoutManager.LayoutParams layout = new GridLayoutManager.LayoutParams(rectWeek.width(), rectWeek.height());
        //layout.setMargins(0, rectBar.bottom, rectWeek.right, rectBar.bottom + rectWeek.height());
        //week.setLayoutParams(layout);

        String[] days = getResources().getStringArray(R.array.days_of_week);

        LayoutParams paramsExample = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
        for (int i = 0; i < days.length; i++)
        {
            TextView dayName = new TextView(getContext());
            dayName.setText(days[i]);
            dayName.setBackgroundResource(R.color.colorPrimary);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                dayName.setTextColor(getResources().getColor(R.color.colorOptionMap, null));
            }else{
                dayName.setTextColor(getResources().getColor(R.color.colorOptionMap));
            }
            dayName.setLayoutParams(paramsExample);
            dayName.setGravity(Gravity.CENTER);
            week.addView(dayName);
        }

        //int posicao[] = new int[2];
        //LinearLayout bar = (LinearLayout) thisView.findViewById(R.id.gridWeek);
        //bar.getLocationOnScreen(posicao);

        return rectWeek.bottom;
    }

    private void runBundle(ArrayList<Offering> mListaOferecimentos, Bundle bundle, boolean checkMsg) {
        if (bundle != null) {
            // Cria o adaptador que preencherá as células da tela com o conteúdo da lista
//            Adapter adaptador = new CustomGrid(this, mListaOferecimentos);
//            setAdapter(adaptador);
            boolean flagMsg = false;
            Gson gson = new Gson();
            for (int k = 0; k < bundle.size(); k++) {
                final Offering object = gson.fromJson(bundle.getString(Integer.toString(k)), Offering.class);
                final int total = 5;

                int w = object.getWeek() - 1;
                int t = object.getTime() - 1;
                int inserido = 0;

                boolean flagFirst = true;
                int posicao = -1;
                for (int i = 0; i < 5; i++) {
                    if (w == i) {//encontrou a semana
                        for (int j = 0; j < 5; j++) {
                            if (t == j) {//encontrou o horário
                                Offering offer = mListaOferecimentos.get(i + (j * total));
                                if (offer.getDescricao().equals("---")) {
                                    if (flagFirst == true) {
                                        posicao = i + (j * total);
                                        flagFirst = false;
                                    }
                                    inserido++;
                                    if (inserido < object.getQtd()) {
                                        t++;
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }

                if (inserido == object.getQtd()) {
                    if (flagSalvar) {
                        try {
                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm bgRealm) {
                                    AddedOffering newObj = new AddedOffering(object);
                                    bgRealm.copyToRealmOrUpdate(newObj);
                                }
                            }, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    Log.d("TCC", "Offering inserted");
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    Log.d("TCC", "Error to insert offering: " + error.toString());
                                }
                            });
                        } catch (Exception e) {
                            Log.d("TCC", "Error to insert Offering: " + e.toString());
                        }
                    }

                    while (inserido > 0) {
                        mListaOferecimentos.add(posicao, object);
                        mListaOferecimentos.remove(posicao + 1);
                        posicao += total;
                        inserido--;
                    }
                } else {
                    flagMsg = true;
                }


                Log.d("TCC", object.getDescricao() + " - " + object.getProfessor());
            }

            if (flagMsg & checkMsg) {
                Toast.makeText(getContext(), "Any offering was not inserted because some other exists in same time", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void configurarAdapter() {
        ArrayList<Offering> mListaOferecimentos = new ArrayList<Offering>();

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);

        for (char i = 0; i < 25; i++) {
            Offering offer = new Offering();
            offer.setDescricao("---");
            mListaOferecimentos.add(offer);
        }

        mainBundle();
        runBundle(mListaOferecimentos, actualBundle, false);
        runBundle(mListaOferecimentos, bundle, true);

        int altura = configurarLegenda();
        Log.d("TCC", "Altura barras: " + altura);

        if (mListaOferecimentos != null) {

            Log.d("TCC", thisView.getClass().getName());
            mRecyclerView = (RecyclerView) thisView.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN, GridLayoutManager.VERTICAL, false);
            //merge of colums
//            gridLayoutManager.setSpanSizeLookup(
//                    new GridLayoutManager.SpanSizeLookup() {
//                        @Override
//                        public int getSpanSize(int position) {
//                                //Stagger every other row
//                            return (position == 7 ? 2 : 1);
//                            //return (position % 2 == 0 ? 3 : 2); //merge de 3 se não merge de 2
//                        }
//                    });
            mRecyclerView.setLayoutManager(gridLayoutManager);

            mListaApdapter = new CustomGrid(mListaOferecimentos, size, altura);
            mRecyclerView.setAdapter(mListaApdapter);

            bundle = null;

            Log.d("TCC", "After set");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TCC", "Before configurarAdapter");
        // Inflate the layout for this fragment
        //thisView = inflater.inflate(R.layout.fragment_grid_notifications, container, false);
        thisView = inflater.inflate(R.layout.fragment_grid_notifications, null);
        week = (LinearLayout) thisView.findViewById(R.id.gridWeek);
        week.post(new Runnable()
        {
            public void run()
            {
                Log.d("TCC", "RUNN!!!");
                configurarAdapter();
            }
        });
        Log.d("TCC", "After onCreateView");
        return thisView;
    }

}
