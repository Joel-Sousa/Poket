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
                    editTextSenha.setText("");
                    editTextSenha.requestFocus();
                }else if(confirmarSenha == 0){
                    Utilitario.toast(getApplicationContext(), Msg.REPETIR_SENHA);
                    editTextRepetirSenha.requestFocus();
                }else if(confirmarSenha <= 5){
                    Utilitario.toast(getApplicationContext(), Msg.SENHA_INSUFICIENTE_R);
                    editTextRepetirSenha.setText("");
                    editTextRepetirSenha.requestFocus();
                }else if(!editTextSenha.getText().toString().equals(editTextRepetirSenha.getText().toString())){
                    Utilitario.toast(getApplicationContext(), Msg.COMPARAR_SENHA);
                    editTextSenha.requestFocus();
                    editTextSenha.setText("");
                }else{
                    UsuarioDAO dao = new UsuarioDAO();
                    dto.setApelido(editTextApelido.getText().toString());
                    dto.setEmail(editTextEmail.getText().toString().trim());
                    dto.setSenha(editTextSenha.getText().toString());
                    dto.setRepetirSenha(editTextRepetirSenha.getText().toString());

                    dao.atualizarUsuario(dto, EditarUsuario.this);
                }
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

    public void mock(){
        editTextSenha.setText("123123");
        editTextRepetirSenha.setText("123123");
    }

}