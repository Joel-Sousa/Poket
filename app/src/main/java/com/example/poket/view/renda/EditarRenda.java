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

    EditText editTextRenda, editTextDataRenda, editTextObservacao;
    TextView textViewId, textViewConta, textViewValorRenda;
    Spinner spinnerTipoRenda;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

    RendaDAO dao = new RendaDAO();

    String idConta = "";
    String valorRendaAntiga = "";

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_renda);

        textViewId = findViewById(R.id.textViewEditarRendaUid);

        editTextRenda = findViewById(R.id.editTextEditarRendaRenda);
        textViewValorRenda = findViewById(R.id.textViewEditarRendaValorRenda);
        spinnerTipoRenda = findViewById(R.id.spinnerEditarRendaTipoRenda);
        editTextDataRenda = findViewById(R.id.editTextEditarRendaDataRenda);
        editTextObservacao = findViewById(R.id.editTextEditarRendaObservacao);

        textViewConta = findViewById(R.id.textViewEditarRendaNomeConta);

        imageViewVoltar = findViewById(R.id.imageViewEditarRendaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarRendaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarRendaExcluir);

        editTextDataRenda.addTextChangedListener(MaskEditUtil.mask(editTextDataRenda, MaskEditUtil.FORMAT_DATE));

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextRenda.setText(intent.getStringExtra("renda"));
        textViewValorRenda.setText(intent.getStringExtra("valorRenda"));
        tipoPF = intent.getStringExtra("tipoRenda");
        editTextDataRenda.setText(Utilitario.convertUsaToBr(intent.getStringExtra("dataRenda")));
        editTextObservacao.setText(intent.getStringExtra("observacao"));

        textViewConta.setText(intent.getStringExtra("conta"));

        idConta = intent.getStringExtra("idConta");
        valorRendaAntiga = intent.getStringExtra("valorRenda");

        Utilitario.listaTipoRenda(spinnerTipoRenda, tipoPF, getApplicationContext());

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RendaDTO dto = new RendaDTO();
                dto.setId(textViewId.getText().toString());
                dto.setRenda(editTextRenda.getText().toString());
                dto.setValorRenda(textViewValorRenda.getText().toString());
                dto.setTipoRenda(spinnerTipoRenda.getSelectedItem().toString());
                dto.setDataRenda(editTextDataRenda.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());

                dto.setIdConta(idConta);
                dto.setConta(textViewConta.getText().toString());

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
                        dao.deletarRenda(textViewId.getText().toString(), idConta, valorRendaAntiga);
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
        }else if(spinnerTipoRenda.getSelectedItem().toString().equals(".:Selecione:.")){
            Utilitario.toast(getApplicationContext(), Msg.TIPO_RENDA);
            spinnerTipoRenda.performClick();
        }else if(dto.getDataRenda().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
        }else if(dto.getDataRenda().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_RENDA);
            editTextDataRenda.requestFocus();
            editTextDataRenda.setText("");
        }else {
            dto.setDataRenda(Utilitario.convertBrToUsa(dto.getDataRenda()));
            dao.editarRenda(dto, EditarRenda.this);
        }
    }
}