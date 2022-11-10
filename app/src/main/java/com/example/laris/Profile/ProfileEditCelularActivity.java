package com.example.laris.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.databinding.ActivityProfileEditCelularBinding;

public class ProfileEditCelularActivity extends AppCompatActivity {

    private ActivityProfileEditCelularBinding binding;
    private String celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditCelularBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());
        binding.imgBack.setOnClickListener(view -> finish());
    }


    private void validaDados(){
        celular = binding.etCelular.getText().toString();
        if (!binding.etCelular.isDone()){
            binding.tvErro.setText("Número de celular inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "Boa", Toast.LENGTH_SHORT).show();
            //enviaDados();
            finish();
        }
    }
}