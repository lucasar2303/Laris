package com.example.laris;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.laris.Model.Colaborador;
import com.example.laris.Task.TaskActivity;
import com.example.laris.databinding.ActivityRequestServiceBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.Duration;

public class RequestServiceActivity extends AppCompatActivity {

    private ActivityRequestServiceBinding binding;
    private double valorContrato, valorTotal;
    private String nomeUser;
    private String nomeColab, profissao, avaliacao;
    private String servico, contrato, dataEntrada, dataSaida, dataVisita, endereco, id, userId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


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
        setTheme(R.style.Theme_Laris);
        binding = ActivityRequestServiceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(view -> setNomeUser());
        binding.tvCancelar.setOnClickListener(view -> finish());




        //Data atual caso n tenha selecionado a data antes
        Date dataTeste = new Date();
        SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataTeste );
        cal.add(Calendar.DATE, 1);
        dataTeste = cal.getTime();
        String proxData = formatData.format(dataTeste);


        // recuperando data do filtro
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        dataEntrada = pref.getString("dataEntrada", proxData);
        dataSaida = pref.getString("dataSaida", proxData);
        dataVisita = pref.getString("dataVisita", proxData);
        endereco = pref.getString("endereco", proxData);

        //recuperando colab selecionado
        Colaborador colaborador1 = (Colaborador) getIntent().getSerializableExtra("colab");
        if (colaborador1!=null){
            nomeColab = colaborador1.getNome();
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

            id = colaborador1.getId();
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

        binding.tvNome.setText(nomeColab);
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

                valorTotal = (diaria+1)*valorContrato;

                binding.tvValorTotal.setText("R$ " + String.format( "%.2f", valorTotal ));

            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

    }

    public void setNomeUser(){
        //Dados do usuario
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId != null){
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null){

                        nomeUser = value.getString("apelido");

                        enviaDados(nomeUser);

                    }
                }
            });

        }

        //
    }

    public void enviaDados(String nomeUser){

//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();


        String descricao = binding.etDescricao.getText().toString();
        String pagamento = binding.etSpinner.getText().toString();

        //criar usuario
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Map<String, Object> atividade = new HashMap<>();

        String filename = UUID.randomUUID().toString();


        atividade.put("idTask", filename);
        atividade.put("idUser", userId);
        atividade.put("idColab", id);
        atividade.put("nomeUser", nomeUser);
        atividade.put("nomeColab", nomeColab);
        atividade.put("status", "A confirmar");

        atividade.put("servico", servico);
        atividade.put("contrato", contrato);
        atividade.put("valorDiaria", valorContrato);
        atividade.put("valorTotal", valorTotal);
        atividade.put("descricao", descricao);
        atividade.put("pagamento", pagamento);
        atividade.put("endereco", endereco);

        atividade.put("dataEntrada", dataEntrada);
        atividade.put("dataSaida", dataSaida);
        atividade.put("dataVisita", dataVisita);




        DocumentReference documentReference = db.collection("Atividades").document(filename);
        documentReference.set(atividade).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", "Sucesso ao salvar dados");
                        Toast.makeText(RequestServiceActivity.this, "Serviço Solicitado", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                        startActivity(intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Erro ao salvar dados" + e.toString());
                    }
                });
    }






}