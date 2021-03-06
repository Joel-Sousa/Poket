package com.example.poket.view.renda;

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
import com.example.poket.DAO.RendaDAO;
import com.example.poket.DTO.RendaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.AdicionarDespesa;
import com.example.poket.view.despesa.EditarDespesa;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AdicionarRenda extends AppCompatActivity {

    EditText editTextRenda, editTextValorRenda, editTextDataRenda, editTextObservacao;
    TextView textViewIdConta, textViewValorConta;
    Spinner spinnerConta;
    ImageView imageViewVoltar;
    Button buttonSalvar;
    DatePickerDialog picker;
    TextInputLayout textInputLayoutTipoRenda;
    AutoCompleteTextView autoCompleteTextViewTipoRenda;

    RendaDTO dto = new RendaDTO();
    RendaDAO dao = new RendaDAO();

    List<String> tipoRendaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_renda);

        editTextRenda = findViewById(R.id.editTextAdicionarRendaRenda);
        editTextValorRenda = findViewById(R.id.editTextAdicionarRendaValorRenda);

        textInputLayoutTipoRenda = findViewById(R.id.editTextAdicionarRendaTipoRenda);
        autoCompleteTextViewTipoRenda = findViewById(R.id.dropdown_menu);

        editTextDataRenda = findViewById(R.id.editTextAdicionarRendaDataRenda);
        editTextObservacao = findViewById(R.id.editTextAdicionarRendaObservacao);

        textViewIdConta = findViewById(R.id.textViewAdicionarRendaIdConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarRendaConta);
        textViewValorConta = findViewById(R.id.textViewAdicionarRendaValorConta);

        imageViewVoltar = findViewById(R.id.imageViewAdidionarRendaVoltar);
        buttonSalvar = findViewById(R.id.buttonAdicionarRendaSalvar);

        editTextDataRenda.setText(Utilitario.dataAtual());
        editTextDataRenda.setInputType(InputType.TYPE_NULL);

        tipoRendaList = Arrays.asList("Sal??rio", "Servi??os", "Presente", "Aluguel", "Outros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AdicionarRenda.this, R.layout.dropdown_item, tipoRendaList);

        autoCompleteTextViewTipoRenda.setAdapter(adapter);

        autoCompleteTextViewTipoRenda.setInputType(InputType.TYPE_NULL);

        editTextDataRenda.setInputType(InputType.TYPE_NULL);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarRenda.this, textViewValorConta, textViewIdConta, false);

        autoCompleteTextViewTipoRenda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextViewTipoRenda.getWindowToken(), 0);
            }
        });

        editTextDataRenda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextDataRenda.getWindowToken(), 0);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int renda = editTextRenda.getText().toString().length();
                int valorRenda = editTextValorRenda.getText().toString().length();
                int tipoRenda = autoCompleteTextViewTipoRenda.getText().toString().length();
                int dataRenda = editTextRenda.getText().toString().length();

                if(renda == 0 && valorRenda == 0 && dataRenda == 0 ) {
                    Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
                    editTextRenda.requestFocus();
                }else if(renda == 0){
                    Utilitario.toast(getApplicationContext(), Msg.RENDA);
                    editTextRenda.requestFocus();
                }else if(valorRenda == 0){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_RENDA);
                    editTextValorRenda.requestFocus();
                }else if(editTextValorRenda.getText().toString().equals("0")){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
                    editTextValorRenda.requestFocus();
                    editTextValorRenda.setText("");
                }else if(tipoRenda == 0){
                    Toast.makeText(getApplicationContext(), Msg.TIPO_RENDA, Toast.LENGTH_LONG).show();
                    autoCompleteTextViewTipoRenda.requestFocus();
                }else if(dataRenda == 0){
                    Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
                    editTextDataRenda.requestFocus();
                }else{

                    dto.setRenda(editTextRenda.getText().toString());
                    dto.setValorRenda(Double.parseDouble(editTextValorRenda.getText().toString()));
                    dto.setTipoRenda(autoCompleteTextViewTipoRenda.getText().toString());
                    dto.setDataRenda(editTextDataRenda.getText().toString());
                    dto.setObservacao(editTextObservacao.getText().toString());

                    dto.setIdConta(textViewIdConta.getText().toString());
                    dto.setConta(spinnerConta.getSelectedItem().toString());
                    dto.setValorConta(Double.parseDouble(textViewValorConta.getText().toString()));
                    dto.setDataRenda(Utilitario.convertBrToUsa(dto.getDataRenda()));

                    dao.cadastarRenda(dto, AdicionarRenda.this);
                }
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextDataRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AdicionarRenda.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int month = i1+1;

                        String day = i2<10 ? "0"+i2 : String.valueOf(i2);
                        String month1 = month<10 ? "0"+month : String.valueOf(month);

                        editTextDataRenda.setText(day + "/" + month1 + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }
}