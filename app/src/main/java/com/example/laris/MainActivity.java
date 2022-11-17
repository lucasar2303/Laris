package com.example.laris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laris.Notify.NotificationsActivity;
import com.example.laris.Profile.ProfileActivity;
import com.example.laris.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String servico, montagem, contrato, dataVisita, dataEntrada, dataSaida, endereco, km, userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.Theme_Laris);
        setContentView(binding.getRoot());

        Bundle extrasFilter = getIntent().getExtras();
        if (extrasFilter != null){
            servico = extrasFilter.getString("servico");
            if(servico.equals("Consertos") || servico.equals("Montagem / Instalação")){
                montagem = extrasFilter.getString("montagem");
            }
            contrato = extrasFilter.getString("contrato");
            if (contrato.equals("A negociar")){
                dataVisita = extrasFilter.getString("dataVisita");
            }

            if (contrato.equals("Diária")){
                dataEntrada = extrasFilter.getString("dateEntrada");
                dataSaida = extrasFilter.getString("dateSaida");
            }

            endereco = extrasFilter.getString("endereco");
            km = extrasFilter.getString("km");
        }

        binding.btnService.setOnClickListener(view -> newActivty(FilterHome.class));
        binding.btnNotify.setOnClickListener(view -> newActivty(NotificationsActivity.class));
        binding.verMais.setOnClickListener(view -> newActivty(ListActivity.class));
        binding.imgUser.setOnClickListener(view -> newActivty(ProfileActivity.class));
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
                    binding.nameUser.setText("Olá, "+value.getString("nome"));
                }
            }
        });

    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}