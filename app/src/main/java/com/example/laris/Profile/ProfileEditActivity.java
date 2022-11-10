package com.example.laris.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.laris.databinding.ActivityProfileEditBinding;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgBack.setOnClickListener(view -> finish());

        binding.layoutApelido.setOnClickListener(view -> newActivty(ProfileEditApelidoActivity.class));
        binding.layoutEmail.setOnClickListener(view -> newActivty(ProfileEditEmailActivity.class));
        binding.layoutCelular.setOnClickListener(view -> newActivty(ProfileEditCelularActivity.class));
        binding.layoutSenha.setOnClickListener(view -> newActivty(ProfileEditSenhaActivity.class));

    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }


}