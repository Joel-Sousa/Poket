package com.example.poket.view.renda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poket.DAO.RendaDAO;
import com.example.poket.R;

public class VerRenda extends AppCompatActivity {

    TextView textViewId, textViewNomeRenda, textViewNomeConta, textViewValorRenda, textViewTipoRenda,
            textViewDataRenda, textViewObservacao;

    ImageView imageViewVoltar;

    RendaDAO dao = new RendaDAO();

    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_renda);

        textViewId = findViewById(R.id.textViewVerRendaId);

        textViewNomeRenda = findViewById(R.id.textViewVerRendaNomeRenda);
        textViewNomeConta = findViewById(R.id.textViewVerRendaNomeConta);
        textViewValorRenda = findViewById(R.id.textViewVerRendaValorRenda);
        textViewTipoRenda = findViewById(R.id.textViewVerRendaTipoRenda);
        textViewDataRenda = findViewById(R.id.textViewVerRendaDataRenda);
        textViewObservacao = findViewById(R.id.textViewVerRendaObservacao);
        imageViewVoltar = findViewById(R.id.imageViewVerRendaVoltar);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        textViewId.setText(id);

        dao.verRenda(id, textViewNomeRenda, textViewNomeConta,
                textViewValorRenda, textViewTipoRenda,
                textViewDataRenda, textViewObservacao);

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}