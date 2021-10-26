package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poket.DAO.DespesaDAO;
import com.example.poket.R;

public class VerDespesa extends AppCompatActivity {

    TextView textViewId, textViewNomeDespesa, textViewNomeConta, textViewValorDespesa, textViewTipoDespesa,
             textViewDataDespesa, textViewObservacao;

    ImageView imageViewVoltar;

    DespesaDAO dao = new DespesaDAO();

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_despesa);

        textViewId = findViewById(R.id.textViewVerDespesaId);

        textViewNomeDespesa = findViewById(R.id.textViewVerDespesaNomeDespesa);
        textViewNomeConta = findViewById(R.id.textViewVerDespesaNomeConta);
        textViewValorDespesa = findViewById(R.id.textViewVerDespesaValorDespesa);
        textViewTipoDespesa = findViewById(R.id.textViewVerDespesaTipoDespesa);
        textViewDataDespesa = findViewById(R.id.textViewVerDespesaDataDespesa);
        textViewObservacao = findViewById(R.id.textViewVerDespesaObservacao);
        imageViewVoltar = findViewById(R.id.imageViewVerDespesaVoltar);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        textViewId.setText(id);

        dao.verDespesa(id, textViewNomeDespesa, textViewNomeConta, textViewValorDespesa,
                textViewTipoDespesa, textViewDataDespesa, textViewObservacao);

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}