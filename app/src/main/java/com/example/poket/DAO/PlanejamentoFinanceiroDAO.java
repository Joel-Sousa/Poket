package com.example.poket.DAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.MainActivity;
import com.example.poket.R;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.despesa.AdicionarDespesa;
import com.example.poket.view.planejamento.AdicionarPlanejamentoFinanceiro;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        dadosPF.put("idConta", dto.getIdConta());
        dadosPF.put("conta", dto.getConta());
        dadosPF.put("contaValor", dto.getContaValor());
        dadosPF.put("valorAtual", dto.getValorAtual());
        dadosPF.put("valorObjetivado", dto.getValorObjetivado());
        dadosPF.put("dataInicial", dto.getDataInicial());
        dadosPF.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid()).collection(user.getUid())
                .document()
                .set(dadosPF)
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

    public void planejamentoFinanceiro(Activity activity, String tipoPF, boolean addpf, View view){

        db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).get()
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

                                    }
                                });

                                buttonVoltar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                    }
                                });
                            }

                            else{
                                Intent intentListarPF = new Intent(activity, ListaPlanejamentoFinanceiro.class);
                                intentListarPF.putExtra("id", tipoPFList.get(0));
                                intentListarPF.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intentListarPF);
                            }

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });

    }

    public void lerPlanejamentoFinanceiro(String id, TextView textViewTipoPF, TextView textViewPFPF,
          TextView textViewValorAtual, TextView textViewValorObjetivado, TextView textViewDataInicio,
          ProgressBar progressBarConcluido, TextView textViewPorcInicio, TextView textViewDataFinal,
          ProgressBar progressBarRestante, TextView textViewPorcFinal){


        DocumentReference docRef = db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).document(id);
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // TODO finalizar o carregamento da tela de ListarPlanejamentoFinanceiro
                        textViewTipoPF.setText(document.getData().get("tipoPF").toString());
                        textViewPFPF.setText(document.getData().get("planejamentoFinanceiro").toString());
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
}
