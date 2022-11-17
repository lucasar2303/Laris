package com.example.laris.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.laris.databinding.ActivityProfileEditBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;

public class ProfileEditActivity extends AppCompatActivity {

    private ActivityProfileEditBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();


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

    @Override
    protected void onStart() {
        super.onStart();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    binding.tvNome.setText(value.getString("nome")+" "+value.getString("sobrenome"));
                    binding.tvApelido.setText(value.getString("apelido"));
                    binding.tvCelular.setText(value.getString("celular"));
                    binding.tvEmail.setText(email);
                }
            }
        });

    }


}