package com.example.laris.Address;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.laris.Login.LoginActivity;
import com.example.laris.Model.Endereco;
import com.example.laris.Profile.ProfileActivity;
import com.example.laris.R;
import com.example.laris.databinding.ActivityAddressBinding;

import java.util.ArrayList;

public class AddressActivity extends AppCompatActivity {

    private ActivityAddressBinding binding;
    private Endereco endereco;
    private  ArrayList<Endereco> enderecos = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgBack.setOnClickListener(view -> finish());

        endereco = (Endereco) getIntent().getSerializableExtra("endereco");
        if (endereco != null){
            enderecos.add(endereco);
            enderecos.add(endereco);
            enderecos.add(endereco);
        }

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

        componentes();


    }

    private void showDialogDelete(int index){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(AddressActivity.this).inflate(R.layout.dialog_logout, (LinearLayout)findViewById(R.id.dialogLinearLayout));
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.btnSim).setOnClickListener(view12 -> {
            if (index==0){
                enderecos.remove(0);
                binding.layoutEnd1.setVisibility(View.GONE);
            }
            if (index==1){
                enderecos.remove(1);
                binding.layoutEnd2.setVisibility(View.GONE);
            }
            if (index==2){
                enderecos.remove(2);
                binding.layoutEnd3.setVisibility(View.GONE);
            }

            alertDialog.dismiss();
        });

        view.findViewById(R.id.btnNao).setOnClickListener(view1 -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void componentes(){
        if (enderecos.size()>0 ){

            binding.layoutEnd1.setVisibility(View.VISIBLE);
            binding.tvNome1.setText(enderecos.get(0).getNomeLocal());
            binding.tvEndereco1.setText(enderecos.get(0).getRua()+", "+enderecos.get(0).getNumero());
            binding.imgEdit1.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra("endereco", enderecos.get(0));
                startActivity(intent);
            });


        }


        if (enderecos.size()>1){
            binding.layoutEnd2.setVisibility(View.VISIBLE);
            binding.tvNome2.setText(enderecos.get(1).getNomeLocal());
            binding.tvEndereco2.setText(enderecos.get(1).getRua()+", "+enderecos.get(1).getNumero());
            binding.imgEdit2.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra("endereco", enderecos.get(1));
                startActivity(intent);
            });
        }


        if (enderecos.size()>2 ){
            binding.layoutEnd3.setVisibility(View.VISIBLE);
            binding.tvNome3.setText(enderecos.get(2).getNomeLocal());
            binding.tvEndereco3.setText(enderecos.get(2).getRua()+", "+enderecos.get(2).getNumero());
            binding.imgEdit3.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                intent.putExtra("endereco", enderecos.get(2));
                startActivity(intent);
            });
        }
    }
}