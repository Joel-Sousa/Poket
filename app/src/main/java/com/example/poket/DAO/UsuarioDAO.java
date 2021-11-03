package com.example.poket.DAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.poket.DTO.UsuarioDTO;
import com.example.poket.MainActivity;
import com.example.poket.util.Msg;
import com.example.poket.util.Utilitario;
import com.example.poket.view.Home;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;
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

        String path = db.collection("contas").document(user.getUid())
                .collection(user.getUid()).getPath();

        deleteCollection(db.collection(path), 50, EXECUTOR);

        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        Toast.makeText(context, Msg.DELETADO, Toast.LENGTH_LONG).show();
        mAuth.getInstance().signOut();
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
                Utilitario.toast(activity.getApplicationContext(), Msg.ERRORM);
            }
        });
    }
}
