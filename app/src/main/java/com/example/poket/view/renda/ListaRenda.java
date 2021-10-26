package com.example.poket.view.renda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

    ImageView imageViewVoltar;
    TextView textViewRendaValorTotal;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartRenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_renda);

        // TODO IMPLEMENTAR A BUSCA DE RENDA

        imageViewVoltar = findViewById(R.id.imageViewListaRendaVoltar);
        textViewRendaValorTotal = findViewById(R.id.textViewListaRendaValorTotal);

        barChartRenda = findViewById(R.id.barChartListaRenda);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaRenda);

        RendaDAO dao = new RendaDAO();
        dao.lerRendas(recyclerView, context, textViewRendaValorTotal);

        graficoBarChartRenda();

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

        RendaDAO dao = new RendaDAO();
        dao.lerRendas(recyclerView, context, textViewRendaValorTotal);
    }

    private void graficoBarChartRenda(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,1));
        barEntries.add(new BarEntry(2,2));
        barEntries.add(new BarEntry(3,3));
        barEntries.add(new BarEntry(4,4));
        barEntries.add(new BarEntry(5,5));
        barEntries.add(new BarEntry(6,6));
        barEntries.add(new BarEntry(7,7));
        barEntries.add(new BarEntry(8,8));
        barEntries.add(new BarEntry(9,9));
        barEntries.add(new BarEntry(10,10));
        barEntries.add(new BarEntry(11,11));
        barEntries.add(new BarEntry(12,12));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Renda");
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);

        barChartRenda.setData(barData);

        String[] mes = new String[]
                {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                        "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

        XAxis xAxis = barChartRenda.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
        xAxis.setLabelCount(12);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(2);
        xAxis.setGranularityEnabled(true);

        barChartRenda.getDescription().setEnabled(false);

        barChartRenda.invalidate();

    }
}