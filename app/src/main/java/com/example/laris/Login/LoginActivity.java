package com.example.laris.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.laris.Register.SignupPersonalActivity;
import com.example.laris.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                finish();
            }
        });

        binding.tvCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPersonalActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void verificaDados(){
        Boolean cpfValido = binding.etCpf.isDone();
        Boolean senhaValida = false;
        String senha = binding.etSenha.getText().toString();

        if (!senha.equals("")){
            senhaValida = true;
        }else{
            senhaValida = false;

        }

        if (cpfValido && senhaValida){
            //Chamar banco
        }else{
            binding.layoutCPF.setBoxStrokeColor(Color.parseColor("#7C4EFF"));
            binding.layoutSenha.setBoxStrokeColor(Color.parseColor("#7C4EFF"));
            binding.tvErro.setVisibility(View.GONE);
            if (!cpfValido && !senhaValida){
                binding.tvErro.setVisibility(View.VISIBLE);
                binding.tvErro.setText("Digite suas credenciais");
                binding.layoutCPF.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.layoutSenha.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.etCpf.requestFocus();
            }else{

            if (!senhaValida){
                binding.tvErro.setVisibility(View.VISIBLE);
                binding.tvErro.setText("Digite sua senha");
                binding.layoutSenha.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.etSenha.requestFocus();
            }
            if (!cpfValido){
                binding.tvErro.setVisibility(View.VISIBLE);
                binding.tvErro.setText("Digite um CPF válido");
                binding.layoutCPF.setBoxStrokeColor(Color.parseColor("#FF0000"));
                binding.etCpf.requestFocus();
            }}

        }


    }

}