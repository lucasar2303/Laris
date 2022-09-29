package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.laris.databinding.ActivitySignupPersonalBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupPersonalActivity extends AppCompatActivity {

    private ActivitySignupPersonalBinding binding;
    String pattern = "dd/MM/yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    private String nome;
    private String sobrenome;
    private String genero;
    private String data;
    private String cpf;

    @Override
    protected void onResume() {
        super.onResume();
        String[] itens = getResources().getStringArray(R.array.generos);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, itens);
        binding.etSpinner.setAdapter(arrayAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());
        Toast.makeText(this, binding.etSpinner.getText(), Toast.LENGTH_SHORT).show();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void enviaDados(){

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key_register), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nome", nome);
        editor.putString("sobrenome", sobrenome);
        editor.putString("cpf", cpf);
        editor.putString("data", data);
        editor.putString("genero", genero);
        editor.commit();

        Intent intent = new Intent(getApplicationContext(), SignupPhotoActivity.class);
        startActivity(intent);
        finish();
    }

    private void validaDados(){
        Boolean confirm = true;
        nome = binding.etNome.getText().toString();
        sobrenome = binding.etSobrenome.getText().toString();
        cpf = binding.etCpf.getText().toString();
        genero = binding.etSpinner.getText().toString();
        sdf.setLenient(false);
        data = binding.etData.getText().toString();

        if (genero.equals("")){
            binding.tvErro.setText("Genero inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (!binding.etCpf.isDone()){
            binding.tvErro.setText("Cpf inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else if (sobrenome.length()<3){
            binding.tvErro.setText("Sobrenome inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else if (nome.length()<3){
            binding.tvErro.setText("Nome inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else {
            try {
                Date date = sdf.parse(data);
                // Data formatada corretamente

                String[] dataYear = data.split("/");
                String dataYear2 = dataYear[2].replace("_", "");
                if (Integer.parseInt(dataYear2)<1900){
                    binding.tvErro.setText("Data inválida");
                    binding.tvErro.setVisibility(View.VISIBLE);
                    confirm = false;
                }
            } catch (ParseException e) {
                // Erro de parsing!!
                e.printStackTrace();
                binding.tvErro.setText("Data inválida");
                binding.tvErro.setVisibility(View.VISIBLE);
                confirm = false;
            }

        if (confirm){
            enviaDados();
        }

    }}

}