package com.example.poket.view.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class AdicionarUsuario extends AppCompatActivity {

    EditText editTextApelido, editTextEmail, editTextSenha, editTextRepetirSenha;
    Button buttonCadastrar;
    ImageView imageViewVoltar;
    ProgressBar load;

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

        load = findViewById(R.id.progressBarCadastrarUsuario);

        mock();

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setApelido(editTextApelido.getText().toString());
                dto.setEmail(editTextEmail.getText().toString());
                dto.setSenha(editTextSenha.getText().toString());
                dto.setRepetirSenha(editTextRepetirSenha.getText().toString());
                validarUsuario(dto);
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

       private void validarUsuario(UsuarioDTO dto){

        if(dto.getApelido().length() == 0 && dto.getEmail().length() == 0 &&
                dto.getSenha().length() == 0 && dto.getRepetirSenha().length() == 0){
            Utilitario.toast(getApplicationContext(),
                    Msg.DADOS_INFORMADOS_N);
            editTextApelido.requestFocus();
        }else if(dto.getApelido().length() == 0 || dto.getApelido().length() < 3){
            Utilitario.toast(getApplicationContext(),
                    Msg.APELIDO);
            editTextApelido.requestFocus();
        }else if(dto.getEmail().length() == 0){
            Utilitario.toast(getApplicationContext(),
                    Msg.EMAIL);
            editTextEmail.requestFocus();
        }else if(!dto.getEmail().contains("@")){
            Utilitario.toast(getApplicationContext(),
                    Msg.EMAIL_VALIDO);
            editTextEmail.requestFocus();
        }else if(dto.getSenha().length() == 0 || dto.getSenha().length() <= 5){
            Utilitario.toast(getApplicationContext(),
                    Msg.SENHA);
            editTextSenha.requestFocus();

        }else if(dto.getRepetirSenha().length() == 0 || dto.getRepetirSenha().length() <= 5){
            Utilitario.toast(getApplicationContext(),
                    Msg.REPETIR_SENHA);
            editTextRepetirSenha.requestFocus();

        }else if(!dto.getSenha().equals(dto.getRepetirSenha())){
            Utilitario.toast(getApplicationContext(),
                    Msg.COMPARAR_SENHA);
            editTextSenha.requestFocus();
        }else{
            UsuarioDAO dao = new UsuarioDAO();
            dao.criarAutenticacao(dto, AdicionarUsuario.this);
        }
    }

//    private void criarConta(UsuarioDTO dto){
//
//        Map<String, String> dadosUsuario = new HashMap<>();
//        dadosUsuario.put("apelido", dto.getApelido());
//        dadosUsuario.put("email", dto.getEmail());
//        dadosUsuario.put("uid", dto.getUid());
//
//        db.collection("usuarios").document(dto.getUid())
//                .set(dadosUsuario)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Utilitario.progresso(false, load);
//
//                        Log.d(Msg.INFO, "DocumentSnapshot successfully written!");
//
//                        Utilitario.toast(getApplicationContext(), Msg.CADASTRADO);
//                        finish();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Utilitario.progresso(false, load);
//
//                        Log.e(Msg.ERROR, "Error adding document", e);
//                    }
//                });
//    }
    public void mock(){
        editTextApelido.setText("ana");
        editTextEmail.setText("ana@email.com");
        editTextSenha.setText("123123");
        editTextRepetirSenha.setText("123123");
    }
}