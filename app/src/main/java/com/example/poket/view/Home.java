package com.example.poket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.MainActivity;
import com.example.poket.R;
import com.example.poket.util.Utilitario;
import com.example.poket.view.conta.ListaConta;
import com.example.poket.view.despesa.AdicionarDespesa;
import com.example.poket.view.despesa.ListaDespesa;
import com.example.poket.view.planejamento.AdicionarPlanejamentoFinanceiro;
import com.example.poket.view.renda.AdicionarRenda;
import com.example.poket.view.renda.ListaRenda;
import com.example.poket.view.usuario.EditarUsuario;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    TextView textViewTst, textViewApelido, textViewIdConta, textViewConta;
    Spinner spinnerConta;
    ImageView imageViewEditarUsuario, imageViewConta, imageViewSair,
    imageViewHistoricoDespesa, imageViewAdicionarDespesa,
    imageViewHistoricoRenda, imageViewAdicionarRenda,
    imageViewAdicionarPFCurto, imageViewAdicionarPFMedio, imageViewAdicionarPFLongo;

    Intent intentEditarUsuario, intentListaConta,
            intentAdicionarDespesa, intentAdicionarRenda,
            intentListaDespesa, intentListaRenda,
            intentPFCurto, intentPFMedio, intentPFLongo,
            intentPFAdicionarCurto, intentPFAdicionarMedio, intentPFAdicionarLongo;

    LinearLayout linearLayoutPFCurto,  linearLayoutPFMedio,  linearLayoutPFLongo;

    BarChart barChart;
    PieChart pieChartDespesa;
    PieChart pieChartRenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textViewTst = findViewById(R.id.textViewTst);
        textViewIdConta = findViewById(R.id.textViewHomeIdConta);

        textViewApelido = findViewById(R.id.textViewHomeApelido);
        textViewConta = findViewById(R.id.textViewHomeContaValor);

        imageViewEditarUsuario = findViewById(R.id.imageViewHomeEdit);
        imageViewConta = findViewById(R.id.imageViewHomeConta);
        imageViewSair = findViewById(R.id.imageViewHomeSair);
        spinnerConta = findViewById(R.id.spinnerHomeContas);
        imageViewHistoricoDespesa = findViewById(R.id.imageViewHomeHistoricoDespesa);
        imageViewAdicionarDespesa = findViewById(R.id.imageViewHomeAdicionarDespesaAdicionar);
        imageViewHistoricoRenda = findViewById(R.id.imageViewHomeHistoricoRenda);
        imageViewAdicionarRenda = findViewById(R.id.imageViewHomeAdicionarRendaAdicionar);

        imageViewAdicionarPFCurto = findViewById(R.id.imageViewHomeAdicionarPFCurto);
        imageViewAdicionarPFMedio = findViewById(R.id.imageViewHomeAdicionarPFMedio);
        imageViewAdicionarPFLongo = findViewById(R.id.imageViewHomeAdicionarPFLongo);

        barChart = findViewById(R.id.barChartHomeDespesaRenda);
        pieChartDespesa = findViewById(R.id.pieChartHomeDespesa);
        pieChartRenda = findViewById(R.id.pieChartHomeRenda);

        linearLayoutPFCurto = findViewById(R.id.linearLayoutHomeCurtoPrazo);
        linearLayoutPFMedio = findViewById(R.id.linearLayoutHomeMedioPrazo);
        linearLayoutPFLongo = findViewById(R.id.linearLayoutHomeLongoPrazo);

        graficoBarChartDespesaRenda();
        graficoPieChartDespesa();
        graficoPieChartRenda();

        UsuarioDAO dao = new UsuarioDAO();
        UsuarioDTO dto = dao.obterUsuario();

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, Home.this, textViewConta, textViewIdConta);

        textViewTst.setText(dto.getUid());
        textViewApelido.setText(dto.getApelido());

        imageViewEditarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentEditarUsuario = new Intent(Home.this, EditarUsuario.class);
                startActivity(intentEditarUsuario);
            }
        });

        imageViewConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentListaConta = new Intent(Home.this, ListaConta.class);
                startActivity(intentListaConta);
            }
        });

        imageViewSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(Home.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Deseja Sair?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dao.sairUsuario();
                        finish();
                        Toast.makeText(getApplicationContext(), "Saiu!", Toast.LENGTH_SHORT).show();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
            }
        });

        imageViewHistoricoDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentListaDespesa = new Intent(Home.this, ListaDespesa.class);
                startActivity(intentListaDespesa);
            }
        });

        imageViewAdicionarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   intentAdicionarDespesa = new Intent(Home.this, AdicionarDespesa.class);
                   startActivity(intentAdicionarDespesa);
            }
        });

        imageViewHistoricoRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentListaRenda = new Intent(Home.this, ListaRenda.class);
                startActivity(intentListaRenda);
            }
        });

        imageViewAdicionarRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentAdicionarRenda = new Intent(Home.this, AdicionarRenda.class);
                startActivity(intentAdicionarRenda);
            }
        });

        pieChartDespesa.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                PieEntry pe = (PieEntry) e;
                Utilitario.toast(getApplicationContext(),
                        pe.getLabel() + " R$: " + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        pieChartRenda.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                PieEntry pe = (PieEntry) e;
                Utilitario.toast(getApplicationContext(),
                        pe.getLabel() + " R$: " + e.getY());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        PlanejamentoFinanceiroDAO daoPF = new PlanejamentoFinanceiroDAO();

        imageViewAdicionarPFCurto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Curto prazo", true, mView);

            }
        });

        linearLayoutPFCurto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Curto prazo", false, mView);
//                Intent intentAdicionarPF = new Intent(Home.this, AdicionarPlanejamentoFinanceiro.class);
//                intentAdicionarPF.putExtra("tipoPF", "Curto prazo");
//                intentAdicionarPF.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intentAdicionarPF);
            }
        });

        imageViewAdicionarPFMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Medio prazo", true, mView);
            }
        });

        linearLayoutPFMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Medio prazo", false, mView);
            }
        });

        imageViewAdicionarPFLongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Longo prazo", true, mView);
            }
        });

        linearLayoutPFLongo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View mView = getLayoutInflater().inflate(R.layout.dialog_addpf, null);
                daoPF.planejamentoFinanceiro(Home.this,"Longo prazo", false, mView);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinnerConta = findViewById(R.id.spinnerHomeContas);
        textViewConta = findViewById(R.id.textViewHomeContaValor);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, Home.this, textViewConta, textViewIdConta);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        textViewApelido = findViewById(R.id.textViewHomeApelido);
        textViewApelido.setText(user.getDisplayName());
    }

    private void graficoBarChartDespesaRenda(){

//        ArrayList<BarEntry> barEntries = new ArrayList<>();
//        barEntries.add(new BarEntry(1,1));
//        barEntries.add(new BarEntry(3,2));
//        barEntries.add(new BarEntry(5,3));
//        barEntries.add(new BarEntry(7,4));
//        barEntries.add(new BarEntry(9,5));
//        barEntries.add(new BarEntry(11,6));
//        barEntries.add(new BarEntry(13,7));
//        barEntries.add(new BarEntry(15,8));
//        barEntries.add(new BarEntry(17,9));
//        barEntries.add(new BarEntry(19,10));
//        barEntries.add(new BarEntry(21,11));
//        barEntries.add(new BarEntry(23,12));
//
//        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
//        barEntries1.add(new BarEntry(2,1));
//        barEntries1.add(new BarEntry(4,2));
//        barEntries1.add(new BarEntry(6,3));
//        barEntries1.add(new BarEntry(8,4));
//        barEntries1.add(new BarEntry(10,5));
//        barEntries1.add(new BarEntry(12,6));
//        barEntries1.add(new BarEntry(14,7));
//        barEntries1.add(new BarEntry(16,8));
//        barEntries1.add(new BarEntry(18,9));
//        barEntries1.add(new BarEntry(20,10));
//        barEntries1.add(new BarEntry(22,11));
//        barEntries1.add(new BarEntry(24,12));

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1,1));
        barEntries.add(new BarEntry(2,2));
        barEntries.add(new BarEntry(3,3));
        barEntries.add(new BarEntry(4,4));
        barEntries.add(new BarEntry(5,5));
        barEntries.add(new BarEntry(6,10));
        barEntries.add(new BarEntry(7,7));
        barEntries.add(new BarEntry(8,8));
        barEntries.add(new BarEntry(9,9));
        barEntries.add(new BarEntry(10,10));
        barEntries.add(new BarEntry(11,11));
        barEntries.add(new BarEntry(12,12));

        ArrayList<BarEntry> barEntries1= new ArrayList<>();
        barEntries1.add(new BarEntry(1,1));
        barEntries1.add(new BarEntry(2,2));
        barEntries1.add(new BarEntry(3,3));
        barEntries1.add(new BarEntry(4,4));
        barEntries1.add(new BarEntry(5,5));
        barEntries1.add(new BarEntry(6,6));
        barEntries1.add(new BarEntry(7,7));
        barEntries1.add(new BarEntry(8,8));
        barEntries1.add(new BarEntry(9,9));
        barEntries1.add(new BarEntry(10,10));
        barEntries1.add(new BarEntry(11,11));
        barEntries1.add(new BarEntry(12,12));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Despesa");
        barDataSet.setColor(Color.RED);

        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Renda");
        barDataSet1.setColor(Color.GREEN);

        BarData barData = new BarData();
        barData.addDataSet(barDataSet);
        barData.addDataSet(barDataSet1);

        barChart.setData(barData);

        String[] mes = new String[]
                {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
                 "Ago", "Set", "Out", "Nov", "Dez"};

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
        xAxis.setCenterAxisLabels(true);
        xAxis.setLabelCount(12);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        float barSpace = 0.08f;
        float groupSpace = 0.30f;
        barData.setBarWidth(0.27f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(12);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0,groupSpace, barSpace);
        barChart.getDescription().setEnabled(false);

        barChart.invalidate();
    }

    private void graficoPieChartDespesa(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(430, "Alimentaçao"));
        pieEntries.add(new PieEntry(530, "Veiculo"));
        pieEntries.add(new PieEntry(330, "Moradia"));
        pieEntries.add(new PieEntry(230, "Lazer"));
        pieEntries.add(new PieEntry(630, "Outros"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(18f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChartDespesa));
//        pieData.setDrawValues(false);

        pieChartDespesa.setData(pieData);
        pieChartDespesa.getDescription().setEnabled(false);
        pieChartDespesa.setUsePercentValues(true);
        pieChartDespesa.setCenterText("R$");
        pieChartDespesa.setCenterTextSize(18f);

        pieChartDespesa.setHoleRadius(40);
        pieChartDespesa.setTransparentCircleRadius(10);
        pieChartDespesa.setEntryLabelColor(Color.BLUE);
        pieChartDespesa.setDrawEntryLabels(false);

        pieChartDespesa.setExtraOffsets(-80, 5, 1, 5);

//        pieChart.setUsePercentValues(false);
        pieChartDespesa.animateX(1000);

        Legend l = pieChartDespesa.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(18f);
//        l.setWordWrapEnabled(false);
//        l.setEnabled(false);

//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(7f);
//        l.setYOffset(0f);

//        pieChart.setEntryLabelTypeface(Typeface.DEFAULT);
//        pieChart.setEntryLabelTextSize(12f);

//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
////        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(8);
//        l.setYEntrySpace(5);

    }

    private void graficoPieChartRenda(){

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(430, "Salario"));
        pieEntries.add(new PieEntry(530, "Servicos"));
        pieEntries.add(new PieEntry(330, "Presente"));
        pieEntries.add(new PieEntry(230, "Aluguel"));
        pieEntries.add(new PieEntry(630, "Outros"));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(18f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChartRenda));
//        pieData.setDrawValues(false);

        pieChartRenda.setData(pieData);
        pieChartRenda.getDescription().setEnabled(false);
        pieChartRenda.setUsePercentValues(true);
        pieChartRenda.setCenterText("R$");
        pieChartRenda.setCenterTextSize(18f);

        pieChartRenda.setHoleRadius(40);
        pieChartRenda.setTransparentCircleRadius(10);
        pieChartRenda.setEntryLabelColor(Color.BLUE);
        pieChartRenda.setDrawEntryLabels(false);

        pieChartRenda.setExtraOffsets(-100, 5, 1, 5);

//        pieChart.setUsePercentValues(false);
        pieChartRenda.animateX(1000);

        Legend l = pieChartRenda.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(18f);
//        l.setWordWrapEnabled(false);
//        l.setEnabled(false);

//        l.setDrawInside(false);
//        l.setXEntrySpace(7f);
//        l.setYEntrySpace(7f);
//        l.setYOffset(0f);

//        pieChart.setEntryLabelTypeface(Typeface.DEFAULT);
//        pieChart.setEntryLabelTextSize(12f);

//        l.setOrientation(Legend.LegendOrientation.VERTICAL);
////        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
//        l.setXEntrySpace(8);
//        l.setYEntrySpace(5);

    }
}