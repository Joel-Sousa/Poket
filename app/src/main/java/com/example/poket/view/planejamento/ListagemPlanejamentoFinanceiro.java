package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;
import com.example.poket.util.Utilitario;

public class ListagemPlanejamentoFinanceiro extends AppCompatActivity {

    Button buttonAdicionarPF;
    ImageView imageViewVoltar;

    Context context;
    RecyclerView recyclerView;

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem_planejamento_financeiro);

        buttonAdicionarPF = findViewById(R.id.buttonListagemPFAdicionarPF);
        imageViewVoltar = findViewById(R.id.imageViewListagemPFVoltar);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListagemPF);

        View view = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
        dao.lerPlanejamentoFinanceiro(recyclerView, context , ListagemPlanejamentoFinanceiro.this, view);

        buttonAdicionarPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAdicionarPF = new Intent(ListagemPlanejamentoFinanceiro.this, AdicionarPlanejamentoFinanceiro.class);
                startActivity(intentAdicionarPF);
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
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListagemPF);

        View view = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
        dao.lerPlanejamentoFinanceiro(recyclerView, context, ListagemPlanejamentoFinanceiro.this, view);

    }
}