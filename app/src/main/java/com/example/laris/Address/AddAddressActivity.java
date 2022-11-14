package com.example.laris.Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.Api.CEPService;
import com.example.laris.Model.CEP;
import com.example.laris.Model.Endereco;
import com.example.laris.databinding.ActivityAddAddressBinding;

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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnviar.setOnClickListener(view -> validaDados());

        endereco = (Endereco) getIntent().getSerializableExtra("endereco");
        if (endereco!=null){editEndereco();}

        binding.imgBack.setOnClickListener(view -> finish());

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

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

    private void validaDados(){
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
            createAddress();
        }
    }
    private void createAddress(){
        Endereco endereco = new Endereco();
        endereco.setNomeLocal(nome);
        endereco.setCep(cep);
        endereco.setRua(rua);
        endereco.setBairro(bairro);
        endereco.setCidade(cidade);
        endereco.setUf(estado);
        endereco.setNumero(numero);
        endereco.setComplemento(complemento);

        Intent intent = new Intent(getApplicationContext(), AddressActivity.class);
        intent.putExtra("endereco", endereco);
        startActivity(intent);
    }

    private void editEndereco(){
        binding.textView10.setTextSize(18);
        binding.textView10.setText("Altere os dados do endereço");
        binding.etNomeLocal.setText(endereco.getNomeLocal());
        binding.etCep.setText(endereco.getCep());
        binding.etRua.setText(endereco.getRua());
        binding.etBairro.setText(endereco.getBairro());
        binding.etCidade.setText(endereco.getCidade());
        binding.etEstado.setText(endereco.getUf());
        binding.etNumero.setText(endereco.getNumero());
        binding.etComplemento.setText(endereco.getComplemento());

        binding.btnEnviar.setOnClickListener(view -> {
            Toast.makeText(this, "Oi", Toast.LENGTH_SHORT).show();
        });

    }

}