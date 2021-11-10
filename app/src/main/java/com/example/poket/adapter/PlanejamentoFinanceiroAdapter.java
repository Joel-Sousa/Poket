package com.example.poket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.EditarDespesa;
import com.example.poket.view.planejamento.EditarPlanejamentoFinanceiro;
import com.example.poket.view.planejamento.ListaHistoricoPF;
import com.example.poket.view.planejamento.ListaPlanejamentoFinanceiro;

import java.util.ArrayList;
import java.util.List;

import io.grpc.okhttp.internal.Util;

public class PlanejamentoFinanceiroAdapter extends RecyclerView.Adapter<PlanejamentoFinanceiroAdapter.ViewHolder>{

    Context context;

    List<String> idPFList = new ArrayList<>();
    List<String> nomePFList = new ArrayList<>();
    List<String> tipoPFList = new ArrayList<>();
    List<String> valorAtualList = new ArrayList<>();
    List<String> valorObjetivadoList = new ArrayList<>();
    List<String> dataInicialList = new ArrayList<>();
    List<String> dataFinalList = new ArrayList<>();

    List<String> porcentagemValorList = new ArrayList<>();
    List<String> porcentagemDataList = new ArrayList<>();

    List<Integer> porcentagemBarValorList = new ArrayList<>();
    List<Integer> porcentagemBarDataList = new ArrayList<>();

    Activity activity;
    View mView;

    View viewOnCreate;
    PlanejamentoFinanceiroAdapter.ViewHolder viewHolderLocal;

    public PlanejamentoFinanceiroAdapter(Context context,
                          List<String> idPFList, List<String> nomePFList, List<String> tipoPFList,
                          List<String> valorAtualList, List<String> valorObjetivadoList,
                          List<String>  dataIncialList,List<String>  dataFinalList,
                          List<String> porcentagemValorList, List<String> porcentagemDataList,
                          List<Integer> porcentagemBarValorList, List<Integer> porcentagemBarDataList,
                          Activity activity, View mView){
        this.context = context;
        this.idPFList.addAll(idPFList);
        this.nomePFList.addAll(nomePFList);
        this.tipoPFList.addAll(tipoPFList);
        this.valorAtualList.addAll(valorAtualList);
        this.valorObjetivadoList.addAll(valorObjetivadoList);
        this.dataInicialList.addAll(dataIncialList);
        this.dataFinalList.addAll(dataFinalList);

        this.porcentagemValorList.addAll(porcentagemValorList);
        this.porcentagemDataList.addAll(porcentagemDataList);

        this.porcentagemBarValorList.addAll(porcentagemBarValorList);
        this.porcentagemBarDataList.addAll(porcentagemBarDataList);

        this.activity = activity;
        this.mView = mView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewIdPF, textViewNomePF, textViewTipoPF, textViewValorAtual,
                textViewValorObjetivado, textViewDataInicial,textViewDataFinal,
                textViewPorcentagemValor, textViewPorcentagemData;
        public ProgressBar progressBarValor, progressBarData;

        public ImageView imageViewAdicionarValor, imageViewEditar, imageViewHistoricoPF;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIdPF = itemView.findViewById(R.id.textViewRecyclerListagemPFIDPF);
            textViewNomePF = itemView.findViewById(R.id.textViewRecyclerListagemPFNomePF);
            textViewTipoPF = itemView.findViewById(R.id.textViewRecyclerListagemPFTipoPF);
            textViewValorAtual = itemView.findViewById(R.id.textViewRecyclerListagemPFValorAtual);
            textViewValorObjetivado = itemView.findViewById(R.id.textViewRecyclerListagemPFValorObjetivado);
            textViewDataInicial = itemView.findViewById(R.id.textViewRecyclerListagemPFDataInicial);
            textViewDataFinal = itemView.findViewById(R.id.textViewRecyclerListagemPFDataFinal);

            textViewPorcentagemValor = itemView.findViewById(R.id.textViewRecyclerListagemPFPorcentagemValor);
            textViewPorcentagemData = itemView.findViewById(R.id.textViewRecyclerListagemPFPorcentagemData);

            progressBarValor = itemView.findViewById(R.id.progressBarRecyclerListagemPFValor);
            progressBarData = itemView.findViewById(R.id.progressBarRecyclerListagemPFData);

            imageViewAdicionarValor = itemView.findViewById(R.id.imageViewRecyclerListagemPFAdicionarValorPF);
            imageViewEditar = itemView.findViewById(R.id.imageViewRecyclerListagemPFEditar);
            imageViewHistoricoPF = itemView.findViewById(R.id.imageViewRecyclerListagemPFHistoricoPF);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }

    @NonNull
    @Override
    public PlanejamentoFinanceiroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_listagempf_item,
                parent, false);
        viewHolderLocal = new PlanejamentoFinanceiroAdapter.ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanejamentoFinanceiroAdapter.ViewHolder holder, int position) {

        holder.textViewIdPF.setText(idPFList.get(position));
        holder.textViewNomePF.setText(nomePFList.get(position));
        holder.textViewTipoPF.setText(tipoPFList.get(position));
        holder.textViewValorAtual.setText(valorAtualList.get(position));
        holder.textViewValorObjetivado.setText(valorObjetivadoList.get(position));
        holder.textViewDataInicial.setText(Utilitario.convertUsaToBr(dataInicialList.get(position)));
        holder.textViewDataFinal.setText(Utilitario.convertUsaToBr(dataFinalList.get(position)));

        holder.textViewPorcentagemValor.setText(porcentagemValorList.get(position));
        holder.textViewPorcentagemData.setText(porcentagemDataList.get(position));

        holder.progressBarValor.setProgress(porcentagemBarValorList.get(position));
        holder.progressBarData.setProgress(porcentagemBarDataList.get(position));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ListaPlanejamentoFinanceiro.class);
//                intent.putExtra("idPF", idPFList.get(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
            }
        });

        holder.imageViewAdicionarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();
                dao.adicionarValorPF(activity, mView, idPFList.get(position));
            }
        });

        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditarPlanejamentoFinanceiro.class);
                intent.putExtra("idPF", idPFList.get(position));
                intent.putExtra("nomePF", nomePFList.get(position));
                intent.putExtra("tipoPF", tipoPFList.get(position));
                intent.putExtra("valorAtual", valorAtualList.get(position));
                intent.putExtra("valorObjetivado", valorObjetivadoList.get(position));
                intent.putExtra("dataInicio", Utilitario.convertUsaToBr(dataInicialList.get(position)));
                intent.putExtra("dataFinal", Utilitario.convertUsaToBr(dataFinalList.get(position)));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.imageViewHistoricoPF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListaHistoricoPF.class);
                intent.putExtra("idPF", idPFList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idPFList.size();
    }
}
