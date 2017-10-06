package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity.MainActivity;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Person;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.util.Connection;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeUserDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeUserDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserDataFragment extends TemplateFragment{
    private EditText inputEmail, inputPassword, inputConfirm, inputUsername, inputFirstName, inputLastName;
    private EditText inputJob, inputGrade, inputGraduation;
    private Spinner inputEducation;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutConfirm;
    private TextInputLayout inputLayoutGraduation, inputLayoutEducation, inputLayoutGrade, inputLayoutJob;
    private TextView txtInDate;
    private Button btnSignUp;
    private RadioButton inputMale, inputFemale;

    private ArrayAdapter<String> spinnerAdapter;
    private Person person;

    private Calendar c;
    private SimpleDateFormat formatDate;
    private int mYear, mMonth, mDay;

    private final Realm realm = Realm.getDefaultInstance();

    public ChangeUserDataFragment(){

    }

    public ChangeUserDataFragment(Context cxt) {
        person = null;

        // Required empty public constructor
        //formatDate = new SimpleDateFormat(getString(R.string.mask_date));
        String dateMask = cxt.getString(R.string.mask_date);
        formatDate = new SimpleDateFormat(dateMask);

        c = Calendar.getInstance();
        c.add(Calendar.YEAR, -70);
        mYear = c.get(Calendar.YEAR) + 70;
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeUserDataFragment.
     */
    // TODO: Rename and change types and number of parameters
    // public static ChangeUserDataFragment newInstance(String param1, String param2) {
    public static ChangeUserDataFragment newInstance(Context context, Bundle args) {
        ChangeUserDataFragment fragment = new ChangeUserDataFragment(context);
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_user_data, container, false);
        if (view != null){
            inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.txtEmail);
            inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.txtPassword);
            inputLayoutConfirm = (TextInputLayout) view.findViewById(R.id.txtConfirmPassword);
            inputUsername = (EditText) view.findViewById(R.id.edtUsername);
            inputFirstName = (EditText) view.findViewById(R.id.edtName);
            inputLastName = (EditText) view.findViewById(R.id.edtLastName);
            inputEmail = (EditText) view.findViewById(R.id.edtEmail);
            inputPassword = (EditText) view.findViewById(R.id.edtPassword);
            inputConfirm = (EditText) view.findViewById(R.id.edtConfirmPassword);
            inputMale = (RadioButton) view.findViewById(R.id.rbtMale);
            inputFemale = (RadioButton) view.findViewById(R.id.rbtFemale);
            btnSignUp = (Button) view.findViewById(R.id.btnSend);

            txtInDate = (TextView) view.findViewById(R.id.txtInDate);
            txtInDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickMethod(view);
                }
            });

            inputLayoutGraduation = (TextInputLayout) view.findViewById(R.id.txtFormacao);
            inputLayoutEducation = (TextInputLayout) view.findViewById(R.id.txtTipoFormacao);
            inputLayoutGrade = (TextInputLayout) view.findViewById(R.id.txtTurma);
            inputLayoutJob = (TextInputLayout) view.findViewById(R.id.txtFuncao);

            inputJob = (EditText) view.findViewById(R.id.edtFuncao);
            inputGrade = (EditText) view.findViewById(R.id.edtTurma);
            inputGraduation = (EditText) view.findViewById(R.id.edtFormacao);
            inputEducation = (Spinner) view.findViewById(R.id.spnTipoFormacao);

            spinnerAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.tipoFormacao));

            inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
            inputConfirm.addTextChangedListener(new MyTextWatcher(inputConfirm));

            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submitForm();
                }
            });
        }
        return view;
    }

    public void onClickMethod(View v) {
        if (v == txtInDate) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            if (year > (mYear - 10)){
                                Toast.makeText(view.getContext(), "Set year before " + (mYear - 10), Toast.LENGTH_LONG).show();
                            }else{
                                Date date = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                                txtInDate.setText(formatDate.format(date));
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.show();
        }
    }

    public void onStop(){
        super.onStop();
        stopTransaction();
    }

    @Override
    public void onResume(){
        super.onResume();


        //Realm realm = Realm.getDefaultInstance();
        person = realm.where(Person.class).equalTo("pk", MainActivity.getUserId()).findFirst();

        if (person != null){
            switch (person.getEnumType()){
                case ENUM_EMPLOYEE:
                    inputLayoutJob.setVisibility(View.VISIBLE);
                    inputJob.setText(person.getFuncao());
                    break;
                case ENUM_PROFESSOR:
                    inputLayoutEducation.setVisibility(View.VISIBLE);
                    inputLayoutGraduation.setVisibility(View.VISIBLE);
                    inputGraduation.setText(person.getFormacao());

                    inputEducation.setAdapter(spinnerAdapter);

                    String[] myResArray = getResources().getStringArray(R.array.tipoFormacaoBR);
                    List<String> myResArrayList = Arrays.asList(myResArray);

                    inputEducation.setSelection(myResArrayList.indexOf(person.getTipo_formacao()));
                    break;
                case ENUM_STUDENT:
                    inputLayoutGrade.setVisibility(View.VISIBLE);
                    inputGrade.setText(person.getTurma());
                    break;
            }

            inputFirstName.setText(person.getFirst_name());
            inputLastName.setText(person.getLast_name());
            inputUsername.setText(person.getUsername());
            inputEmail.setText(person.getEmail());
            if (person.getSexo().equals(getString(R.string.json_female))){
                inputFemale.setChecked(true);
                inputMale.setChecked(false);
            }else{
                inputFemale.setChecked(false);
                inputMale.setChecked(true);
            }
            txtInDate.setText(person.getDatanascimento());

            Log.d("TCC", person.toString());
            //Toast.makeText(this.getContext(), person.toString(), Toast.LENGTH_LONG).show();
        }

    }

    //private boolean myFinish = false;
    //private void setMyFinish(boolean flag){
    //    this.myFinish = flag;
    //}
    private boolean loadNewData() {
        boolean myFinish = false;
        if (person != null){
            //Não pode alterar um objeto realm fora de uma transaction!!!
            realm.beginTransaction();
            try {
                switch (person.getEnumType()) {
                    case ENUM_EMPLOYEE:
                        if (validateText(inputJob, inputLayoutJob) == true) {
                            person.setFuncao(inputJob.getText().toString());
                            //setMyFinish(true);
                            myFinish = true;
                        }
                        break;
                    case ENUM_PROFESSOR:
                        if (validateText(inputGraduation, inputLayoutGraduation) == true) {
                            person.setFormacao(inputGraduation.getText().toString());
                            String[] myResArray = getResources().getStringArray(R.array.tipoFormacaoBR);
                            List<String> myResArrayList = Arrays.asList(myResArray);
                            person.setTipo_formacao(myResArrayList.get(inputEducation.getSelectedItemPosition()));
                            //setMyFinish(true);
                            myFinish = true;
                        }
                        break;
                    case ENUM_STUDENT:
                        //not used
                        //setMyFinish(true);
                        myFinish = true;
                        break;
                }

                if (myFinish == true) {
                    if (inputPassword.getText().toString().isEmpty()){
                        person.setPassword("0"); //Nenhum campo pode ficar nulo na hora de enviar
                    }
                    else {
                        person.setPassword(inputPassword.getText().toString());
                    }

                    person.setEmail(inputEmail.getText().toString());
                    if (inputFemale.isChecked() == true) {
                        person.setSexo(getString(R.string.json_female));
                    } else {
                        person.setSexo(getString(R.string.json_male));
                    }

                    //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    //SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                    //Date data = formato.parse(txtInDate.getText().toString());
                    //person.setDatanascimento(data);
                    person.setDatanascimento(txtInDate.getText().toString());

                    Log.d("TCC", "New Person: " + person.toString());
                    //Toast.makeText(this.getContext(), person.toString(), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                //setMyFinish(true);
                myFinish = false;
                stopTransaction();
                Log.d("TCC", "ERROR: " + e.toString());
            }

            //Realm realm = Realm.getDefaultInstance();
//            realm.executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    try {
//                    //DESTA MANEIRA ELE JÁ ALTERA NO BANCO DIRETAMENTE!
//                            person.setDatanascimento(txtInDate.getText().toString());
//
//                            Log.d("TCC", "New Person: " + person.toString());
//                            //Toast.makeText(this.getContext(), person.toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }catch (Exception e){
//                        setMyFinish(false);
//                        Log.d("TCC", "ERROR: " + e.toString());
//                    }
//                }
//            });
        }
        return myFinish;
    }

    private void stopTransaction() {
        if(realm.isInTransaction()) {
            realm.cancelTransaction();
        }
    }

    /**
     * Validating form
     */
    private void submitForm() {
        boolean flagConnected = Connection.connectionVerify(getContext());
        if (flagConnected == true) {
            boolean flagEmail = validateEmail();
            boolean flagPassword = validatePassword();
            if (flagEmail & flagPassword == true) {
                if (loadNewData() == true) {

                    String url = getString(R.string.url_base);

                    boolean flagOk = true;

                    switch (MainActivity.getPeronType()) {
                        case ENUM_STUDENT:
                            url += getString(R.string.url_student);
                            break;
                        case ENUM_EMPLOYEE:
                            url += getString(R.string.url_employee);
                            break;
                        case ENUM_PROFESSOR:
                            url += getString(R.string.url_professor);
                            break;
                        default:
                            Log.e("TCC", "Tipo pessoa desconhecida");
                            flagOk = false;
                    }

                    if (flagOk == true) {
                        url += Integer.toString(person.getPk()) + "/"; //PUT to update is necessary this last slash!!!

                        try {
                            //Gson gson = new Gson();
                            Gson gson = new GsonBuilder()
                                    .setExclusionStrategies(new ExclusionStrategy() {
                                        @Override
                                        public boolean shouldSkipField(FieldAttributes f) {
                                            return f.getDeclaringClass().equals(RealmObject.class);
                                        }

                                        @Override
                                        public boolean shouldSkipClass(Class<?> clazz) {
                                            return false;
                                        }
                                    })
                                    .create();

                            //Realm realm = Realm.getDefaultInstance();
                            //Person managedModel = realm.copyFromRealm(person);
                            String json = gson.toJson(realm.copyFromRealm(person));
                            JSONObject user = new JSONObject(json);

                            try {
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, user,
                                        new Response.Listener<JSONObject>() {
                                            public void onResponse(JSONObject s) {
                                                realm.commitTransaction();
                                                Toast.makeText(getContext(), R.string.msg_changed,
                                                        Toast.LENGTH_SHORT).show();
                                                getActivity().getFragmentManager().popBackStack();
                                            }
                                        }, new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError volleyError) {
                                        stopTransaction();
                                        Toast.makeText(getContext(), "Error during update user.",
                                                Toast.LENGTH_SHORT).show();
                                        Log.e("TCC", "Error during update user." + volleyError.toString());
                                    }
                                }) {
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                        Map<String, String> headers = new HashMap<>();
                                        // Basic Authentication
                                        //String auth = "Basic " + Base64.encodeToString(CONSUMER_KEY_AND_SECRET.getBytes(), Base64.NO_WRAP);
                                        headers.put("Content-Type", "application/json");
                                        headers.put(getString(R.string.authorization), MainActivity.getAuth()); //Authorization Token ...
                                        return headers;
                                    }
                                };

//                                VERIFICAR ERRO!!!!! --> problema variaveis do backend!!!
//                                09-21 05:11:25.233 11000-11477/br.edu.ifspsaocarlos.sdm.notificacaoifsp E/Volley: [237] BasicNetwork.performRequest: Unexpected response code 400 for http://192.168.0.16:8000/professor_json/5/
//                                09-21 05:11:25.261 11000-11000/br.edu.ifspsaocarlos.sdm.notificacaoifsp E/TCC: Error during update user.com.android.volley.ServerError

                                queue.add(jsonObjectRequest);
                            } catch (Exception e) {
                                stopTransaction();
                                Log.e("TCC", "Erro na leitura de mensagens");
                                Toast.makeText(getContext(), "Error during update user: " + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            stopTransaction();
                            Log.e("TCC", "ERROR: " + e.toString());
                        }
                    }
                }
            }
        }else{
            stopTransaction();
            Toast.makeText(getContext(), "Without connection!",
                    Toast.LENGTH_SHORT).show();
        }
    }
/*
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        if (checked) {
            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.rbtMale:
                    inputFemale.setChecked(false);
                        break;
                case R.id.rbtFemale:
                    inputMale.setChecked(false);
                        break;
                default:
                    Log.d("TCC", "Sex optiion wrong");
            }
        }
    }
*/
    private boolean validateText(EditText edit, TextInputLayout input) {
        if (edit != null) {
            String texto = edit.getText().toString().trim();

            if (texto.isEmpty()) {
                input.setError(getString(R.string.error_invalid_text));
                requestFocus(input);
                return false;
            } else {
                input.setErrorEnabled(false);
            }

            return true;
        }
        return false;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.error_invalid_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        String password = inputPassword.getText().toString().trim();
        String confirm = inputConfirm.getText().toString().trim();
        int qtd = getResources().getInteger(R.integer.qtd_caractere_pass);
        boolean ok;

        if ((password.isEmpty()) && (confirm.isEmpty())){
            //caso não queira trocar a senha
            return true;
        }

        if ((password.isEmpty()) || (confirm.isEmpty()) || (password.length() < qtd) || (confirm.length() < qtd)){
            inputLayoutPassword.setError(getString(R.string.error_invalid_password));
            inputLayoutConfirm.setError(getString(R.string.error_invalid_password));
            requestFocus(inputPassword);
            ok = false;
        } else {
            if (password.equals(confirm)) {
                inputLayoutPassword.setErrorEnabled(false);
                inputLayoutConfirm.setErrorEnabled(false);
                ok = true;
            }else{
                inputLayoutPassword.setError(getString(R.string.error_incorrect_password));
                inputLayoutConfirm.setError(getString(R.string.error_incorrect_password));
                requestFocus(inputPassword);
                ok = false;
            }
        }

        return ok;
    }

    private static boolean isValidEmail(String email) {
        boolean ok = !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        return ok;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            //event to validate every caractere's change
            switch (view.getId()) {
                case R.id.edtEmail:
                    //validateEmail();
                    break;
                case R.id.edtPassword:
                case R.id.edtConfirmPassword:
                    //validatePassword();
                    break;
            }
        }
    }
}