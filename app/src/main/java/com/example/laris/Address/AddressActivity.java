package com.example.laris.Address;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.laris.Model.Endereco;
import com.example.laris.R;
import com.example.laris.databinding.ActivityAddressBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding binding;
    private Endereco endereco;
    private String  userId;
    public int enderecoSup;
    private List<DocumentSnapshot> enderecos;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgBack.setOnClickListener(view -> finish());

//        endereco = (Endereco) getIntent().getSerializableExtra("endereco");
//        if (endereco != null){
//            enderecos.add(endereco);
//            enderecos.add(endereco);
//            enderecos.add(endereco);
//        }

        binding.imgDelete1.setOnClickListener(view -> {
            showDialogDelete(0);
        });
        binding.imgDelete2.setOnClickListener(view -> {
            showDialogDelete(1);

        });
        binding.imgDelete3.setOnClickListener(view -> {
            showDialogDelete(2);
        });



        binding.tvAdd.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
            startActivity(intent);
        });




    }

    private void showDialogDelete(int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(AddressActivity.this).inflate(R.layout.dialog_logout, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();



        view.findViewById(R.id.btnSim).setOnClickListener(view12 -> {
            if (index==0){
                DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document("1");
                documentReference.delete();
                if (enderecoSup==3){
                    binding.layoutEnd3.setVisibility(View.GONE);
                }else if (enderecoSup==2){
                    binding.layoutEnd2.setVisibility(View.GONE);
                }
            }
            if (index==1){
                DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document("2");
                documentReference.delete();
                if (enderecoSup==3){
                    binding.layoutEnd3.setVisibility(View.GONE);
                }else if (enderecoSup==2){
                    binding.layoutEnd2.setVisibility(View.GONE);
                }
            }
            if (index==2){
                DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document("3");
                documentReference.delete();
                if (enderecoSup==3){
                    binding.layoutEnd3.setVisibility(View.GONE);
                }else if (enderecoSup==2){
                    binding.layoutEnd2.setVisibility(View.GONE);
                }
            }

            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnNao).setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();



        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference = db.collection("Usuarios").document(userId).collection("enderecos");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                binding.etComplemento.setText(value.getDocuments().get(0).getString("nomeLocal"));
                enderecoSup = value.getDocuments().size();
                enderecos = value.getDocuments();

                if (enderecoSup>0 ){

                    binding.layoutEnd1.setVisibility(View.VISIBLE);
                    binding.tvNome1.setText(enderecos.get(0).getString("nomeLocal"));

                    binding.tvEndereco1.setText(enderecos.get(0).getString("rua") + ", " + enderecos.get(0).getString("numero"));
                    binding.imgEdit1.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                        intent.putExtra("endereco", value.getDocuments().get(0).getId());
                        startActivity(intent);
                    });


                }

                if (enderecoSup>1 ){

                    binding.layoutEnd2.setVisibility(View.VISIBLE);
                    binding.tvNome2.setText(enderecos.get(1).getString("nomeLocal"));

                    binding.tvEndereco2.setText(enderecos.get(1).getString("rua") + ", " + enderecos.get(1).getString("numero"));
                    binding.imgEdit2.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                        intent.putExtra("endereco", value.getDocuments().get(1).getId());
                        startActivity(intent);
                    });


                }

                if (enderecoSup>2 ){

                    binding.layoutEnd3.setVisibility(View.VISIBLE);
                    binding.tvNome3.setText(enderecos.get(2).getString("nomeLocal"));

                    binding.tvEndereco3.setText(enderecos.get(2).getString("rua") + ", " + enderecos.get(2).getString("numero"));
                    binding.imgEdit3.setOnClickListener(view -> {
                        Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                        intent.putExtra("endereco", value.getDocuments().get(2).getId());
                        startActivity(intent);
                    });


                }

                if (enderecoSup>=3){
                    binding.tvAdd.setVisibility(View.GONE);
                }else{
                    binding.tvAdd.setVisibility(View.VISIBLE);
                }

            }
        });






    }
}