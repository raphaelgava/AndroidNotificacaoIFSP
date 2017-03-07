package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.graphics.Color;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
//todo decidir se tera o de fato o botao de "criate = mail = vermelhinho abaixo" -- só para servidores
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
    private void configurarAdapter() {
        ArrayList<Oferecimento> mListaOferecimentos = new ArrayList<Oferecimento>();
        Date currentDate = Calendar.getInstance().getTime();
//        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
//        String output = simpleDateFormat.format(currentDate);
//        Log.d("TCC", output);
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate); // Now use today date.
        for (char i = 0; i < 102; i++) {
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

        if (mListaOferecimentos != null) {
            Log.d("TCC", thisView.getClass().getName());
            mRecyclerView = (RecyclerView) thisView.findViewById(R.id.recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_COLUMN, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(gridLayoutManager);

            mListaApdapter = new CustomGrid(mListaOferecimentos);
            mRecyclerView.setAdapter(mListaApdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //thisView = inflater.inflate(R.layout.fragment_grid_notifications, container, false);
        thisView = inflater.inflate(R.layout.fragment_grid_notifications, null);
        configurarAdapter();
        return thisView;
    }
}
