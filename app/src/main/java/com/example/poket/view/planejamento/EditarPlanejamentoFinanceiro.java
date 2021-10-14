package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;

public class EditarPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewId, textViewIdConta,textViewIdContaDB, textViewContaValor, textViewTipoPF;
    EditText editTextNomePF, editTextValorAtual, editTextValorObjetivado,
            editTextDataInicial, editTextDataFinal;
    ImageView imageViewVoltar;
    Spinner spinnerConta;
    Button buttonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_planejamento_financeiro);

        textViewId = findViewById(R.id.textViewEditarPFId);
        textViewIdConta = findViewById(R.id.textViewEditarPFIdConta);
        textViewIdContaDB = findViewById(R.id.textViewEditarPFIdContaDB);

        editTextNomePF = findViewById(R.id.editTextEditarPFNomePF);
//        editTextTipoPF = findViewById(R.id.editTextAdicionarPFTipoPF);
        textViewTipoPF = findViewById(R.id.textViewEditarPFTipoPF);
        spinnerConta = findViewById(R.id.spinnerEditarPFConta);
        textViewContaValor= findViewById(R.id.textViewEditarPFContaValor);
        editTextValorAtual = findViewById(R.id.editTextEditarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextEditarPFValorObjetivado);
        editTextDataInicial = findViewById(R.id.editTextEditarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextEditarPFDataFinal);
        buttonEditar = findViewById(R.id.buttonEditarPFEditar);
        imageViewVoltar = findViewById(R.id.imageViewEditarPFVoltar);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        textViewIdContaDB.setText(intent.getStringExtra("idConta"));
        editTextNomePF.setText(intent.getStringExtra("PF"));
        textViewTipoPF.setText(intent.getStringExtra("tipoPF"));
//        spinnerConta.setText(intent.getStringExtra("id"));
//        textViewContaValor.setText(intent.getStringExtra(""));
        editTextValorAtual.setText(intent.getStringExtra("valorAtual"));
        editTextValorObjetivado.setText(intent.getStringExtra("valorObjetivado"));
        editTextDataInicial.setText(intent.getStringExtra("dataInicio"));
        editTextDataFinal.setText(intent.getStringExtra("dataFinal"));

        editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(editTextDataInicial, MaskEditUtil.FORMAT_DATE));
//        editTextDataInicial.setText(Utilitario.dataAtual());
        editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(editTextDataFinal, MaskEditUtil.FORMAT_DATE));

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, EditarPlanejamentoFinanceiro.this, textViewContaValor, textViewIdConta);

        //TODO criar logica agora para salvar no DB de editar

    }
}