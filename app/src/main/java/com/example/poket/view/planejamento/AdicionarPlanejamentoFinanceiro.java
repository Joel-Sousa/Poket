package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.DTO.HistoricoPFDTO;
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class AdicionarPlanejamentoFinanceiro extends AppCompatActivity {

    ImageView imageViewVoltar;
    TextView textViewIdConta, textViewContaValor, textViewTipoPF;
    EditText editTextNomePF, editTextValoAtual, editTextValorObjetivado,
    editTextDataInicial, editTextDataFinal;
    Spinner spinnerConta, spinnerTipoPF;
    Button buttonSalvar;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
    PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
    HistoricoPFDTO hdto = new HistoricoPFDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_planejamento_financeiro);

        textViewIdConta = findViewById(R.id.textViewAdicionarPFidConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarPFConta);
        textViewContaValor= findViewById(R.id.textViewAdicionarPFContaValor);

        editTextNomePF = findViewById(R.id.editTextAdicionarPFNomePF);
        spinnerTipoPF = findViewById(R.id.spinnerAdicionarPFTipoPF);
        editTextValoAtual = findViewById(R.id.editTextAdicionarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextAdicionarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextAdicionarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextAdicionarPFDataFinal);

        buttonSalvar = findViewById(R.id.buttonAdicionarPFSalvar);
        imageViewVoltar = findViewById(R.id.imageViewAdicionarPFVoltar);

        Utilitario.listaTipoPF(spinnerTipoPF, getApplicationContext());

        editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(editTextDataInicial, MaskEditUtil.FORMAT_DATE));
        editTextDataInicial.setText(Utilitario.dataAtual());
        editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(editTextDataFinal, MaskEditUtil.FORMAT_DATE));

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarPlanejamentoFinanceiro.this, textViewContaValor, textViewIdConta);

        mock();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto.setNomePF(editTextNomePF.getText().toString());
                dto.setTipoPF(spinnerTipoPF.getSelectedItem().toString());
                dto.setValorAtual(editTextValoAtual.getText().toString());
                dto.setValorObjetivado(editTextValorObjetivado.getText().toString());
                dto.setDataInicial(editTextDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());

                hdto.setIdConta(textViewIdConta.getText().toString());
                hdto.setNomeConta(spinnerConta.getSelectedItem().toString());
                hdto.setValorConta(textViewContaValor.getText().toString());

                hdto.setValorHistoricoPF(editTextValoAtual.getText().toString());

                validarConta(dto, hdto);
            }
        });
        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void validarConta(PlanejamentoFinanceiroDTO dto, HistoricoPFDTO hdto){

        if(dto.getNomePF().length() == 0 && dto.getValorAtual().length() == 0 &&
                dto.getValorObjetivado().length() == 0 && dto.getDataInicial().length() == 0 &&
                dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextNomePF.requestFocus();
        }else if(dto.getNomePF().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.NOME_PF);
            editTextNomePF.requestFocus();
        }else if(dto.getValorAtual().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ATUAL);
            editTextValoAtual.requestFocus();
        }else if(dto.getValorObjetivado().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getDataInicial().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_INICIAL);
            editTextDataInicial.requestFocus();
        }else if(dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL);
            editTextDataFinal.requestFocus();
        }else{
            dao.cadastrarPlanejamentoFinanceiro(dto, hdto, AdicionarPlanejamentoFinanceiro.this);
        }
    }

    public void mock(){
        editTextNomePF.setText("nomeTst");
//        editTextTipoPF.setText("Curto prazo");
        editTextValoAtual.setText("100");
        editTextValorObjetivado.setText("900");
        editTextDataFinal.setText("12/12/2021");
//        editTextDataDespesa.setText("11/11/1111");
    }
}