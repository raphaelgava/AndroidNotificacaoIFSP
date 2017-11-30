package br.edu.ifspsaocarlos.sdm.notificacaoifsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import br.edu.ifspsaocarlos.sdm.notificacaoifsp.R;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.MyClass;
import br.edu.ifspsaocarlos.sdm.notificacaoifsp.model.Offering;
import io.realm.Realm;

public class CreateOffering extends AppCompatActivity {

    private EditText edtTime, edtWeek, edtYear, edtQtd, edtTitle, edtInitials, edtClass;
    private Button btnGoBack;
    private TextInputLayout txtClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offering);

        Intent i = getIntent();
        Gson gson = new Gson();
        Bundle bundle = i.getExtras();
        Offering obj;
        obj = gson.fromJson(bundle.getString("oferecimento"), Offering.class);
        if (obj != null) {
            Log.d("TCC", obj.toString());

            edtTime = (EditText) findViewById(R.id.edtTime);
            edtYear = (EditText) findViewById(R.id.edtYear);
            edtWeek = (EditText) findViewById(R.id.edtWeek);
            edtQtd = (EditText) findViewById(R.id.edtQtd);
            edtTitle = (EditText) findViewById(R.id.edtTitle);
            edtInitials = (EditText) findViewById(R.id.edtInitials);
            edtClass = (EditText) findViewById(R.id.edtClass);
            btnGoBack = (Button) findViewById(R.id.btnGoBack);
            txtClass = (TextInputLayout) findViewById(R.id.txtClass);

            switch (obj.getTime()){
                case 1: edtTime.setText("First"); break;
                case 2: edtTime.setText("Second"); break;
                case 3: edtTime.setText("Third"); break;
                case 4: edtTime.setText("Fourth"); break;
                case 5: edtTime.setText("Fifth"); break;
            }

            switch (obj.getWeek()){
                case 1: edtWeek.setText("Monday"); break;
                case 2: edtWeek.setText("Tuesday"); break;
                case 3: edtWeek.setText("Wednesday"); break;
                case 4: edtWeek.setText("Thursday"); break;
                case 5: edtWeek.setText("Friday"); break;
            }

            edtYear.setText(Integer.toString(obj.getAno()));
            edtQtd.setText(Integer.toString(obj.getQtd()));
            edtTitle.setText(obj.getDescricao());

            Realm realm = Realm.getDefaultInstance();
            MyClass c = realm.where(MyClass.class).equalTo("pk", obj.getId_curso()).findFirst();
            if (c != null){
                edtClass.setText(c.getDescricao());
            }else{
                edtClass.setVisibility(View.GONE);
                txtClass.setVisibility(View.GONE);
            }
            edtInitials.setText(obj.getSigla());

            btnGoBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }else{
            finish();
        }
    }
}
