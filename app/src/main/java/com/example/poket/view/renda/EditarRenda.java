package com.example.poket.view.renda;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.RendaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.DTO.RendaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.EditarDespesa;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditarRenda extends AppCompatActivity {

    EditText editTextRenda, editTextDataRenda, editTextObservacao;
    TextView textViewId, textViewConta, textViewValorRenda;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;
    DatePickerDialog picker;
    TextInputLayout textInputLayoutTipoRenda;
    AutoCompleteTextView autoCompleteTextViewTipoRenda;

    RendaDAO dao = new RendaDAO();

    String idConta = "";
    String valorRendaAntiga = "";
    String tipoPF = "";
    List<String> tipoRendaList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_renda);

        textViewId = findViewById(R.id.textViewEditarRendaUid);

        editTextRenda = findViewById(R.id.editTextEditarRendaRenda);
        textViewValorRenda = findViewById(R.id.textViewEditarRendaValorRenda);

        textInputLayoutTipoRenda = findViewById(R.id.editTextEditarRendaTipoRenda);
        autoCompleteTextViewTipoRenda = findViewById(R.id.dropdown_menu);

        editTextDataRenda = findViewById(R.id.editTextEditarRendaDataRenda);
        editTextObservacao = findViewById(R.id.editTextEditarRendaObservacao);

        textViewConta = findViewById(R.id.textViewEditarRendaNomeConta);

        imageViewVoltar = findViewById(R.id.imageViewEditarRendaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarRendaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarRendaExcluir);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextRenda.setText(intent.getStringExtra("renda"));
        textViewValorRenda.setText(intent.getStringExtra("valorRenda"));
        tipoPF = intent.getStringExtra("tipoRenda");
        editTextDataRenda.setText(Utilitario.convertUsaToBr(intent.getStringExtra("dataRenda")));
        editTextObservacao.setText(intent.getStringExtra("observacao"));

        textViewConta.setText(intent.getStringExtra("conta"));

        idConta = intent.getStringExtra("idConta");
        valorRendaAntiga = intent.getStringExtra("valorRenda");

        tipoRendaList = Arrays.asList("Salário", "Serviços", "Presente", "Aluguel", "Outros");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                EditarRenda.this, R.layout.dropdown_item, tipoRendaList);

        autoCompleteTextViewTipoRenda.setAdapter(adapter);

        editTextDataRenda.setInputType(InputType.TYPE_NULL);

        autoCompleteTextViewTipoRenda.setText(tipoPF, false);

        editTextDataRenda.setInputType(InputType.TYPE_NULL);

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

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RendaDTO dto = new RendaDTO();
                dto.setId(textViewId.getText().toString());
                dto.setRenda(editTextRenda.getText().toString());
                dto.setValorRenda(textViewValorRenda.getText().toString());
                dto.setTipoRenda(autoCompleteTextViewTipoRenda.getText().toString());
                dto.setDataRenda(editTextDataRenda.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());

                dto.setIdConta(idConta);
                dto.setConta(textViewConta.getText().toString());

                validarCampos(dto);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarRenda.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarRenda(textViewId.getText().toString(), idConta, valorRendaAntiga);
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

        editTextDataRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(EditarRenda.this, new DatePickerDialog.OnDateSetListener() {
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

    public void validarCampos(RendaDTO dto){

        if(dto.getRenda().length() == 0 && dto.getValorRenda().length() == 0 &&
                dto.getDataRenda().length() == 0 ) {
            Utilitario.toast(getApplicationContext(),
                    Msg.DADOS_INFORMADOS_N);
            editTextRenda.requestFocus();
        }else if(dto.getRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.RENDA);
            editTextRenda.requestFocus();
        }else if(dto.getTipoRenda().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_RENDA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoRenda.requestFocus();
        }else if(!tipoRendaList.contains(dto.getTipoRenda())){
            Toast.makeText(getApplicationContext(), Msg.TIPO_RENDA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoRenda.setText("");
            autoCompleteTextViewTipoRenda.requestFocus();
        }else if(dto.getDataRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
        }else {
            dto.setDataRenda(Utilitario.convertBrToUsa(dto.getDataRenda()));
            dao.editarRenda(dto, EditarRenda.this);
        }
    }
}