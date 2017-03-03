package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;

import com.google.android.gms.plus.PlusOneButton;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static android.app.DatePickerDialog.*;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CreateNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNotificationFragment extends TemplateFragment {

    private Button btnDatePicker, btnTimePicker;
    private EditText txtDate, txtTime;
    private TextView tvDate, tvTime;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private Calendar c;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatTime;

    public CreateNotificationFragment() {
        // Required empty public constructor
        c = Calendar.getInstance();
        formatDate = new SimpleDateFormat("d/MM/yyyy");
        //formatTime = new SimpleDateFormat("hh:mm");//12h
        formatTime = new SimpleDateFormat("kk:mm");//24h
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateNotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    //public static CreateNotificationFragment newInstance(String param1, String param2) {
    public static CreateNotificationFragment newInstance(Context context, Bundle args) {
        CreateNotificationFragment fragment = new CreateNotificationFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_notification, container, false);
        if (view != null)
        {
            tvDate = (TextView) view.findViewById(R.id.txtInDate);
            tvTime = (TextView) view.findViewById(R.id.txtInTime);

            // TODO: 3/2/2017 esta setando a data de ontem! (1/mar quando já era 2/mar
            tvDate.setText(formatDate.format(c.getTime()));
            tvTime.setText(formatTime.format(c.getTime()));

            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickMethod(view);
                }
            });
            tvTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickMethod(view);
                }
            });
        }
        return view;
    }

    public void onClickMethod(View v) {
        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        if (v == tvDate){
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        Date date = new GregorianCalendar(year, monthOfYear-1, dayOfMonth).getTime();
                        tvDate.setText(formatDate.format(date));
                        //tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1)+ "/" + year);
                    }
                }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }
        //// TODO: 3/2/2017 verificar a hora mínima baseada na data TESTAR!!!!!!
        if (v == tvTime){
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (((mYear == c.get(Calendar.YEAR)) && (mMonth == c.get(Calendar.MONTH)) && (mDay == c.get(Calendar.DAY_OF_MONTH))) &&
                            ((mHour < c.get(Calendar.HOUR_OF_DAY)) || ((mHour == c.get(Calendar.HOUR_OF_DAY)) && (mMinute <= (c.get(Calendar.MINUTE) + 10))))) {
                            Toast.makeText(view.getContext(), "Set time at least 10 minutes from now", Toast.LENGTH_LONG).show();
                        } else {
                            //tvTime.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, mMinute);
                            tvTime.setText(formatTime.format(c.getTime()));
                        }
                    }
                }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}
//public class CreateNotificationFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    // The request code must be 0 or greater.
//    private static final int PLUS_ONE_REQUEST_CODE = 0;
//    // The URL to +1.  Must be a valid URL.
//    private final String PLUS_ONE_URL = "http://developer.android.com";
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//    private PlusOneButton mPlusOneButton;
//
//    private OnFragmentInteractionListener mListener;
//
//    public CreateNotificationFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CreateNotificationFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    //public static CreateNotificationFragment newInstance(String param1, String param2) {
//    public static CreateNotificationFragment newInstance(Context context, Bundle args) {
//        CreateNotificationFragment fragment = new CreateNotificationFragment();
//        //Bundle args = new Bundle();
//        //args.putString(ARG_PARAM1, param1);
//        //args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_create_notification, container, false);
//
//        //Find the +1 button
//        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
//
//        return view;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // Refresh the state of the +1 button each time the activity receives focus.
//        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    // TODO: 2/28/2017 retirar os métodos das interfaces de cada fragmento se não for usar
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//}
