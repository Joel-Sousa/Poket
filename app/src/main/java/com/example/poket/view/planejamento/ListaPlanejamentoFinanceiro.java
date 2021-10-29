package com.example.poket.view.planejamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;
import com.example.poket.util.Msg;

public class ListaPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewIdPF, textViewNomePF, textViewTipoPF, textViewValorAtual,
             textViewValorObjetivado, textViewDataInicio, textViewDataFinal,
             textViewPorcentagemValor, textViewPorcentagemData;
    ProgressBar progressBarValor, progressBarData;
    Button buttonEditar, buttonExcluir;

    ImageView imageViewVoltar, imageViewHistoricoPF;

    String idPF = "";

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planejamento_financeiro);

        textViewIdPF = findViewById(R.id.textViewListaPFId);
        textViewNomePF = findViewById(R.id.textViewListaPFNomePF);
        textViewTipoPF = findViewById(R.id.textViewListaPFTipoPF);
        textViewValorAtual = findViewById(R.id.textViewListaPFValorAtual);
        textViewValorObjetivado = findViewById(R.id.textViewListaPFValorObjetivado);
        textViewDataInicio = findViewById(R.id.textViewListaPFDataInicio);
        textViewDataFinal = findViewById(R.id.textViewListarPFDataFinal);

        textViewPorcentagemValor = findViewById(R.id.textViewListarPFPorcentagemValor);
        textViewPorcentagemData = findViewById(R.id.textViewListarPFPorcentagemData);
        progressBarValor = findViewById(R.id.progressBarListarPFValor);
        progressBarData = findViewById(R.id.progressBarListarPFData);

        imageViewVoltar = findViewById(R.id.imageViewListaPFVoltar);
        imageViewHistoricoPF = findViewById(R.id.imageViewListarPFHistorico);

        buttonEditar = findViewById(R.id.buttonListaPFEditar);
        buttonExcluir = findViewById(R.id.buttonListaPFExcluir);

        Intent intent = getIntent();
        idPF = intent.getStringExtra("idPF");
        textViewIdPF.setText(idPF);

//        dao.visualizarPlanejamentoFinanceiro(idPF, textViewTipoPF, textViewNomePF, textViewValorAtual,
//                textViewValorObjetivado, textViewDataInicio, textViewDataFinal ,textViewPorcentagemValor,
//                textViewPorcentagemData, progressBarValor, progressBarData);

        imageViewHistoricoPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentListaHistorico = new Intent(ListaPlanejamentoFinanceiro.this, ListaHistoricoPF.class);
                intentListaHistorico.putExtra("idPF", idPF);
                intentListaHistorico.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentListaHistorico);
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditarPF = new Intent(ListaPlanejamentoFinanceiro.this, EditarPlanejamentoFinanceiro.class);
                intentEditarPF.putExtra("idPF", textViewIdPF.getText().toString());
                intentEditarPF.putExtra("nomePF", textViewNomePF.getText().toString());
                intentEditarPF.putExtra("tipoPF", textViewTipoPF.getText().toString());
                intentEditarPF.putExtra("valorAtual", textViewValorAtual.getText().toString());
                intentEditarPF.putExtra("valorObjetivado", textViewValorObjetivado.getText().toString());
                intentEditarPF.putExtra("dataInicio", textViewDataInicio.getText().toString());
                intentEditarPF.putExtra("dataFinal", textViewDataFinal.getText().toString());
                intentEditarPF.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentEditarPF);
            }
        });

        buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(ListaPlanejamentoFinanceiro.this);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dao.deletarPF(idPF);
                        dao.deletarPF(textViewIdPF.getText().toString(), ListaPlanejamentoFinanceiro.this);
                        Toast.makeText(getApplicationContext(), Msg.DELETADO, Toast.LENGTH_LONG).show();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
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

//        dao.visualizarPlanejamentoFinanceiro(idPF, textViewTipoPF, textViewNomePF, textViewValorAtual,
//                textViewValorObjetivado, textViewDataInicio, textViewDataFinal ,textViewPorcentagemValor,
//                textViewPorcentagemData, progressBarValor, progressBarData);
    }
}