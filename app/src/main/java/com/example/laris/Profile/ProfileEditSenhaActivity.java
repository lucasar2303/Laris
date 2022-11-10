package com.example.laris.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.Register.SignupConfirmActivity;
import com.example.laris.databinding.ActivityProfileEditSenhaBinding;

public class ProfileEditSenhaActivity extends AppCompatActivity {

    private ActivityProfileEditSenhaBinding binding;
    private boolean confirm1 = false, confirm2 = false, confirm3 = false, confirm4 = false;
    private String senha, senhaAntigaBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditSenhaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> enviaDados());
        binding.imgBack.setOnClickListener(view -> finish());

        senhaAntigaBD = "12qwaszxX!";


        binding.etSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                senha = binding.etSenha.getText().toString();
                String senhaTrue = binding.etSenhaTrue.getText().toString();
                boolean supConfirm3 = false;
                boolean supConfirm2 = false;
                char[] caracteres = senha.toCharArray();

                //Verifica a senha
                for (int u = 0; u < senha.length(); u++){
                    if (Character.isUpperCase(caracteres[u])){
                        supConfirm2 = true;
                    }
                    if (!Character.isDigit(caracteres[u] )&&!Character.isLetter(caracteres[u])&&!Character.isSpaceChar(caracteres[u])){
                        supConfirm3 = true;
                    }
                }
                //Confirmando se a senha possui letra maiuscula
                if (supConfirm2){
                    binding.tvconfirm2.setTextColor(Color.parseColor("#121212"));
                    confirm2 = true;
                }else{
                    binding.tvconfirm2.setTextColor(Color.parseColor("#999999"));
                    confirm2 = false;
                }
                //Confirmando se a senha possui caractere especial
                if (supConfirm3){
                    binding.tvconfirm3.setTextColor(Color.parseColor("#121212"));
                    confirm3 = true;
                }else{
                    binding.tvconfirm3.setTextColor(Color.parseColor("#999999"));
                    confirm3 = false;
                }
                //Confirmando se a senha possui entre 8 a 15 caracteres
                if (senha.length() >= 8 && senha.length()<= 15){
                    binding.tvconfirm1.setTextColor(Color.parseColor("#121212"));
                    confirm1 = true;
                }else{
                    binding.tvconfirm1.setTextColor(Color.parseColor("#999999"));
                    confirm1 = false;
                }

                if (senha.equals(senhaTrue)){
                    binding.tvconfirm4.setTextColor(Color.parseColor("#121212"));
                    confirm4 = true;
                }else{
                    binding.tvconfirm4.setTextColor(Color.parseColor("#999999"));
                    confirm4 = false;
                }

                botaoValido();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.etSenhaTrue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                senha = binding.etSenha.getText().toString();
                String senhaTrue = binding.etSenhaTrue.getText().toString();

                if (senha.equals(senhaTrue)){
                    binding.tvconfirm4.setTextColor(Color.parseColor("#121212"));
                    confirm4 = true;
                }else{
                    binding.tvconfirm4.setTextColor(Color.parseColor("#999999"));
                    confirm4 = false;
                }

                botaoValido();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void botaoValido(){
        if (confirm1 && confirm2 && confirm3 && confirm4 ){
            binding.btnEnviar.setEnabled(true);
            binding.btnEnviar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7C4EFF")));
        }else{

            binding.btnEnviar.setEnabled(false);
            binding.btnEnviar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
        }

    }

    private void enviaDados(){

        if (binding.etSenhaAntiga.getText().toString().equals(senhaAntigaBD)){
            if (!senhaAntigaBD.equals(senha)){
                Intent intent = new Intent(getApplicationContext(), ProfileEditActivity.class);
                startActivity(intent);
                finish();
            }else{
                binding.tvErro.setText("Senha antiga e nova são iguais");
                binding.tvErro.setVisibility(View.VISIBLE);
            }
        }else{
            binding.tvErro.setText("Senha antiga errada");
            binding.tvErro.setVisibility(View.VISIBLE);
        }


    }

}