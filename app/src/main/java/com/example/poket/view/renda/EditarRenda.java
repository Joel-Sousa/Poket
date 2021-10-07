package com.example.poket.view.renda;

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
import com.example.poket.DAO.RendaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.DTO.RendaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.EditarDespesa;

public class EditarRenda extends AppCompatActivity {

    EditText editTextRenda, editTextValorRenda, editTextDataRenda, editTextObservacao;
    TextView textViewIdConta, textViewContaValor, textViewId;
    Spinner spinnerConta, spinnerTipoRenda;
    Switch switchRendaFixa;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

    RendaDAO dao = new RendaDAO();

    String idContaAntiga = "";
    String contaValorAntigo = "";
    String valorRendaAntiga = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_renda);

        textViewId = findViewById(R.id.textViewEditarRendaUid);

        editTextRenda = findViewById(R.id.editTextEditarRendaRenda);
        textViewIdConta = findViewById(R.id.textViewEditarRendaIdConta);
        spinnerConta = findViewById(R.id.spinnerEditarRendaConta);
        textViewContaValor = findViewById(R.id.textViewEditarRendaContaValor);
        editTextValorRenda = findViewById(R.id.editTextEditarRendaValorRenda);
        spinnerTipoRenda = findViewById(R.id.spinnerEditarRendaTipoRenda);
        editTextDataRenda = findViewById(R.id.editTextEditarRendaDataRenda);
        editTextObservacao = findViewById(R.id.editTextEditarRendaObservacao);
        switchRendaFixa = findViewById(R.id.switchEditarRendaRendaFixa);
        imageViewVoltar = findViewById(R.id.imageViewEditarRendaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarRendaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarRendaExcluir);

        editTextDataRenda.addTextChangedListener(MaskEditUtil.mask(editTextDataRenda, MaskEditUtil.FORMAT_DATE));
        Utilitario.listaTipoRenda(spinnerTipoRenda, getApplicationContext());

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, EditarRenda.this, textViewContaValor, textViewIdConta);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextRenda.setText(intent.getStringExtra("renda"));
//        spinnerConta.setText(intent.getStringExtra("conta"));
//        textViewContaValor.setText(intent.getStringExtra("contaValor"));
        editTextValorRenda.setText(intent.getStringExtra("valorRenda"));
//        spinnerTipoRenda.setText(intent.getStringExtra("tipoRenda"));
        editTextDataRenda.setText(intent.getStringExtra("dataRenda"));
        editTextObservacao.setText(intent.getStringExtra("observacao"));
//        switchRendaFixa.setText(intent.getStringExtra("rendaFixa"));

        idContaAntiga = intent.getStringExtra("idConta");
        contaValorAntigo = intent.getStringExtra("contaValor");
        valorRendaAntiga = intent.getStringExtra("valorRenda");

//        Utilitario.toast(getApplicationContext(), valorRendaAntiga);

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RendaDTO dto = new RendaDTO();
                dto.setId(textViewId.getText().toString());
                dto.setRenda(editTextRenda.getText().toString());
                dto.setIdConta(textViewIdConta.getText().toString());
                dto.setConta(spinnerConta.getSelectedItem().toString());
                dto.setContaValor(textViewContaValor.getText().toString());
                dto.setValorRenda(editTextValorRenda.getText().toString());
                dto.setTipoRenda(spinnerTipoRenda.getSelectedItem().toString());
                dto.setDataRenda(editTextDataRenda.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());
//                dto.setRendaFixa(switchRendaFixa.getText().toString());
                validarCampos(dto);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarRenda.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarRenda(textViewId.getText().toString(), idContaAntiga, valorRendaAntiga);
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

    public void validarCampos(RendaDTO dto){

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
        }else {
            dao.editarRenda(dto, EditarRenda.this, idContaAntiga, valorRendaAntiga);
//            finish();
        }
    }
}