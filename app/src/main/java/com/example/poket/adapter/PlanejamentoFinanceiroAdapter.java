package com.example.poket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DAO.PlanejamentoFinanceiroDAO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.EditarDespesa;
import com.example.poket.view.planejamento.EditarPlanejamentoFinanceiro;
import com.example.poket.view.planejamento.ListaPlanejamentoFinanceiro;

import java.util.ArrayList;
import java.util.List;

public class PlanejamentoFinanceiroAdapter extends RecyclerView.Adapter<PlanejamentoFinanceiroAdapter.ViewHolder>{

    Context context;

    List<String> idPFList = new ArrayList<>();
    List<String> nomePFList = new ArrayList<>();
    List<String> tipoPFList = new ArrayList<>();
    List<String> valorAtualList = new ArrayList<>();
    List<String> valorObjetivadoList = new ArrayList<>();
    List<String> dataInicialList = new ArrayList<>();
    List<String> dataFinalList = new ArrayList<>();

    Activity activity;
    View mView;

    View viewOnCreate;
    PlanejamentoFinanceiroAdapter.ViewHolder viewHolderLocal;

    public PlanejamentoFinanceiroAdapter(Context context,
                          List<String> idPFList, List<String> nomePFList, List<String> tipoPFList,
                          List<String> valorAtualList, List<String> valorObjetivadoList,
                          List<String>  dataIncialList,List<String>  dataFinalList,
                          Activity activity, View mView){
        this.context = context;
        this.idPFList.addAll(idPFList);
        this.nomePFList.addAll(nomePFList);
        this.tipoPFList.addAll(tipoPFList);
        this.valorAtualList.addAll(valorAtualList);
        this.valorObjetivadoList.addAll(valorObjetivadoList);
        this.dataInicialList.addAll(dataIncialList);
        this.dataFinalList.addAll(dataFinalList);
        this.activity = activity;
        this.mView = mView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewIdPF, textViewNomePF, textViewTipoPF, textViewValorAtual, textViewDataFinal;
        public ImageView imageViewAdicionarValor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIdPF = itemView.findViewById(R.id.textViewRecyclerListagemPFIDPF);
            textViewNomePF = itemView.findViewById(R.id.textViewRecyclerListagemPFNomePF);
            textViewTipoPF = itemView.findViewById(R.id.textViewRecyclerListagemPFTipoPF);
            textViewValorAtual = itemView.findViewById(R.id.textViewRecyclerListagemPFValorAtual);
            textViewDataFinal = itemView.findViewById(R.id.textViewRecyclerListagemPFDataFinal);

            imageViewAdicionarValor = itemView.findViewById(R.id.imageViewRecyclerListagemPFAdicionarValorPF);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(Msg.INFO, "Click");
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
        holder.textViewDataFinal.setText(Utilitario.convertUsaToBr(dataFinalList.get(position)));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListaPlanejamentoFinanceiro.class);
                intent.putExtra("idPF", idPFList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.imageViewAdicionarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("---", idPFList.get(position));
                PlanejamentoFinanceiroDAO dao = new PlanejamentoFinanceiroDAO();

                dao.adicionarValorPF(activity, mView, idPFList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return idPFList.size();
    }
}
