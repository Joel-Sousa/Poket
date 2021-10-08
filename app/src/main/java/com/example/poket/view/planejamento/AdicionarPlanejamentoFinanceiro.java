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
import com.example.poket.util.Utilitario;

public class AdicionarPlanejamentoFinanceiro extends AppCompatActivity {

    ImageView imageViewVoltar;
    TextView textViewIdConta, textViewContaValor, textViewTipoPF;
    EditText editTextNomePF, editTextValoAtual, editTextValorObjetivado,
    editTextDataInicial, editTextDataFinal;
    Spinner spinnerConta;
    Button buttonSalvar;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
    PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_planejamento_financeiro);

        textViewIdConta = findViewById(R.id.textViewAdicionarPFidConta);

        editTextNomePF = findViewById(R.id.editTextAdicionarPFNomePF);
//        editTextTipoPF = findViewById(R.id.editTextAdicionarPFTipoPF);
        textViewTipoPF = findViewById(R.id.textViewAdicionarPFTipoPF);
        spinnerConta = findViewById(R.id.spinnerAdicionarPFConta);
        textViewContaValor= findViewById(R.id.textViewAdicionarPFContaValor);
        editTextValoAtual = findViewById(R.id.editTextAdicionarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextAdicionarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextAdicionarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextAdicionarPFDataFinal);
        buttonSalvar = findViewById(R.id.buttonAdicionarPFSalvar);
        imageViewVoltar = findViewById(R.id.imageViewAdicionarPFVoltar);

        Intent intent = getIntent();
        textViewTipoPF.setText(intent.getStringExtra("tipoPF"));

        editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(editTextDataInicial, MaskEditUtil.FORMAT_DATE));
        editTextDataInicial.setText(Utilitario.dataAtual());
        editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(editTextDataFinal, MaskEditUtil.FORMAT_DATE));

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, AdicionarPlanejamentoFinanceiro.this, textViewContaValor, textViewIdConta);

        mock();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dto.setPlanejamentoFinanceiro(editTextNomePF.getText().toString());
                dto.setTipoPlanejamentoFinanceiro(textViewTipoPF.getText().toString());
                dto.setIdConta(textViewIdConta.getText().toString());
                dto.setConta(spinnerConta.getSelectedItem().toString());
                dto.setContaValor(textViewContaValor.getText().toString());
                dto.setValorAtual(editTextValoAtual.getText().toString());
                dto.setValorObjetivado(editTextValorObjetivado.getText().toString());
                dto.setDataInicial(editTextDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());
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

    private void validarConta(PlanejamentoFinanceiroDTO dto){
        dao.cadastrarPlanejamentoFinanceiro(dto, AdicionarPlanejamentoFinanceiro.this);
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