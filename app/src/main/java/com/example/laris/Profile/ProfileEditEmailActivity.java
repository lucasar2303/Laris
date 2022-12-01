package com.example.laris.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.databinding.ActivityProfileEditEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileEditEmailActivity extends AppCompatActivity {

    private ActivityProfileEditEmailBinding binding;
    private String email, userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivityProfileEditEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());
        binding.imgBack.setOnClickListener(view -> finish());
    }

    private void validaDados(){
        email = binding.etEmail.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            salvarDadosUsuario();

        }else{
            binding.tvErro.setText("Email inválido");
            binding.tvErro.setVisibility(View.VISIBLE);

        }



    }
    @Override
    protected void onStart() {
        super.onStart();



        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    binding.etEmail.setText(value.getString("email"));
                }
            }
        });



    }

    private void salvarDadosUsuario(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseAuth.getInstance().getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    DocumentReference documentReference = db.collection("Usuarios").document(userId);
                    documentReference.update("email", email);
                    finish();
                }else{
                    String erro;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthUserCollisionException e){
                        erro = "O email ja esta sendo usado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail inválido";
                    }catch (FirebaseAuthRecentLoginRequiredException e){
                        erro = "Para melhor segurança, faça o login na conta novamente, para poder alterar o email";
                    }catch (Exception e){
                        erro = "Erro ao cadastrar usuário";
                    }

                    binding.tvErro.setText(erro);
                    binding.tvErro.setVisibility(View.VISIBLE);
                }
            }
        });





    }
}