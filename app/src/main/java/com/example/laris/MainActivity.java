package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laris.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String servico, montagem, contrato, dataVisita, dataEntrada, dataSaida, endereco, km;

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

        binding.verMais.setOnClickListener(view -> newActivty(ListActivity.class));
   }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }
}