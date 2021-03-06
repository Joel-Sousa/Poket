package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AdicionarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextValorDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewIdConta, textViewValorConta;
    Spinner spinnerConta;
    ImageView imageViewVoltar;
    Button buttonSalvar;
    DatePickerDialog picker;

    TextInputLayout textInputLayoutTipoDespesa;
    AutoCompleteTextView autoCompleteTextViewTipoDespesa;

    DespesaDAO dao = new DespesaDAO();
    DespesaDTO dto = new DespesaDTO();

    String tipoPF = "";
    List<String> tipoDespesaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);

        editTextDespesa = findViewById(R.id.editTextAdicionarDespesaDespesa);
        editTextValorDespesa = findViewById(R.id.editTextAdicionarDespesaValorDespesa);

        textInputLayoutTipoDespesa = findViewById(R.id.editTextAdicionarDespesaTipoDespesa);
        autoCompleteTextViewTipoDespesa = findViewById(R.id.dropdown_menu);

        editTextDataDespesa = findViewById(R.id.editTextAdicionarDespesaDataDespesa);
        editTextObservacao = findViewById(R.id.editTextAdicionarDespesaObservacao);

        textViewIdConta = findViewById(R.id.textViewAdicionarDespesaIdConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarDespesaConta);
        textViewValorConta = findViewById(R.id.textViewAdicionarDespesaValorConta);

        imageViewVoltar = findViewById(R.id.imageViewAdicionarDespesaVoltar);
        buttonSalvar = findViewById(R.id.buttonAdicionarDespesaSalvar);

        editTextDataDespesa.setText(Utilitario.dataAtual());
        editTextDataDespesa.setInputType(InputType.TYPE_NULL);


        tipoDespesaList = Arrays.asList("Alimenta????o", "Ve??culo", "Moradia", "Lazer", "Outros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AdicionarDespesa.this, R.layout.dropdown_item, tipoDespesaList);

        autoCompleteTextViewTipoDespesa.setAdapter(adapter);

        autoCompleteTextViewTipoDespesa.setInputType(InputType.TYPE_NULL);

        editTextDataDespesa.setInputType(InputType.TYPE_NULL);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarDespesa.this, textViewValorConta, textViewIdConta, true);

        autoCompleteTextViewTipoDespesa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextViewTipoDespesa.getWindowToken(), 0);
            }
        });

        editTextDataDespesa.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextDataDespesa.getWindowToken(), 0);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int despesa = editTextDespesa.getText().toString().length();
                int valorDespesa = editTextValorDespesa.getText().toString().length();
                int tipoDespesa = autoCompleteTextViewTipoDespesa.getText().toString().length();
                int dataDespesa = editTextDespesa.getText().toString().length();

                if(despesa == 0 && valorDespesa == 0 &&
                        dataDespesa == 0 ) {
                    Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
                    editTextDespesa.requestFocus();
                }else if(despesa == 0){
                    Utilitario.toast(getApplicationContext(), Msg.DESPESA);
                    editTextDespesa.requestFocus();
                }else if(valorDespesa == 0){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_DESPESA);
                    editTextValorDespesa.requestFocus();
                }else if(editTextValorDespesa.getText().toString().equals("0")){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
                    editTextValorDespesa.requestFocus();
                    editTextValorDespesa.setText("");
                }else if(Double.valueOf(editTextValorDespesa.getText().toString()) > Double.valueOf(textViewValorConta.getText().toString())){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_MAIOR);
                    editTextValorDespesa.requestFocus();
                    editTextValorDespesa.setText("");
                }else if(tipoDespesa == 0){
                    Toast.makeText(getApplicationContext(), Msg.TIPO_DESPESA, Toast.LENGTH_LONG).show();
                    autoCompleteTextViewTipoDespesa.requestFocus();
                }else if(dataDespesa == 0){
                    Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA);
                    editTextDataDespesa.requestFocus();
                }else{
                    dto.setDespesa(editTextDespesa.getText().toString());
                    dto.setValorDespesa(Double.parseDouble(editTextValorDespesa.getText().toString()));
                    dto.setTipoDespesa(autoCompleteTextViewTipoDespesa.getText().toString());
                    dto.setDataDespesa(editTextDataDespesa.getText().toString());
                    dto.setObservacao(editTextObservacao.getText().toString());

                    dto.setIdConta(textViewIdConta.getText().toString());
                    dto.setConta(spinnerConta.getSelectedItem().toString());
                    dto.setValorConta(Double.parseDouble(textViewValorConta.getText().toString()));
                    dto.setDataDespesa(Utilitario.convertBrToUsa(dto.getDataDespesa()));

                    dao.cadastarDespesa(dto, AdicionarDespesa.this);
                }
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextDataDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AdicionarDespesa.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int month = i1+1;
                        String day = i2<10 ? "0"+i2 : String.valueOf(i2);
                        String month1 = month<10 ? "0"+month : String.valueOf(month);

                        editTextDataDespesa.setText(day + "/" + month1 + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });

    }
}