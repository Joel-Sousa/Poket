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
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.renda.EditarRenda;

public class EditarPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewId, textViewTipoPF, textViewValorAtual;
    EditText editTextNomePF, editTextValorObjetivado,
            editTextDataInicial, editTextDataFinal;
    ImageView imageViewVoltar;
    Spinner spinnerConta;
    Button buttonEditar;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_planejamento_financeiro);

        textViewId = findViewById(R.id.textViewEditarPFId);

        editTextNomePF = findViewById(R.id.editTextEditarPFNomePF);
        textViewTipoPF = findViewById(R.id.textViewEditarPFTipoPF);
        textViewValorAtual = findViewById(R.id.textViewEditarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextEditarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextEditarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextEditarPFDataFinal);
        buttonEditar = findViewById(R.id.buttonEditarPFEditar);
        imageViewVoltar = findViewById(R.id.imageViewEditarPFVoltar);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextNomePF.setText(intent.getStringExtra("PF"));
        textViewTipoPF.setText(intent.getStringExtra("tipoPF"));
        textViewValorAtual.setText(intent.getStringExtra("valorAtual"));
        editTextValorObjetivado.setText(intent.getStringExtra("valorObjetivado"));
        editTextDataInicial.setText(intent.getStringExtra("dataInicio"));
        editTextDataFinal.setText(intent.getStringExtra("dataFinal"));

        editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(editTextDataInicial, MaskEditUtil.FORMAT_DATE));
//        editTextDataInicial.setText(Utilitario.dataAtual());
        editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(editTextDataFinal, MaskEditUtil.FORMAT_DATE));

//        ContaDAO daoC = new ContaDAO();
//        daoC.listaContaSpinner(spinnerConta, EditarPlanejamentoFinanceiro.this, textViewContaValor, textViewIdConta);

//        idContaAntiga = intent.getStringExtra("idConta");
//        valorPFAntigo = intent.getStringExtra("valorAtual");

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
                dto.setId(textViewId.getText().toString());
                dto.setPlanejamentoFinanceiro(editTextNomePF.getText().toString());
                dto.setTipoPlanejamentoFinanceiro(textViewTipoPF.getText().toString());
//                dto.setIdConta(textViewIdConta.getText().toString());
//                dto.setConta(spinnerConta.getSelectedItem().toString());
//                dto.setContaValor(textViewContaValor.getText().toString());
                dto.setValorAtual(textViewValorAtual.getText().toString());
                dto.setValorObjetivado(editTextValorObjetivado.getText().toString());
                dto.setDataInicial(editTextDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());
                validarConta(dto);
            }
        });

    }

    private void validarConta(PlanejamentoFinanceiroDTO dto){

        if(dto.getPlanejamentoFinanceiro().length() == 0 && dto.getValorAtual().length() == 0 &&
                dto.getValorObjetivado().length() == 0 && dto.getDataInicial().length() == 0 &&
                dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextNomePF.requestFocus();
        }else if(dto.getPlanejamentoFinanceiro().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.NOME_PF);
            editTextNomePF.requestFocus();
        }else if(dto.getValorObjetivado().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getDataInicial().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_INICIAL);
            editTextDataInicial.requestFocus();
        }else if(dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL);
            editTextDataFinal.requestFocus();
        }else {
            dao.editarPlanejamentoFinanceiro(dto, EditarPlanejamentoFinanceiro.this);
        }
    }
}