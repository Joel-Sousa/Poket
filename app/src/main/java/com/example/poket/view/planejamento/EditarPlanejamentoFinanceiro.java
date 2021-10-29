package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.renda.EditarRenda;

public class EditarPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewIdPF, textViewValorAtual, textViewDataInicial;
    EditText editTextNomePF, editTextValorObjetivado, editTextDataFinal;
    Spinner spinnerTipoPF;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_planejamento_financeiro);

        textViewIdPF = findViewById(R.id.textViewEditarPFId);

        editTextNomePF = findViewById(R.id.editTextEditarPFNomePF);
        spinnerTipoPF = findViewById(R.id.spinnerEditarPFTipoPF);
        textViewValorAtual = findViewById(R.id.textViewEditarPFValorAtual);
        editTextValorObjetivado = findViewById(R.id.editTextEditarPFValorObjetivado);
        textViewDataInicial = findViewById(R.id.textViewEditarPFDataInicial);
        editTextDataFinal = findViewById(R.id.editTextEditarPFDataFinal);

        buttonEditar = findViewById(R.id.buttonEditarPFEditar);
        buttonExcluir = findViewById(R.id.buttonEditarPFExcluir);
        imageViewVoltar = findViewById(R.id.imageViewEditarPFVoltar);

        Intent intent = getIntent();
        textViewIdPF.setText(intent.getStringExtra("idPF"));
        editTextNomePF.setText(intent.getStringExtra("nomePF"));
        tipoPF = intent.getStringExtra("tipoPF");
        textViewValorAtual.setText(intent.getStringExtra("valorAtual"));
        editTextValorObjetivado.setText(intent.getStringExtra("valorObjetivado"));
        textViewDataInicial.setText(intent.getStringExtra("dataInicio"));
        editTextDataFinal.setText(intent.getStringExtra("dataFinal"));

        Utilitario.listaTipoPF(spinnerTipoPF, tipoPF, getApplicationContext());

//        editTextDataInicial.addTextChangedListener(MaskEditUtil.mask(editTextDataInicial, MaskEditUtil.FORMAT_DATE));
        editTextDataFinal.addTextChangedListener(MaskEditUtil.mask(editTextDataFinal, MaskEditUtil.FORMAT_DATE));

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
                dto.setIdPF(textViewIdPF.getText().toString());
                dto.setNomePF(editTextNomePF.getText().toString());
                dto.setTipoPF(spinnerTipoPF.getSelectedItem().toString());
                dto.setValorAtual(textViewValorAtual.getText().toString());
                dto.setValorObjetivado(editTextValorObjetivado.getText().toString());
                dto.setDataInicial(textViewDataInicial.getText().toString());
                dto.setDataFinal(editTextDataFinal.getText().toString());

                validarConta(dto);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarPlanejamentoFinanceiro.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarPF(textViewIdPF.getText().toString(), EditarPlanejamentoFinanceiro.this);
                        Toast.makeText(getApplicationContext(), Msg.DELETADO, Toast.LENGTH_LONG).show();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
            }
        });

    }

    private void validarConta(PlanejamentoFinanceiroDTO dto){

        if(dto.getNomePF().length() == 0 && dto.getValorAtual().length() == 0 &&
                dto.getValorObjetivado().length() == 0 && dto.getDataInicial().length() == 0 &&
                dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextNomePF.requestFocus();
        }else if(dto.getNomePF().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.NOME_PF);
            editTextNomePF.requestFocus();
        }else if(dto.getValorObjetivado().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.VALOR_OBJETIVADO);
            editTextValorObjetivado.requestFocus();
        }else if(dto.getDataFinal().length() == 0) {
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL);
            editTextDataFinal.requestFocus();
        }else if(dto.getDataFinal().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_FINAL_VALIDA);
            editTextDataFinal.requestFocus();
            editTextDataFinal.setText("");
        }else if(dto.getValorObjetivado().equals("0")){
            Utilitario.toast(getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValorObjetivado.requestFocus();
        }else {
            dto.setDataInicial(Utilitario.convertBrToUsa(dto.getDataInicial()));
            dto.setDataFinal(Utilitario.convertBrToUsa(dto.getDataFinal()));
            dao.editarPlanejamentoFinanceiro(dto, EditarPlanejamentoFinanceiro.this);
        }
    }
}