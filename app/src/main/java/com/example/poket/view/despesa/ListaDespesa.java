package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.List;

public class ListaDespesa extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar;
    TextView textViewDespesaValorTotal;
    Button buttonBuscar, buttonAdicionar, buttonLimpar;
    Spinner spinnerMes;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartDespesa;

    DespesaDAO dao = new DespesaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_despesa);

        editTextBusca = findViewById(R.id.editTextListaDespesaBusca);

        imageViewVoltar = findViewById(R.id.imageViewListaDespesaVoltar);
        textViewDespesaValorTotal = findViewById(R.id.textViewListaDespesaValorTotal);

        buttonBuscar = findViewById(R.id.buttonListaDespesaBusca);
        buttonLimpar = findViewById(R.id.buttonAdicionarDespesaLimpar);
        buttonAdicionar = findViewById(R.id.buttonListaDespesaAdicionarDespesa);

        barChartDespesa = findViewById(R.id.barChartListaDespesa);

        spinnerMes = findViewById(R.id.spinnerAdicionarDespesaMes);


        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaDespesa);

        Utilitario.meses(spinnerMes, context);

        dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal, 0);

//        graficoBarChartDespesa();

        dao.graficoBarChartDespesa(barChartDespesa);

        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String busca = editTextBusca.getText().toString();
                dao.buscarDespesa(recyclerView, context, textViewDespesaValorTotal, busca);
            }
        });

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaDespesa.this, AdicionarDespesa.class);
                startActivity(intent);
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        spinnerMes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dao.lerDespesas(recyclerView,context, textViewDespesaValorTotal, i);
//                    Utilitario.toast(getApplicationContext(), i+"-"+mesList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerMes.setSelection(0);
//                dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaDespesa);

        dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal, 0);

        barChartDespesa = findViewById(R.id.barChartListaDespesa);
        dao.graficoBarChartDespesa(barChartDespesa);
    }
}