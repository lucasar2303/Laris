package com.example.laris.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.databinding.ActivityProfileEditApelidoBinding;

import java.text.ParseException;
import java.util.Date;

public class ProfileEditApelidoActivity extends AppCompatActivity {

    private ActivityProfileEditApelidoBinding binding;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditApelidoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());
        binding.imgBack.setOnClickListener(view -> finish());
    }

    private void validaDados(){
        nome = binding.etNome.getText().toString();
        if (nome.length()<3){
            binding.tvErro.setText("Nome inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
        }else {
                //enviaDados();
            finish();
        }
    }
}