package com.example.poket.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.R;
import com.example.poket.util.Msg;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPFAdapter extends RecyclerView.Adapter<HistoricoPFAdapter.ViewHolder>{

    Context context;

    List<String> idHistoricoList = new ArrayList<>();
    List<String> idPFList = new ArrayList<>();
    List<String> idContaList = new ArrayList<>();
    List<String> nomeContaList = new ArrayList<>();
    List<String> valorContaList = new ArrayList<>();

    View viewOnCreate;
    HistoricoPFAdapter.ViewHolder viewHolderLocal;

    public HistoricoPFAdapter(Context context,
                          List<String> idHistoricoList, List<String> idPFList, List<String> idContaList,
                          List<String> nomeContaList, List<String> valorContaList){
        this.context = context;
        this.idHistoricoList.addAll(idHistoricoList);
        this.idPFList.addAll(idPFList);
        this.idContaList.addAll(idContaList);
        this.nomeContaList.addAll(nomeContaList);
        this.valorContaList.addAll(valorContaList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textViewIdHistorico, textViewIdPF, textViewIdConta, textViewNomeConta,
                textViewValorConta;
//        public ImageView imageViewEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewIdHistorico = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdHistorico);
            textViewIdPF = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdPF);
            textViewIdConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoIdConta);
            textViewNomeConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoNomeConta);
            textViewValorConta = itemView.findViewById(R.id.textViewRecyclerListaHistoricoValorConta);
//            imageViewEditar = itemView.findViewById(R.id.imageViewListaDespesaEditar);

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

        viewOnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Msg.INFO, "Click1");
            }
        });

//        holder.imageViewEditar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, EditarDespesa.class);
//                intent.putExtra("id", idList.get(position));
//                intent.putExtra("despesa", despesaList.get(position));
//                intent.putExtra("idConta", idContaList.get(position));
//                intent.putExtra("conta", contaList.get(position));
//                intent.putExtra("contaValor", contaValorList.get(position));
//                intent.putExtra("valorDespesa", valorDespesaList.get(position));
//                intent.putExtra("tipoDespesa", tipoDespesaList.get(position));
//                intent.putExtra("dataDespesa", dataDespesaList.get(position));
//                intent.putExtra("observacao", observacaoList.get(position));
////                intent.putExtra("despesaFixa", despesaFixaList.get(position));
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return idHistoricoList.size();
    }
}
