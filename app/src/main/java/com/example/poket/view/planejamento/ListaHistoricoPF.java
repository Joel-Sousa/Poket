package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.poket.DAO.DespesaDAO;
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;

public class ListaHistoricoPF extends AppCompatActivity {

    ImageView imageViewVoltar;

    Context context;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_historico_pf);

        imageViewVoltar = findViewById(R.id.imageViewListaHistoricoVoltar);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaHistoricoPF);

        Intent intent = getIntent();
        String idPF = intent.getStringExtra("idPF");

        PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
        dao.lerHistorico(recyclerView, context, ListaHistoricoPF.this, idPF);

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}