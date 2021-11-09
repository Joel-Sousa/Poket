package com.example.poket.view.conta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.R;

public class ListaConta extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar, imageViewAdicionarConta, imageViewBuscar;
    TextView textViewContaValor;
    Intent intentAdicionarConta;

    Context context;
    RecyclerView recyclerView;

    ContaDAO dao = new ContaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_conta);

        imageViewAdicionarConta = findViewById(R.id.imageViewListaContaAdicionarConta);
        imageViewBuscar = findViewById(R.id.imageViewListaContaBuscar);
        editTextBusca = findViewById(R.id.editTextListaContaBuscar);
        imageViewVoltar = findViewById(R.id.imageViewListaContaVoltar);

        textViewContaValor = findViewById(R.id.textViewListaContaContaValor);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaConta);

        dao.lerContas(recyclerView, context, textViewContaValor);

        imageViewAdicionarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAdicionarConta = new Intent(ListaConta.this, AdicionarConta.class);
                startActivity(intentAdicionarConta);
            }
        });

        imageViewBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextBusca.getWindowToken(), 0);

                String busca = editTextBusca.getText().toString();
                dao.buscarConta(recyclerView, context, textViewContaValor, busca);
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaConta);

        dao.lerContas(recyclerView, context, textViewContaValor);
    }
}