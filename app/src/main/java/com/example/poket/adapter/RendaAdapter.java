package com.example.poket.adapter;

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

import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.view.despesa.VerDespesa;
import com.example.poket.view.renda.EditarRenda;
import com.example.poket.view.renda.VerRenda;

import java.util.ArrayList;
import java.util.List;

public class RendaAdapter extends RecyclerView.Adapter<RendaAdapter.ViewHolder>{

    Context context;

    List<String> idList = new ArrayList<>();
    List<String> rendaList = new ArrayList<>();
    List<String> valorRendaList = new ArrayList<>();
    List<String> tipoRendaList = new ArrayList<>();
    List<String> dataRendaList = new ArrayList<>();
    List<String> observacaoList = new ArrayList<>();

    List<String> idContaList = new ArrayList<>();
    List<String> contaList = new ArrayList<>();

    View viewOnCreate;
    RendaAdapter.ViewHolder viewHolderLocal;

    public RendaAdapter(Context context,
                        List<String> idList, List<String> rendaList,List<String>  valorRendaList,
                        List<String>  tipoRendaList, List<String>  dataRendaList,
                        List<String>  observacaoList,
                        List<String> idContaList, List<String> contaList){

        this.context = context;
        this.idList.addAll(idList);
        this.rendaList.addAll(rendaList);
        this.valorRendaList.addAll(valorRendaList);
        this.tipoRendaList.addAll(tipoRendaList);
        this.dataRendaList.addAll(dataRendaList);
        this.observacaoList.addAll(observacaoList);

        this.idContaList.addAll(idContaList);
        this.contaList.addAll(contaList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewId, textViewRenda, textViewConta, textViewValorRenda;
        public ImageView imageViewEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textViewRecyclerRendaUid);
            textViewRenda = itemView.findViewById(R.id.textViewRecyclerRendaRenda);
            textViewConta = itemView.findViewById(R.id.textViewRecyclerRendaConta);
            textViewValorRenda = itemView.findViewById(R.id.textViewRecyclerRendaValorRenda);
            imageViewEditar = itemView.findViewById(R.id.imageViewListaRendaEditar);

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
    public RendaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_listarenda_item,
                parent, false);
        viewHolderLocal = new RendaAdapter.ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull RendaAdapter.ViewHolder holder, int position) {

        holder.textViewId.setText(idList.get(position));
        holder.textViewRenda.setText(rendaList.get(position));
        holder.textViewConta.setText(contaList.get(position));
        holder.textViewValorRenda.setText(valorRendaList.get(position));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VerRenda.class);
                intent.putExtra("id", idList.get(position));
//                intent.putExtra("nomePF", nomePFList.get(position));
//                intent.putExtra("tipoPF", tipoPFList.get(position));
//                intent.putExtra("valorAtual", valorAtualList.get(position));
//                intent.putExtra("valorObjetivado", valorObjetivadoList.get(position));
//                intent.putExtra("dataInicial", dataInicialList.get(position));
//                intent.putExtra("dataFinal", dataFinalList.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
//                Log.d(Msg.INFO, "Click1");
            }
        });

        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditarRenda.class);
                intent.putExtra("id", idList.get(position));
                intent.putExtra("renda", rendaList.get(position));
                intent.putExtra("valorRenda", valorRendaList.get(position));
                intent.putExtra("tipoRenda", tipoRendaList.get(position));
                intent.putExtra("dataRenda", dataRendaList.get(position));
                intent.putExtra("observacao", observacaoList.get(position));

                intent.putExtra("idConta", idContaList.get(position));
                intent.putExtra("conta", contaList.get(position));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return idList.size();
    }
}
