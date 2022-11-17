package com.example.laris.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.databinding.ActivityProfileEditCelularBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileEditCelularActivity extends AppCompatActivity {

    private ActivityProfileEditCelularBinding binding;
    private String celular, userId;

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
            salvarDadosUsuario();
            finish();
        }
    }
    private void salvarDadosUsuario(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.update("celular", celular).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ProfileEditCelularActivity.this, "Número de celular atualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}