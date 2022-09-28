package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laris.databinding.ActivityRecoveryPasswordBinding;

public class RecoveryPasswordActivity extends AppCompatActivity {

    private ActivityRecoveryPasswordBinding binding;
    private boolean envioSuporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoveryPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        envioSuporte = false;

        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                if (!email.equals("")){
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        if (!envioSuporte) {
                            binding.tvInforme.setText("Enviamos uma mensagem para o seu email com os próximos passos para recuperar sua senha!");
                            binding.tvInforme.setTextSize(20);
                            binding.etEmail.setVisibility(View.GONE);
                            binding.btnEnviar.setText("Voltar");
                            envioSuporte = true;
                        } else {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }else {Toast.makeText(RecoveryPasswordActivity.this, "Digite um email válido", Toast.LENGTH_SHORT).show();}
                }else{Toast.makeText(RecoveryPasswordActivity.this, "Digite um email", Toast.LENGTH_SHORT).show();}
            }
        });
    }
}