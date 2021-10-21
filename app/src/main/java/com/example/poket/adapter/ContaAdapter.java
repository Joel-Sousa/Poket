package com.example.poket.adapter;

import android.content.Context;
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

import com.example.poket.DTO.ContaDTO;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.view.conta.EditarConta;
import com.example.poket.view.planejamento.ListaPlanejamentoFinanceiro;

import java.util.ArrayList;
import java.util.List;

public class ContaAdapter extends RecyclerView.Adapter<ContaAdapter.ViewHolder>{

    Context context;

    List<String> idList = new ArrayList<>();
    List<String> contaList = new ArrayList<>();
    List<String> valorList = new ArrayList<>();

    View viewOnCreate;
    ViewHolder viewHolderLocal;

    public ContaAdapter(Context context, List<String> idList, List<String> contaList,
                        List<String> valorList){
        this.context = context;
        this.idList.addAll(idList);
        this.contaList.addAll(contaList);
        this.valorList.addAll(valorList);
    }

public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     public TextView textViewId, textViewConta, textViewValor;
     public ImageView imageViewEditar;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        textViewId = itemView.findViewById(R.id.textViewRecyclerContaUid);
        textViewConta = itemView.findViewById(R.id.textViewRecyclerContaConta);
        textViewValor = itemView.findViewById(R.id.textViewRecyclerContaValor);
        imageViewEditar = itemView.findViewById(R.id.imageViewListaContaEditar);

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
    public ContaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewOnCreate = LayoutInflater.from(context).inflate(R.layout.recyclerview_listaconta_item,
                parent, false);
        viewHolderLocal = new ViewHolder(viewOnCreate);
        return viewHolderLocal;
    }

    @Override
    public void onBindViewHolder(@NonNull ContaAdapter.ViewHolder holder, int position) {

        holder.textViewId.setText(idList.get(position));
        holder.textViewConta.setText(contaList.get(position));
        holder.textViewValor.setText(valorList.get(position));

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Msg.INFO, "Click1");
            }
        });

        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditarConta.class);
                intent.putExtra("uidc", idList.get(position));
                intent.putExtra("conta", contaList.get(position));
                intent.putExtra("valor", valorList.get(position));
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
