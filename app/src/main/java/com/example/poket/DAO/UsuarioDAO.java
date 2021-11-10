package com.example.poket.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.poket.DTO.PlanejamentoFinanceiroDTO;
import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.MainActivity;
import com.example.poket.adapter.PlanejamentoFinanceiroAdapter;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.Home;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UsuarioDAO {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    public UsuarioDAO(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void criarAutenticacao(UsuarioDTO dto, Activity activity){

       try {
       mAuth.createUserWithEmailAndPassword(dto.getEmail(), dto.getSenha())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.i(Msg.SUCESSO, Msg.CADASTRADO);

                            Utilitario.toast(activity.getApplicationContext(), Msg.CADASTRADO);
                            reautenticacao(dto.getApelido(), dto.getEmail(), dto.getSenha(), true);
                            activity.finish();
                        }else{
                            try{
                                throw task.getException();
                            }catch (FirebaseAuthException e){
                                Log.e(Msg.ERROR, e.getMessage(), e);

                                Utilitario.toast(activity.getApplicationContext(), Msg.EMAIL_EXISTENTE);
                            } catch (Exception e) {
                                Log.e(Msg.ERROR, e.getMessage(), e);

                                Utilitario.toast(activity.getApplicationContext(), Msg.ERRORM);

                            e.printStackTrace();
                            }
                        }
                    }
                });
       }catch (Exception e){
           Log.e(Msg.ERROR, Msg.ERRORM, e);

           e.printStackTrace();
       }
    }

    private void reautenticacao(String usuario ,String email, String senha, boolean b) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, senha);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(Msg.SUCESSO, Msg.ALTENTICACAO_R);

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(usuario).build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(Msg.SUCESSO, Msg.ALTERADO);

                                            if(b) sairUsuario();
                                        }
                                    }
                                });
                    }
                });
    }

    public void autenticarUsuario(UsuarioDTO dto, Activity activity) {
        try {
        mAuth.signInWithEmailAndPassword(dto.getEmail(), dto.getSenha())
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           Intent intentLogin = new Intent(activity , Home.class);
                           activity.startActivity(intentLogin);

                        } else {
                            Log.e(Msg.ERROR, Msg.ALTENTICACAO_F, task.getException());

                            Utilitario.toast(activity.getApplicationContext(), Msg.EMAIL_SENHAI);
                        }
                    }
                });
        }catch (Exception e){
            Log.e(Msg.ERROR, Msg.ERRORM, e);
        }
    }

    public UsuarioDTO obterUsuario(){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUid(user.getUid());
        dto.setApelido(user.getDisplayName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public void atualizarUsuario(UsuarioDTO dto, Activity activity){
        try {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(dto.getApelido()).build();

            user.updateEmail(dto.getEmail())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(Msg.INFO, "User email address updated.");
                                    user.updateProfile(profileUpdates);
//                                    user.updateEmail(dto.getEmail());
                                    user.updatePassword(dto.getSenha());

                                    reautenticacao(dto.getApelido(), dto.getEmail(), dto.getSenha(), false);

                                    Log.i(Msg.SUCESSO, Msg.ALTERADO);

                                    Utilitario.toast(activity.getApplicationContext(), Msg.ALTERADO);

                            }else{
                                Utilitario.toast(activity.getApplicationContext(), Msg.EMAIL_EXISTENTE);
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void excluirUsuario(Context context){
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(Msg.INFO, Msg.DELETADO);

                            sairUsuario();
                        }
                    }
                });

        deleteCollection(db.collection("contas").document(user.getUid())
                .collection(user.getUid()), 50, EXECUTOR);

        deleteCollection(db.collection("despesas").document(user.getUid())
                .collection(user.getUid()), 50, EXECUTOR);

        deleteCollection(db.collection("rendas").document(user.getUid())
                .collection(user.getUid()), 50, EXECUTOR);

        deletarPFAll(user.getUid());

//        deleteCollection(db.collection("planejamentoFinanceiro").document(user.getUid())
//                .collection(user.getUid()), 50, EXECUTOR);


        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, Msg.DELETADO, Toast.LENGTH_LONG).show();
        mAuth.getInstance().signOut();
    }

    public void deletarPFAll(String uid){

        db.collection("planejamentoFinanceiro")
                .document(user.getUid()).collection(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> idPFList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult())
                                idPFList.add(document.getId());

                            for(String e : idPFList){
                                db.collection("planejamentoFinanceiro").document(uid)
                                        .collection(uid).document(e).collection(e)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                                if(task1.isSuccessful()) {
                                                    List<String> idHPFList = new ArrayList<>();

                                                    for (QueryDocumentSnapshot document1 : task1.getResult())
                                                        idHPFList.add(document1.getId());

                                                    for (String e1 : idHPFList) {

                                                    db.collection("planejamentoFinanceiro").document(uid)
                                                            .collection(uid).document(e).collection(e)
                                                            .document(e1)
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

                                        db.collection("planejamentoFinanceiro").document(uid)
                                                .collection(uid).document(e)
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

                            Log.d("---", "Tst");

                        } else {
                            Log.w(Msg.ERROR, Msg.ERRORM, task.getException());
                        }
                    }

                });
    }

    private Task<Void> deleteCollection(final CollectionReference collection, final int batchSize,
                                        Executor executor) {
        return Tasks.call(executor, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Query query = collection.orderBy(FieldPath.documentId()).limit(batchSize);

                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                while (deleted.size() >= batchSize) {
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = collection.orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);

                    deleted = deleteQueryBatch(query);
                }

                return null;
            }
        });
    }

    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (QueryDocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
        }
        Tasks.await(batch.commit());

        return querySnapshot.getDocuments();
    }


    public void sairUsuario(){
        FirebaseAuth.getInstance().signOut();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.e(Msg.SUCESSO, "Usuario Conectado!");
        } else {
            Log.e(Msg.SUCESSO, "Usuario saiu!");
        }
    }

    public void resetSenha(String email, Activity activity){
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Utilitario.toast(activity.getApplicationContext(), Msg.VERIFICAR_EMAIL);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg = "The user may have been deleted.";

                if(e.getMessage().contains(msg)){
                    Utilitario.toast(activity.getApplicationContext(), Msg.EMAIL_V);
                }else{
                    Utilitario.toast(activity.getApplicationContext(), Msg.ERRORM);
                }
            }
        });
    }

    public void graficoBarChartDespesaRenda(BarChart barChart){

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<Integer, Float> listaMes = new TreeMap<>();

                    for(int i=1; i<=12; i++)
                        listaMes.put(i, 0.0f);

                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String dataBD = document.getData().get("dataDespesa").toString();
                        String[] parts = dataBD.split("-");
                        float valor = Float.parseFloat(document.getData().get("valorDespesa").toString());

                        if(parts[1].equals("01") && parts[0].equals(Utilitario.ano())){
                            listaMes.put(1, listaMes.get(1)+valor);
                        }else if(parts[1].equals("02")  && parts[0].equals(Utilitario.ano())){
                            listaMes.put(2, listaMes.get(2)+valor);
                        }else if(parts[1].equals("03")  && parts[0].equals(Utilitario.ano())){
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

                    ArrayList<BarEntry> barEntriesDespesa = new ArrayList<>();
                    int mesBD = 0;
                    float valorSomado = 0.0f;

                    for(Map.Entry<Integer, Float> entry : listaMes.entrySet()){
                        mesBD = entry.getKey();
                        valorSomado = entry.getValue();

                        barEntriesDespesa.add(new BarEntry(mesBD, entry.getValue()));
                    }

                    // rendas
                    db.collection("rendas").document(user.getUid()).collection(user.getUid())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                            if (task1.isSuccessful()) {
                                Map<Integer, Float> listaMes1 = new TreeMap<>();

                                for(int i=1; i<=12; i++)
                                    listaMes1.put(i, 0.0f);

                                for (QueryDocumentSnapshot document1 : task1.getResult()) {

                                    String dataBD1 = document1.getData().get("dataRenda").toString();
                                    String[] parts1 = dataBD1.split("-");
                                    float valor1 = Float.parseFloat(document1.getData().get("valorRenda").toString());

                                    if(parts1[1].equals("01") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(1, listaMes1.get(1)+valor1);
                                    }else if(parts1[1].equals("02") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(2, listaMes1.get(2)+valor1);
                                    }else if(parts1[1].equals("03") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(3, listaMes1.get(3)+valor1);
                                    }else if(parts1[1].equals("04") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(4, listaMes1.get(4)+valor1);
                                    }else if(parts1[1].equals("05") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(5, listaMes1.get(5)+valor1);
                                    }else if(parts1[1].equals("06") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(6, listaMes1.get(6)+valor1);
                                    }else if(parts1[1].equals("07") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(7, listaMes1.get(7)+valor1);
                                    }else if(parts1[1].equals("08") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(8, listaMes1.get(8)+valor1);
                                    }else if(parts1[1].equals("09") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(9, listaMes1.get(9)+valor1);
                                    }else if(parts1[1].equals("10") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(10, listaMes1.get(10)+valor1);
                                    }else if(parts1[1].equals("11") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(11, listaMes1.get(11)+valor1);
                                    }else if(parts1[1].equals("12") && parts1[0].equals(Utilitario.ano())){
                                        listaMes1.put(12, listaMes1.get(12)+valor1);
                                    }
                                }

                                ArrayList<BarEntry> barEntriesRenda= new ArrayList<>();
                                int mesBD1 = 0;
                                float valorSomado1 = 0.0f;

                                for(Map.Entry<Integer, Float> entry1 : listaMes1.entrySet()){
                                    mesBD1 = entry1.getKey();
                                    valorSomado1 = entry1.getValue();

                                    barEntriesRenda.add(new BarEntry(mesBD1, entry1.getValue()));
                                }

                                BarDataSet barDataSet = new BarDataSet(barEntriesDespesa, "Despesa");
                                barDataSet.setColor(Color.RED);

                                BarDataSet barDataSet1 = new BarDataSet(barEntriesRenda, "Renda");
                                barDataSet1.setColor(Color.GREEN);

                                barDataSet.setValueFormatter(new DefaultValueFormatter(1));
                                barDataSet1.setValueFormatter(new DefaultValueFormatter(1));

                                BarData barData = new BarData();

                                barData.addDataSet(barDataSet);
                                barData.addDataSet(barDataSet1);
                                barData.setValueTextSize(14f);

                                barChart.setData(barData);

                                String[] mes = new String[]
                                        {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul",
                                                "Ago", "Set", "Out", "Nov", "Dez"};

                                Legend l = barChart.getLegend();
                                l.setTextSize(14f);

                                XAxis xAxis = barChart.getXAxis();
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(mes));
                                xAxis.setCenterAxisLabels(true);
                                xAxis.setLabelCount(12);
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setGranularityEnabled(true);
                                xAxis.setTextSize(14f);

                                barChart.setDragEnabled(true);
                                barChart.setVisibleXRangeMaximum(12);

                                float barSpace = 0.0f;
                                float groupSpace = 0.12f;
                                barData.setBarWidth(0.44f);

                                barChart.getXAxis().setAxisMinimum(0);
                                barChart.getXAxis().setAxisMaximum(0+barChart.getData().getGroupWidth(groupSpace,  barSpace)*12);
                                barChart.getAxisLeft().setAxisMinimum(0);
                                barChart.groupBars(0, groupSpace, barSpace);
                                barChart.getDescription().setEnabled(false);

                                barChart.invalidate();
                            }
                        }
                    });
                }
            }
        });

//        db.collection("despesas").document(user.getUid()).collection(user.getUid())
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                        if(!value.isEmpty()){
//                            Map<Integer, Double> listaMes = new TreeMap<>();
//
//                            for(int i=1; i<=12; i++)
//                                listaMes.put(i, 0.0);
//
//                            for (QueryDocumentSnapshot e : value){
//                                String dataBD = e.getData().get("dataDespesa").toString();
//                                String[] parts = dataBD.split("-");
//                                Double valor = Double.valueOf(e.getData().get("valorDespesa").toString());
//
//
//                            }
////
//                        }
//                    }
//                });
    }

    public void graficoPieChartDespesa(PieChart pieChartDespesa){

        db.collection("despesas").document(user.getUid()).collection(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Double> listaTipoDespesa = new TreeMap<>();

                    listaTipoDespesa.put("Alimentação", 0.0);
                    listaTipoDespesa.put("Veículo", 0.0);
                    listaTipoDespesa.put("Moradia", 0.0);
                    listaTipoDespesa.put("Lazer", 0.0);
                    listaTipoDespesa.put("Outros", 0.0);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tipoDespesa = document.getData().get("tipoDespesa").toString();
                        Double valorDespesa = Double.valueOf(document.getData().get("valorDespesa").toString());
                        String[] parts = document.getData().get("dataDespesa").toString().split("-");

                        if(tipoDespesa.equals("Alimentação") && parts[0].equals(Utilitario.ano())){
                            listaTipoDespesa.put(tipoDespesa, listaTipoDespesa.get(tipoDespesa)+valorDespesa);
                        }else if(tipoDespesa.equals("Veículo") && parts[0].equals(Utilitario.ano())){
                            listaTipoDespesa.put(tipoDespesa, listaTipoDespesa.get(tipoDespesa)+valorDespesa);
                        }else if(tipoDespesa.equals("Moradia") && parts[0].equals(Utilitario.ano())){
                            listaTipoDespesa.put(tipoDespesa, listaTipoDespesa.get(tipoDespesa)+valorDespesa);
                        }else if(tipoDespesa.equals("Lazer") && parts[0].equals(Utilitario.ano())){
                            listaTipoDespesa.put(tipoDespesa, listaTipoDespesa.get(tipoDespesa)+valorDespesa);
                        }else if(tipoDespesa.equals("Outros") && parts[0].equals(Utilitario.ano())){
                            listaTipoDespesa.put(tipoDespesa, listaTipoDespesa.get(tipoDespesa)+valorDespesa);
                        }
                    }

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();

                    double valor = 0.0;
                    for(Map.Entry<String, Double> e : listaTipoDespesa.entrySet()){
                        valor = e.getValue();
                        pieEntries.add(new PieEntry((float) valor, e.getKey()));
                    }

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(18f);

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter(pieChartDespesa));

                    pieChartDespesa.setData(pieData);
                    pieChartDespesa.getDescription().setEnabled(false);
                    pieChartDespesa.setUsePercentValues(true);
                    pieChartDespesa.setCenterText("R$");
                    pieChartDespesa.setCenterTextSize(18f);

                    pieChartDespesa.setHoleRadius(40);
                    pieChartDespesa.setTransparentCircleRadius(10);
                    pieChartDespesa.setEntryLabelColor(Color.BLUE);
                    pieChartDespesa.setDrawEntryLabels(false);

                    pieChartDespesa.setExtraOffsets(40, 5, 0, 5);

                    pieChartDespesa.animateX(1000);

                    Legend l = pieChartDespesa.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setTextSize(18f);
                    Log.d("---", "tst");
                    }
                }
            });
    }

    public void graficoPieChartRenda(PieChart pieChartRenda){

        db.collection("rendas").document(user.getUid()).collection(user.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Double> listaTipoRenda = new TreeMap<>();

                    listaTipoRenda.put("Salário", 0.0);
                    listaTipoRenda.put("Serviços", 0.0);
                    listaTipoRenda.put("Presente", 0.0);
                    listaTipoRenda.put("Aluguel", 0.0);
                    listaTipoRenda.put("Outros", 0.0);

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String tipoRenda = document.getData().get("tipoRenda").toString();
                        Double valorRenda = Double.valueOf(document.getData().get("valorRenda").toString());
                        String[] parts = document.getData().get("dataRenda").toString().split("-");

                        if(tipoRenda.equals("Salário") && parts[0].equals(Utilitario.ano())){
                            listaTipoRenda.put(tipoRenda, listaTipoRenda.get(tipoRenda)+valorRenda);
                        }else if(tipoRenda.equals("Serviços") && parts[0].equals(Utilitario.ano())){
                            listaTipoRenda.put(tipoRenda, listaTipoRenda.get(tipoRenda)+valorRenda);
                        }else if(tipoRenda.equals("Presente") && parts[0].equals(Utilitario.ano())){
                            listaTipoRenda.put(tipoRenda, listaTipoRenda.get(tipoRenda)+valorRenda);
                        }else if(tipoRenda.equals("Aluguel") && parts[0].equals(Utilitario.ano())){
                            listaTipoRenda.put(tipoRenda, listaTipoRenda.get(tipoRenda)+valorRenda);
                        }else if(tipoRenda.equals("Outros") && parts[0].equals(Utilitario.ano())){
                            listaTipoRenda.put(tipoRenda, listaTipoRenda.get(tipoRenda)+valorRenda);
                        }
                    }

                    ArrayList<PieEntry> pieEntries = new ArrayList<>();

                    double valor = 0.0;
                    for(Map.Entry<String, Double> e : listaTipoRenda.entrySet()){
                        valor = e.getValue();
                        pieEntries.add(new PieEntry((float) valor, e.getKey()));
                    }

                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieDataSet.setValueTextColor(Color.BLACK);
                    pieDataSet.setValueTextSize(18f);

                    PieData pieData = new PieData(pieDataSet);
                    pieData.setValueFormatter(new PercentFormatter(pieChartRenda));

                    pieChartRenda.setData(pieData);
                    pieChartRenda.getDescription().setEnabled(false);
                    pieChartRenda.setUsePercentValues(true);
                    pieChartRenda.setCenterText("R$");
                    pieChartRenda.setCenterTextSize(18f);

                    pieChartRenda.setHoleRadius(40);
                    pieChartRenda.setTransparentCircleRadius(10);
                    pieChartRenda.setEntryLabelColor(Color.BLUE);
                    pieChartRenda.setDrawEntryLabels(false);

                    pieChartRenda.setExtraOffsets(40, 5, 0, 5);

                    pieChartRenda.animateX(1000);

                    Legend l = pieChartRenda.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setOrientation(Legend.LegendOrientation.VERTICAL);
                    l.setTextSize(18f);
                    Log.d("---", "tst");
                }
            }
        });
    }
}
