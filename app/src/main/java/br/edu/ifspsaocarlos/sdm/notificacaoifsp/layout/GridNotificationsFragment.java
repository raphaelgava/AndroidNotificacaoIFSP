package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.adapter.CustomGrid;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Oferecimento;
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

        return fragment;
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

    private void configurarAdapter() {
        ArrayList<Oferecimento> mListaOferecimentos = new ArrayList<Oferecimento>();
        Date currentDate = Calendar.getInstance().getTime();
//        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
//        String output = simpleDateFormat.format(currentDate);
//        Log.d("TCC", output);
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate); // Now use today date.

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);

        for (char i = 0; i < 25; i++) {
            Oferecimento offer = new Oferecimento();

            c.add(Calendar.DATE, i); // Adding days
            offer.setData(c.getTime());

            switch (i % NUMBER_COLUMN){
                case 0:
                    offer.setSigla("SDM");
                    break;
                case 1:
                    offer.setSigla("BTDS");
                    break;
                case 2:
                    offer.setSigla("PRJ");
                    break;
                case 3:
                    offer.setSigla("INT");
                    break;
                case 4:
                    offer.setSigla("AUX");
                    break;
                default:
                    Log.d("TCC", "conta inválida!!!");
            }
            //offer.setSigla(String.valueOf(i));
            mListaOferecimentos.add(offer);
        }

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

            /*
            // Cria o adaptador que preencherá as células da tela com o conteúdo da lista
            Adapter adaptador = new CustomGrid(this, mListaOferecimentos);
            setAta(adaptador);*/
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
/*
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d("TCC", "onActivityCreated");
        //configurarAdapter();
    }

    public void onResume(){
        super.onResume();
        Log.d("TCC", "onResume");
        week = (LinearLayout) thisView.findViewById(R.id.gridWeek);
        week.post(new Runnable()
        {
            public void run()
            {
                Log.d("TCC", "RUNN!!!");
                configurarAdapter();
            }
        });
        Log.d("TCC", "onResume end");
        //configurarAdapter();
    }
    */
}
