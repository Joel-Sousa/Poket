package com.example.poket.DAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.poket.adapter.PlanejamentoFinanceiroAdapter;
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

    public void cadastrarPlanejamentoFinanceiro(PlanejamentoFinanceiroDTO dto, HistoricoPFDTO hdto,
                                                Activity activity){

        Map<String, String> dadosPF = new HashMap<>();
        dadosPF.put("nomePF", dto.getNomePF());
        dadosPF.put("tipoPF", dto.getTipoPF());
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

                        double resultado = Double.valueOf(hdto.getValorConta()) - Double.valueOf(dto.getValorAtual());
                        String valorContaTotal = String.valueOf(resultado);

                        db.collection("contas").document(user.getUid()).collection(user.getUid())
                                .document(hdto.getIdConta())
                                .update("valor", valorContaTotal);

                        hdto.setIdPF(documentReference.getId());

                        salvarHistoricoPF(hdto);

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

    private void salvarHistoricoPF(HistoricoPFDTO hdto){

        Map<String, String> dadosHistoricoPF = new HashMap<>();
        dadosHistoricoPF.put("idPF", hdto.getIdPF());
        dadosHistoricoPF.put("idConta", hdto.getIdConta());
        dadosHistoricoPF.put("conta", hdto.getConta());
        dadosHistoricoPF.put("valorHistoricoPF", hdto.getValorHistoricoPF());
        dadosHistoricoPF.put("dataHistorico", Utilitario.convertBrToUsa(Utilitario.dataAtual()));

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(hdto.getIdPF()).collection(hdto.getIdPF()).document()
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

    public void lerPlanejamentoFinanceiro(RecyclerView recyclerView, Context context,
                                          Activity activity, View view){
        List<PlanejamentoFinanceiroDTO> pfList = new ArrayList<PlanejamentoFinanceiroDTO>();

        db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
                                dto.setIdPF(document.getId());
                                dto.setNomePF(document.getData().get("nomePF").toString());
                                dto.setTipoPF(document.getData().get("tipoPF").toString());
                                dto.setValorAtual(document.getData().get("valorAtual").toString());
                                dto.setValorObjetivado(document.getData().get("valorObjetivado").toString());
                                dto.setDataInicial(document.getData().get("dataInicial").toString());
                                dto.setDataFinal(document.getData().get("dataFinal").toString());
                                pfList.add(dto);
                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            List<String> idPFList = new ArrayList<>();
                            List<String> nomePFList = new ArrayList<>();
                            List<String> tipoPFList = new ArrayList<>();
                            List<String> valorAtualList = new ArrayList<>();
                            List<String> valorObjetivadoList = new ArrayList<>();
                            List<String> dataInicialList = new ArrayList<>();
                            List<String> dataFinalList = new ArrayList<>();

                            List<String> porcentagemValorList = new ArrayList<>();
                            List<String> porcentagemDataList = new ArrayList<>();
                            List<Integer> porcentagemBarValorList = new ArrayList<>();
                            List<Integer> porcentagemBarDataList = new ArrayList<>();

                            DecimalFormat df = new DecimalFormat("#,###.00");
                            for(PlanejamentoFinanceiroDTO pf : pfList){

                                idPFList.add(pf.getIdPF());
                                nomePFList.add(pf.getNomePF());
                                tipoPFList.add(pf.getTipoPF());
                                valorAtualList.add(pf.getValorAtual());
                                valorObjetivadoList.add(pf.getValorObjetivado());
                                dataInicialList.add(pf.getDataInicial());
                                dataFinalList.add(pf.getDataFinal());

                                double porcentagemValor = porcentagemValor(pf.getValorAtual(), pf.getValorObjetivado());

                                String dataInicial = Utilitario.convertUsaToBr(pf.getDataInicial());
                                String dataAtual = Utilitario.dataAtual();
                                String dataFinal = Utilitario.convertUsaToBr(pf.getDataFinal());

                                double porcentagemData = porcentagemData(dataInicial, dataAtual, dataFinal);

                                porcentagemValorList.add(df.format(porcentagemValor)+"%");

                                if(porcentagemData == (0.0))
                                    porcentagemDataList.add(0+"%");
                                else
                                    porcentagemDataList.add(df.format(porcentagemData)+"%");

                                porcentagemBarValorList.add((int) porcentagemValor);
                                porcentagemBarDataList.add((int) porcentagemData);
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new PlanejamentoFinanceiroAdapter(context,
                                    idPFList, nomePFList, tipoPFList, valorAtualList,
                                    valorObjetivadoList, dataInicialList, dataFinalList
                                    ,porcentagemValorList, porcentagemDataList,
                                    porcentagemBarValorList, porcentagemBarDataList,
                                    activity, view);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }

    public void editarPlanejamentoFinanceiro(PlanejamentoFinanceiroDTO dto, Activity activity){

        Map<String, String> data = new HashMap<>();

        data.put("nomePF", dto.getNomePF());
        data.put("tipoPF", dto.getTipoPF());
        data.put("valorAtual", dto.getValorAtual());
        data.put("valorObjetivado", dto.getValorObjetivado());
        data.put("dataInicial", dto.getDataInicial());
        data.put("dataFinal", dto.getDataFinal());

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(dto.getIdPF())
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

                                double valorConta = Double.valueOf(document.getData().get("valorHistoricoPF").toString());

                                if(!contaList.containsKey(idConta))
                                    contaList.put(idConta, valorConta);
                                else
                                    contaList.put(idConta, Double.valueOf(contaList.get(idConta)) + valorConta);
                            }

                            for(Map.Entry<String, Double> conta : contaList.entrySet()){
                                devolverValorConta(conta.getKey(), conta.getValue());
                            }

                            for(String idHPF : idHistoricoPFList)
                                deletaHistoricoPF(idPF, idHPF, activity);

                            db.collection("planejamentoFinanceiro").document(user.getUid())
                                    .collection(user.getUid())
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
                        } else {
                            Log.w(Msg.ERRORM, "Error getting documents.", task.getException());
                        }
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

        private void deletaHistoricoPF(String idPF, String idHPF, Activity activity){
        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid()).document(idPF).collection(idPF)
                .document(idHPF)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Msg.INFO, Msg.DOCUMENTO_S);
//                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    public void lerHistorico(RecyclerView recyclerView, Context context, Activity activity, String idPF){
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
                                dto.setConta(document.getData().get("conta").toString());
                                dto.setValorConta(document.getData().get("valorHistoricoPF").toString());
                                dto.setDataHistorico(document.getData().get("dataHistorico").toString());
                                historicoPFList.add(dto);
                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            List<String> idHistoricoList = new ArrayList<>();
                            List<String> idPFList = new ArrayList<>();
                            List<String> idContaList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();
                            List<String> valorContaList = new ArrayList<>();
                            List<String> dataHistoricoList = new ArrayList<>();

                            for(HistoricoPFDTO historico : historicoPFList){
                                idHistoricoList.add(historico.getIdHistorico());
                                idPFList.add(historico.getIdPF());
                                idContaList.add(historico.getIdConta());
                                contaList.add(historico.getConta());
                                valorContaList.add(historico.getValorConta());
                                dataHistoricoList.add(historico.getDataHistorico());
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new HistoricoPFAdapter(context, activity,
                                    idHistoricoList, idPFList, idContaList, contaList,
                                    valorContaList, dataHistoricoList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }

    public void adicionarValorPF(Activity activity, View view, String idPF){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
//        final TextView textViewIdPF = view.findViewById(R.id.textViewDialogPFUidPF);

        final TextView textViewIdConta = view.findViewById(R.id.textViewDialogAddPFIdConta);
        final Spinner spinnerConta = view.findViewById(R.id.spinnerDialogAddPFConta);
        final EditText editTextValor = view.findViewById(R.id.editTextDialogAddPFValor);
        final TextView textViewContaValor = view.findViewById(R.id.textViewDialogAddPFValorConta);
        Button buttonAdicionar = view.findViewById(R.id.buttonDialogAddPFAdicionar);
        Button buttonVoltar = view.findViewById(R.id.buttonDialogAddPFVoltar);

//        textViewIdPF.setText(idPF);

        ContaDAO daoC = new ContaDAO();
        daoC.listaContaSpinner(spinnerConta, activity, textViewContaValor, textViewIdConta, true);

        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        if(editTextValor.getText().toString().equals("0")){
            Utilitario.toast(activity.getApplicationContext(), Msg.VALOR_ZERADO);
            editTextValor.requestFocus();
        }

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String idPF = tipoPFList.get(0);
                String idConta = textViewIdConta.getText().toString();
                String conta = spinnerConta.getSelectedItem().toString();
                String valorConta = textViewContaValor.getText().toString();
                String valorAtual = editTextValor.getText().toString();

                if(editTextValor.getText().length() == 0){
                    Utilitario.toast(activity.getApplicationContext(), "Preencha o campo com um valor!");
                    editTextValor.requestFocus();
                }else if(editTextValor.getText().toString().equals("0")){
                    Utilitario.toast(activity.getApplicationContext(), Msg.VALOR_ZERADO);
                    editTextValor.requestFocus();
                }else if(Double.valueOf(valorAtual) > Double.valueOf(valorConta)){
                    Utilitario.toast(activity.getApplicationContext(), Msg.VALOR_MAIOR_PF);
                    editTextValor.requestFocus();
                    editTextValor.setText("");
                }else{

                    adicionarValor(idPF, idConta , conta, valorConta, valorAtual);
                    Utilitario.toast(activity.getApplicationContext(), "Adicionado");

                    try {
                        new Thread().sleep(1000);

                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                        activity.startActivity(activity.getIntent());
                        activity.overridePendingTransition(0, 0);

                        dialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.finish();
                activity.overridePendingTransition(0, 0);
                activity.startActivity(activity.getIntent());
                activity.overridePendingTransition(0, 0);
                dialog.dismiss();
            }
        });

    }

    private void adicionarValor(String idPF, String idConta ,String conta, String valorConta,
                                String valorAtual){

        double resultado = Double.valueOf(valorConta) - Double.valueOf(valorAtual);
        String valorContaTotal = String.valueOf(resultado);

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idConta)
                .update("valor", valorContaTotal);


        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .document(idPF)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TAG", "DocumentSnapshot data: " + document.getData());

                                double valorTotal = Double.valueOf(document.getData().get("valorAtual").toString());
                                double valorAtual1 = Double.valueOf(valorAtual);

                                double resultado =  valorTotal + valorAtual1;
                                String valorContaTotal = String.valueOf(resultado);

                                db.collection("planejamentoFinanceiro").document(user.getUid()).collection(user.getUid())
                                        .document(idPF)
                                        .update("valorAtual", valorContaTotal);

                                HistoricoPFDTO hdto = new HistoricoPFDTO();
                                hdto.setIdPF(idPF);
                                hdto.setIdConta(idConta);
                                hdto.setConta(conta);
                                hdto.setValorHistoricoPF(valorAtual);

                                salvarHistoricoPF(hdto);
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void buscarPlanejamentoFinanceiro(RecyclerView recyclerView, Context context,
                Activity activity, View view, String busca){
        List<PlanejamentoFinanceiroDTO> pfList = new ArrayList<PlanejamentoFinanceiroDTO>();

        db.collection("planejamentoFinanceiro").document(user.getUid())
                .collection(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PlanejamentoFinanceiroDTO dto = new PlanejamentoFinanceiroDTO();
                                dto.setIdPF(document.getId());
                                dto.setNomePF(document.getData().get("nomePF").toString());
                                dto.setTipoPF(document.getData().get("tipoPF").toString());
                                dto.setValorAtual(document.getData().get("valorAtual").toString());
                                dto.setValorObjetivado(document.getData().get("valorObjetivado").toString());
                                dto.setDataInicial(document.getData().get("dataInicial").toString());
                                dto.setDataFinal(document.getData().get("dataFinal").toString());
                                pfList.add(dto);
                            }

                            List<String> idPFList = new ArrayList<>();
                            List<String> nomePFList = new ArrayList<>();
                            List<String> tipoPFList = new ArrayList<>();
                            List<String> valorAtualList = new ArrayList<>();
                            List<String> valorObjetivadoList = new ArrayList<>();
                            List<String> dataInicialList = new ArrayList<>();
                            List<String> dataFinalList = new ArrayList<>();

                            List<String> porcentagemValorList = new ArrayList<>();
                            List<String> porcentagemDataList = new ArrayList<>();
                            List<Integer> porcentagemBarValorList = new ArrayList<>();
                            List<Integer> porcentagemBarDataList = new ArrayList<>();

                            DecimalFormat df = new DecimalFormat("###.00");

                            for(PlanejamentoFinanceiroDTO pf : pfList){
                                if(pf.getNomePF().equals(busca)){

                                    idPFList.add(pf.getIdPF());
                                    nomePFList.add(pf.getNomePF());
                                    tipoPFList.add(pf.getTipoPF());
                                    valorAtualList.add(pf.getValorAtual());
                                    valorObjetivadoList.add(pf.getValorObjetivado());
                                    dataInicialList.add(pf.getDataInicial());
                                    dataFinalList.add(pf.getDataFinal());

                                    double porcentagemValor = porcentagemValor(pf.getValorAtual(), pf.getValorObjetivado());

                                    String dataInicial = Utilitario.convertUsaToBr(pf.getDataInicial());
                                    String dataAtual = Utilitario.dataAtual();
                                    String dataFinal = Utilitario.convertUsaToBr(pf.getDataFinal());

                                    double porcentagemData = porcentagemData(dataInicial, dataAtual, dataFinal);

                                    porcentagemValorList.add(df.format(porcentagemValor)+"%");

                                    if(porcentagemData == (0.0))
                                        porcentagemDataList.add(0+"%");
                                    else
                                        porcentagemDataList.add(df.format(porcentagemData)+"%");

                                    porcentagemBarValorList.add((int) porcentagemValor);
                                    porcentagemBarDataList.add((int) porcentagemData);

                                }else if(busca.equals("")){
                                    lerPlanejamentoFinanceiro(recyclerView, context ,activity, view);
                                }
                            }

                            if(!busca.equals("")){
                                layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager((layoutManager));
                                adapter = new PlanejamentoFinanceiroAdapter(context,
                                        idPFList, nomePFList, tipoPFList, valorAtualList,
                                        valorObjetivadoList, dataInicialList, dataFinalList
                                        ,porcentagemValorList, porcentagemDataList,
                                        porcentagemBarValorList, porcentagemBarDataList,
                                        activity, view);
                                recyclerView.setAdapter(adapter);
                            }

                        } else {
                            Log.w(Msg.ERROR, Msg.DOCUMENTO_F, task.getException());
                        }
                    }
                });
    }

    public void deletarHistoricoPF(String idPF, String idPFH, String idConta, String valorHistorico){

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idConta).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        double valorAntigo1 = Double.valueOf(document.getData().get("valor").toString()) + Double.valueOf(valorHistorico);
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

        db.collection("planejamentoFinanceiro").document(user.getUid()).collection(user.getUid())
                .document(idPF).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        double valorAntigo1 = Double.valueOf(document.getData().get("valorAtual").toString()) - Double.valueOf(valorHistorico);
                        db.collection("planejamentoFinanceiro").document(user.getUid()).collection(user.getUid())
                                .document(idPF).update("valorAtual", String.valueOf(valorAntigo1));

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
                .collection(user.getUid()).document(idPF).collection(idPF)
                .document(idPFH)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Msg.INFO, Msg.DOCUMENTO_S);
//                        activity.finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Msg.INFO, Msg.DOCUMENTO_F, e);
                    }
                });
    }

    private double porcentagemValor(String valorAtual, String valorObjetivado){
        double valorNewAtual = Double.valueOf(valorAtual);
        double valorNewObjetivado = Double.valueOf(valorObjetivado);

        double resposta = (valorNewAtual / valorNewObjetivado) * 100;

        if(valorNewObjetivado <= valorNewAtual)
            resposta = 100.0;

        return resposta;
    }

    private double porcentagemData(String dataInicio, String dataAtual, String dataFinal){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

//        String dataAtual = Utilitario.dataAtual();

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

        if(atual.after(fim))
            resultado = 100.0;

        return resultado;
    }
}
