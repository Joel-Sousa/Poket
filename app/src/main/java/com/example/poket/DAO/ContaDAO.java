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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        Map<String, String> dadosConta = new HashMap<>();
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
                                dto.setValor(document.getData().get("valor").toString());
                                valorConta += Double.valueOf(document.getData().get("valor").toString());
                                listConta.add(dto);
                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            textViewContaValor.setText(String.valueOf(valorConta));

                            List<String> idList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
                            List<String> valorList = new ArrayList<>();

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

        Map<String, String> data = new HashMap<>();
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

    public void deletarConta(String id){

        db.collection("contas").document(user.getUid()).collection(user.getUid())
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


    public void listaContaSpinner(Spinner spinnerConta, Context context, TextView textViewConta,
                                  TextView textViewIdConta){
//        List<ContaDTO> listConta = new ArrayList<ContaDTO>();

        db.collection("contas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            List<ContaDTO> listConta = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ContaDTO dto = new ContaDTO();
                                dto.setId(document.getId());
                                dto.setConta(document.getData().get("conta").toString());
                                dto.setValor(document.getData().get("valor").toString());
                                listConta.add(dto);

                                Log.d(Msg.INFO, document.getId() + " => " + document.getData());
                            }

                            if(listConta.isEmpty()){
                                semConta();
                            }

//                            Log.d("tst", listConta.toString());

                            ArrayAdapter<ContaDTO> adapterConta =
                                    new ArrayAdapter<>(context, android.R.layout.simple_list_item_1,
                                            listConta);
                            spinnerConta.setAdapter(adapterConta);
                            spinnerConta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    textViewIdConta.setText(listConta.get(i).getId());
                                    textViewConta.setText(listConta.get(i).getValor());
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

    public void semConta(){
        List<ContaDTO> listConta = new ArrayList<>();
        ContaDTO d = new ContaDTO();
        d.setId("0");
        d.setConta(".:Sem Conta:.");
        d.setValor("0");
        listConta.add(d);
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
                                dto.setValor(document.getData().get("valor").toString());
                                listConta.add(dto);
//                                Log.d("---", document.getId() + " => " + document.getData());
                            }

                            List<String> idList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
                            List<String> valorList = new ArrayList<>();

                            for(ContaDTO conta : listConta){
                                if(conta.getConta().equals(busca)){
                                    idList.add(conta.getId());
                                    contaList.add(conta.getConta());
                                    valorList.add(conta.getValor());
                                    valorConta += Double.valueOf(conta.getValor());
//                                  Log.i("---", conta.getId());
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



