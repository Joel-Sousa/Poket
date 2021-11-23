package com.example.poket.view.conta;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DTO.ContaDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class EditarConta extends AppCompatActivity {

    TextView textViewId;
    EditText editTextConta, editTextValorConta;
    Button buttonEditar, buttonExcluir;
    ImageView imageViewVoltar;

    ContaDAO dao = new ContaDAO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_conta);

        textViewId = findViewById(R.id.textViewEditarContauid);
        editTextConta = findViewById(R.id.editTextEditarContaConta);
        editTextValorConta = findViewById(R.id.editTextEditarContaValor);
        buttonEditar = findViewById(R.id.buttonEditarContaEditar);
        buttonExcluir = findViewById(R.id.buttonEditarContaExcluir);
        imageViewVoltar = findViewById(R.id.imageViewEditarContaVoltar);

        Intent intent = getIntent();

        textViewId.setText(intent.getStringExtra("uidc"));
        editTextConta.setText(intent.getStringExtra("conta"));
        editTextValorConta.setText(intent.getStringExtra("valor"));

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int conta = editTextConta.getText().toString().length();
                int valor = editTextValorConta.getText().toString().length();

                if(conta == 0 && valor == 0){
                    Utilitario.toast(getApplicationContext(),
                            Msg.DADOS_INFORMADOS_N);
                    editTextConta.requestFocus();
                }else if(conta == 0 ){
                    Utilitario.toast(getApplicationContext(),
                            Msg.CONTA);
                    editTextConta.requestFocus();
                }else if(valor == 0){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_CONTA);
                    editTextValorConta.requestFocus();
                }else{
                    ContaDTO dto = new ContaDTO();
                    dto.setId(textViewId.getText().toString());
                    dto.setConta(editTextConta.getText().toString());
                    dto.setValor(Double.parseDouble(editTextValorConta.getText().toString()));
                    dao.editarConta(dto);
                    Toast.makeText(getApplicationContext(), "Alterado!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarConta.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.deletarConta(textViewId.getText().toString());
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
}