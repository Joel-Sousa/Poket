package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
import com.example.poket.MainActivity;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdicionarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextValorDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewIdConta, textViewValorConta;
    Spinner spinnerConta, spinnerTipoDespesa;
    ImageView imageViewVoltar;
    Button buttonSalvar;
    DatePickerDialog picker;

    DespesaDAO dao = new DespesaDAO();
    DespesaDTO dto = new DespesaDTO();

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);

        editTextDespesa = findViewById(R.id.editTextAdicionarDespesaDespesa);
        editTextValorDespesa = findViewById(R.id.editTextAdicionarDespesaValorDespesa);
        spinnerTipoDespesa = findViewById(R.id.spinnerAdicionarDespesaTipoDespesa);
        editTextDataDespesa = findViewById(R.id.editTextAdicionarDespesaDataDespesa);
        editTextObservacao = findViewById(R.id.editTextAdicionarDespesaObservacao);

        textViewIdConta = findViewById(R.id.textViewAdicionarDespesaIdConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarDespesaConta);
        textViewValorConta = findViewById(R.id.textViewAdicionarDespesaValorConta);

        imageViewVoltar = findViewById(R.id.imageViewAdicionarDespesaVoltar);
        buttonSalvar = findViewById(R.id.buttonAdicionarDespesaSalvar);

        editTextDataDespesa.setText(Utilitario.dataAtual());
        editTextDataDespesa.setInputType(InputType.TYPE_NULL);

        Utilitario.listaTipoDespesa(spinnerTipoDespesa, tipoPF, getApplicationContext());

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarDespesa.this, textViewValorConta, textViewIdConta, true);

        mock();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dto.setDespesa(editTextDespesa.getText().toString());
                dto.setValorDespesa(editTextValorDespesa.getText().toString());
                dto.setTipoDespesa(spinnerTipoDespesa.getSelectedItem().toString());
                dto.setDataDespesa(editTextDataDespesa.getText().toString());
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
                        String day = i2<10 ? "0"+i2 : String.valueOf(i2);

                        editTextDataDespesa.setText(day + "/" + (i1 + 1) + "/" + i);
                    }
                }, year, month, day);
                picker.show();
            }
        });

    }

    private void validarConta(DespesaDTO dto){

        if(dto.getDespesa().length() == 0 && dto.getValorDespesa().length() == 0 &&
                dto.getDataDespesa().length() == 0 ) {
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextDespesa.requestFocus();
        }else if(dto.getDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DESPESA);
            editTextDespesa.requestFocus();
        }else if(dto.getValorDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_DESPESA);
            editTextValorDespesa.requestFocus();
        }else if(spinnerTipoDespesa.getSelectedItem().toString().equals(".:Selecione:.")){
            Utilitario.toast(getApplicationContext(), Msg.TIPO_DESPESA);
            spinnerTipoDespesa.performClick();
        }else if(dto.getValorDespesa().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorDespesa.requestFocus();
        }else if(dto.getTipoDespesa().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_DESPESA, Toast.LENGTH_LONG).show();
            spinnerTipoDespesa.requestFocus();
        }else if(dto.getDataDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA);
            editTextDataDespesa.requestFocus();
        }else{
            dto.setDataDespesa(Utilitario.convertBrToUsa(dto.getDataDespesa()));
            dao.cadastarDespesa(dto, AdicionarDespesa.this);
        }
    }

    public void mock(){
        editTextDespesa.setText("carroTst");
        editTextValorDespesa.setText("100");
        editTextObservacao.setText("pagoTst");
    }
}