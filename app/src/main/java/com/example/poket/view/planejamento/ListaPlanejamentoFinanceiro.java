package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.poket.R;

public class ListaPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planejamento_financeiro);

        textViewId = findViewById(R.id.textViewListaPFId);

        Intent intent = getIntent();
        textViewId.setText(intent.getStringExtra("id"));
    }
}