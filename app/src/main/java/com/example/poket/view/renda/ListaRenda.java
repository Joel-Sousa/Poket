package com.example.poket.view.renda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.RendaDAO;
import com.example.poket.R;
import com.example.poket.util.Utilitario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ListaRenda extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar;
    TextView textViewRendaValorTotal;
    Button buttonBuscar;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartRenda;

    RendaDAO dao = new RendaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_renda);

        editTextBusca = findViewById(R.id.editTextListaRendaBusca);

        buttonBuscar = findViewById(R.id.buttonListaRendaBusca);
        imageViewVoltar = findViewById(R.id.imageViewListaRendaVoltar);
        textViewRendaValorTotal = findViewById(R.id.textViewListaRendaValorTotal);

        barChartRenda = findViewById(R.id.barChartListaRenda);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaRenda);

        dao.lerRendas(recyclerView, context, textViewRendaValorTotal);

        dao.graficoBarChartRenda(barChartRenda);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busca = editTextBusca.getText().toString();
                dao.buscarRenda(recyclerView, context, textViewRendaValorTotal, busca);
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
        recyclerView = findViewById(R.id.recyclerViewListaRenda);

//        RendaDAO dao = new RendaDAO();
        dao.lerRendas(recyclerView, context, textViewRendaValorTotal);

        barChartRenda = findViewById(R.id.barChartListaRenda);
        dao.graficoBarChartRenda(barChartRenda);
    }
}