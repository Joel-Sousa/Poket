package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewId, textViewConta, textViewValorDespesa;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;
    DatePickerDialog picker;

    TextInputLayout textInputLayoutTipoDespesa;
    AutoCompleteTextView autoCompleteTextViewTipoDespesa;

    DespesaDAO dao = new DespesaDAO();

    String idConta = "";
    String valorDespesaAntiga = "";
    String tipoDespesa = "";
    List<String> tipoDespesaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_despesa);

        textViewId = findViewById(R.id.textViewEditarDespesaId);

        editTextDespesa = findViewById(R.id.editTextEditarDespesaDespesa);
        textViewValorDespesa = findViewById(R.id.textViewEditarDespesaValorDespesa);

        textInputLayoutTipoDespesa = findViewById(R.id.editTextEditarDespesaTipoDespesa);
        autoCompleteTextViewTipoDespesa = findViewById(R.id.dropdown_menu);

        editTextDataDespesa = findViewById(R.id.editTextEditarDespesaDataDespesa);
        editTextObservacao = findViewById(R.id.editTextEditarDespesaObservacao);

        textViewConta = findViewById(R.id.textViewEditarDespesaConta);

        imageViewVoltar = findViewById(R.id.imageViewEditarDespesaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarDespesaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarDespesaExcluir);

        tipoDespesaList = Arrays.asList("Alimentaçao", "Veiculo", "Moradia", "Lazer", "Outros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditarDespesa.this, R.layout.dropdown_item, tipoDespesaList);

        autoCompleteTextViewTipoDespesa.setAdapter(adapter);

        autoCompleteTextViewTipoDespesa.setInputType(InputType.TYPE_NULL);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextDespesa.setText(intent.getStringExtra("despesa"));
        tipoDespesa = intent.getStringExtra("tipoDespesa");
        textViewValorDespesa.setText(intent.getStringExtra("valorDespesa"));
        editTextDataDespesa.setText(Utilitario.convertUsaToBr(intent.getStringExtra("dataDespesa")));
        editTextObservacao.setText(intent.getStringExtra("observacao"));

        textViewConta.setText(intent.getStringExtra("conta"));

        idConta = intent.getStringExtra("idConta");
        valorDespesaAntiga = intent.getStringExtra("valorDespesa");

        autoCompleteTextViewTipoDespesa.setText(tipoDespesa, false);

        editTextDataDespesa.setInputType(InputType.TYPE_NULL);

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

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DespesaDTO dto = new DespesaDTO();
                dto.setId(textViewId.getText().toString());
                dto.setDespesa(editTextDespesa.getText().toString());
                dto.setValorDespesa(textViewValorDespesa.getText().toString());
                dto.setTipoDespesa(autoCompleteTextViewTipoDespesa.getText().toString());
                dto.setDataDespesa(editTextDataDespesa.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());

                dto.setIdConta(idConta);
                dto.setConta(textViewConta.getText().toString());

                validarCampos(dto);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarDespesa.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarDespesa(textViewId.getText().toString(), idConta, valorDespesaAntiga);
                        Toast.makeText(getApplicationContext(), "Dados excluídos com sucesso!", Toast.LENGTH_LONG).show();

                        finish();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
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

                picker = new DatePickerDialog(EditarDespesa.this, new DatePickerDialog.OnDateSetListener() {
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

    public void validarCampos(DespesaDTO dto) {

        if(dto.getDespesa().length() == 0 && dto.getValorDespesa().length() == 0 &&
                dto.getDataDespesa().length() == 0 ) {
            Utilitario.toast(getApplicationContext(),
                    Msg.DADOS_INFORMADOS_N);
            editTextDespesa.requestFocus();
        }else if(dto.getDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DESPESA);
            editTextDespesa.requestFocus();
        }else if(dto.getTipoDespesa().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_DESPESA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoDespesa.requestFocus();
        }else if(!tipoDespesaList.contains(dto.getTipoDespesa())){
            Toast.makeText(getApplicationContext(), Msg.TIPO_DESPESA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoDespesa.setText("");
            autoCompleteTextViewTipoDespesa.requestFocus();
        }else if(dto.getDataDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA);
            editTextDataDespesa.requestFocus();
        }else{
            dto.setDataDespesa(Utilitario.convertBrToUsa(dto.getDataDespesa()));
            dao.editarDespesa(dto, EditarDespesa.this);
        }
    }
}