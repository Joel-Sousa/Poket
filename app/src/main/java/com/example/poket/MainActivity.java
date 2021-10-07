package com.example.poket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.usuario.AdicionarUsuario;
import com.example.poket.view.usuario.EsqueceuSenha;

public class MainActivity extends AppCompatActivity {

    Intent intentEsqueceuSenha, intentCadastrarUsuario, intentLogin;
    TextView textViewEsqueceuSenha, textViewCadastrarUsuario;
    EditText editTextEmail, editTextSenha;
    Button buttonLogin, buttonTst;

    ProgressBar load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.editTexMainActivityEmail);
        editTextSenha = findViewById(R.id.editTextMainActivitySenha);
        textViewEsqueceuSenha = findViewById(R.id.textViewMainActivityEsqueceuSenha);
        textViewCadastrarUsuario = findViewById(R.id.textViewMainActivityCadastrarUsuario);
        buttonLogin = findViewById(R.id.buttonMainActivityLogin);
        buttonTst = findViewById(R.id.buttonHomeTst);
        load = findViewById(R.id.progressBarMainActivity);

        mock();

//        PlanejamentoFinanceiroDAO daoPF = new PlanejamentoFinanceiroDAO();
//        daoPF.planejamentoFinanceiro();

//        buttonTst.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
//                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
//                final TextView textViewIdPF = mView.findViewById(R.id.textViewDialogPFUidPF);
//
//                final TextView textViewIdConta = mView.findViewById(R.id.textViewDialogAddPFIdConta);
//                final Spinner spinnerConta = mView.findViewById(R.id.spinnerDialogAddPFConta);
//                final EditText editTextValor = mView.findViewById(R.id.editTextDialogAddPFValor);
//                final TextView textViewValorConta = mView.findViewById(R.id.textViewDialogAddPFValorConta);
//                Button buttonAdicionar = mView.findViewById(R.id.buttonDialogAddPFAdicionar);
//                Button buttonVoltar = mView.findViewById(R.id.buttonDialogAddPFVoltar);
//
//                mBuilder.setView(mView);
//                final AlertDialog dialog = mBuilder.create();
//                dialog.show();
//
//                buttonAdicionar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });
//
//                buttonVoltar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
////                        dialog.hide();
//                    }
//                });
//            }
//        });

        textViewEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentEsqueceuSenha = new Intent(MainActivity.this , EsqueceuSenha.class);
                startActivity(intentEsqueceuSenha);
            }
        });

        textViewCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentCadastrarUsuario = new Intent(MainActivity.this , AdicionarUsuario.class);
                startActivity(intentCadastrarUsuario);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioDTO dto = new UsuarioDTO();
                dto.setEmail(editTextEmail.getText().toString());
                dto.setSenha(editTextSenha.getText().toString());
                validaCampos(dto);
            }
        });
    }

    private void validaCampos(UsuarioDTO dto){
        if(dto.getEmail().length() == 0 && dto.getSenha().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL_SENHA);
            editTextEmail.requestFocus();
        }else if(dto.getEmail().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL);
            editTextEmail.requestFocus();
        }else if(!dto.getEmail().contains("@")){
            Utilitario.toast(getApplicationContext(), Msg.EMAIL_VALIDO);
            editTextEmail.requestFocus();
        }else if(dto.getSenha().length() == 0){
            Utilitario.toast(getApplicationContext(), Msg.SENHA);
            editTextSenha.requestFocus();
        }else{
            UsuarioDAO dao = new UsuarioDAO();
            dao.autenticarUsuario(dto, MainActivity.this);
        }
    }


    public void mock(){
        editTextEmail.setText("ana@email.com");
        editTextSenha.setText("123123");
    }
}