package com.example.laris;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.laris.databinding.ActivityFilterHomeBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterHome extends AppCompatActivity {

    private ActivityFilterHomeBinding binding;
    private RadioButton btnServico, btnMontagem, btnContrato, btnEndereco;
    private String servicoSelect, montagemSelect, contratoSelect, enderecoSelect, kmSelect, auxDate;
    private String dateVisita, dateEntrada, dateSaida;
    private int hourVisit, minuteVisit;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDatePicker();

        binding.imgBack.setOnClickListener(view -> finish());
        binding.tvVisita.setOnClickListener(view -> openDatePicker("visita"));
        binding.tvEntrada.setOnClickListener(view -> openDatePicker("diariaEntrada"));
        binding.tvSaida.setOnClickListener(view -> {
            if (dateEntrada == null){
                Toast.makeText(this, "Escolha a data de entrada", Toast.LENGTH_SHORT).show();
            }else{
                openDatePicker("diariaSaida");
            }

        });

        binding.btnBuscar.setOnClickListener(view -> {
            verificaCampos();
        });

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int o = i-i/2;
                binding.tvQntKm.setText("" + o);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(day, month, year);

                SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
                Date dataAtual = new Date();
                String dataAtualFormatada = formatData.format(dataAtual);
                String dataSeparada[] = dataAtualFormatada.split("/");


                if (year!=Integer.parseInt(dataSeparada[2])){
                    Toast.makeText(FilterHome.this, "Escolha um ano válida", Toast.LENGTH_SHORT).show();
                }else if(Integer.parseInt(dataSeparada[1])>month){
                    Toast.makeText(FilterHome.this, "Escolha um mês válido", Toast.LENGTH_SHORT).show();
                }else if (Integer.parseInt(dataSeparada[0])>=day && Integer.parseInt(dataSeparada[1])==month){
                    Toast.makeText(FilterHome.this, "Escolha um dia válido", Toast.LENGTH_SHORT).show();
                }else{
                    if (auxDate.equals("visita")){
                        binding.tvVisita.setText(date);
                        dateVisita = date;
                    }else if(auxDate.equals("diariaEntrada")){
                        if (dateSaida!=null){
                            try {
                                Date dataSaidaC = formatData.parse(binding.tvSaida.getText().toString());
                                Date dataEntradaC = formatData.parse(date);

                                if (dataEntradaC.after(dataSaidaC)){
                                    Toast.makeText(FilterHome.this, "Escolha um dia antes da saída", Toast.LENGTH_SHORT).show();

                                }else{
                                    binding.tvEntrada.setText(date);
                                    dateEntrada = date;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else{
                            binding.tvEntrada.setText(date);
                            dateEntrada = date;
                        }

                    }else if(auxDate.equals("diariaSaida")){

                            try {
                                Date dataEntradaC = formatData.parse(binding.tvEntrada.getText().toString());
                                Date dataSaidaC = formatData.parse(date);

                                if (dataSaidaC.after(dataEntradaC) || dataSaidaC.equals(dataEntradaC)){
                                    binding.tvSaida.setText(date);
                                    dateSaida = date;
                                }else{
                                    Toast.makeText(FilterHome.this, "Escolha um dia de saída válido", Toast.LENGTH_SHORT).show();
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                    }
                }


            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = android.app.AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year){
        if (day<10){
            return "0"+day + "/" + month + "/" + year;
        }else {
            return day + "/" + month + "/" + year;
        }
    }

    public void openDatePicker(String aux){
        auxDate = aux;
        datePickerDialog.show();
    }

    public void checkServico(View v){
        int radioId = binding.groupServico.getCheckedRadioButtonId();

        btnServico = findViewById(radioId);
        servicoSelect = btnServico.getText().toString();

        if (servicoSelect.equals("Consertos") || servicoSelect.equals("Montagem / Instalação")){
            binding.tvMontagem.setVisibility(View.VISIBLE);
            binding.groupMontagem.setVisibility(View.VISIBLE);
        }else{
            binding.tvMontagem.setVisibility(View.GONE);
            binding.groupMontagem.setVisibility(View.GONE);
        }
    }

    public void checkMontagem(View v){
        int radioId = binding.groupMontagem.getCheckedRadioButtonId();

        btnMontagem = findViewById(radioId);
        montagemSelect = btnMontagem.getText().toString();


    }

    public void checkEndereco(View v){
        int radioId = binding.groupEndereco.getCheckedRadioButtonId();

        btnEndereco = findViewById(radioId);
        enderecoSelect = btnEndereco.getText().toString();


    }

    public void checkContrato(View v){
        int radioId = binding.groupContrato.getCheckedRadioButtonId();

        btnContrato = findViewById(radioId);
        contratoSelect = btnContrato.getText().toString();

        if (contratoSelect.equals("Diária")){
            binding.layoutDatas.setVisibility(View.VISIBLE);
            binding.tvVisita.setVisibility(View.GONE);
        }else if (contratoSelect.equals("A negociar")){
            binding.tvVisita.setVisibility(View.VISIBLE);
            binding.layoutDatas.setVisibility(View.GONE);
        }

    }

    public void verificaCampos(){
        if (servicoSelect==null){
            Toast.makeText(this, "Selecione um serviço", Toast.LENGTH_SHORT).show();
            return;
        }

        if(servicoSelect.equals("Consertos") || servicoSelect.equals("Montagem / Instalação")){
            if (montagemSelect==null){
                Toast.makeText(this, "Selecione um tipo de montagem / instalação", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(contratoSelect==null){
            Toast.makeText(this, "Selecione um tipo de contrato", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contratoSelect.equals("A negociar")){
            if (dateVisita == null){
                Toast.makeText(this, "Escolha a data da visita", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (contratoSelect.equals("Diária")){
            if (dateEntrada == null || dateSaida == null){
                Toast.makeText(this, "Escolha as datas da diária", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(enderecoSelect==null){
            Toast.makeText(this, "Selecione um endereço", Toast.LENGTH_SHORT).show();
            return;
        }
            kmSelect = binding.tvKm.getText().toString();
            enviaDados();
    }

    public void enviaDados(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.putExtra("servico", servicoSelect);

        if(servicoSelect.equals("Consertos") || servicoSelect.equals("Montagem / Instalação")){
            intent.putExtra("montagem", montagemSelect);
        }

        intent.putExtra("contrato", contratoSelect);

        if (contratoSelect.equals("A negociar")){
            intent.putExtra("dataVisita", dateVisita);
        }

        if (contratoSelect.equals("Diária")){
            intent.putExtra("dataEntrada", dateEntrada);
            intent.putExtra("dataSaida", dateSaida);
        }

        intent.putExtra("endereco", enderecoSelect);
        intent.putExtra("km", kmSelect);

        startActivity(intent);
        finish();
    }


//
//    private void showDialogFilter(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(FilterHome.this, R.style.AlertDialogTheme);
//        View view = LayoutInflater.from(FilterHome.this).inflate(R.layout.dialog_date, (LinearLayout)findViewById(R.id.dialogLinearLayout));
//        builder.setView(view);
//
//        final AlertDialog alertDialog = builder.create();
//
//        TextView hora = (TextView) view.findViewById(R.id.tvHora);
//
//        view.findViewById(R.id.tvHora).setOnClickListener(view12 -> {
//            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int sHour, int sMinute) {
//                    hourVisit = sHour;
//                    minuteVisit = sMinute;
//                    hora.setText(String.format(Locale.getDefault(), "%02d:%02d",hourVisit,minuteVisit));
//
//
//                }
//            };
//            int style = android.app.AlertDialog.THEME_HOLO_DARK;
//
//            TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener, hourVisit, minuteVisit, true);
//
//
//
//            timePickerDialog.setTitle("Selecione o horário");
//            timePickerDialog.show();
//
//        });
//
//
//        alertDialog.show();
//    }





}