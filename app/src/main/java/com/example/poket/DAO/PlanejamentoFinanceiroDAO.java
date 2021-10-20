package com.example.poket.DAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DTO.ContaDTO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.DTO.HistoricoPFDTO;
import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.MainActivity;
import com.example.poket.R;
import com.example.poket.adapter.ContaAdapter;
import com.example.poket.adapter.DespesaAdapter;
import com.example.poket.adapter.HistoricoPFAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.Home;
import com.example.poket.view.despesa.AdicionarDespesa;
import com.example.poket.view.planejamento.AdicionarPlanejamentoFinanceiro;
import com.example.poket.view.planejamento.EditarPlanejamentoFinanceiro;
import com.example.poket.view.planejamento.ListaPlanejamentoFinanceiro;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.DecimalFormat;


public class PlanejamentoFinanceiroDAO {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public PlanejamentoFinanceiroDAO(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void cadastrarPlanejamentoFinanceiro(PlanejamentoFinanceiroDTO dto, Activity activity){

        Map<String, String> dadosPF = new HashMap<>();
        dadosPF.put("planejamentoFinanceiro", dto.getPlanejamentoFinanceiro());
        dadosPF.put("tipoPF", dto.getTipoPlanejamentoFinanceiro());
        dadosPF.put("valorAtual", dto.getValorAtual());
        dadosPF.put("valorObjetivado", dto.getValorObjetivado());
        dadosPF.put("dataInicial", dto.getDataInicial());
        dadosPF.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .add(dadosPF)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                        Log.d("===", "DocumentSnapshot added with ID: " + documentReference.getId());
                        String idPF =  documentReference.getId();

                        double resultado = Double.valueOf(dto.getContaValor()) - Double.valueOf(dto.getValorAtual());
                        String valorContaTotal = String.valueOf(resultado);

                        db.collection("contas").document(user.getUid()).collection(user.getUid())
                                .document(dto.getIdConta())
                                .update("valor", valorContaTotal);

                        salvarHistoricoPF(idPF, dto.getIdConta(), dto.getConta(), dto.getValorAtual());

                        Utilitario.toast(activity.getApplicationContext(), Msg.CADASTRADO);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
                    }
                });


    }

    private void salvarHistoricoPF(String idPF, String idConta, String nomeConta, String valorAtual){

        Map<String, String> dadosHistoricoPF = new HashMap<>();
        dadosHistoricoPF.put("idPF", idPF);
        dadosHistoricoPF.put("idConta", idConta);
        dadosHistoricoPF.put("nomeConta", nomeConta);
        dadosHistoricoPF.put("valorConta", valorAtual);

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(idPF).collection(idPF).document()
                .set(dadosHistoricoPF)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Msg.SUCESSO, Msg.DOCUMENTO_S);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(Msg.ERROR, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    public void planejamentoFinanceiro(Activity activity, String tipoPF, boolean addpf, View view, Intent intent){

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> tipoPFList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String tipoPF1 = document.getData().get("tipoPF").toString();

                                if(tipoPF1.equals(tipoPF)){
                                    tipoPFList.add(document.getId());
                                    tipoPFList.add(tipoPF1);
                                }
                            }

//                            String id = "";
//                            for (QueryDocumentSnapshot document : task.getResult())
//                                id = document.getId();

                            if(tipoPFList.isEmpty()){
                                Intent intentAdicionarPF = new Intent(activity, AdicionarPlanejamentoFinanceiro.class);
                                intentAdicionarPF.putExtra("tipoPF", tipoPF);
                                intentAdicionarPF.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intentAdicionarPF);

                            }else if(addpf){
                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
                                final TextView textViewIdPF = view.findViewById(R.id.textViewDialogPFUidPF);

                                final TextView textViewIdConta = view.findViewById(R.id.textViewDialogAddPFIdConta);
                                final Spinner spinnerConta = view.findViewById(R.id.spinnerDialogAddPFConta);
                                final EditText editTextValor = view.findViewById(R.id.editTextDialogAddPFValor);
                                final TextView textViewContaValor = view.findViewById(R.id.textViewDialogAddPFValorConta);
                                Button buttonAdicionar = view.findViewById(R.id.buttonDialogAddPFAdicionar);
                                Button buttonVoltar = view.findViewById(R.id.buttonDialogAddPFVoltar);

                                textViewIdPF.setText(tipoPFList.get(0));

                                ContaDAO daoC = new ContaDAO();
                                daoC.listaContaSpinner(spinnerConta, activity, textViewContaValor, textViewIdConta);

                                mBuilder.setView(view);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.show();

                                buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String idPF = tipoPFList.get(0);
                                        String idConta = textViewIdConta.getText().toString();
                                        String nomeConta = spinnerConta.getSelectedItem().toString();
                                        String valorConta = textViewContaValor.getText().toString();
                                        String valorAtual = editTextValor.getText().toString();

                                        adicionarValor(idPF, idConta , nomeConta, valorConta, valorAtual);
                                        Utilitario.toast(activity.getApplicationContext(), "Adicionado");

//                                        activityoverridePendingTransition(0, 0);

                                        activity.overridePendingTransition(0, 0);
                                        activity.finish();
                                        activity.startActivity(intent);

                                        dialog.dismiss();
                                    }
                                });

                                buttonVoltar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }else{
                                Intent intentListarPF = new Intent(activity, ListaPlanejamentoFinanceiro.class);
                                intentListarPF.putExtra("id", tipoPFList.get(0));
                                intentListarPF.putExtra("tipoPF", tipoPF);
                                intentListarPF.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intentListarPF);
                            }

                        } else {
                            Log.w(Msg.ERRORM, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void adicionarValor(String idPF, String idConta ,String nomeConta, String valorConta, String valorAtual){
        double resultado = Double.valueOf(valorConta) - Double.valueOf(valorAtual);
        String valorContaTotal = String.valueOf(resultado);

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idConta)
                .update("valor", valorContaTotal);

        salvarHistoricoPF(idPF, idConta, nomeConta, valorAtual);
    }

    public void lerPlanejamentoFinanceiro(String id, TextView textViewTipoPF,
          TextView textViewPFPF, TextView textViewValorAtual, TextView textViewValorObjetivado,
          TextView textViewDataInicio, ProgressBar progressBarConcluido, TextView textViewPorcInicio,
          TextView textViewDataFinal,  ProgressBar progressBarRestante, TextView textViewPorcFinal){

     db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        DecimalFormat df = new DecimalFormat("#,###.00");

                        double valorAtual = Double.valueOf(document.getData().get("valorAtual").toString());
                        double valorObjetivado = Double.valueOf(document.getData().get("valorObjetivado").toString());

                        double porcentagemValor = (valorAtual / valorObjetivado) * 100;

                        String dataInicio = document.getData().get("dataInicial").toString();
                        String dataAtual = Utilitario.dataAtual();
                        String dataFinal = document.getData().get("dataFinal").toString();

                        double porcentagemData = porcentagemData(dataInicio, dataAtual, dataFinal);

                        int valorBarraAtualValor = (int) porcentagemValor;
                        String porcentagemValorAtual = df.format(porcentagemValor)+"%";

                        int valorBarraAtualData = (int) porcentagemData;
                        String porcentagemDataAtual = df.format(porcentagemData)+"%";

                        if(porcentagemData == (0.0))
                            porcentagemDataAtual = "0%";

                        textViewTipoPF.setText(document.getData().get("tipoPF").toString());
                        textViewPFPF.setText(document.getData().get("planejamentoFinanceiro").toString());
                        textViewValorAtual.setText(document.getData().get("valorAtual").toString());
                        textViewValorObjetivado.setText(document.getData().get("valorObjetivado").toString());
                        textViewDataInicio.setText(dataInicio);
                        progressBarConcluido.setProgress(valorBarraAtualValor);
                        textViewPorcInicio.setText(porcentagemValorAtual);
                        textViewDataFinal.setText(dataFinal);
                        progressBarRestante.setProgress(valorBarraAtualData);
                        textViewPorcFinal.setText(porcentagemDataAtual);
                    } else {
                        Log.d(Msg.INFO, "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void editarPlanejamentoFinanceiro(PlanejamentoFinanceiroDTO dto, Activity activity){

        Map<String, String> data = new HashMap<>();

        data.put("planejamentoFinanceiro", dto.getPlanejamentoFinanceiro());
        data.put("tipoPF", dto.getTipoPlanejamentoFinanceiro());
        data.put("valorAtual", dto.getValorAtual());
        data.put("valorObjetivado", dto.getValorObjetivado());
        data.put("dataInicial", dto.getDataInicial());
        data.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(dto.getId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Utilitario.toast(activity.getApplicationContext(), Msg.ALTERADO);
                        activity.finish();
                        Log.d(Msg.INFO, Msg.DOCUMENTO_S);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    public void deletarPF(String idPF, Activity activity){

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid()).document(idPF).collection(idPF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            List<String> idHistoricoPFList = new ArrayList<>();
                            Map<String, Double> contaList = new HashMap<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                idHistoricoPFList.add(document.getId());
                                String idConta = document.getData().get("idConta").toString();

                                double valorConta = Double.valueOf(document.getData().get("valorConta").toString());

                                if(!contaList.containsKey(idConta))
                                    contaList.put(idConta, valorConta);
                                else
                                    contaList.put(idConta, Double.valueOf(contaList.get(idConta)) + valorConta);
                            }

                            for(Map.Entry<String, Double> conta : contaList.entrySet()){
                                devolverValorConta(conta.getKey(), conta.getValue());
                            }

                            for(String id : idHistoricoPFList)
                                deletaHistoricoPF(id, activity);

                        } else {
                            Log.w(Msg.ERRORM, "Error getting documents.", task.getException());
                        }
                    }
                });

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(idPF)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Msg.INFO, Msg.DOCUMENTO_S);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    private void devolverValorConta(String idConta, Double valorAtual){

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idConta)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                double valorTotal = Double.valueOf(document.getData().get("valor").toString());

                                double resultado =  valorTotal + valorAtual;
                                String valorContaTotal = String.valueOf(resultado);

                                db.collection("contas").document(user.getUid()).collection(user.getUid())
                                        .document(idConta)
                                        .update("valor", valorContaTotal);
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void deletaHistoricoPF(String idPF, Activity activity){
        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid()).document(idPF).collection(idPF)
                .document(idPF)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Msg.INFO, Msg.DOCUMENTO_S);
                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    private double porcentagemData(String dataInicio, String dataAtual, String dataFinal){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        Date inicio = null;
        Date atual = null;
        Date fim = null;

        try {
            inicio = sdf.parse(dataInicio);
            atual = sdf.parse(dataAtual);
            fim = sdf.parse(dataFinal);
        } catch (java.text.ParseException e) { e.getMessage(); }

        long diferencaMes = atual.getTime() - inicio.getTime();
        long diferencaSegundos = diferencaMes / 1000;
        long diferencaMinutos = diferencaSegundos / 60;
        long diferencaHoras = diferencaMinutos / 60;
        long diferencaDias = diferencaHoras / 24;

        long diferencaMes1 = fim.getTime() - inicio.getTime();
        long diferencaSegundos1 = diferencaMes1 / 1000;
        long diferencaMinutos1 = diferencaSegundos1 / 60;
        long diferencaHoras1 = diferencaMinutos1 / 60;
        long diferencaDias1 = diferencaHoras1 / 24;

        double resultado =  ((float) diferencaDias/diferencaDias1) * 100;
        return resultado;
    }

    public void lerHistorico(RecyclerView recyclerView, Context context, String idPF){
        List<HistoricoPFDTO> historicoPFList = new ArrayList<HistoricoPFDTO>();

        db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).document(idPF).collection(idPF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HistoricoPFDTO dto = new HistoricoPFDTO();
                                dto.setIdHistorico(document.getId());
                                dto.setIdPF(document.getData().get("idPF").toString());
                                dto.setIdConta(document.getData().get("idConta").toString());
                                dto.setNomeConta(document.getData().get("nomeConta").toString());
                                dto.setValorConta(document.getData().get("valorConta").toString());
                                historicoPFList.add(dto);
                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            List<String> idHistoricoList = new ArrayList<>();
                            List<String> idPFList = new ArrayList<>();
                            List<String> idContaList = new ArrayList<>();
                            List<String> nomeContaList = new ArrayList<>();
                            List<String> valorContaList = new ArrayList<>();

                            for(HistoricoPFDTO historico : historicoPFList){
                                idHistoricoList.add(historico.getIdHistorico());
                                idPFList.add(historico.getIdPF());
                                idContaList.add(historico.getIdConta());
                                nomeContaList.add(historico.getNomeConta());
                                valorContaList.add(historico.getValorConta());
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new HistoricoPFAdapter(context,
                                    idHistoricoList, idPFList, idContaList, nomeContaList, valorContaList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }
}




















