package com.example.poket.view.renda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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

    String tipoPF = "";
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

//        Utilitario.listaTipoRenda(spinnerTipoRenda, tipoPF, getApplicationContext());
        editTextDataRenda.setText(Utilitario.dataAtual());
        editTextDataRenda.setInputType(InputType.TYPE_NULL);
//         editTextDataRenda.addTextChangedListener(MaskEditUtil.mask(editTextDataRenda, MaskEditUtil.FORMAT_DATE));

        tipoRendaList = Arrays.asList("Salario", "Servicos", "Presente", "Aluguel", "Outros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                AdicionarRenda.this, R.layout.dropdown_item, tipoRendaList);

        autoCompleteTextViewTipoRenda.setAdapter(adapter);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarRenda.this, textViewValorConta, textViewIdConta, false);

        mock();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto.setRenda(editTextRenda.getText().toString());
                dto.setValorRenda(editTextValorRenda.getText().toString());
                dto.setTipoRenda(autoCompleteTextViewTipoRenda.getText().toString());
                dto.setDataRenda(editTextDataRenda.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());

                dto.setIdConta(textViewIdConta.getText().toString());
                dto.setConta(spinnerConta.getSelectedItem().toString());
                dto.setValorConta(textViewValorConta.getText().toString());

                validarConta(dto);
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

    private void validarConta(RendaDTO dto){

        if(dto.getRenda().length() == 0 && dto.getValorRenda().length() == 0 &&
                dto.getDataRenda().length() == 0 ) {
            Utilitario.toast(getApplicationContext(),
                    Msg.DADOS_INFORMADOS_N);
            editTextRenda.requestFocus();
        }else if(dto.getRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.RENDA);
            editTextRenda.requestFocus();
        }else if(dto.getValorRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_RENDA);
            editTextValorRenda.requestFocus();
        }else if(dto.getTipoRenda().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_RENDA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoRenda.requestFocus();
        }else if(!tipoRendaList.contains(dto.getTipoRenda())){
            Toast.makeText(getApplicationContext(), Msg.TIPO_RENDA, Toast.LENGTH_LONG).show();
            autoCompleteTextViewTipoRenda.setText("");
            autoCompleteTextViewTipoRenda.requestFocus();
        }else if(dto.getValorRenda().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorRenda.requestFocus();
        }else if(dto.getDataRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
        }else{
            dto.setDataRenda(Utilitario.convertBrToUsa(dto.getDataRenda()));
            dao.cadastarRenda(dto, AdicionarRenda.this);
        }
    }

    public void mock(){
        editTextRenda.setText("aluguelTst");
        editTextValorRenda.setText("20");
        editTextObservacao.setText("observacaoTst");
    }
}