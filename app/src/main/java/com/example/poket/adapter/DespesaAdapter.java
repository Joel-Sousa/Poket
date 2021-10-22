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
import com.example.poket.view.conta.EditarConta;
import com.example.poket.view.despesa.EditarDespesa;

import java.util.ArrayList;
import java.util.List;

public class DespesaAdapter extends RecyclerView.Adapter<DespesaAdapter.ViewHolder>{

    Context context;

    List<String> idList = new ArrayList<>();
    List<String> despesaList = new ArrayList<>();
    List<String> valorDespesaList = new ArrayList<>();
    List<String> tipoDespesaList = new ArrayList<>();
    List<String> dataDespesaList = new ArrayList<>();
    List<String> observacaoList = new ArrayList<>();

    List<String> idContaList = new ArrayList<>();
    List<String> contaList = new ArrayList<>();
//    List<String> contaValorList = new ArrayList<>();

    View viewOnCreate;
    DespesaAdapter.ViewHolder viewHolderLocal;

    public DespesaAdapter(Context context,
                          List<String> idList, List<String> despesaList,List<String>  valorDespesaList,
                          List<String>  tipoDespesaList, List<String>  dataDespesaList,
                          List<String>  observacaoList,
                          List<String> idContaList, List<String> contaList){

        this.context = context;
        this.idList.addAll(idList);
        this.despesaList.addAll(despesaList);
        this.valorDespesaList.addAll(valorDespesaList);
        this.tipoDespesaList.addAll(tipoDespesaList);
        this.dataDespesaList.addAll(dataDespesaList);
        this.observacaoList.addAll(observacaoList);

        this.idContaList.addAll(idContaList);
        this.contaList.addAll(contaList);
//        this.contaValorList.addAll(contaValorList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewId, textViewDespesa, textViewConta, textViewValorDespesa;
        public ImageView imageViewEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textViewRecyclerDespesaUid);
            textViewDespesa = itemView.findViewById(R.id.textViewRecyclerDespesaDespesa);
            textViewConta = itemView.findViewById(R.id.textViewRecyclerDespesaConta);
            textViewValorDespesa = itemView.findViewById(R.id.textViewRecyclerDespesaValorDespesa);
            imageViewEditar = itemView.findViewById(R.id.imageViewListaDespesaEditar);

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
    public DespesaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_listadespesa_item,
                parent, false);
        viewHolderLocal = new DespesaAdapter.ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull DespesaAdapter.ViewHolder holder, int position) {

        holder.textViewId.setText(idList.get(position));
        holder.textViewDespesa.setText(despesaList.get(position));
        holder.textViewConta.setText(contaList.get(position));
        holder.textViewValorDespesa.setText(valorDespesaList.get(position));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Msg.INFO, "Click1");
            }
        });

        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditarDespesa.class);
                intent.putExtra("id", idList.get(position));
                intent.putExtra("despesa", despesaList.get(position));
                intent.putExtra("valorDespesa", valorDespesaList.get(position));
                intent.putExtra("tipoDespesa", tipoDespesaList.get(position));
                intent.putExtra("dataDespesa", dataDespesaList.get(position));
                intent.putExtra("observacao", observacaoList.get(position));

                intent.putExtra("idConta", idContaList.get(position));
                intent.putExtra("conta", contaList.get(position));
//                intent.putExtra("valorConta", contaValorList.get(position));

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
