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
import com.example.poket.view.despesa.EditarDespesa;

public class ListaPlanejamentoFinanceiro extends AppCompatActivity {

    TextView textViewId, textViewTipoPF, textViewPFPF, textViewValorAtual, textViewValorObjetivado,
            textViewDataInicio, textViewDataFinal, textViewPorcInicio, textViewPorcFinal;
    ProgressBar progressBarConcluido, progressBarRestante;
    Button buttonEditar, buttonExcluir;

    ImageView imageViewVoltar, imageViewHistoricoPF;

    String id = "";
    String tipoPF = "";

    PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_planejamento_financeiro);

        textViewId = findViewById(R.id.textViewListaPFId);
//        textViewIdConta = findViewById(R.id.textViewListaPFIdConta);

        textViewTipoPF = findViewById(R.id.textViewListaPFTipoPF);
        textViewPFPF = findViewById(R.id.textViewListaPFPlanejamentoF);
        textViewValorAtual = findViewById(R.id.textViewListaPFValorAtual);
        textViewValorObjetivado = findViewById(R.id.textViewListaPFValorObjetivado);
        textViewDataInicio = findViewById(R.id.textViewListaPFDataInicio);
        progressBarConcluido = findViewById(R.id.progressBarListarPFConcluido);
        textViewPorcInicio = findViewById(R.id.textViewListarPFPorcentagemInicio);
        textViewDataFinal = findViewById(R.id.textViewListarPFDataFinal);
        progressBarRestante = findViewById(R.id.progressBarListarPFResta);
        textViewPorcFinal = findViewById(R.id.textViewListarPFPorcentagemResta);

        imageViewVoltar = findViewById(R.id.imageViewListaPFVoltar);
        imageViewHistoricoPF = findViewById(R.id.imageViewListarPFHistorico);
        buttonEditar = findViewById(R.id.buttonListaPFEditar);
        buttonExcluir = findViewById(R.id.buttonListaPFExcluir);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        tipoPF = intent.getStringExtra("tipoPF");
        textViewId.setText(id);

        dao.lerPlanejamentoFinanceiro(id, textViewTipoPF, textViewPFPF, textViewValorAtual,
                textViewValorObjetivado, textViewDataInicio, progressBarConcluido,
                textViewPorcInicio, textViewDataFinal, progressBarRestante, textViewPorcFinal);

        imageViewHistoricoPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentListaHistorico = new Intent(ListaPlanejamentoFinanceiro.this, ListaHistoricoPF.class);
                intentListaHistorico.putExtra("idPF", id);
                intentListaHistorico.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentListaHistorico);
            }
        });

        buttonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentEditarPF = new Intent(ListaPlanejamentoFinanceiro.this, EditarPlanejamentoFinanceiro.class);
                intentEditarPF.putExtra("id", textViewId.getText().toString());
                intentEditarPF.putExtra("PF", textViewPFPF.getText().toString());
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
                        dao.deletarPF(textViewId.getText().toString(), ListaPlanejamentoFinanceiro.this);
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

        dao.lerPlanejamentoFinanceiro(id, textViewTipoPF, textViewPFPF, textViewValorAtual,
                textViewValorObjetivado, textViewDataInicio, progressBarConcluido,
                textViewPorcInicio, textViewDataFinal, progressBarRestante, textViewPorcFinal);
    }
}