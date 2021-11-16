package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.DTO.HistoricoPFDTO;
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
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

public class AdicionarPlanejamentoFinanceiro extends AppCompatActivity {

    ImageView imageViewVoltar;
    TextView textViewIdConta, textViewContaValor;
    EditText editTextNomePF, editTextValorAtual, editTextValorObjetivado,
    editTextDataInicial, editTextDataFinal;
    Spinner spinnerConta;
    Button buttonSalvar;
    DatePickerDialog picker;
    TextInputLayout textInputLayoutTipoPF;
    AutoCompleteTextView autoCompleteTextViewTipoPF;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
    PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
    HistoricoPFDTO hdto = new HistoricoPFDTO();

    String tipoPF = "";
    List<String> tipoPFList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_planejamento_financeiro);

        textViewIdConta = findViewById(R.id.textViewAdicionarPFidConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarPFConta);
        textViewContaValor= findViewById(R.id.textViewAdicionarPFContaValor);

        editTextNomePF = findViewById(R.id.editTextAdicionarPFNomePF);

        textInputLayoutTipoPF = findViewById(R.id.editTextAdicionarPFTipoPF);
        autoCompleteTextViewTipoPF = findViewById(R.id.dropdown_menu);

        editTextValorAtual = findViewById(R.id.editTextAdicionarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextAdicionarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextAdicionarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextAdicionarPFDataFinal);

        buttonSalvar = findViewById(R.id.buttonAdicionarPFSalvar);
        imageViewVoltar = findViewById(R.id.imageViewAdicionarPFVoltar);

//        Utilitario.listaTipoPF(spinnerTipoPF, tipoPF, getApplicationContext());

        editTextDataInicial.setText(Utilitario.dataAtual());
        editTextDataInicial.setInputType(InputType.TYPE_NULL);
        editTextDataFinal.setInputType(InputType.TYPE_NULL);

        tipoPFList = Arrays.asList("Curto Prazo", "Médio Prazo", "Longo Prazo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AdicionarPlanejamentoFinanceiro.this, R.layout.dropdown_item, tipoPFList);

        autoCompleteTextViewTipoPF.setAdapter(adapter);

        autoCompleteTextViewTipoPF.setInputType(InputType.TYPE_NULL);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarPlanejamentoFinanceiro.this, textViewContaValor, textViewIdConta, true);

        mock();

        autoCompleteTextViewTipoPF.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextViewTipoPF.getWindowToken(), 0);
            }
        });

        editTextDataInicial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextDataInicial.getWindowToken(), 0);
            }
        });
        editTextDataFinal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextDataFinal.getWindowToken(), 0);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto.setNomePF(editTextNomePF.getText().toString());
                dto.setTipoPF(autoCompleteTextViewTipoPF.getText().toString());
                dto.setValorAtual(Double.parseDouble(editTextValorAtual.getText().toString()));
                dto.setValorObjetivado(Double.parseDouble(editTextValorObjetivado.getText().toString()));
                dto.setDataInicial(editTextDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());

                hdto.setIdConta(textViewIdConta.getText().toString());
                hdto.setConta(spinnerConta.getSelectedItem().toString());
                hdto.setValorConta(Double.parseDouble(textViewContaValor.getText().toString()));

                hdto.setValorHistoricoPF(Double.parseDouble(editTextValorAtual.getText().toString()));

                validarConta(dto, hdto);
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editTextDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AdicionarPlanejamentoFinanceiro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int month = i1+1;

                        String day = i2<10 ? "0"+i2 : String.valueOf(i2);
                        String month1 = month<10 ? "0"+month : String.valueOf(month);

                        editTextDataInicial.setText(day + "/" + month1 + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        editTextDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AdicionarPlanejamentoFinanceiro.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        int month = i1+1;

                        String day = i2<10 ? "0"+i2 : String.valueOf(i2);
                        String month1 = month<10 ? "0"+month : String.valueOf(month);

                        editTextDataFinal.setText(day + "/" + month1 + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void validarConta(PlanejamentoFinanceiroDTO dto, HistoricoPFDTO hdto){


        if(dto.getNomePF().length() == 0 && dto.getValorAtual() == 0 &&
                dto.getValorObjetivado() == 0 && dto.getDataInicial().length() == 0 &&
                dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextNomePF.requestFocus();
        }else if(dto.getNomePF().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.NOME_PF);
            editTextNomePF.requestFocus();
        }else if(dto.getTipoPF().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_PF, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoPF.requestFocus();
        }else if(!tipoPFList.contains(dto.getTipoPF())){
            Toast.makeText(getApplicationContext(), Msg.TIPO_PF, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoPF.setText("");
            autoCompleteTextViewTipoPF.requestFocus();
        }else if(dto.getValorAtual() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ATUAL);
            editTextValorAtual.requestFocus();
        }else if(dto.getValorAtual().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorAtual.requestFocus();
        }else if(Double.valueOf(dto.getValorAtual()) > Double.valueOf(hdto.getValorConta())){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_MAIOR_PF);
            editTextValorAtual.requestFocus();
            editTextValorAtual.setText("");
        }else if(dto.getValorObjetivado().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getValorObjetivado() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getDataInicial().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_INICIAL);
            editTextDataInicial.requestFocus();
        }else if(dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL);
            editTextDataFinal.requestFocus();
        }else if(Utilitario.comparaDatas(editTextDataFinal.getText().toString() , Utilitario.dataAtual())){
            Utilitario.toast(getApplicationContext(), Msg.DATA_ATUAL);
            editTextDataFinal.requestFocus();
            editTextDataFinal.setText("");
        }else if(editTextDataFinal.getText().toString().equals(Utilitario.dataAtual())){
            Utilitario.toast(getApplicationContext(), Msg.DATA_IGUAL);
            editTextDataFinal.requestFocus();
            editTextDataFinal.setText("");
        }else{
            dto.setDataInicial(Utilitario.convertBrToUsa(dto.getDataInicial()));
            dto.setDataFinal(Utilitario.convertBrToUsa(dto.getDataFinal()));
            dao.cadastrarPlanejamentoFinanceiro(dto, hdto, AdicionarPlanejamentoFinanceiro.this);
        }
    }

    public void mock(){
        editTextNomePF.setText("viajar");
        editTextValorAtual.setText("10");
        autoCompleteTextViewTipoPF.setText("Médio Prazo", false);
        editTextValorObjetivado.setText("900");
        editTextDataFinal.setText("12/12/2021");
    }
}