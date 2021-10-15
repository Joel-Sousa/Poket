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
import java.util.ArrayList;
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
        dadosPF.put("idConta", dto.getIdConta());
        dadosPF.put("conta", dto.getConta());
        dadosPF.put("contaValor", dto.getContaValor());
        dadosPF.put("valorAtual", dto.getValorAtual());
        dadosPF.put("valorObjetivado", dto.getValorObjetivado());
        dadosPF.put("dataInicial", dto.getDataInicial());
        dadosPF.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(dto.getTipoPlanejamentoFinanceiro())
                .document()
                .set(dadosPF)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Msg.SUCESSO, Msg.DOCUMENTO_S);

                        // TODO IMPLEMENTAR A RETIRADA DO DINHEIRO NA CONTA

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

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(tipoPF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
//                            boolean bol = task.getResult().isEmpty();
                            String id = "";
                            for (QueryDocumentSnapshot document : task.getResult())
                                id = document.getId();

                            if(task.getResult().isEmpty()){
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

                                textViewIdPF.setText(id);

                                ContaDAO daoC = new ContaDAO();
                                daoC.listaContaSpinner(spinnerConta, activity, textViewContaValor, textViewIdConta);

                                mBuilder.setView(view);
                                final AlertDialog dialog = mBuilder.create();
                                dialog.show();

                                buttonAdicionar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // TODO IMPLENENTAR A RETIRADA DO DINHEIRO DA CONTA
                                        Utilitario.toast(activity.getApplicationContext(), "add");
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
                                intentListarPF.putExtra("id", id);
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

    public void lerPlanejamentoFinanceiro(String id, String tipoPF, TextView textViewIdConta,TextView textViewTipoPF,
          TextView textViewPFPF, TextView textViewValorAtual, TextView textViewValorObjetivado,
          TextView textViewDataInicio, ProgressBar progressBarConcluido, TextView textViewPorcInicio,
          TextView textViewDataFinal,  ProgressBar progressBarRestante, TextView textViewPorcFinal){

     db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(tipoPF).document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        double valorInicial = Double.valueOf(document.getData().get("valorAtual").toString());
                        double valorObjetivado = Double.valueOf(document.getData().get("valorObjetivado").toString());

                        DecimalFormat df = new DecimalFormat("#,###.00");
                        double valorPorcentagem = (valorInicial / valorObjetivado) * 100;

                        String porcentagemConcluido = df.format(valorPorcentagem)+"%";
                        String porcentagemFinal = df.format((valorPorcentagem - 100) * -1 )+"%";

                        int valorBarraConcluido = (int) valorPorcentagem;
                        int valorBarraRestante = (int) (valorPorcentagem - 100 ) * -1;

                        textViewIdConta.setText(document.getData().get("idConta").toString());
                        textViewTipoPF.setText(document.getData().get("tipoPF").toString());
                        textViewPFPF.setText(document.getData().get("planejamentoFinanceiro").toString());
                        textViewValorAtual.setText(document.getData().get("valorAtual").toString());
                        textViewValorObjetivado.setText(document.getData().get("valorObjetivado").toString());
                        textViewDataInicio.setText(document.getData().get("dataInicial").toString());
                        progressBarConcluido.setProgress(valorBarraConcluido);
                        textViewPorcInicio.setText(porcentagemConcluido);
                        textViewDataFinal.setText(document.getData().get("dataFinal").toString());
                        progressBarRestante.setProgress(valorBarraRestante);
                        textViewPorcFinal.setText(porcentagemFinal);
                    } else {
                        Log.d(Msg.INFO, "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void editarPlanejamentoFinanceiro
            (PlanejamentoFinanceiroDTO dto, Activity activity,
             String idContaAntiga, String valorPFAntigo){

        Map<String, String> data = new HashMap<>();

        double valorAntigo = 0.0;

        if(idContaAntiga.equals(dto.getIdConta())){
            valorAntigo = Double.valueOf(dto.getContaValor()) + Double.valueOf(valorPFAntigo);
            db.collection("contas").document(user.getUid()).collection(user.getUid())
                    .document(idContaAntiga).update("valor", String.valueOf(valorAntigo));

            data.put("contaValor", String.valueOf(valorAntigo));
        }else {
            DocumentReference docRef = db.collection("contas").document(user.getUid())
                    .collection(user.getUid()).document(idContaAntiga);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            double valorAntigo1 = Double.valueOf(document.getData().get("valor").toString()) + Double.valueOf(valorPFAntigo);
                            db.collection("contas").document(user.getUid()).collection(user.getUid())
                                    .document(idContaAntiga).update("valor", String.valueOf(valorAntigo1));

                            Log.d(Msg.INFO, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(Msg.INFO, "No such document");
                        }
                    } else {
                        Log.d(Msg.INFO, "get failed with ", task.getException());
                    }
                }
            });

            data.put("contaValor", dto.getContaValor());
        }

        data.put("planejamentoFinanceiro", dto.getPlanejamentoFinanceiro());
        data.put("tipoPF", dto.getTipoPlanejamentoFinanceiro());
        data.put("idConta", dto.getIdConta());
        data.put("conta", dto.getConta());
//        data.put("contaValor", dto.getContaValor());
        data.put("valorAtual", dto.getValorAtual());
        data.put("valorObjetivado", dto.getValorObjetivado());
        data.put("dataInicial", dto.getDataInicial());
        data.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(dto.getTipoPlanejamentoFinanceiro())
                .document(dto.getId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        double valorAntigo1 = Double.valueOf(data.get("contaValor")) - Double.valueOf(dto.getValorAtual());
                        db.collection("contas").document(user.getUid()).collection(user.getUid())
                                .document(dto.getIdConta()).update("valor", String.valueOf(valorAntigo1));

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

    public void deletarPF(String id, String tipoPF, String idConta, String valorConta){

        DocumentReference docRef = db.collection("contas").document(user.getUid())
                .collection(user.getUid()).document(idConta);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        double valorAntigo1 = Double.valueOf(document.getData().get("valor").toString()) + Double.valueOf(valorConta);
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

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(tipoPF)
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




















