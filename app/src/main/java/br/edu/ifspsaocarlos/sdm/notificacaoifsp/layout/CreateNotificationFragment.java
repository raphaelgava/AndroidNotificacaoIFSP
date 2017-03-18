package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.RemetentListActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.color.MyColorChosserDialog;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.color.MyColorListener;

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

    private TextView tvDate, tvTime;
    private Button btnSave, btnColor, btnRemetent;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private EditText edtDescription;
    //private Spinner spCountries, spBusinessType;

    private Calendar c;
    private SimpleDateFormat formatDate;
    private SimpleDateFormat formatTime;

    // TODO: 3/13/2017 FAZER CAMPOS
//    datahora = models.DateTimeField("Data notificação", auto_now_add=True)  # Field name made lowercase.
//    id_tipo = models.ForeignKey(TipoNotificacao, verbose_name="Tipo notificação")  # Field name made lowercase.
//    id_local = models.ForeignKey(Local, verbose_name="Local", blank=True, null=True)  # Field name made lowercase.
//    descricao = models.CharField("Descrição", max_length=255)  # Field name made lowercase.
//    titulo = models.CharField("Título", max_length=45)  # Field name made lowercase.
//    servidor = models.ForeignKey(Servidor)  # Field name made lowercase.
//    remetente = models.ManyToManyField(Remetente)

    public CreateNotificationFragment() {
        // Required empty public constructor
        c = Calendar.getInstance();
        formatDate = new SimpleDateFormat("dd/MM/yyyy");
        //formatTime = new SimpleDateFormat("hh:mm");//12h
        formatTime = new SimpleDateFormat("kk:mm");//24h

        c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
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
            btnSave = (Button) view.findViewById(R.id.btnSendNotification);
            btnColor = (Button) view.findViewById(R.id.btnColor);
            btnRemetent = (Button) view.findViewById(R.id.btnRemetent);

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

            edtDescription = (EditText) view.findViewById(R.id.edtDescription);
            edtDescription.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent event) {
                    // TODO Auto-generated method stub
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

            // TODO: 3/12/2017 ADICIONAR SPINNER PARA FAZER FUNÇÃO DO COMBOBOX DOS ITENS!!!

            // TODO: 3/13/2017 utilizar lista para poder fazer a função do ajax! de ir filtrando!!!
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
                    Intent intent = new Intent(view.getContext(),RemetentListActivity.class);
                    startActivityForResult(intent, RESULT_REMETENT_ACTIVITY);
                }
            });
            // TODO: 3/18/2017 chcekbox nos itens da lista para representar o para quem será enviado 
            // TODO: 3/18/2017 acertar quadradinho representando a cor  
            // TODO: 3/18/2017 pensar no esquema do servidor igual ao que o professor tinha feito de pegar dados a partir do ultimo item reccebido apenas


            final ImageView shape =  (ImageView) view.findViewById(R.id.imvShape);
            //shape.setBackgroundColor(0xff9C27B0);
            btnColor.setOnClickListener(new View.OnClickListener(){
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
                        //shape.setBackground(back);
                        shape.setBackgroundColor(color);
                        Toast.makeText(v.getContext(), "oi", Toast.LENGTH_LONG).show();
                    }
                });
                //customize the dialog however you want
                dialog.show();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    submitForm();
                }
            });
        }
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        switch (requestCode){
            case RESULT_REMETENT_ACTIVITY:
                //String message=data.getStringExtra("MESSAGE");
                //textView1.setText(message);
                break;
            default:
                Log.d("TCC", "Activity Result wrong");
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {
//        if ((!validateEmail()) || (!validatePassword())) {
//            return;
//        }
        Toast.makeText(getActivity().getApplicationContext(), R.string.msg_changed, Toast.LENGTH_SHORT).show();
        getActivity().getFragmentManager().popBackStack();
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

        if (v == tvTime){
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (((mYear == c.get(Calendar.YEAR)) && (mMonth == c.get(Calendar.MONTH)) && (mDay == c.get(Calendar.DAY_OF_MONTH))) &&
                            ((hourOfDay < c.get(Calendar.HOUR_OF_DAY)) || ((hourOfDay == c.get(Calendar.HOUR_OF_DAY)) && (minute <= (c.get(Calendar.MINUTE) + 10))))) {
                            Toast.makeText(view.getContext(), "Set time at least 10 minutes from now", Toast.LENGTH_LONG).show();
                        } else {
                            //tvTime.setText(hourOfDay + ":" + minute);
                            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            c.set(Calendar.MINUTE, minute);
                            tvTime.setText(formatTime.format(c.getTime()));
                        }
                    }
                }, mHour, mMinute, true);
            timePickerDialog.show();
        }
    }
}