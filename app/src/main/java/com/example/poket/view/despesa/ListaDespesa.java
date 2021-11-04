package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.DespesaDAO;
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

public class ListaDespesa extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar;
    TextView textViewDespesaValorTotal;
    Button buttonBuscar;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartDespesa;

    DespesaDAO dao = new DespesaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_despesa);

        editTextBusca = findViewById(R.id.editTextListaDespesaBusca);

        buttonBuscar = findViewById(R.id.buttonListaDespesaBusca);
        imageViewVoltar = findViewById(R.id.imageViewListaDespesaVoltar);
        textViewDespesaValorTotal = findViewById(R.id.textViewListaDespesaValorTotal);

        barChartDespesa = findViewById(R.id.barChartListaDespesa);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaDespesa);

        dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal);

//        graficoBarChartDespesa();

        dao.graficoBarChartDespesa(barChartDespesa);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busca = editTextBusca.getText().toString();
                dao.buscarDespesa(recyclerView, context, textViewDespesaValorTotal, busca);
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
        recyclerView = findViewById(R.id.recyclerViewListaDespesa);

        dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal);

        barChartDespesa = findViewById(R.id.barChartListaDespesa);
        dao.graficoBarChartDespesa(barChartDespesa);
    }
}