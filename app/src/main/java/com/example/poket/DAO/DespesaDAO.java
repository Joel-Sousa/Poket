package com.example.poket.DAO;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DTO.ContaDTO;
import com.example.poket.DTO.DespesaDTO;
import com.example.poket.adapter.ContaAdapter;
import com.example.poket.adapter.DespesaAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

import org.w3c.dom.Text;

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

                            for(DespesaDTO despesa : listDespesa){
                                idList.add(despesa.getId());
                                despesaList.add(despesa.getDespesa());
                                valorDespesaList.add(despesa.getValorDespesa());
                                tipoDespesaList.add(despesa.getTipoDespesa());
                                dataDespesaList.add(despesa.getDataDespesa());
                                observacaoList.add(despesa.getObservacao());

                                idContaList.add(despesa.getIdConta());
                                contaList.add(despesa.getConta());
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

    public void editarDespesa(DespesaDTO dto, Activity activity){

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

    public void verDespesa(String id, TextView textViewNomeDespesa, TextView textViewNomeConta,
                           TextView textViewValorDespesa, TextView textViewTipoDespesa,
                           TextView textViewDataDespesa, TextView textViewObservacao){

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        textViewNomeDespesa.setText(document.getData().get("despesa").toString());
                        textViewNomeConta.setText(document.getData().get("conta").toString());
                        textViewValorDespesa.setText(document.getData().get("valorDespesa").toString());
                        textViewTipoDespesa.setText(document.getData().get("tipoDespesa").toString());
                        textViewDataDespesa.setText(Utilitario.convertUsaToBr(document.getData().get("dataDespesa").toString()));
                        textViewObservacao.setText(document.getData().get("observacao").toString());

                        Log.d(Msg.INFO, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(Msg.INFO, "No such document");
                    }
                } else {
                    Log.d(Msg.INFO, "get failed with ", task.getException());
                }
            }
        });

    }
    public void buscarDespesa(RecyclerView recyclerView, Context context,TextView textViewValor,
                            String busca){
        List<DespesaDTO> listDespesa = new ArrayList<DespesaDTO>();

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .get()
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

                                listDespesa.add(dto);
//                                valorDespesa += Double.valueOf(document.getData().get("valorDespesa").toString());
//                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            List<String> idList = new ArrayList<>();
                            List<String> despesaList = new ArrayList<>();
                            List<String> valorDespesaList = new ArrayList<>();
                            List<String> tipoDespesaList = new ArrayList<>();
                            List<String> dataDespesaList = new ArrayList<>();
                            List<String> observacaoList = new ArrayList<>();

                            List<String> idContaList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();

                            for(DespesaDTO despesa : listDespesa){
                                if(despesa.getDespesa().equalsIgnoreCase(busca)){
//                                if(conta.getConta().contains(busca)){
                                    idList.add(despesa.getId());
                                    despesaList.add(despesa.getDespesa());
                                    valorDespesaList.add(despesa.getValorDespesa());
                                    tipoDespesaList.add(despesa.getTipoDespesa());
                                    dataDespesaList.add(despesa.getDataDespesa());
                                    observacaoList.add(despesa.getObservacao());

                                    idContaList.add(despesa.getIdConta());
                                    contaList.add(despesa.getConta());

                                    valorDespesa = Double.valueOf(despesa.getValorDespesa());

//                                  Log.i("---", conta.getId());
                                }else if(busca.equals("")){
                                    lerDespesas(recyclerView, context ,textViewValor);
                                }
                            }

                            if(!busca.equals("")){
                                textViewValor.setText(String.valueOf(valorDespesa));
                                layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager((layoutManager));
                                adapter = new DespesaAdapter(context,
                                        idList, despesaList, valorDespesaList, tipoDespesaList,
                                        dataDespesaList, observacaoList,
                                        idContaList, contaList);
                                recyclerView.setAdapter(adapter);
                            }

                        } else {
                            Log.w(Msg.ERROR, Msg.DOCUMENTO_F, task.getException());
                        }
                    }
                });
    }

    public void graficoBarChartDespesa(BarChart barChartDespesa){

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<Integer, Double> listaMes = new HashMap<>();

                    for(int i=1; i<=12; i++)
                        listaMes.put(i, 0.0);

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String dataBD = document.getData().get("dataDespesa").toString();
                        String[] parts = dataBD.split("-");
                        Double valor = Double.valueOf(document.getData().get("valorDespesa").toString());

                        if(parts[1].equals("01")){
                            listaMes.put(1, listaMes.get(1)+valor);
                        }else if(parts[1].equals("02")){
                            listaMes.put(2, listaMes.get(2)+valor);
                        }else if(parts[1].equals("03")){
                            listaMes.put(3, listaMes.get(3)+valor);
                        }else if(parts[1].equals("04")){
                            listaMes.put(4, listaMes.get(4)+valor);
                        }else if(parts[1].equals("05")){
                            listaMes.put(5, listaMes.get(5)+valor);
                        }else if(parts[1].equals("06")){
                            listaMes.put(6, listaMes.get(6)+valor);
                        }else if(parts[1].equals("07")){
                            listaMes.put(7, listaMes.get(7)+valor);
                        }else if(parts[1].equals("08")){
                            listaMes.put(8, listaMes.get(8)+valor);
                        }else if(parts[1].equals("09")){
                            listaMes.put(9, listaMes.get(9)+valor);
                        }else if(parts[1].equals("10")){
                            listaMes.put(10, listaMes.get(10)+valor);
                        }else if(parts[1].equals("11")){
                            listaMes.put(11, listaMes.get(11)+valor);
                        }else if(parts[1].equals("12")){
                            listaMes.put(12, listaMes.get(12)+valor);
                        }
                    }

                    ArrayList<BarEntry> barEntries = new ArrayList<>();
                    int mesBD = 0;
                    double valorSomado = 0.0;

                    for(Map.Entry<Integer, Double> entry : listaMes.entrySet()){
                         mesBD = entry.getKey();
                         valorSomado = entry.getValue();

                        barEntries.add(new BarEntry(mesBD, (float) valorSomado));
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Despesa");
                    barDataSet.setColor(Color.RED);
                    barDataSet.setValueTextSize(10f);

                    BarData barData = new BarData();
                    barData.addDataSet(barDataSet);

                    barChartDespesa.setData(barData);

                    String[] mes = new String[]
                            {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                                    "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

                    XAxis xAxis = barChartDespesa.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
                    xAxis.setLabelCount(12);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(2);
                    xAxis.setGranularityEnabled(true);

                    float barSpace = 0.10f;
                    float groupSpace = 0.10f;
//        barData.setBarWidth(0.27f);

//        barChartDespesa.getXAxis().setAxisMinimum(0);
//        barChartDespesa.getXAxis().setAxisMaximum(12);
//        barChartDespesa.getAxisLeft().setAxisMinimum(0);

//        barChartDespesa.groupBars(0,groupSpace, barSpace);

                    barChartDespesa.getDescription().setEnabled(false);

                    barChartDespesa.invalidate();

                    Log.d("---", listaMes.toString());
                }
            }
        });
    }
}
