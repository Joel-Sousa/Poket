package com.example.poket.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.view.despesa.EditarDespesa;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPFAdapter extends RecyclerView.Adapter<HistoricoPFAdapter.ViewHolder>{

    Context context;
    Activity activity;

    List<String> idHistoricoList = new ArrayList<>();
    List<String> idPFList = new ArrayList<>();
    List<String> idContaList = new ArrayList<>();
    List<String> nomeContaList = new ArrayList<>();
    List<String> valorContaList = new ArrayList<>();
    List<String> dataHistoricoList = new ArrayList<>();

    View viewOnCreate;
    HistoricoPFAdapter.ViewHolder viewHolderLocal;

    public HistoricoPFAdapter(Context context, Activity activity,
                          List<String> idHistoricoList, List<String> idPFList, List<String> idContaList,
                          List<String> nomeContaList, List<String> valorContaList, List<String> dataHistoricoList){
        this.context = context;
        this.activity = activity;
        this.idHistoricoList.addAll(idHistoricoList);
        this.idPFList.addAll(idPFList);
        this.idContaList.addAll(idContaList);
        this.nomeContaList.addAll(nomeContaList);
        this.valorContaList.addAll(valorContaList);
        this.dataHistoricoList.addAll(dataHistoricoList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewIdHistorico, textViewIdPF, textViewIdConta, textViewNomeConta,
                textViewValorConta, textViewdataHistorico;
        public ImageView imageViewExcluir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIdHistorico = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdHistorico);
            textViewIdPF = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdPF);
            textViewIdConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdConta);
            textViewNomeConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoNomeConta);
            textViewValorConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoValorConta);
            textViewdataHistorico = itemView.findViewById(R.id.textViewRecyclerListaHistoricoDataHistorico);
            imageViewExcluir = itemView.findViewById(R.id.imageViewRecyclerListaHistoricoExcluir);


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
    public HistoricoPFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_listahistorico_item,
                parent, false);
        viewHolderLocal = new HistoricoPFAdapter.ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoPFAdapter.ViewHolder holder, int position) {

        holder.textViewIdHistorico.setText(idHistoricoList.get(position));
        holder.textViewIdPF.setText(idPFList.get(position));
        holder.textViewIdConta.setText(idContaList.get(position));
        holder.textViewNomeConta.setText(nomeContaList.get(position));
        holder.textViewValorConta.setText(valorContaList.get(position));
        holder.textViewdataHistorico.setText(dataHistoricoList.get(position));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Msg.INFO, "Click1");
            }
        });

        holder.imageViewExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmacao = new AlertDialog.Builder(activity);
                confirmacao.setTitle("Atencao!");
                confirmacao.setMessage("Você tem certeza que deseja excluir esse dado?");
                confirmacao.setCancelable(false);
                confirmacao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // TODO IMPLEMENTAR A EXCLUSAO DO HISTORICO
                        Toast.makeText(context, idHistoricoList.get(position)+"", Toast.LENGTH_LONG).show();

                        activity.finish();
                    }
                });
                confirmacao.setNegativeButton("Nao",null);
                confirmacao.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return idHistoricoList.size();
    }
}
