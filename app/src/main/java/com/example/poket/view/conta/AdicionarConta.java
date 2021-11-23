package com.example.poket.view.conta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DTO.ContaDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;

public class AdicionarConta extends AppCompatActivity {

    EditText editTextConta, editTextValorConta;
    Button buttonCadastar;
    ImageView imageViewVoltar;

    ContaDTO dto = new ContaDTO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_conta);

        editTextConta = findViewById(R.id.editTextAdicionarContaConta);
        editTextValorConta = findViewById(R.id.editTextAdicionarContaValor);
        buttonCadastar = findViewById(R.id.buttonAdicionarContaSalvar);
        imageViewVoltar = findViewById(R.id.imageViewCadastrarContaVoltar);


        buttonCadastar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int conta = editTextConta.getText().toString().length();
                int valor = editTextValorConta.getText().toString().length();

                if(conta == 0 && valor == 0){
                    Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
                    editTextConta.requestFocus();
                }else if(conta == 0){
                    Utilitario.toast(getApplicationContext(),
                            Msg.CONTA);
                    editTextConta.requestFocus();
                }else if(valor == 0){
                    Utilitario.toast(getApplicationContext(), Msg.VALOR_CONTA);
                    editTextValorConta.requestFocus();
                }else{
                    ContaDAO dao = new ContaDAO();

                    dto.setConta(editTextConta.getText().toString());
                    dto.setValor(Double.parseDouble(editTextValorConta.getText().toString()));
                    dao.cadastarConta(dto, AdicionarConta.this);
                }
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