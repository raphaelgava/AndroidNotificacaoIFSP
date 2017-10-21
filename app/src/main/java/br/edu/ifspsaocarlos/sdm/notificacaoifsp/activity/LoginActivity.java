package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;

import static android.Manifest.permission.INTERNET;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    // UI references.
    private AutoCompleteTextView mProntuarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
//    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mProntuarioView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        Button btnSignInButton = (Button) findViewById(R.id.btnSign_in_button);
        btnSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (requestInternet()) {
//            serviceIntent = new Intent(this, FetchJSONService.class);
//            startService(serviceIntent);
            Log.d("TCC", "internet fine");
        }
    }

    /**
     * Só vai precisar dessa função se houver necessidade de alguma outra permissão (INTERNET) e liberado.
     * @return
     */
    private boolean requestInternet() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(INTERNET) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(INTERNET)) {
            Snackbar.make(mProntuarioView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{INTERNET}, 0);
                        }
                    });
        } else {
            requestPermissions(new String[]{INTERNET}, 0);
        }
        return false;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mProntuarioView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String prontuario = mProntuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(prontuario)) {
            mProntuarioView.setError(getString(R.string.error_field_required));
            focusView = mProntuarioView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            showProgress(true);

            // perform the user login attempt.
            String url = getString(R.string.url_base) + getString(R.string.url_login);
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(getString(R.string.url_login_username), prontuario);
            params.put(getString(R.string.url_login_password), password);
            JSONObject user = new JSONObject(params);

            try {
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, user,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject s) {
                                showProgress(false);
                                try {
                                    // TODO: 4/2/2017 O maps pra inserir um marker tem que pressionar e segurar!!!

                                    if ((s.has(getString(R.string.json_token)) && (!s.isNull(getString(R.string.json_token)))) &&
                                        (s.has(getString(R.string.json_id)) && (!s.isNull(getString(R.string.json_id)))) &&
                                        (s.has(getString(R.string.json_group)) && (!s.isNull(getString(R.string.json_group)))) &&
                                        (s.has(getString(R.string.json_prof)) && (!s.isNull(getString(R.string.json_prof))))){

                                        String token;
                                        Integer id;
                                        String group;
                                        Boolean prof;

                                        token = s.getString(getString(R.string.json_token));
                                        id = s.getInt(getString(R.string.json_id));
                                        group = s.getString(getString(R.string.json_group));
                                        prof = s.getBoolean(getString(R.string.json_prof));

                                        Intent inicio = new Intent(LoginActivity.this, MainActivity.class);
                                        inicio.putExtra(getString(R.string.json_token), token);
                                        inicio.putExtra(getString(R.string.json_id), id);
                                        inicio.putExtra(getString(R.string.json_group), group);
                                        inicio.putExtra(getString(R.string.json_prof), prof);
                                        inicio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(inicio);
                                    }else{
                                        Log.e("SDM", "nas entradas do user");
                                        Toast.makeText(LoginActivity.this, "Missing some user data or you are inactive",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginActivity.this, "Error during receive json data: " + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }catch(Exception e){
                                    Toast.makeText(LoginActivity.this, "Error during receive data: " + e.toString(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this, "Error during authenticate user.",
                                Toast.LENGTH_SHORT).show();
                        showProgress(false);
                    }
                });
                queue.add(jsonObjectRequest);
            } catch (Exception e) {
                Log.e("SDM", "Erro na leitura de mensagens");
                Toast.makeText(LoginActivity.this, "Error during sending authentication: " + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= getResources().getInteger(R.integer.qtd_caractere_pass);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
