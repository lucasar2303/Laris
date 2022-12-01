package com.example.laris.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.laris.MainActivity;
import com.example.laris.R;
import com.example.laris.Register.SignupPersonalActivity;
import com.example.laris.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Button btnEntrar;
    private Boolean supLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEntrar.setOnClickListener(view -> verificaDados());
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.tvEsqueceSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecoveryPasswordActivity.class);
                startActivity(intent);
            }
        });

        binding.tvCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPersonalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verificaDados(){

            String email = binding.etEmail.getText().toString();
            String senha = binding.etSenha.getText().toString();


            if (email.isEmpty() || senha.isEmpty()){
                binding.tvErro.setVisibility(View.VISIBLE);
                binding.tvErro.setText("Digite suas credenciais");
                binding.layoutEmail.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.layoutSenha.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.etEmail.requestFocus();
            }else{
                autenticarUser();
            }


    }

    private void autenticarUser(){
        String email = binding.etEmail.getText().toString();
        String senha = binding.etSenha.getText().toString();
        binding.btnEntrar.setEnabled(false);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    binding.btnEntrar.setEnabled(true);
                    String erro;
                    try {
                        throw task.getException();
                    }
                    catch (Exception e){
                        erro = "Erro ao logar usuário";
                    }
                    binding.tvErro.setVisibility(View.VISIBLE);
                    binding.tvErro.setText(erro);
                    binding.layoutEmail.setBoxStrokeColor(Color.parseColor("#FF0000"));
                    binding.layoutSenha.setBoxStrokeColor(Color.parseColor("#FF0000"));
                    binding.etEmail.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}