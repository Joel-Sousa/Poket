package com.example.poket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.conta.ListaConta;
import com.example.poket.view.despesa.ListaDespesa;
import com.example.poket.view.usuario.AdicionarUsuario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Intent intentEsqueceuSenha, intentCadastrarUsuario, intentLogin;
    TextView textViewEsqueceuSenha, textViewCadastrarUsuario;
    EditText editTextEmail, editTextSenha, editTextDt;
    Button buttonLogin, buttonES;
    DatePickerDialog picker;

    BarChart barChart;

    UsuarioDAO dao = new UsuarioDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(MainActivity.this, ListaDespesa.class);
//        startActivity(intent);

//        barChart = findViewById(R.id.barChartHomeDR);
//            graficoBarChartDespesaRenda();

        editTextEmail = findViewById(R.id.editTexMainActivityEmail);
        editTextSenha = findViewById(R.id.editTextMainActivitySenha);
        textViewEsqueceuSenha = findViewById(R.id.textViewMainActivityEsqueceuSenha);
        textViewCadastrarUsuario = findViewById(R.id.textViewMainActivityCadastrarUsuario);
        buttonLogin = findViewById(R.id.buttonMainActivityLogin);

        mock();

        textViewEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View view1 = getLayoutInflater().inflate(R.layout.dialog_esqueceusenha, null);

                final EditText editTextEmail = view1.findViewById(R.id.editTextDialogEsqueceuSenhaEmail);
                Button buttonAdicionar = view1.findViewById(R.id.buttonDialogEsqueceuSenhaEnviar);
                Button buttonVoltar = view1.findViewById(R.id.buttonDialogEsqueceuSenhaVoltar);

                mBuilder.setView(view1);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = editTextEmail.getText().toString();

                        if(email.length() == 0){
                            Utilitario.toast(getApplicationContext(), "Insira um email.");
                            editTextEmail.requestFocus();
                        }else if(!email.contains("@")){
                            Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
                            editTextEmail.requestFocus();
                        }else{
                            dao.resetSenha(editTextEmail.getText().toString(), MainActivity.this);
                            dialog.dismiss();
                        }
                    }
                });

                buttonVoltar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        activity.finish();
//                        activity.overridePendingTransition(0, 0);
//                        activity.startActivity(activity.getIntent());
//                        activity.overridePendingTransition(0, 0);
                        dialog.dismiss();
                    }
                });
            }
        });

        textViewCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCadastrarUsuario = new Intent(MainActivity.this , AdicionarUsuario.class);
                startActivity(intentCadastrarUsuario);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setEmail(editTextEmail.getText().toString().trim());
                dto.setSenha(editTextSenha.getText().toString());
                validaCampos(dto);
            }
        });

//         fb();
    }

    private void validaCampos(UsuarioDTO dto){
        if(dto.getEmail().length() == 0 && dto.getSenha().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL_SENHA);
            editTextEmail.requestFocus();
        }else if(dto.getEmail().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL);
            editTextEmail.requestFocus();
        }else if(!dto.getEmail().contains("@")){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
            editTextEmail.requestFocus();
        }else if(dto.getSenha().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.SENHA);
            editTextSenha.requestFocus();
        }else{
            dao.autenticarUsuario(dto, MainActivity.this);
        }
    }

    public void mock(){
        editTextEmail.setText("ana@email.com");
        editTextSenha.setText("123123");
//        editTextEmail.setText("joelfsousa95@gmail.com");
//        editTextSenha.setText("111222");
    }

    private void fb(){
        FirebaseFirestore db;
        FirebaseAuth mAuth;
        FirebaseUser user;

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        db.collection("conta").document("cXPEJ6kvlLMGIcTMEsHrFLNnG3E3")
                .collection("cXPEJ6kvlLMGIcTMEsHrFLNnG3E3")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(!value.isEmpty()){

                        List<String> list = new ArrayList<>();
                            String v = "";
                        for (QueryDocumentSnapshot e : value){
                            list.add(e.getData().get("valor").toString());
//                            Log.d("---", "tst");
                        }

                        for(String e : list){
                            Log.d("---", e);
                        }
                        }
                    }
                });

//        boolean b = mAuth.isSignInWithEmailLink("any@email.com");
//        mAuth. isSignInWithEmailLink("any@email.com");
//        Log.d("---", b+"");
    }
}