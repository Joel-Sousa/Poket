package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;

public class ListaPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewId, textViewTipoPF, textViewPFPF, textViewValorAtual, textViewValorObjetivado,
            textViewDataInicio, textViewDataFinal, textViewPorcInicio, textViewPorcFinal;
    ProgressBar progressBarConcluido, progressBarRestante;
    Button buttonEditar, buttonExcluir;

    ImageView imageViewVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planejamento_financeiro);

        textViewId = findViewById(R.id.textViewListaPFId);

        textViewTipoPF = findViewById(R.id.textViewListaPFTipoPF);
        textViewPFPF = findViewById(R.id.textViewListaPFPlanejamentoF);
        textViewValorAtual = findViewById(R.id.textViewListaPFValorAtual);
        textViewValorObjetivado = findViewById(R.id.textViewListaPFValorObjetivado);
        textViewDataInicio = findViewById(R.id.textViewListaPFDataInicio);
        progressBarConcluido = findViewById(R.id.progressBarListarPFConcluido);
        textViewPorcInicio = findViewById(R.id.textViewListarPFPorcentagemInicio);
        textViewDataFinal = findViewById(R.id.textViewListarPFDataFinal);
        progressBarRestante = findViewById(R.id.progressBarListarPFResta);
        textViewPorcFinal = findViewById(R.id.textViewListarPFPorcentagemResta);

        imageViewVoltar = findViewById(R.id.imageViewListaPFVoltar);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        textViewId.setText(id);

        PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
        dao.lerPlanejamentoFinanceiro(id, textViewTipoPF, textViewPFPF, textViewValorAtual,
                textViewValorObjetivado, textViewDataInicio, progressBarConcluido,
                textViewPorcInicio, textViewDataFinal, progressBarRestante, textViewPorcFinal);


        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}