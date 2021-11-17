package com.example.poket.view.despesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaDespesa extends AppCompatActivity {

    EditText editTextBusca;
    ImageView imageViewVoltar, imageViewAdicionarDespesa, imageViewBuscar;
    TextView textViewDespesaValorTotal;
    TextInputLayout textInputLayoutMes;
    AutoCompleteTextView autoCompleteTextViewMes;
    Button buttonLimpar;

    Context context;
    RecyclerView recyclerView;

    BarChart barChartDespesa;

    DespesaDAO dao = new DespesaDAO();
    List<String> mesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_despesa);

        TextView textViewAno =  findViewById(R.id.textViewListaDespesaDataAno);
        textViewAno.setText(Utilitario.ano());

        editTextBusca = findViewById(R.id.editTextListaDespesaBusca);

        imageViewVoltar = findViewById(R.id.imageViewListaDespesaVoltar);
        textViewDespesaValorTotal = findViewById(R.id.textViewListaDespesaValorTotal);

        imageViewBuscar = findViewById(R.id.imageViewListaDespesaBuscar);

        buttonLimpar = findViewById(R.id.buttonListaDespesaLimpar);
        imageViewAdicionarDespesa = findViewById(R.id.imageViewListaDespesaAdicionarDespesa);

        barChartDespesa = findViewById(R.id.barChartListaDespesa);

        textInputLayoutMes = findViewById(R.id.editTextListaDespesaMes);
        autoCompleteTextViewMes = findViewById(R.id.dropdown_menu);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerViewListaDespesa);

        mesList = Arrays.asList(
                "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                ListaDespesa.this, R.layout.dropdown_item, mesList);

        autoCompleteTextViewMes.setAdapter(adapter);
//        Utilitario.meses(spinnerMes, context);

        autoCompleteTextViewMes.setInputType(InputType.TYPE_NULL);

        dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal, 0);

        dao.graficoBarChartDespesa(barChartDespesa);

        imageViewBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextBusca.getWindowToken(), 0);

                String busca = editTextBusca.getText().toString();
                dao.buscarDespesa(recyclerView, context, textViewDespesaValorTotal, busca);
            }
        });

        imageViewAdicionarDespesa.setOnClickListener(new View.OnClickListener() {
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

                dao.lerDespesas(recyclerView,context, textViewDespesaValorTotal, 1+i);
            }
        });

        buttonLimpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                spinnerMes.setSelection(0);
                autoCompleteTextViewMes.setText("");
                dao.lerDespesas(recyclerView, context, textViewDespesaValorTotal, 0);
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