package com.example.poket.view.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.poket.R;
import com.example.poket.util.Utilitario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EsqueceuSenha extends AppCompatActivity {

    EditText editTextEmail;
    Button button;
    Spinner spinner;

    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        spinner = findViewById(R.id.spinnerTst);

        Utilitario.listaTipoDespesa(spinner, "", getApplicationContext());



//        textInputLayout = findViewById(R.id.text_input_layout);
//        autoCompleteTextView = findViewById(R.id.dropdown_menu);
//        button = findViewById(R.id.buttonTst);

//        List<String> nome = Arrays.asList("one", "two", "three");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                EsqueceuSenha.this, R.layout.dropdown_item, nome);

//        autoCompleteTextView.setAdapter(adapter);

//        autoCompleteTextView.setText("two", false);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                String ed =  autoCompleteTextView.getText().toString();
////
////                if(!nome.contains(ed)){
////
////                    Log.d("----", "Selecione um item da lista");
////                    autoCompleteTextView.setText("");
////                    autoCompleteTextView.requestFocus();
////                }
//////
////                for(String e : nome){
////                  if(ed.equals(e)){
//////                    autoCompleteTextView.setText("");
////                  }
////                 }
//            }
//        });
    }
}