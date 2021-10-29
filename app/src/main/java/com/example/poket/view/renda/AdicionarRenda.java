package com.example.poket.view.renda;

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
import com.example.poket.DAO.RendaDAO;
import com.example.poket.DTO.RendaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class AdicionarRenda extends AppCompatActivity {

    EditText editTextRenda, editTextValorRenda, editTextDataRenda, editTextObservacao;
    TextView textViewIdConta, textViewValorConta;
    Spinner spinnerConta, spinnerTipoRenda;
    ImageView imageViewVoltar;
    Button buttonSalvar;

    RendaDTO dto = new RendaDTO();
    RendaDAO dao = new RendaDAO();

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_renda);

        editTextRenda = findViewById(R.id.editTextAdicionarRendaRenda);
        editTextValorRenda = findViewById(R.id.editTextAdicionarRendaValorRenda);
        spinnerTipoRenda = findViewById(R.id.spinnerAdicionarRendaTipoRenda);
        editTextDataRenda = findViewById(R.id.editTextAdicionarRendaDataRenda);
        editTextObservacao = findViewById(R.id.editTextAdicionarRendaObservacao);

        textViewIdConta = findViewById(R.id.textViewAdicionarRendaIdConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarRendaConta);
        textViewValorConta = findViewById(R.id.textViewAdicionarRendaValorConta);

        imageViewVoltar = findViewById(R.id.imageViewAdidionarRendaVoltar);
        buttonSalvar = findViewById(R.id.buttonAdicionarRendaSalvar);

        Utilitario.listaTipoRenda(spinnerTipoRenda, tipoPF, getApplicationContext());
        editTextDataRenda.addTextChangedListener(MaskEditUtil.mask(editTextDataRenda, MaskEditUtil.FORMAT_DATE));
        editTextDataRenda.setText(Utilitario.dataAtual());

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarRenda.this, textViewValorConta, textViewIdConta);

//        mock();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto.setRenda(editTextRenda.getText().toString());
                dto.setValorRenda(editTextValorRenda.getText().toString());
                dto.setTipoRenda(spinnerTipoRenda.getSelectedItem().toString());
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
            spinnerTipoRenda.requestFocus();
        }else if(dto.getDataRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
        }else if(dto.getDataRenda().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA_VALIDA);
            editTextDataRenda.requestFocus();
            editTextDataRenda.setText("");
        }else if(dto.getDataRenda().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
            editTextDataRenda.setText("");
        }else{
            dto.setDataRenda(Utilitario.convertBrToUsa(dto.getDataRenda()));
            dao.cadastarRenda(dto, AdicionarRenda.this);
        }
    }

    public void mock(){
        editTextRenda.setText("aluguelTst");
        editTextValorRenda.setText("200");
        editTextObservacao.setText("observacaoTst");
    }
}