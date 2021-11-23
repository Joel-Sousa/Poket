package com.example.poket.view.renda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.RendaDAO;
import com.example.poket.R;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.ListaDespesa;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaRenda extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar, imageViewBuscar, imageViewAdicionarRenda;
    TextView textViewRendaValorTotal;
    TextInputLayout textInputLayoutMes;
    AutoCompleteTextView autoCompleteTextViewMes;
    Button buttonLimpar;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartRenda;

    RendaDAO dao = new RendaDAO();
    List<String> mesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_renda);

        TextView textViewAno = findViewById(R.id.textViewListaRendaDataAno);
        textViewAno.setText(Utilitario.ano());

        editTextBusca = findViewById(R.id.editTextListaRendaBusca);

        imageViewVoltar = findViewById(R.id.imageViewListaRendaVoltar);
        textViewRendaValorTotal = findViewById(R.id.textViewListaRendaValorTotal);

        imageViewBuscar = findViewById(R.id.imageViewListaRendaBuscar);
        imageViewAdicionarRenda = findViewById(R.id.imageViewListaRendaAdicionarRenda);
        buttonLimpar = findViewById(R.id.buttonListaRendaLimpar);

        textInputLayoutMes = findViewById(R.id.editTextListaRendaMes);
        autoCompleteTextViewMes = findViewById(R.id.dropdown_menu);

        barChartRenda = findViewById(R.id.barChartListaRenda);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaRenda);

        mesList = Arrays.asList(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ListaRenda.this, R.layout.dropdown_item, mesList);

        autoCompleteTextViewMes.setAdapter(adapter);

        autoCompleteTextViewMes.setInputType(InputType.TYPE_NULL);

        dao.lerRendas(recyclerView, context, textViewRendaValorTotal,0);

        dao.graficoBarChartRenda(barChartRenda);

        imageViewBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextBusca.getWindowToken(), 0);

                String busca = editTextBusca.getText().toString();
                dao.buscarRenda(recyclerView, context, textViewRendaValorTotal, busca);
            }
        });

        imageViewAdicionarRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(ListaRenda.this, AdicionarRenda.class);
                startActivity(intent);
            }
        });

        imageViewVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        autoCompleteTextViewMes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextViewMes.getWindowToken(), 0);
            }
        });

        autoCompleteTextViewMes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextViewMes.getWindowToken(), 0);

                dao.lerRendas(recyclerView,context, textViewRendaValorTotal, 1+i);
            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextViewMes.setText("");
                dao.lerRendas(recyclerView, context, textViewRendaValorTotal, 0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaRenda);

        dao.lerRendas(recyclerView, context, textViewRendaValorTotal,0);

        barChartRenda = findViewById(R.id.barChartListaRenda);
        dao.graficoBarChartRenda(barChartRenda);
    }
}