package com.example.laris.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.databinding.ActivityRecoveryPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private ActivityRecoveryPasswordBinding binding;
    private boolean envioSuporte = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivityRecoveryPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> finish());

        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                if (!email.equals("")){
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (!envioSuporte) {
                                        binding.tvInforme.setText("Enviamos uma mensagem para o seu email com os próximos passos para recuperar sua senha!");
                                        binding.tvInforme.setTextSize(20);
                                        binding.etEmail.setVisibility(View.GONE);
                                        binding.btnEnviar.setText("Voltar");
                                        binding.tvErro.setVisibility(View.GONE);
                                        envioSuporte = true;
                                    } else {

                                        finish();
                                    }
                                } else {
                                    String erro;
                                    try {
                                        throw task.getException();
                                    } catch (Exception e) {
                                        binding.tvErro.setVisibility(View.VISIBLE);
                                        binding.tvErro.setText("Email não cadastrado");
                                    }
                                }
                            }});


                    }else {binding.tvErro.setVisibility(View.VISIBLE);
                        binding.tvErro.setText("Email inválido");}
                }else{binding.tvErro.setVisibility(View.VISIBLE);
                    binding.tvErro.setText("Digite seu email");}
            }
        });
    }
}