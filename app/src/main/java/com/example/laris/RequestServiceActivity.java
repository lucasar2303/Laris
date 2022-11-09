package com.example.laris;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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

        nome = "Lucas";
        profissao = "Pintor";
        avaliacao = "4,9";

        servico = "Pintura";
        contrato = "Diária";
        dataEntrada = "10/11/2022";
        dataSaida = "12/11/2022";
        valorContrato = 70.77;

/*
        servico = "Limpeza";
        contrato = "A negociar";
        dataVisita = "15/11/2022";

 */

        iniciaComponents();



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