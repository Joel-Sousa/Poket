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
    EditText editTextNomePF, editTextValorAtual, editTextValorObjetivado,
    editTextDataInicial, editTextDataFinal;
    Spinner spinnerConta, spinnerTipoPF;
    Button buttonSalvar;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
    PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
    HistoricoPFDTO hdto = new HistoricoPFDTO();

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_planejamento_financeiro);

        textViewIdConta = findViewById(R.id.textViewAdicionarPFidConta);
        spinnerConta = findViewById(R.id.spinnerAdicionarPFConta);
        textViewContaValor= findViewById(R.id.textViewAdicionarPFContaValor);

        editTextNomePF = findViewById(R.id.editTextAdicionarPFNomePF);
        spinnerTipoPF = findViewById(R.id.spinnerAdicionarPFTipoPF);
        editTextValorAtual = findViewById(R.id.editTextAdicionarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextAdicionarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextAdicionarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextAdicionarPFDataFinal);

        buttonSalvar = findViewById(R.id.buttonAdicionarPFSalvar);
        imageViewVoltar = findViewById(R.id.imageViewAdicionarPFVoltar);

        Utilitario.listaTipoPF(spinnerTipoPF, tipoPF, getApplicationContext());

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
                dto.setValorAtual(editTextValorAtual.getText().toString());
                dto.setValorObjetivado(editTextValorObjetivado.getText().toString());
                dto.setDataInicial(editTextDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());

                hdto.setIdConta(textViewIdConta.getText().toString());
                hdto.setConta(spinnerConta.getSelectedItem().toString());
                hdto.setValorConta(textViewContaValor.getText().toString());

                hdto.setValorHistoricoPF(editTextValorAtual.getText().toString());

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
            editTextValorAtual.requestFocus();
        }else if(dto.getValorAtual().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorAtual.requestFocus();
        }else if(dto.getValorObjetivado().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getValorObjetivado().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getDataInicial().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_INICIAL);
            editTextDataInicial.requestFocus();
        }else if(dto.getDataInicial().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_INICIAL_VALIDA);
            editTextDataInicial.requestFocus();
            editTextDataInicial.setText("");
        }else if(dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL);
            editTextDataFinal.requestFocus();
        }else if(dto.getDataFinal().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL_VALIDA);
            editTextDataFinal.requestFocus();
            editTextDataFinal.setText("");
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
        editTextNomePF.setText("nomeTst");
        editTextValorAtual.setText("100");
        editTextValorObjetivado.setText("900");
        editTextDataFinal.setText("12/12/2021");
    }
}