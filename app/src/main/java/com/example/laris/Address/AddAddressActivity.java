package com.example.laris.Address;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.laris.Api.CEPService;
import com.example.laris.Model.CEP;
import com.example.laris.Model.Endereco;
import com.example.laris.Profile.ProfileEditApelidoActivity;
import com.example.laris.databinding.ActivityAddAddressBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddAddressActivity extends AppCompatActivity {

    private ActivityAddAddressBinding binding;
    private Retrofit retrofit;
    private String nome, cep, rua, bairro, cidade, estado, numero, complemento;
    private Endereco endereco;
    private int enderecoSup;
    private List<Endereco> enderecos = new ArrayList<Endereco>();
    private List<DocumentSnapshot> enderecosDocuments;
    private String  userId, enderecoEdit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String enderecoSup1, enderecoSup2, enderecoSup3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        binding.btnEnviar.setOnClickListener(view -> validaDados(10));

        enderecoEdit = getIntent().getStringExtra("endereco");

        if (enderecoEdit!=null){
            Toast.makeText(this, enderecoEdit, Toast.LENGTH_SHORT).show();
            editEndereco();

        }

        binding.imgBack.setOnClickListener(view -> finish());





        binding.etCep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String cep = binding.etCep.getText().toString();
                    cep = cep.replaceAll("-","");
                    recuperaCepRetro(cep);
                }
            }
        });



    }

    private void recuperaCepRetro(String cep){

        CEPService cepService = retrofit.create( CEPService.class );
        Call<CEP> call = cepService.recuperarCEP(cep);

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {
                if (response.isSuccessful()){
                    CEP cep = response.body();
                    binding.etBairro.setText(cep.getBairro());
                    binding.etCidade.setText(cep.getLocalidade());
                    binding.etEstado.setText(cep.getUf());
                    binding.etRua.setText(cep.getLogradouro());
                    binding.etComplemento.setText(cep.getComplemento());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });

    }

    private void validaDados(int edit){
        Boolean confirm = true;
        nome = binding.etNomeLocal.getText().toString();
        cep = binding.etCep.getText().toString();
        rua = binding.etRua.getText().toString();
        bairro = binding.etBairro.getText().toString();
        cidade = binding.etCidade.getText().toString();
        estado = binding.etEstado.getText().toString();
        numero = binding.etNumero.getText().toString();
        complemento = binding.etComplemento.getText().toString();


        if (nome.length()<3){
            binding.tvErro.setText("Nome inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (cep.length()<3){
            binding.tvErro.setText("CEP inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (rua.length()<3){
            binding.tvErro.setText("Rua inválida");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (bairro.length()<3){
            binding.tvErro.setText("Bairro inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (cidade.length()<3){
            binding.tvErro.setText("Cidade inválida");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (estado.length()<2){
            binding.tvErro.setText("Estado inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (numero.equals("") && numero.length()>=6){
            binding.tvErro.setText("Número inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }else  if (complemento.length()<3){
            binding.tvErro.setText("Complemento inválido");
            binding.tvErro.setVisibility(View.VISIBLE);
            confirm = false;
        }
        if (confirm == true){
            if (edit <= 4){
                updateAddress(edit);
            }else{
                createAddress();
            }

        }
    }










    private void createAddress(){

        if (enderecoSup<3){
            int indexEndereco = enderecoSup+1;
            Map<String, Object> endereco = new HashMap<>();

            endereco.put("nomeLocal", nome);
            endereco.put("cep", cep);
            endereco.put("rua", rua);
            endereco.put("bairro", bairro);
            endereco.put("cidade", cidade);
            endereco.put("estado", estado);
            endereco.put("numero", numero);
            endereco.put("complemento" , complemento);


            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (enderecoSup3==null) {
                indexEndereco = 3;
            }else if (enderecoSup2==null){
                indexEndereco = 2;
            }else if (enderecoSup1==null){
                indexEndereco = 1;
            }
            DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document(""+indexEndereco);
            documentReference.set(endereco).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("db", "Sucesso ao salvar dados");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db", "Erro ao salvar dados" + e.toString());
                        }
                    });

            finish();
        }else{
            Toast.makeText(this, "Atingiu o limite de 3 endereços", Toast.LENGTH_SHORT).show();
        }

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
                enderecosDocuments = value.getDocuments();
                enderecoSup1 = "0";
                String[] dados = new String[2];
                for (int i=0; i<enderecoSup; i++){
                    dados[i] = enderecosDocuments.get(i).getId();
                }
                if (dados[2]==null){
                    Toast.makeText(AddAddressActivity.this, "Fon", Toast.LENGTH_SHORT).show();
                }




            }
        });


    }







    private void editEndereco(){
        int index = Integer.parseInt(enderecoEdit)+1;
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document(""+index);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null){
                    binding.etNomeLocal.setText(value.getString("nomeLocal"));
                    binding.etCep.setText(value.getString("cep"));
                    binding.etRua.setText(value.getString("rua"));
                    binding.etBairro.setText(value.getString("bairro"));
                    binding.etCidade.setText(value.getString("cidade"));
                    binding.etEstado.setText(value.getString("estado"));
                    binding.etNumero.setText(value.getString("numero"));
                    binding.etComplemento.setText(value.getString("complemento"));
                }
            }
        });

        binding.textView10.setTextSize(18);
        binding.textView10.setText("Altere os dados do endereço");
        binding.btnEnviar.setText("Alterar");

        binding.btnEnviar.setOnClickListener(view -> {
            validaDados(index);
        });

    }


    private void updateAddress(int index){


            Map<String, Object> endereco = new HashMap<>();

            endereco.put("nomeLocal", nome);
            endereco.put("cep", cep);
            endereco.put("rua", rua);
            endereco.put("bairro", bairro);
            endereco.put("cidade", cidade);
            endereco.put("estado", estado);
            endereco.put("numero", numero);
            endereco.put("complemento" , complemento);


            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("Usuarios").document(userId).collection("enderecos").document(""+index);
            documentReference.update(endereco).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("db", "Sucesso ao salvar dados");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("db", "Erro ao salvar dados" + e.toString());
                        }
                    });

            finish();

    }



}