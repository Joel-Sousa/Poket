package com.example.poket.view.despesa;

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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.conta.EditarConta;

import java.util.Arrays;
import java.util.List;

public class EditarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextValorDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewIdConta, textViewContaValor, textViewUid;
    Spinner spinnerConta, spinnerTipoDespesa;
    Switch switchDespesaFixa;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

    String idContaAntiga = "";
    String contaValorAntigo = "";
    String valorDespesaAntiga = "";

    DespesaDAO dao = new DespesaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_despesa);

        textViewUid = findViewById(R.id.textViewEditarDespesaUid);

        editTextDespesa = findViewById(R.id.editTextEditarDespesaDespesa);
        textViewIdConta = findViewById(R.id.textViewEditarDespesaIdConta);
        spinnerConta = findViewById(R.id.spinnerEditarDespesaConta);
        textViewContaValor = findViewById(R.id.textViewEditarDespesaContaValor);
        editTextValorDespesa = findViewById(R.id.editTextEditarDespesaValorDespesa);
        spinnerTipoDespesa = findViewById(R.id.spinnerEditarDespesaTipoDespesa);
        editTextDataDespesa = findViewById(R.id.editTextEditarDespesaDataDespesa);
        editTextObservacao = findViewById(R.id.editTextEditarDespesaObservacao);
        switchDespesaFixa = findViewById(R.id.switchEditarDespesaDespesaFixa);
        imageViewVoltar = findViewById(R.id.imageViewEditarDespesaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarDespesaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarDespesaExcluir);

        Utilitario.listaTipoDespesa(spinnerTipoDespesa, getApplicationContext());
        editTextDataDespesa.addTextChangedListener(MaskEditUtil.mask(editTextDataDespesa, MaskEditUtil.FORMAT_DATE));

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, EditarDespesa.this, textViewContaValor, textViewIdConta);

        Intent intent = getIntent();
        textViewUid.setText(intent.getStringExtra("id"));
        editTextDespesa.setText(intent.getStringExtra("despesa"));
//        spinnerConta.setText(intent.getStringExtra("conta"));
//        textViewContaValor.setText(intent.getStringExtra("contaValor"));
        editTextValorDespesa.setText(intent.getStringExtra("valorDespesa"));
//        spinnerTipoDespesa.setText(intent.getStringExtra("tipoDespesa"));
        editTextDataDespesa.setText(intent.getStringExtra("dataDespesa"));
        editTextObservacao.setText(intent.getStringExtra("observacao"));
//        switchDespesaFixa.setText(intent.getStringExtra("despesaFixa"));

//        Utilitario.toast(getApplicationContext(), intent.getStringExtra("idConta"));

        idContaAntiga = intent.getStringExtra("idConta");
        contaValorAntigo = intent.getStringExtra("contaValor");
        valorDespesaAntiga = intent.getStringExtra("valorDespesa");

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DespesaDTO dto = new DespesaDTO();
                dto.setId(textViewUid.getText().toString());
                dto.setDespesa(editTextDespesa.getText().toString());
                dto.setIdConta(textViewIdConta.getText().toString());
                dto.setConta(spinnerConta.getSelectedItem().toString());
                dto.setContaValor(textViewContaValor.getText().toString());
                dto.setValorDespesa(editTextValorDespesa.getText().toString());
                dto.setTipoDespesa(spinnerTipoDespesa.getSelectedItem().toString());
                dto.setDataDespesa(editTextDataDespesa.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());
//                dto.setDespesaFixa(switchDespesaFixa.getText().toString());
                validarCampos(dto);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarDespesa.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarDespesa(textViewUid.getText().toString(), idContaAntiga, valorDespesaAntiga);
                        Toast.makeText(getApplicationContext(), "Dados excluídos com sucesso!", Toast.LENGTH_LONG).show();

                        finish();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void validarCampos(DespesaDTO dto) {

        if(dto.getDespesa().length() == 0 && dto.getValorDespesa().length() == 0 &&
                dto.getDataDespesa().length() == 0 ) {
            Utilitario.toast(getApplicationContext(),
                    Msg.DADOS_INFORMADOS_N);
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
            dao.editarDespesa(dto, EditarDespesa.this, idContaAntiga, valorDespesaAntiga);
        }
    }

}