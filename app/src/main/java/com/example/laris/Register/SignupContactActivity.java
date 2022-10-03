package com.example.laris.Register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.laris.R;
import com.example.laris.databinding.ActivitySignupContactBinding;

public class SignupContactActivity extends AppCompatActivity {

    private ActivitySignupContactBinding binding;
    private String celular;
    private String email;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupPhotoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnEnviar.setOnClickListener(view -> validaDados());
    }

    private void validaDados(){
        Boolean confirm = true;
        email = binding.etEmail.getText().toString();
        celular = binding.etCelular.getText().toString();

        if (!binding.etCelular.isDone()){
            binding.tvErro.setText("Número de celular inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        }else{
            binding.tvErro.setText("Email inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }
        if (confirm){
            enviaDados();
        }
    }


    private void enviaDados(){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key_register), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", email);
        editor.putString("celular", celular);

        editor.commit();

        Intent intent = new Intent(getApplicationContext(), SignupPasswordActivity.class);
        startActivity(intent);
        finish();
    }
}