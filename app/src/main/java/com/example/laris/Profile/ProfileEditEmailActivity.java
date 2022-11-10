package com.example.laris.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laris.databinding.ActivityProfileEditEmailBinding;

public class ProfileEditEmailActivity extends AppCompatActivity {

    private ActivityProfileEditEmailBinding binding;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());
        binding.imgBack.setOnClickListener(view -> finish());
    }

    private void validaDados(){
        email = binding.etEmail.getText().toString();
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //enviaDados();
            finish();
        }else{
            binding.tvErro.setText("Email inválido");
            binding.tvErro.setVisibility(View.VISIBLE);

        }



    }
}