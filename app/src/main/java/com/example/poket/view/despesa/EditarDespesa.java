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
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.R;
import com.example.poket.util.MaskEditUtil;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class EditarDespesa extends AppCompatActivity {

    EditText editTextDespesa, editTextDataDespesa, editTextObservacao;
    TextView textViewId, textViewConta, textViewValorDespesa;
    Spinner spinnerTipoDespesa;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

    String idConta = "";
    String valorDespesaAntiga = "";

    DespesaDAO dao = new DespesaDAO();

    String tipoPF = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_despesa);

        textViewId = findViewById(R.id.textViewEditarDespesaId);

        editTextDespesa = findViewById(R.id.editTextEditarDespesaDespesa);
        textViewValorDespesa = findViewById(R.id.textViewEditarDespesaValorDespesa);
        spinnerTipoDespesa = findViewById(R.id.spinnerEditarDespesaTipoDespesa);
        editTextDataDespesa = findViewById(R.id.editTextEditarDespesaDataDespesa);
        editTextObservacao = findViewById(R.id.editTextEditarDespesaObservacao);

        textViewConta = findViewById(R.id.textViewEditarDespesaConta);

        imageViewVoltar = findViewById(R.id.imageViewEditarDespesaVoltar);
        buttonEditar = findViewById(R.id.buttonEditarDespesaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarDespesaExcluir);

        editTextDataDespesa.addTextChangedListener(MaskEditUtil.mask(editTextDataDespesa, MaskEditUtil.FORMAT_DATE));

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
        editTextDespesa.setText(intent.getStringExtra("despesa"));
        tipoPF = intent.getStringExtra("tipoDespesa");
        textViewValorDespesa.setText(intent.getStringExtra("valorDespesa"));
        editTextDataDespesa.setText(Utilitario.convertUsaToBr(intent.getStringExtra("dataDespesa")));
        editTextObservacao.setText(intent.getStringExtra("observacao"));

        textViewConta.setText(intent.getStringExtra("conta"));

        idConta = intent.getStringExtra("idConta");
        valorDespesaAntiga = intent.getStringExtra("valorDespesa");

        Utilitario.listaTipoDespesa(spinnerTipoDespesa, tipoPF, getApplicationContext());

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DespesaDTO dto = new DespesaDTO();
                dto.setId(textViewId.getText().toString());
                dto.setDespesa(editTextDespesa.getText().toString());
                dto.setValorDespesa(textViewValorDespesa.getText().toString());
                dto.setTipoDespesa(spinnerTipoDespesa.getSelectedItem().toString());
                dto.setDataDespesa(editTextDataDespesa.getText().toString());
                dto.setObservacao(editTextObservacao.getText().toString());

                dto.setIdConta(idConta);
                dto.setConta(textViewConta.getText().toString());

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
                        dao.deletarDespesa(textViewId.getText().toString(), idConta, valorDespesaAntiga);
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
        }else if(spinnerTipoDespesa.getSelectedItem().toString().equals(".:Selecione:.")){
            Utilitario.toast(getApplicationContext(), Msg.TIPO_DESPESA);
            spinnerTipoDespesa.performClick();
        }else if(dto.getDataDespesa().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA);
            editTextDataDespesa.requestFocus();
        }else if(dto.getDataDespesa().length() < 10){
            Utilitario.toast(getApplicationContext(), Msg.DATA_DESPESA_VALIDA);
            editTextDataDespesa.requestFocus();
            editTextDataDespesa.setText("");
        }else{
            dto.setDataDespesa(Utilitario.convertBrToUsa(dto.getDataDespesa()));
            dao.editarDespesa(dto, EditarDespesa.this);
        }
    }
}