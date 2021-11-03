package com.example.poket.view.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.conta.EditarConta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarUsuario extends AppCompatActivity {

    EditText editTextApelido, editTextEmail, editTextSenha, editTextRepetirSenha;
    ImageView imageViewVoltar;
    Button buttonEditar, buttonExcluir;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        editTextApelido = findViewById(R.id.editTextEditarUsuarioApelido);
        editTextEmail = findViewById(R.id.editTextEditarUsuarioEmail);
        editTextSenha = findViewById(R.id.editTextEditarUsuarioSenha);
        editTextRepetirSenha = findViewById(R.id.editTextEditarUsuarioRepetirSenha);
        buttonEditar = findViewById(R.id.buttonEditarUsuarioEditar);
        buttonExcluir = findViewById(R.id.buttonEditarUsuarioExcluir);
        imageViewVoltar = findViewById(R.id.imageViewEditarUsuarioVoltar);

        mock();

        UsuarioDAO dao = new UsuarioDAO();
        UsuarioDTO dto = dao.obterUsuario();

        editTextApelido.setText(dto.getApelido());
        editTextEmail.setText(dto.getEmail());

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            dto.setApelido(editTextApelido.getText().toString());
            dto.setEmail(editTextEmail.getText().toString().trim());
            dto.setSenha(editTextSenha.getText().toString());
            dto.setRepetirSenha(editTextRepetirSenha.getText().toString());
            validarCampos(dto);
            }
        });

           buttonExcluir.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AlertDialog.Builder confirmacao = new AlertDialog.Builder(EditarUsuario.this);
                   confirmacao.setTitle("Atencao!");
                   confirmacao.setMessage("Deseja excluir?");
                   confirmacao.setCancelable(false);
                   confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           dao.excluirUsuario(getApplicationContext());
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

    private void validarCampos(UsuarioDTO dto){

        if(dto.getApelido().length() == 0 && dto.getEmail().length() == 0 &&
                dto.getSenha().length() == 0 && dto.getRepetirSenha().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.DADOS_INFORMADOS_N);
            editTextApelido.requestFocus();
        }else if(dto.getApelido().length() == 0 || dto.getApelido().length() < 3){
            Utilitario.toast(getApplicationContext(), Msg.APELIDO);
            editTextApelido.requestFocus();
        }else if(dto.getEmail().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL);
            editTextEmail.requestFocus();
        }else if(!dto.getEmail().contains("@")){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
            editTextEmail.requestFocus();
        }else if(dto.getSenha().length() == 0 || dto.getSenha().length() <= 5){
            Utilitario.toast(getApplicationContext(), Msg.SENHA);
            editTextSenha.requestFocus();
        }else if(dto.getRepetirSenha().length() == 0 || dto.getRepetirSenha().length() <= 5){
            Utilitario.toast(getApplicationContext(), Msg.REPETIR_SENHA);
            editTextRepetirSenha.requestFocus();
        }else if(!dto.getSenha().equals(dto.getRepetirSenha())){
            Utilitario.toast(getApplicationContext(), Msg.COMPARAR_SENHA);
            editTextSenha.requestFocus();
        }else{
            UsuarioDAO dao = new UsuarioDAO();
            dao.atualizarUsuario(dto, EditarUsuario.this);
        }
    }

    public void mock(){
        editTextSenha.setText("123123");
        editTextRepetirSenha.setText("123123");
    }

}