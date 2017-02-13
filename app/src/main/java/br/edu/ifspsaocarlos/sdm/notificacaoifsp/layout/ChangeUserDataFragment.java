package br.edu.ifspsaocarlos.sdm.notificacaoifsp.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeUserDataFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeUserDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeUserDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText inputEmail, inputPassword, inputConfirm;
    private TextInputLayout inputLayoutEmail, inputLayoutPassword, inputLayoutConfirm;
    private Button btnSignUp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChangeUserDataFragment() {
        // Required empty public constructor
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
        ChangeUserDataFragment fragment = new ChangeUserDataFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View root = inflater.inflate(R.layout.fragment_change_user_data, container, false);
        if (root != null){
            inputLayoutEmail = (TextInputLayout) root.findViewById(R.id.txtEmail);
            inputLayoutPassword = (TextInputLayout) root.findViewById(R.id.txtPassword);
            inputLayoutConfirm = (TextInputLayout) root.findViewById(R.id.txtConfirmPassword);
            inputEmail = (EditText) root.findViewById(R.id.edtEmail);
            inputPassword = (EditText) root.findViewById(R.id.edtPassword);
            inputConfirm = (EditText) root.findViewById(R.id.edtConfirmPassword);
            btnSignUp = (Button) root.findViewById(R.id.btnSend);

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
        return root;
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

    /**
     * Validating form
     */
    private void submitForm() {
        if ((!validateEmail()) || (!validatePassword())) {
            return;
        }
        Toast.makeText(getActivity().getApplicationContext(), R.string.msg_changed, Toast.LENGTH_SHORT).show();
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
        if ((password.isEmpty()) || (confirm.isEmpty()) || (password.length() < qtd) || (confirm.length() < qtd)){
            inputLayoutPassword.setError(getString(R.string.error_invalid_password));
            inputLayoutConfirm.setError(getString(R.string.error_invalid_password));
            requestFocus(inputPassword);
            return false;
        } else {
            if (password.equals(confirm)) {
                inputLayoutPassword.setErrorEnabled(false);
                inputLayoutConfirm.setErrorEnabled(false);
            }else{
                inputLayoutPassword.setError(getString(R.string.error_incorrect_password));
                inputLayoutConfirm.setError(getString(R.string.error_incorrect_password));
                requestFocus(inputPassword);
                return false;
            }
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
