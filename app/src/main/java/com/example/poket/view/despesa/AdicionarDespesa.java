package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class AdicionarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextValorDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewIdConta, textViewValorConta;
    Spinner spinnerConta, spinnerTipoDespesa;
    ImageView imageViewVoltar;
    Button buttonSalvar;

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

        editTextDataDespesa.addTextChangedListener(MaskEditUtil.mask(editTextDataDespesa, MaskEditUtil.FORMAT_DATE));
        editTextDataDespesa.setText(Utilitario.dataAtual());

        Utilitario.listaTipoDespesa(spinnerTipoDespesa, tipoPF, getApplicationContext());

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarDespesa.this, textViewValorConta, textViewIdConta);

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
        }else if(dto.getTipoDespesa().length() == 0){
            Toast.makeText(getApplicationContext(), Msg.TIPO_DESPESA, Toast.LENGTH_LONG).show();
            spinnerTipoDespesa.requestFocus();
        }else if(dto.getDataDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA);
            editTextDataDespesa.requestFocus();
        }else if(dto.getDataDespesa().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA_VALIDA);
            editTextDataDespesa.requestFocus();
            editTextDataDespesa.setText("");
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