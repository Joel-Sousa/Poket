package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.renda.EditarRenda;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditarPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewIdPF, textViewValorAtual;
    EditText editTextNomePF, editTextValorObjetivado, editTextDataFinal, editTextDataInicial;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;
    DatePickerDialog picker;
    TextInputLayout textInputLayoutTipoPF;
    AutoCompleteTextView autoCompleteTextViewTipoPF;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    String tipoPF = "";
    List<String> tipoPFList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_planejamento_financeiro);

        textViewIdPF = findViewById(R.id.textViewEditarPFId);

        editTextNomePF = findViewById(R.id.editTextEditarPFNomePF);

        textInputLayoutTipoPF = findViewById(R.id.editTextEditarPFTipoPF);
        autoCompleteTextViewTipoPF = findViewById(R.id.dropdown_menu);

        textViewValorAtual = findViewById(R.id.textViewEditarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextEditarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextEditarPFDataInicial);
//        textViewDataInicial = findViewById(R.id.textViewEditarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextEditarPFDataFinal);

        buttonEditar = findViewById(R.id.buttonEditarPFEditar);
        buttonExcluir = findViewById(R.id.buttonEditarPFExcluir);
        imageViewVoltar = findViewById(R.id.imageViewEditarPFVoltar);

        Intent intent = getIntent();
        textViewIdPF.setText(intent.getStringExtra("idPF"));
        editTextNomePF.setText(intent.getStringExtra("nomePF"));
        tipoPF = intent.getStringExtra("tipoPF");
        textViewValorAtual.setText(intent.getStringExtra("valorAtual"));
        editTextValorObjetivado.setText(intent.getStringExtra("valorObjetivado"));
        editTextDataInicial.setText(intent.getStringExtra("dataInicio"));
        editTextDataFinal.setText(intent.getStringExtra("dataFinal"));


        tipoPFList = Arrays.asList("Curto Prazo", "Médio Prazo", "Longo Prazo");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditarPlanejamentoFinanceiro.this, R.layout.dropdown_item, tipoPFList);

        autoCompleteTextViewTipoPF.setAdapter(adapter);

        autoCompleteTextViewTipoPF.setInputType(InputType.TYPE_NULL);

        autoCompleteTextViewTipoPF.setText(tipoPF, false);

        editTextDataInicial.setInputType(InputType.TYPE_NULL);
        editTextDataFinal.setInputType(InputType.TYPE_NULL);

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

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int nomePF = editTextNomePF.getText().toString().length();
                int tipoPF = autoCompleteTextViewTipoPF.getText().toString().length();
                int valorObjetivado = editTextValorObjetivado.getText().toString().length();
                int dataInicial = editTextDataInicial.getText().toString().length();
                int dataObjetivada = editTextDataFinal.getText().toString().length();

                if(nomePF == 0 && valorObjetivado == 0 && dataInicial == 0 && dataObjetivada == 0) {
                    Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
                    editTextNomePF.requestFocus();
                }else if(nomePF == 0) {
                    Utilitario.toast(getApplicationContext(), Msg.NOME_PF);
                    editTextNomePF.requestFocus();
                }else if(tipoPF == 0){
                    Toast.makeText(getApplicationContext(), Msg.TIPO_PF, Toast.LENGTH_LONG).show();
                    autoCompleteTextViewTipoPF.requestFocus();
                }else if(valorObjetivado == 0) {
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
                    editTextValorObjetivado.requestFocus();
                }else if(editTextValorObjetivado.getText().toString().equals("0")){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
                    editTextValorObjetivado.requestFocus();
                    editTextValorObjetivado.setText("");
                }else if(dataObjetivada == 0) {
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
                }else {
                    PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
                    dto.setIdPF(textViewIdPF.getText().toString());
                    dto.setNomePF(editTextNomePF.getText().toString());
                    dto.setTipoPF(autoCompleteTextViewTipoPF.getText().toString());
                    dto.setValorAtual(Double.parseDouble(textViewValorAtual.getText().toString()));
                    dto.setValorObjetivado(Double.parseDouble(editTextValorObjetivado.getText().toString()));
                    dto.setDataInicial(editTextDataInicial.getText().toString());
                    dto.setDataFinal(editTextDataFinal.getText().toString());

                    dto.setDataInicial(Utilitario.convertBrToUsa(dto.getDataInicial()));
                    dto.setDataFinal(Utilitario.convertBrToUsa(dto.getDataFinal()));
                    dao.editarPlanejamentoFinanceiro(dto, EditarPlanejamentoFinanceiro.this);
                }
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarPlanejamentoFinanceiro.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarPF(textViewIdPF.getText().toString(), EditarPlanejamentoFinanceiro.this);
                        Toast.makeText(getApplicationContext(), Msg.DELETADO, Toast.LENGTH_LONG).show();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
            }
        });

        editTextDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(EditarPlanejamentoFinanceiro.this, new DatePickerDialog.OnDateSetListener() {
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

                picker = new DatePickerDialog(EditarPlanejamentoFinanceiro.this, new DatePickerDialog.OnDateSetListener() {
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
}