package com.example.poket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.ContaDAO;
import com.example.poket.DAO.UsuarioDAO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.R;
import com.example.poket.util.Utilitario;
import com.example.poket.view.conta.AdicionarConta;
import com.example.poket.view.conta.ListaConta;
import com.example.poket.view.despesa.AdicionarDespesa;
import com.example.poket.view.despesa.ListaDespesa;
import com.example.poket.view.planejamento.ListagemPlanejamentoFinanceiro;
import com.example.poket.view.renda.AdicionarRenda;
import com.example.poket.view.renda.ListaRenda;
import com.example.poket.view.usuario.EditarUsuario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;

public class Home extends AppCompatActivity {

    TextView textViewApelido, textViewIdConta, textViewContaValor;

    Spinner spinnerConta;
    ImageView imageViewEditarUsuario, imageViewConta, imageViewSair,
    imageViewHistoricoDespesa, imageViewAdicionarDespesa,
    imageViewHistoricoRenda, imageViewAdicionarRenda;

    Intent intentEditarUsuario, intentListaConta,
            intentAdicionarDespesa, intentAdicionarRenda,
            intentListaDespesa, intentListaRenda;

    Button buttonPF;

    BarChart barChart;
    PieChart pieChartDespesa;
    PieChart pieChartRenda;

    UsuarioDAO dao = new UsuarioDAO();
    UsuarioDTO dto = dao.obterUsuario();
    ContaDAO daoC = new ContaDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        textViewTst = findViewById(R.id.textViewTst);
        textViewIdConta = findViewById(R.id.textViewHomeIdConta);

        textViewApelido = findViewById(R.id.textViewHomeApelido);
        textViewContaValor = findViewById(R.id.textViewHomeContaValor);

        imageViewEditarUsuario = findViewById(R.id.imageViewHomeEdit);
        imageViewConta = findViewById(R.id.imageViewHomeConta);
        imageViewSair = findViewById(R.id.imageViewHomeSair);
        spinnerConta = findViewById(R.id.spinnerHomeContas);
        imageViewHistoricoDespesa = findViewById(R.id.imageViewHomeHistoricoDespesa);
        imageViewAdicionarDespesa = findViewById(R.id.imageViewHomeAdicionarDespesaAdicionar);
        imageViewHistoricoRenda = findViewById(R.id.imageViewHomeHistoricoRenda);
        imageViewAdicionarRenda = findViewById(R.id.imageViewHomeAdicionarRendaAdicionar);
        buttonPF = findViewById(R.id.buttonHomePF);

        barChart = findViewById(R.id.barChartHomeDR);
        pieChartDespesa = findViewById(R.id.pieChartHomeDespesa);
        pieChartRenda = findViewById(R.id.pieChartHomeRenda);

        dao.graficoBarChartDespesaRenda(barChart);
        dao.graficoPieChartDespesa(pieChartDespesa);
        dao.graficoPieChartRenda(pieChartRenda);

        try {
            new Thread().sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        daoC.listaContaSpinner(spinnerConta, Home.this, textViewContaValor, textViewIdConta, true);

//        textViewTst.setText(dto.getUid());
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

        imageViewAdicionarDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selecaoConta = spinnerConta.getSelectedItem().toString();

                if(selecaoConta.equals(".:Sem Conta:.")){
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Home.this);
                    dialogo.setMessage("Insira uma conta para cadastrar uma despesa.");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this, AdicionarConta.class);
                            startActivity(intent);
                        }
                    });
                    dialogo.setTitle("Aviso");
                    dialogo.show();
                }else{
                            intentAdicionarDespesa = new Intent(Home.this, AdicionarDespesa.class);
                            startActivity(intentAdicionarDespesa);
                }
            }
        });

        imageViewHistoricoDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selecaoConta = spinnerConta.getSelectedItem().toString();

                if(selecaoConta.equals(".:Sem Conta:.")){
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Home.this);
                    dialogo.setMessage("Insira uma conta para visualizar as despesas.");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this, AdicionarConta.class);
                            startActivity(intent);
                        }
                    });
                    dialogo.setTitle("Aviso");
                    dialogo.show();
                }else{
                          intentListaDespesa = new Intent(Home.this, ListaDespesa.class);
                          startActivity(intentListaDespesa);
                }
            }
        });

        imageViewAdicionarRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selecaoConta = spinnerConta.getSelectedItem().toString();

                if(selecaoConta.equals(".:Sem Conta:.")){
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Home.this);
                    dialogo.setMessage("Insira uma conta para cadastrar uma renda.");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this, AdicionarConta.class);
                            startActivity(intent);
                        }
                    });
                    dialogo.setTitle("Aviso");
                    dialogo.show();
                }else{
                intentAdicionarRenda = new Intent(Home.this, AdicionarRenda.class);
                startActivity(intentAdicionarRenda);
                }
            }
        });

        imageViewHistoricoRenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selecaoConta = spinnerConta.getSelectedItem().toString();

                if(selecaoConta.equals(".:Sem Conta:.")){
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Home.this);
                    dialogo.setMessage("Insira uma conta para visualizar as rendas.");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this, AdicionarConta.class);
                            startActivity(intent);
                        }
                    });
                    dialogo.setTitle("Aviso");
                    dialogo.show();
                }else{
                    intentListaRenda = new Intent(Home.this, ListaRenda.class);
                    startActivity(intentListaRenda);
                }
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

        buttonPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selecaoConta = spinnerConta.getSelectedItem().toString();

                if(selecaoConta.equals(".:Sem Conta:.")){
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(Home.this);
                    dialogo.setMessage("Insira uma conta para cadastrar um planejamento financeiro.");
                    dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Home.this, AdicionarConta.class);
                            startActivity(intent);
                        }
                    });
                    dialogo.setTitle("Aviso");
                    dialogo.show();
                }else{
                Intent intentListagemPF = new Intent(Home.this, ListagemPlanejamentoFinanceiro.class);
                startActivity(intentListagemPF);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinnerConta = findViewById(R.id.spinnerHomeContas);
        textViewContaValor = findViewById(R.id.textViewHomeContaValor);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, Home.this, textViewContaValor, textViewIdConta, true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        textViewApelido = findViewById(R.id.textViewHomeApelido);
        textViewApelido.setText(user.getDisplayName());

        barChart = findViewById(R.id.barChartHomeDR);
        dao.graficoBarChartDespesaRenda(barChart);

        pieChartDespesa = findViewById(R.id.pieChartHomeDespesa);
        dao.graficoPieChartDespesa(pieChartDespesa);

        pieChartRenda = findViewById(R.id.pieChartHomeRenda);
        dao.graficoPieChartRenda(pieChartRenda);
    }
}