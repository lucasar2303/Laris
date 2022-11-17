package com.example.laris.Profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.laris.Address.AddressActivity;
import com.example.laris.Login.LoginActivity;
import com.example.laris.MainActivity;
import com.example.laris.Notify.NotificationsActivity;
import com.example.laris.Policy.TermsPolicyActivity;
import com.example.laris.R;
import com.example.laris.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DecimalFormat;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.layoutSair.setOnClickListener(view -> showDialogDelete());
        binding.imgBack.setOnClickListener(view -> finish());
        binding.layoutNotify.setOnClickListener(view -> newActivty(NotificationsActivity.class));
        binding.layoutEditDados.setOnClickListener(view -> newActivty(ProfileEditActivity.class));
        binding.layoutTermos.setOnClickListener(view -> newActivty(TermsPolicyActivity.class));
        binding.layoutEndereco.setOnClickListener(view -> newActivty(AddressActivity.class));
    }

    private void showDialogDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_logout, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnSim).setOnClickListener(view12 -> {
            FirebaseAuth.getInstance().signOut();
            newActivty(LoginActivity.class);
            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnNao).setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
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
                    Float avaliacao = Float.valueOf(value.getString("avaliacao"));
                    DecimalFormat df = new DecimalFormat("0.00");
                    binding.tvAvaliacao.setText(df.format(avaliacao));
                }
            }
        });

    }
}