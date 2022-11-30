package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.laris.Model.Colaborador;
import com.example.laris.databinding.ActivityRequestServiceBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

import javax.xml.datatype.Duration;

public class RequestServiceActivity extends AppCompatActivity {

    private ActivityRequestServiceBinding binding;
    private double valorContrato;
    private String nome, profissao, avaliacao;
    private String servico, contrato, dataEntrada, dataSaida, dataVisita, valorTotal;

    @Override
    protected void onResume() {
        super.onResume();
        String[] itens = getResources().getStringArray(R.array.pagamentos);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item, itens);
        binding.etSpinner.setAdapter(arrayAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvCancelar.setOnClickListener(view -> finish());

        Date dataTeste = new Date();
        SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataTeste );
        cal.add(Calendar.DATE, 1);
        dataTeste = cal.getTime();
        String proxData = formatData.format(dataTeste);



        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        dataEntrada = pref.getString("dataEntrada", proxData);
        dataSaida = pref.getString("dataSaida", proxData);
        dataVisita = pref.getString("dataVisita", proxData);



        Colaborador colaborador1 = (Colaborador) getIntent().getSerializableExtra("colab");
        if (colaborador1!=null){
            nome = colaborador1.getNome();
            servico = colaborador1.getProfissao();
            valorContrato = colaborador1.getValor();
            contrato = colaborador1.getContrato();
            avaliacao = "5.0";

            if (servico.equals("Pintura")){
                profissao = "Pintor";
            }

            if (servico.equals("Montagem") || servico.equals("Conserto")){
                profissao = "Montador";
            }

            if (servico.equals("Limpeza")){
                profissao = "Faxineiro";
            }

        }


/*
        servico = "Limpeza";
        contrato = "A negociar";
        dataVisita = "15/11/2022";

 */
        iniciaComponents();
    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    private void iniciaComponents(){

        binding.tvNome.setText(nome);
        binding.tvProfissao.setText(profissao);
        binding.tvAvaliacao.setText(avaliacao);

        binding.tvServico.setText(servico);
        binding.tvContrato.setText(contrato);
        if (contrato.equals("A negociar")){
            binding.textValor.setVisibility(View.GONE);
            binding.tvValor.setVisibility(View.GONE);
            binding.textDataEntrada.setVisibility(View.GONE);
            binding.textDataSaida.setVisibility(View.GONE);
            binding.tvDataEntrada.setVisibility(View.GONE);
            binding.tvDataSaida.setVisibility(View.GONE);

            binding.tvDataVisita.setVisibility(View.VISIBLE);
            binding.textDataVisita.setVisibility(View.VISIBLE);
            binding.tvDataVisita.setText(dataVisita);

            binding.tvValorTotal.setVisibility(View.GONE);
            binding.textValorTotal.setVisibility(View.GONE);
            binding.textPagamento.setVisibility(View.GONE);
            binding.layoutSpinner.setVisibility(View.GONE);
        }else if (contrato.equals("Diária")){
            binding.tvValor.setText("R$ " + String.format( "%.2f", valorContrato ));
            binding.tvDataEntrada.setText(dataEntrada);
            binding.tvDataSaida.setText(dataSaida);

            SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date dataEntradaC = formatData.parse(dataEntrada);
                Date dataSaidaC = formatData.parse(dataSaida);

                Calendar calendarEntrada = Calendar.getInstance();
                calendarEntrada.setTime(dataEntradaC);

                Calendar calendarSaida = Calendar.getInstance();
                calendarSaida.setTime(dataSaidaC);

                long diaria = (calendarSaida.getTimeInMillis()-calendarEntrada.getTimeInMillis())/(1000*60*60*24);

                double valorTotal = (diaria+1)*valorContrato;

                binding.tvValorTotal.setText("R$ " + String.format( "%.2f", valorTotal ));

            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

    }


}