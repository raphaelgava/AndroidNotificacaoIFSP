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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridNotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridNotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridNotificationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

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
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        //String output = sdf.format(c.getTime());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("TCC", context.getClass().getName());
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
