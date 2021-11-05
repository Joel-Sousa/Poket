package com.example.poket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.ContaDTO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.adapter.ContaAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.usuario.AdicionarUsuario;
import com.example.poket.view.usuario.EsqueceuSenha;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Intent intentEsqueceuSenha, intentCadastrarUsuario, intentLogin;
    TextView textViewEsqueceuSenha, textViewCadastrarUsuario;
    EditText editTextEmail, editTextSenha, editTextDt;
    Button buttonLogin;
    DatePickerDialog picker;

    BarChart barChart;

    UsuarioDAO dao = new UsuarioDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        barChart = findViewById(R.id.barChartHomeDR);
//            graficoBarChartDespesaRenda();

//        Map<Integer, Float> listaMes = new HashMap<>();
//        listaMes.put(1, 0.0f);
//
//        Log.d("---", listaMes.get(1).toString());
//        listaMes.put(1, listaMes.get(1)+5);
//        Log.d("---", listaMes.get(1).toString());

        editTextEmail = findViewById(R.id.editTexMainActivityEmail);
        editTextSenha = findViewById(R.id.editTextMainActivitySenha);
        textViewEsqueceuSenha = findViewById(R.id.textViewMainActivityEsqueceuSenha);
        textViewCadastrarUsuario = findViewById(R.id.textViewMainActivityCadastrarUsuario);
        buttonLogin = findViewById(R.id.buttonMainActivityLogin);

        mock();

        textViewEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextEmail.length() == 0){
                    Utilitario.toast(getApplicationContext(), Msg.EMAIL);
                    editTextEmail.requestFocus();
                }else if(!editTextEmail.getText().toString().trim().contains("@")){
                    Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
                    editTextEmail.requestFocus();
                }else{
                    dao.resetSenha(editTextEmail.getText().toString(), MainActivity.this);
                }
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

         fb();
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

    private void graficoBarChartDespesaRenda(){

//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        barEntries.add(new BarEntry(1,1));
//        barEntries.add(new BarEntry(3,2));
//        barEntries.add(new BarEntry(5,3));
//        barEntries.add(new BarEntry(7,4));
//        barEntries.add(new BarEntry(9,5));
//        barEntries.add(new BarEntry(11,6));
//        barEntries.add(new BarEntry(13,7));
//        barEntries.add(new BarEntry(15,8));
//        barEntries.add(new BarEntry(17,9));
//        barEntries.add(new BarEntry(19,10));
//        barEntries.add(new BarEntry(21,11));
//        barEntries.add(new BarEntry(23,12));
//
//        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
//        barEntries1.add(new BarEntry(2,1));
//        barEntries1.add(new BarEntry(4,2));
//        barEntries1.add(new BarEntry(6,3));
//        barEntries1.add(new BarEntry(8,4));
//        barEntries1.add(new BarEntry(10,5));
//        barEntries1.add(new BarEntry(12,6));
//        barEntries1.add(new BarEntry(14,7));
//        barEntries1.add(new BarEntry(16,8));
//        barEntries1.add(new BarEntry(18,9));
//        barEntries1.add(new BarEntry(20,10));
//        barEntries1.add(new BarEntry(22,11));
//        barEntries1.add(new BarEntry(24,12));

        ArrayList<BarEntry> barEntriesDespesa = new ArrayList<>();
        barEntriesDespesa.add(new BarEntry(1,1));
        barEntriesDespesa.add(new BarEntry(2,2));
        barEntriesDespesa.add(new BarEntry(3,3));
        barEntriesDespesa.add(new BarEntry(4,4));
        barEntriesDespesa.add(new BarEntry(5,5));
        barEntriesDespesa.add(new BarEntry(6,10));
        barEntriesDespesa.add(new BarEntry(7,7));
        barEntriesDespesa.add(new BarEntry(8,8));
        barEntriesDespesa.add(new BarEntry(9,9));
        barEntriesDespesa.add(new BarEntry(10,10));
        barEntriesDespesa.add(new BarEntry(11,11));
        barEntriesDespesa.add(new BarEntry(12,12));

        ArrayList<BarEntry> barEntriesRenda= new ArrayList<>();
        barEntriesRenda.add(new BarEntry(1,1));
        barEntriesRenda.add(new BarEntry(2,2));
        barEntriesRenda.add(new BarEntry(3,3));
        barEntriesRenda.add(new BarEntry(4,4));
        barEntriesRenda.add(new BarEntry(5,5));
        barEntriesRenda.add(new BarEntry(6,6));
        barEntriesRenda.add(new BarEntry(7,7));
        barEntriesRenda.add(new BarEntry(8,8));
        barEntriesRenda.add(new BarEntry(9,9));
        barEntriesRenda.add(new BarEntry(10,10));
        barEntriesRenda.add(new BarEntry(11,11));
        barEntriesRenda.add(new BarEntry(12,12));

        BarDataSet barDataSet = new BarDataSet(barEntriesDespesa, "Despesa");
        barDataSet.setColor(Color.RED);

        BarDataSet barDataSet1 = new BarDataSet(barEntriesRenda, "Renda");
        barDataSet1.setColor(Color.GREEN);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.addDataSet(barDataSet1);

        barChart.setData(barData);

        String[] mes = new String[]
                {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
                        "Ago", "Set", "Out", "Nov", "Dez"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(12);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.08f;
        float groupSpace = 0.30f;
        barData.setBarWidth(0.27f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(12);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0,groupSpace, barSpace);
        barChart.getDescription().setEnabled(false);

        barChart.invalidate();
    }
}