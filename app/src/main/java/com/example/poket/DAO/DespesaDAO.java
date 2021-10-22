package com.example.poket.DAO;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DTO.DespesaDTO;
import com.example.poket.adapter.DespesaAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DespesaDAO {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public DespesaDAO(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void cadastarDespesa(DespesaDTO dto, Activity activity) {
        Map<String, String> dadosDespesa = new HashMap<>();
        dadosDespesa.put("despesa", dto.getDespesa());
        dadosDespesa.put("valorDespesa", dto.getValorDespesa());
        dadosDespesa.put("tipoDespesa", dto.getTipoDespesa());
        dadosDespesa.put("dataDespesa", dto.getDataDespesa());
        dadosDespesa.put("observacao", dto.getObservacao());

        dadosDespesa.put("idConta", dto.getIdConta());
        dadosDespesa.put("conta", dto.getConta());
//        dadosDespesa.put("valorConta", dto.getValorConta());

            db.collection("despesas").document(user.getUid()).collection(user.getUid())
                    .document()
                    .set(dadosDespesa)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(Msg.SUCESSO, Msg.DOCUMENTO_S);

                            double resultado = Double.valueOf(dto.getValorConta()) - Double.valueOf(dto.getValorDespesa());
                            String valorContaTotal = String.valueOf(resultado);

                            db.collection("contas").document(user.getUid()).collection(user.getUid())
                                    .document(dto.getIdConta())
                                    .update("valor", valorContaTotal);

    //                        alterarValorConta(idConta, dto.getContaValor(), dto.getValorDespesa());
                            Utilitario.toast(activity.getApplicationContext(), Msg.CADASTRADO);
                            activity.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(Msg.ERROR, Msg.DOCUMENTO_F, e);
                        }
                    });
    }

    public void lerDespesas(RecyclerView recyclerView, Context context, TextView textViewDespesaValorTotal){
        List<DespesaDTO> listDespesa = new ArrayList<DespesaDTO>();

        db.collection("despesas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;
                            double valorDespesa = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DespesaDTO dto = new DespesaDTO();
                                dto.setId(document.getId());
                                dto.setDespesa(document.getData().get("despesa").toString());
                                dto.setValorDespesa(document.getData().get("valorDespesa").toString());
                                dto.setTipoDespesa(document.getData().get("tipoDespesa").toString());
                                dto.setDataDespesa(document.getData().get("dataDespesa").toString());
                                dto.setObservacao(document.getData().get("observacao").toString());

                                dto.setIdConta(document.getData().get("idConta").toString());
                                dto.setConta(document.getData().get("conta").toString());
//                                dto.setValorConta(document.getData().get("valorConta").toString());

                                listDespesa.add(dto);
                                valorDespesa += Double.valueOf(document.getData().get("valorDespesa").toString());
                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            textViewDespesaValorTotal.setText(String.valueOf(valorDespesa));

                            List<String> idList = new ArrayList<>();
                            List<String> despesaList = new ArrayList<>();
                            List<String> valorDespesaList = new ArrayList<>();
                            List<String> tipoDespesaList = new ArrayList<>();
                            List<String> dataDespesaList = new ArrayList<>();
                            List<String> observacaoList = new ArrayList<>();

                            List<String> idContaList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
//                            List<String> valorContaList = new ArrayList<>();

                            for(DespesaDTO despesa : listDespesa){
                                idList.add(despesa.getId());
                                despesaList.add(despesa.getDespesa());
                                valorDespesaList.add(despesa.getValorDespesa());
                                tipoDespesaList.add(despesa.getTipoDespesa());
                                dataDespesaList.add(despesa.getDataDespesa());
                                observacaoList.add(despesa.getObservacao());

                                idContaList.add(despesa.getIdConta());
                                contaList.add(despesa.getConta());
//                                valorContaList.add(despesa.getValorConta());
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new DespesaAdapter(context,
                                    idList, despesaList, valorDespesaList, tipoDespesaList,
                                    dataDespesaList, observacaoList,
                                    idContaList, contaList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }

    public void editarDespesa(DespesaDTO dto, Activity activity,
                              String idContaAntiga, String valorDespesaAntiga){

        double valorConta = 0.0;

        if(idContaAntiga.equals(dto.getIdConta())){
            valorConta = (Double.valueOf(dto.getValorConta()) + Double.valueOf(valorDespesaAntiga)) - Double.valueOf(dto.getValorDespesa());

            db.collection("contas").document(user.getUid()).collection(user.getUid())
                    .document(idContaAntiga).update("valor", String.valueOf(valorConta));
        }else{

            db.collection("contas")
                    .document(user.getUid()).collection(user.getUid()).document(idContaAntiga)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
//                                                  double valorAtual = Double.valueOf(document.getData().get("valorAtual").toString());

                                    String valorAtualConta = document.getData().get("valor").toString();

                                    double valorDevolverConta = Double.valueOf(valorAtualConta) + Double.valueOf(valorDespesaAntiga);

                                    db.collection("contas").document(user.getUid()).collection(user.getUid())
                                            .document(idContaAntiga).update("valor", String.valueOf(valorDevolverConta));

                                } else {
                                    Log.d(Msg.INFO, "No such document");
                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                        }
                    });

            double valorNovoConta = Double.valueOf(dto.getValorConta()) - Double.valueOf(dto.getValorDespesa());

            db.collection("contas").document(user.getUid()).collection(user.getUid())
                    .document(dto.getIdConta()).update("valor", String.valueOf(valorNovoConta));
        }

        Map<String, String> data = new HashMap<>();

        data.put("despesa", dto.getDespesa());
        data.put("valorDespesa", dto.getValorDespesa());
        data.put("tipoDespesa", dto.getTipoDespesa());
        data.put("dataDespesa", dto.getDataDespesa());
        data.put("observacao", dto.getObservacao());

        data.put("idConta", dto.getIdConta());
        data.put("conta", dto.getConta());

        db.collection("despesas").document(user.getUid())
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

    public void deletarDespesa(String id, String idConta, String valorDespesa){

         db.collection("contas").document(user.getUid()).collection(user.getUid())
                 .document(idConta).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        double valorAntigo1 = Double.valueOf(document.getData().get("valor").toString()) + Double.valueOf(valorDespesa);
                        db.collection("contas").document(user.getUid()).collection(user.getUid())
                                .document(idConta).update("valor", String.valueOf(valorAntigo1));

                        Log.d(Msg.INFO, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(Msg.INFO, "No such document");
                    }
                } else {
                    Log.d(Msg.INFO, "get failed with ", task.getException());
                }
            }
        });

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .document(id)
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
}
