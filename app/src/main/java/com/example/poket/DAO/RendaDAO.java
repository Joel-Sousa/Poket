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
import com.example.poket.DTO.RendaDTO;
import com.example.poket.DTO.RendaDTO;
import com.example.poket.adapter.ContaAdapter;
import com.example.poket.adapter.DespesaAdapter;
import com.example.poket.adapter.RendaAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RendaDAO {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public RendaDAO(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void cadastarRenda(RendaDTO dto, Activity activity) {
        Map<String, String> dadosRenda = new HashMap<>();
        dadosRenda.put("renda", dto.getRenda());
        dadosRenda.put("valorRenda", dto.getValorRenda());
        dadosRenda.put("tipoRenda", dto.getTipoRenda());
        dadosRenda.put("dataRenda", dto.getDataRenda());
        dadosRenda.put("observacao", dto.getObservacao());

        dadosRenda.put("idConta", dto.getIdConta());
        dadosRenda.put("conta", dto.getConta());

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
                .document()
                .set(dadosRenda)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(Msg.SUCESSO, Msg.DOCUMENTO_S);

                        double resultado = Double.valueOf(dto.getValorConta()) + Double.valueOf(dto.getValorRenda());
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

    public void lerRendas(RecyclerView recyclerView, Context context,
                          TextView textViewRendaValorTotal, int mes){
        List<RendaDTO> listRenda = new ArrayList<RendaDTO>();

        db.collection("rendas")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;
                            double valorRenda = 0.0;

                            if(mes == 0){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    RendaDTO dto = new RendaDTO();
                                    dto.setId(document.getId());
                                    dto.setRenda(document.getData().get("renda").toString());
                                    dto.setValorRenda(document.getData().get("valorRenda").toString());
                                    dto.setTipoRenda(document.getData().get("tipoRenda").toString());
                                    dto.setDataRenda(document.getData().get("dataRenda").toString());
                                    dto.setObservacao(document.getData().get("observacao").toString());

                                    dto.setIdConta(document.getData().get("idConta").toString());
                                    dto.setConta(document.getData().get("conta").toString());

                                    listRenda.add(dto);
                                    valorRenda += Double.valueOf(document.getData().get("valorRenda").toString());
                                    Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                                }
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    String dataMes = document.getData().get("dataRenda").toString();
                                    String[] parts = dataMes.split("-");

                                    if(mes == Integer.parseInt(parts[1])){
                                        RendaDTO dto = new RendaDTO();
                                        dto.setId(document.getId());
                                        dto.setRenda(document.getData().get("renda").toString());
                                        dto.setValorRenda(document.getData().get("valorRenda").toString());
                                        dto.setTipoRenda(document.getData().get("tipoRenda").toString());
                                        dto.setDataRenda(document.getData().get("dataRenda").toString());
                                        dto.setObservacao(document.getData().get("observacao").toString());

                                        dto.setIdConta(document.getData().get("idConta").toString());
                                        dto.setConta(document.getData().get("conta").toString());

                                        listRenda.add(dto);
                                        valorRenda += Double.valueOf(document.getData().get("valorRenda").toString());
                                        Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                                    }
                                }
                            }

                            DecimalFormat df = new DecimalFormat("###.00");

                            textViewRendaValorTotal.setText(df.format(valorRenda));

                            List<String> idList = new ArrayList<>();
                            List<String> rendaList = new ArrayList<>();
                            List<String> valorRendaList = new ArrayList<>();
                            List<String> tipoRendaList = new ArrayList<>();
                            List<String> dataRendaList = new ArrayList<>();
                            List<String> observacaoList = new ArrayList<>();

                            List<String> idContaList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();

                            for(RendaDTO renda : listRenda){
                                idList.add(renda.getId());
                                rendaList.add(renda.getRenda());
                                valorRendaList.add(renda.getValorRenda());
                                tipoRendaList.add(renda.getTipoRenda());
                                dataRendaList.add(renda.getDataRenda());
                                observacaoList.add(renda.getObservacao());

                                idContaList.add(renda.getIdConta());
                                contaList.add(renda.getConta());
                            }

                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager((layoutManager));
                            adapter = new RendaAdapter(context, idList, rendaList, valorRendaList,
                                    tipoRendaList, dataRendaList, observacaoList,
                                    idContaList, contaList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }
    public void editarRenda(RendaDTO dto, Activity activity){

        Map<String, String> data = new HashMap<>();

        data.put("renda", dto.getRenda());
        data.put("valorRenda", dto.getValorRenda());
        data.put("tipoRenda", dto.getTipoRenda());
        data.put("dataRenda", dto.getDataRenda());
        data.put("observacao", dto.getObservacao());

        data.put("idConta", dto.getIdConta());
        data.put("conta", dto.getConta());

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
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

    public void deletarRenda(String id, String idConta, String valorRenda){

        db.collection("contas").document(user.getUid()).collection(user.getUid())
                .document(idConta).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        double valorAntigo1 = Double.valueOf(document.getData().get("valor").toString()) - Double.valueOf(valorRenda);
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

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
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

    public void verRenda(String id, TextView textViewNomeRenda, TextView textViewNomeConta,
                           TextView textViewValorRenda, TextView textViewTipoRenda,
                           TextView textViewDataRenda, TextView textViewObservacao){

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
                .document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {

                        textViewNomeRenda.setText(document.getData().get("renda").toString());
                        textViewNomeConta.setText(document.getData().get("conta").toString());
                        textViewValorRenda.setText(document.getData().get("valorRenda").toString());
                        textViewTipoRenda.setText(document.getData().get("tipoRenda").toString());
                        textViewDataRenda.setText(Utilitario.convertUsaToBr(document.getData().get("dataRenda").toString()));
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

    public void buscarRenda(RecyclerView recyclerView, Context context,TextView textViewValor,
                              String busca){

        List<RendaDTO> listRenda = new ArrayList<RendaDTO>();

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            RecyclerView.Adapter  adapter;
                            RecyclerView.LayoutManager layoutManager;
                            double valorRenda = 0.0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RendaDTO dto = new RendaDTO();
                                dto.setId(document.getId());
                                dto.setRenda(document.getData().get("renda").toString());
                                dto.setValorRenda(document.getData().get("valorRenda").toString());
                                dto.setTipoRenda(document.getData().get("tipoRenda").toString());
                                dto.setDataRenda(document.getData().get("dataRenda").toString());
                                dto.setObservacao(document.getData().get("observacao").toString());

                                dto.setIdConta(document.getData().get("idConta").toString());
                                dto.setConta(document.getData().get("conta").toString());

                                listRenda.add(dto);
//                                valorRenda += Double.valueOf(document.getData().get("valorRenda").toString());
//                                Log.d(Msg.INFO, document.getId() + " -> " + document.getData());
                            }

                            List<String> idList = new ArrayList<>();
                            List<String> rendaList = new ArrayList<>();
                            List<String> valorRendaList = new ArrayList<>();
                            List<String> tipoRendaList = new ArrayList<>();
                            List<String> dataRendaList = new ArrayList<>();
                            List<String> observacaoList = new ArrayList<>();

                            List<String> idContaList = new ArrayList<>();
                            List<String> contaList = new ArrayList<>();

                            for(RendaDTO renda : listRenda){
                                if(renda.getRenda().equalsIgnoreCase(busca)){
//                                if(conta.getConta().contains(busca)){
                                    idList.add(renda.getId());
                                    rendaList.add(renda.getRenda());
                                    valorRendaList.add(renda.getValorRenda());
                                    tipoRendaList.add(renda.getTipoRenda());
                                    dataRendaList.add(renda.getDataRenda());
                                    observacaoList.add(renda.getObservacao());

                                    idContaList.add(renda.getIdConta());
                                    contaList.add(renda.getConta());

                                    valorRenda = Double.valueOf(renda.getValorRenda());
                                }else if(busca.equals("")){
                                    lerRendas(recyclerView, context ,textViewValor,0);
                                }
                            }

                            if(!busca.equals("")){
                                textViewValor.setText(String.valueOf(valorRenda));
                                layoutManager = new LinearLayoutManager(context);
                                recyclerView.setLayoutManager((layoutManager));
                                adapter = new RendaAdapter(context, idList, rendaList, valorRendaList,
                                        tipoRendaList, dataRendaList, observacaoList,
                                        idContaList, contaList);
                                recyclerView.setAdapter(adapter);
                            }

                        } else {
                            Log.w(Msg.ERROR, Msg.DOCUMENTO_F, task.getException());
                        }
                    }
                });
    }

    public void graficoBarChartRenda(BarChart barChartRenda){

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<Integer, Double> listaMes = new TreeMap<>();

                    for(int i=1; i<=12; i++)
                        listaMes.put(i, 0.0);


                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String dataBD = document.getData().get("dataRenda").toString();
                        String[] parts = dataBD.split("-");
                        Double valor = Double.valueOf(document.getData().get("valorRenda").toString());

                        if(parts[1].equals("01") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(1, listaMes.get(1)+valor);
                        }else if(parts[1].equals("02") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(2, listaMes.get(2)+valor);
                        }else if(parts[1].equals("03") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(3, listaMes.get(3)+valor);
                        }else if(parts[1].equals("04") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(4, listaMes.get(4)+valor);
                        }else if(parts[1].equals("05") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(5, listaMes.get(5)+valor);
                        }else if(parts[1].equals("06") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(6, listaMes.get(6)+valor);
                        }else if(parts[1].equals("07") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(7, listaMes.get(7)+valor);
                        }else if(parts[1].equals("08") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(8, listaMes.get(8)+valor);
                        }else if(parts[1].equals("09") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(9, listaMes.get(9)+valor);
                        }else if(parts[1].equals("10") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(10, listaMes.get(10)+valor);
                        }else if(parts[1].equals("11") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(11, listaMes.get(11)+valor);
                        }else if(parts[1].equals("12") && parts[0].equals(Utilitario.ano())){
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

                    BarDataSet barDataSet = new BarDataSet(barEntries, "Renda");
                    barDataSet.setColor(Color.GREEN);
                    barDataSet.setValueTextSize(14f);

                    barDataSet.setValueFormatter(new DefaultValueFormatter(1));

                    BarData barData = new BarData();
                    barData.addDataSet(barDataSet);

                    barChartRenda.setData(barData);

                    String[] mes = new String[]
                            {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
                                    "Jul", "Ago", "Set", "Out", "Nov", "Dez"};


                    XAxis xAxis = barChartRenda.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setLabelCount(12);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setGranularity(2);
                    xAxis.setTextSize(14f);

                    Legend l = barChartRenda.getLegend();
                    l.setTextSize(14f);

                    barChartRenda.getDescription().setEnabled(false);
                    barChartRenda.invalidate();

                    Log.d("---", listaMes.toString());
                }
            }
        });
    }
}
