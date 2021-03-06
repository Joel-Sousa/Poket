package com.example.poket.view.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.Home;

public class AdicionarUsuario extends AppCompatActivity {

    EditText editTextApelido, editTextEmail, editTextSenha, editTextRepetirSenha;
    Button buttonCadastrar;
    ImageView imageViewVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_usuario);

        editTextApelido = findViewById(R.id.editTextAdicionarUsuarioApelido);
        editTextEmail = findViewById(R.id.editTextAdicionarUsuarioEmail);
        editTextSenha = findViewById(R.id.editTextAdicionarUsuarioSenha);
        editTextRepetirSenha = findViewById(R.id.editTextAdicionarUsuarioRepetirSenha);
        buttonCadastrar = findViewById(R.id.buttonAdicionarUsuarioAdicionar);
        imageViewVoltar = findViewById(R.id.imageViewAdicionarUsuarioVoltar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int apelido = editTextApelido.getText().toString().length();
                int email = editTextEmail.getText().toString().length();
                int senha = editTextSenha.getText().toString().length();
                int confirmarSenha = editTextRepetirSenha.getText().toString().length();

                if(apelido == 0 && email == 0 && senha == 0 && confirmarSenha == 0){
                    Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
                    editTextApelido.requestFocus();
                }else if(apelido == 0){
                    Utilitario.toast(getApplicationContext(), Msg.APELIDO);
                    editTextApelido.requestFocus();
                }else if(email == 0){
                    Utilitario.toast(getApplicationContext(), Msg.EMAIL);
                    editTextEmail.requestFocus();
                }else if(!editTextEmail.getText().toString().contains("@")){
                    Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
                    editTextEmail.requestFocus();
                }else if(senha == 0){
                    Utilitario.toast(getApplicationContext(), Msg.SENHA);
                    editTextSenha.requestFocus();
                }else if(senha <= 5){
                    Utilitario.toast(getApplicationContext(), Msg.SENHA_INSUFICIENTE);
                    editTextSenha.requestFocus();
                }else if(confirmarSenha == 0){
                    Utilitario.toast(getApplicationContext(), Msg.REPETIR_SENHA);
                    editTextRepetirSenha.requestFocus();
                }else if(confirmarSenha <= 5){
                    Utilitario.toast(getApplicationContext(), Msg.SENHA_INSUFICIENTE_R);
                    editTextRepetirSenha.requestFocus();
                }else if(!editTextSenha.getText().toString().equals(editTextRepetirSenha.getText().toString())){
                    Utilitario.toast(getApplicationContext(), Msg.COMPARAR_SENHA);
                    editTextSenha.requestFocus();
                }else{
                    UsuarioDAO dao = new UsuarioDAO();
                    UsuarioDTO dto = new UsuarioDTO();
                    dto.setApelido(editTextApelido.getText().toString());
                    dto.setEmail(editTextEmail.getText().toString().trim());
                    dto.setSenha(editTextSenha.getText().toString());
                    dto.setRepetirSenha(editTextRepetirSenha.getText().toString());

                    dao.criarAutenticacao(dto, AdicionarUsuario.this);
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