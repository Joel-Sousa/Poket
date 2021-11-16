package com.example.poket.DAO;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DTO.ContaDTO;
import com.example.poket.adapter.ContaAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContaDAO {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    List<ContaDTO> listConta = new ArrayList<ContaDTO>();

    public ContaDAO(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void cadastarConta(ContaDTO dto, Activity activity){

        Map<String, Object> dadosConta = new HashMap<>();
        dadosConta.put("conta", dto.getConta());
        dadosConta.put("valor", dto.getValor());

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document()
                .set(dadosConta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Msg.SUCESSO, Msg.DOCUMENTO_S);

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

    public void lerContas(RecyclerView recyclerView, Context context, TextView textViewContaValor){
        List<ContaDTO> listConta = new ArrayList<ContaDTO>();

        db.collection("contas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;
                            double valorConta = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContaDTO dto = new ContaDTO();
                                dto.setId(document.getId());
                                dto.setConta(document.getData().get("conta").toString());
                                dto.setValor(Double.parseDouble(document.getData().get("valor").toString()));
                                valorConta += Double.parseDouble(document.getData().get("valor").toString());
                                listConta.add(dto);
                            }

                            textViewContaValor.setText(String.valueOf(valorConta));

                            List<String> idList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
                            List<Double> valorList = new ArrayList<>();

                            for(ContaDTO conta : listConta){
                                idList.add(conta.getId());
                                contaList.add(conta.getConta());
                                valorList.add(conta.getValor());
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new ContaAdapter(context, idList, contaList, valorList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }

    public void editarConta(ContaDTO dto){

        Map<String, Object> data = new HashMap<>();
        data.put("conta", dto.getConta());
        data.put("valor", dto.getValor());

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(dto.getId())
                .set(data)
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

    public void deletarConta(String idc){

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idc)
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

        db.collection("despesas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> despesasList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(idc.equals(document.getData().get("idConta").toString())){
                                    despesasList.add(document.getId());
                                }
                            }

                            for(String e : despesasList){
                                db.collection("despesas").document(user.getUid())
                                        .collection(user.getUid())
                                        .document(e)
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
                    }
                });

        db.collection("rendas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> rendasList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if(idc.equals(document.getData().get("idConta").toString())){
                                    rendasList.add(document.getId());
                                }
//                                document.getData().get("despesa").toString();
                            }

                            for(String e : rendasList){
                                db.collection("rendas").document(user.getUid())
                                        .collection(user.getUid())
                                        .document(e)
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
                    }
                });

        db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> idpfList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult())
                                idpfList.add(document.getId());

                            List<String> hpfidList = new ArrayList<>();
                            Map<String, Double> idpfvList = new HashMap<>();

                            for(String e : idpfList){
                                db.collection("planejamentoFinanceiro")
                                    .document(user.getUid()).collection(user.getUid())
                                    .document(e).collection(e).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                            if (task1.isSuccessful()) {
                                                for (QueryDocumentSnapshot document1 : task1.getResult()){
                                                    if(idc.equals(document1.getData().get("idConta").toString())){

                                                        String idpf1 = document1.getData().get("idPF").toString();
                                                        Double valorH = Double.valueOf(document1.getData().get("valorHistoricoPF").toString());
                                                        hpfidList.add(document1.getId());

                                                        if(!idpfvList.containsKey(idpf1))
                                                            idpfvList.put(idpf1, valorH);
                                                        else
                                                            idpfvList.put(idpf1, Double.valueOf(idpfvList.get(idpf1)) + valorH);


                                                        db.collection("planejamentoFinanceiro")
                                                            .document(user.getUid()).collection(user.getUid())
                                                            .document(e).collection(e)
                                                                .document(document1.getId())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d(Msg.INFO, Msg.DOCUMENTO_S);
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                    Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                                                            }
                                                        });
                                                    }
                                                }//

//                                                for(String e2: idpfList){
//
//                                                }

                                                if(!hpfidList.isEmpty()){

                                                        for(Map.Entry<String, Double> e1 : idpfvList.entrySet()){

                                                        db.collection("planejamentoFinanceiro")
                                                                    .document(user.getUid()).collection(user.getUid())
                                                                    .document(e1.getKey())
                                                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                                                DocumentSnapshot document2 = task2.getResult();

                                                                Double valor = 0.0;
                                                                Double valorAtual = 0.0;
                                                                Double result = 0.0;
                                                                if(task2.isSuccessful()){
                                                                     valor = e1.getValue();
                                                                     valorAtual = Double.parseDouble(document2.getData().get("valorAtual").toString());

                                                                     result = valorAtual - valor;

                                                                    db.collection("planejamentoFinanceiro").document(user.getUid()).collection(user.getUid())
                                                                            .document(e1.getKey()).update("valorAtual", String.valueOf(result));
                                                                }
                                                            }
                                                        });
                                                    }
                                                    hpfidList.remove(0);
                                                    idpfvList.clear();
                                                }
                                            }
                                        }
                                    });
                            }
                        }
                    }
                });
    }


    public void listaContaSpinner(Spinner spinnerConta, Context context, TextView textViewContaValor,
                                  TextView textViewIdConta, boolean contaZerada){

        db.collection("contas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContaDTO dto = new ContaDTO();

                                double valor = Double.valueOf(document.getData().get("valor").toString());
                                if(contaZerada){
                                    if(valor > 0){
                                        dto.setId(document.getId());
                                        dto.setConta(document.getData().get("conta").toString());
                                        dto.setValor(Double.parseDouble(document.getData().get("valor").toString()));
                                        listConta.add(dto);
                                    }
                                }else{
                                    dto.setId(document.getId());
                                    dto.setConta(document.getData().get("conta").toString());
                                    dto.setValor(Double.parseDouble(document.getData().get("valor").toString()));
                                    listConta.add(dto);
                                }
                            }

                            if(listConta.isEmpty()){
                                ContaDTO d = new ContaDTO();
                                d.setId("0");
                                d.setConta(".:Sem Conta:.");
                                d.setValor(0.0);
                                listConta.add(d);
                            }

                            ArrayAdapter<ContaDTO> adapterConta =
                                    new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                                            listConta);
                            spinnerConta.setAdapter(adapterConta);

                            DecimalFormat df = new DecimalFormat("###.00");
//                            df.format(textViewContaValor.getText().toString());

                            spinnerConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String valor = df.format(Double.valueOf(listConta.get(i).getValor()));

                                    textViewIdConta.setText(listConta.get(i).getId());
                                    textViewContaValor.setText(valor);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                        } else {
                            Log.w(Msg.ERROR, Msg.DOCUMENTO_F, task.getException());
                        }
                    }

                });
    }

    public void buscarConta(RecyclerView recyclerView, Context context,TextView textViewContaValor,
                            String busca){
        List<ContaDTO> listConta = new ArrayList<ContaDTO>();

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;
                            double valorConta = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContaDTO dto = new ContaDTO();
                                dto.setId(document.getId());
                                dto.setConta(document.getData().get("conta").toString());
                                dto.setValor(Double.parseDouble(document.getData().get("valor").toString()));
                                listConta.add(dto);
                            }

                            List<String> idList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
                            List<Double> valorList = new ArrayList<>();

                            for(ContaDTO conta : listConta){
                                if(conta.getConta().contains(busca)){
//                                if(conta.getConta().contains(busca)){
                                    idList.add(conta.getId());
                                    contaList.add(conta.getConta());
                                    valorList.add(conta.getValor());
                                    valorConta += Double.valueOf(conta.getValor());
                                }else if(busca.equals("")){
                                    lerContas(recyclerView, context ,textViewContaValor);
                                }
                            }

                            if(!busca.equals("")){
                                textViewContaValor.setText(String.valueOf(valorConta));
                                layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager((layoutManager));
                                adapter = new ContaAdapter(context, idList, contaList, valorList);
                                recyclerView.setAdapter(adapter);
                            }

                        } else {
                            Log.w(Msg.ERROR, Msg.DOCUMENTO_F, task.getException());
                        }
                    }
                });
    }
}



