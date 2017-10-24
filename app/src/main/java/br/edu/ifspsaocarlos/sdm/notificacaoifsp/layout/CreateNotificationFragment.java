package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.RemetentListActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Local;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Notification;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Remetente;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.TipoNotificacao;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.service.FetchJSONService;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.ServiceState;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

import static android.app.DatePickerDialog.OnDateSetListener;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CreateNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateNotificationFragment extends TemplateFragment {

    private final int RESULT_REMETENT_ACTIVITY = 1;

//    private TextView tvDate, tvTime, tvType;
    private TextView tvDate;
    private Button btnSave, btnRemetent;
    private int mYear, mMonth, mDay;
//    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText edtDescription, edtTitle;
    private TextInputLayout txtTitle, txtDescription;
    private Spinner spLocal, spTipoNotificacao;

    private Calendar c;
    private SimpleDateFormat formatDate;
//    private SimpleDateFormat formatTime;
    private boolean loadRemetent;
    private Handler handler;
    private Thread secontThread;

    private Notification notificationObject = null;

    //// TODO: 3/27/2017 criar esquema de setar o local pelo marker (save the state of mapFragment - maps example)
//    id_local = models.ForeignKey(Local, verbose_name="Local", blank=True, null=True)  # Field name made lowercase.

    public CreateNotificationFragment(){

    }

    public CreateNotificationFragment(Context cxt) {
        // Required empty public constructor
        //c = Calendar.getInstance();
        //formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String dateMask = cxt.getString(R.string.mask_date);
        formatDate = new SimpleDateFormat(dateMask);
        //formatTime = new SimpleDateFormat("hh:mm");//12h
//        String timeMask = cxt.getString(R.string.mask_time);
//        formatTime = new SimpleDateFormat(timeMask);//24h

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//        mHour = c.get(Calendar.HOUR_OF_DAY);
//        mMinute = c.get(Calendar.MINUTE);

        notificationObject = new Notification();
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
        CreateNotificationFragment fragment = new CreateNotificationFragment(context);
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
//            tvTime = (TextView) view.findViewById(R.id.txtInTime);
            btnSave = (Button) view.findViewById(R.id.btnSendNotification);
//            tvType = (TextView) view.findViewById(R.id.txtCor);
            btnRemetent = (Button) view.findViewById(R.id.btnRemetent);

            tvDate.setText(formatDate.format(c.getTime()));
//            tvTime.setText(formatTime.format(c.getTime()));

            tvDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickMethod(view);
                }
            });
//            tvTime.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onClickMethod(view);
//                }
//            });

            txtDescription = (TextInputLayout) view.findViewById(R.id.txtDescription);
            txtTitle = (TextInputLayout) view.findViewById(R.id.txtTitle);

            spLocal = (Spinner) view.findViewById(R.id.spinner);
            spTipoNotificacao = (Spinner) view.findViewById(R.id.spinnerCor);
            spLocal.setEnabled(false);
            spTipoNotificacao.setEnabled(false);

            edtDescription = (EditText) view.findViewById(R.id.edtDescription);
            edtDescription.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    if (view.getId() == R.id.edtDescription) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction()&MotionEvent.ACTION_MASK){
                            case MotionEvent.ACTION_UP:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    return false;
                }
            });

            edtTitle = (EditText) view.findViewById(R.id.edtTitle);

            /*
            spCountries = (Spinner) view.findViewById(R.id.spCountries);
            spBusinessType = (Spinner) view.findViewById(R.id.spBussinessType);
            String businessType[] = { "Automobile", "Food", "Computers", "Education",
                    "Personal", "Travel" };
            ArrayAdapter<String> adapterBusinessType;
            adapterBusinessType = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, businessType);
            adapterBusinessType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spBusinessType.setAdapter(adapterBusinessType);
            spCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View v,
                                           int position, long id) {
                    // On selecting a spinner item
                    String scountry = adapter.getItemAtPosition(position).toString();
                    // Showing selected spinner item
                    Toast.makeText(v.getContext().getApplicationContext(),
                            "Selected Country : " + scountry, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
            */

            btnRemetent.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    ServiceState.getInstance().pushState(ServiceState.EnumServiceState.ENUM_REMETENTE);

                    Intent intent = new Intent(view.getContext(),RemetentListActivity.class);
                    //intent.putExtra("notificacao", notificationObject);
                    Gson gson = new Gson();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    Notification object = realm.copyToRealmOrUpdate(notificationObject);
                    String json = gson.toJson(realm.copyFromRealm(object));
                    intent.putExtra("notificacao", json);
                    Realm.getDefaultInstance().cancelTransaction();
                    startActivityForResult(intent, RESULT_REMETENT_ACTIVITY);
                }
            });
            // TODO: 3/18/2017 pensar no esquema do servidor igual ao que o professor tinha feito de pegar dados a partir do ultimo item reccebido apenas

/*
            //final ImageView shape =  (ImageView) view.findViewById(R.id.imvShape);
            final GradientDrawable bgDrawable = (GradientDrawable) tvType.getBackground();

            tvType.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                ArrayList<Integer> images = new ArrayList<Integer>();

                int red =        0xffF44336;
                int pink =       0xffE91E63;
                int Purple =     0xff9C27B0;
                int DeepPurple = 0xff673AB7;
                int Indigo =     0xff3F51B5;
                int Blue =       0xff2196F3;
                int LightBlue =  0xff03A9F4;
                int Cyan =       0xff00BCD4;
                int Teal =       0xff009688;
                int Green =      0xff4CAF50;
                int LightGreen = 0xff8BC34A;
                int Lime =       0xffCDDC39;
                int Yellow =     0xffFFEB3B;
                int Amber =      0xffFFC107;
                int Orange =     0xffFF9800;
                int DeepOrange = 0xffFF5722;
                int Brown =      0xff795548;
                int Grey =       0xff9E9E9E;
                int BlueGray =   0xff607D8B;
                int Black =      0xff000000;
                int White =      0xffffffff;
                images.add(red);
                images.add(pink);
                images.add(Purple);
                images.add(DeepPurple);
                images.add(Indigo);
                images.add(Blue);
                images.add(LightBlue);
                images.add(Cyan);
                images.add(Teal);
                images.add(Green);
                images.add(LightGreen);
                images.add(Lime);
                images.add(Yellow);
                images.add(Amber);
                images.add(Orange);
                images.add(DeepOrange);
                images.add(Brown);
                images.add(Grey);
                images.add(BlueGray);
                images.add(Black);
                images.add(White);

                MyColorChosserDialog dialog = new MyColorChosserDialog(view.getContext(), images);
                dialog.setTitle("teste");
                dialog.setColorListener(new MyColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        //do whatever you want to with the values

                        //back.setColorFilter(color, PorterDuff.Mode.SRC);
                        //shape.setBackgroundColor(color);
                        bgDrawable.setColor(color);

                        Toast.makeText(v.getContext(), "oi", Toast.LENGTH_LONG).show();
                    }
                });
                //customize the dialog however you want
                dialog.show();
                }
            });
*/
            btnSave.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    submitForm();
                }
            });

            handler = new Handler();
            new Thread(){
                public void run() {
                    loadRemetent = true;
                    while (loadRemetent) {
                        try {
                            Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                            if ((FetchJSONService.isBuscandoDadosTerminou() == true) &&
                                    (FetchJSONService.isLocalTerminou() == true)){
                                handler.post(new Runnable() {
                                    public void run() {
                                        Realm realm = Realm.getDefaultInstance();
                                        RealmResults<TipoNotificacao> realmResults = realm.where(TipoNotificacao.class).findAll().sort("pk");
                                        List<TipoNotificacao> documents = realm.copyFromRealm(realmResults);;
                                        ArrayAdapter<TipoNotificacao> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, documents);
                                        spTipoNotificacao.setAdapter(adapter);
                                        spTipoNotificacao.setEnabled(true);

                                        secontThread.start(); //Por conta da ordem que foi inserido na fila do service json
                                    }
                                });

                                loadRemetent = false;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            secontThread = new Thread(){
                public void run() {
                    loadRemetent = true;
                    while (loadRemetent) {
                        try {
                            Thread.sleep(getResources().getInteger(R.integer.tempo_inatividade_servico));
                            if ((FetchJSONService.isBuscandoDadosTerminou() == true) &&
                                    (FetchJSONService.isLocalTerminou())){
                                handler.post(new Runnable() {
                                    public void run() {
                                        Realm realm = Realm.getDefaultInstance();
                                        RealmResults<Local> realmResults = realm.where(Local.class).findAll().sort("pk");
                                        List<Local> documents = realm.copyFromRealm(realmResults);
                                        Local obj = new Local();
                                        obj.setDescricao("No place");
                                        documents.add(0, obj);
                                        ArrayAdapter<Local> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, documents);
                                        spLocal.setAdapter(adapter);
                                        spLocal.setEnabled(true);

                                        //secontThread.start(); //Por conta da ordem que foi inserido na fila do service json
                                    }
                                });

                                loadRemetent = false;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        return view;
    }

//    public ArrayList<String> retrieve()
//    {
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<Local> realmResults = realm.where(Local.class).findAll();
//        //List<Local> documents = realm.copyFromRealm(realmResults);
//
//        ArrayList<String> spacecraftNames=new ArrayList<>();
//        for(Local s: realmResults)
//        {
//            spacecraftNames.add(Integer.toString(s.getPk())+"-"+s.getDescricao());
//        }
//        return spacecraftNames;
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch (requestCode){
            case RESULT_REMETENT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK){
                    Gson gson = new Gson();
                    Bundle bundle = data.getExtras();
                    if (notificationObject != null) {
                        notificationObject.clearRemetent();
                        //notificationObject.clear();
                        for (int i = 0; i < bundle.size(); i++) {
                            Remetente object = gson.fromJson(bundle.getString(Integer.toString(i)), Remetente.class);

                            notificationObject.addRemetent(object.getPk());

                            Log.d("TCC", object.getDescricao() + " - " + object.getTipo());
                        }
                    }
                    else{
                        Log.d("TCC", "notification is null! Please check!");
                    }
                }
                break;
            default:
                Log.d("TCC", "Activity Result wrong");
        }
    }

    /**
     * Validating form
     */
    public void submitForm() {
        if ((!validateTitle()) || (!validateDescription()) || (!validateRemetent())) {
            return;
        }

        if (criarNotificao()) {
            //getActivity().getFragmentManager().popBackStack();
            Toast.makeText(getActivity().getApplicationContext(), R.string.msg_send_notification, Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }

    private boolean criarNotificao(){
        Gson gson = new Gson();
        Realm realm = Realm.getDefaultInstance();


        //object = gson.fromJson(json.toString(), Person.class);
        if (notificationObject != null){
            notificationObject.setDescricao(edtDescription.getText().toString().trim());
            notificationObject.setTitulo(edtTitle.getText().toString().trim());
            notificationObject.setDatahora(tvDate.getText().toString());
            notificationObject.setServidor(MainActivity.getUserId());

            Local l = (Local )spLocal.getSelectedItem();
            TipoNotificacao tn = (TipoNotificacao) spTipoNotificacao.getSelectedItem();

            if (l != null && l.getPk() != 0) {
                notificationObject.setId_local(l.getPk());
            }

            if (tn != null){
                notificationObject.setId_tipo(tn.getPk());
            }


            final RealmObject finalObject = notificationObject;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(finalObject);

                    FetchJSONService.setNotification((Notification) finalObject);
                    //bgRealm.insertOrUpdate(finalObject);//faster than copyToRealmOrUpdate
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.e("TCC", "User saved");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.e("TCC", "Erro: " + error.toString());
                }
            });
            return true;
        }
        return false;
    }

    private boolean validateTitle(){
        String title = edtTitle.getText().toString().trim();

        if (title.isEmpty()) {
            txtTitle.setError(getString(R.string.error_invalid_title));
            requestFocus(txtTitle);
            return false;
        } else {
            txtTitle.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateDescription(){
        String desc = edtDescription.getText().toString().trim();

        if (desc.isEmpty()) {
            txtDescription.setError(getString(R.string.error_invalid_description));
            requestFocus(txtDescription);
            return false;
        } else {
            txtDescription.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateRemetent(){
        if (notificationObject.getSize() <= 0) {
            Toast.makeText(getActivity().getApplicationContext(), R.string.error_invalid_remetent, Toast.LENGTH_SHORT).show();
            requestFocus(btnRemetent);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    public void onClickMethod(View v) {
        if (v == tvDate){
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                        Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                        tvDate.setText(formatDate.format(date));
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, monthOfYear);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //tvDate.setText(dayOfMonth + "/" + (monthOfYear + 1)+ "/" + year);
                    }
                }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }

//        if (v == tvTime){
//            // Launch Time Picker Dialog
//            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
//                new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        if (((mYear == c.get(Calendar.YEAR)) && (mMonth == c.get(Calendar.MONTH)) && (mDay == c.get(Calendar.DAY_OF_MONTH))) &&
//                            ((hourOfDay < c.get(Calendar.HOUR_OF_DAY)) || ((hourOfDay == c.get(Calendar.HOUR_OF_DAY)) && (minute <= (c.get(Calendar.MINUTE) + 10))))) {
//                            Toast.makeText(view.getContext(), "Set time at least 10 minutes from now", Toast.LENGTH_LONG).show();
//                        } else {
//                            //tvTime.setText(hourOfDay + ":" + minute);
//                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                            c.set(Calendar.MINUTE, minute);
//                            tvTime.setText(formatTime.format(c.getTime()));
//                        }
//                    }
//                }, mHour, mMinute, true);
//            timePickerDialog.show();
//        }
    }
}