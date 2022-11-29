package com.example.laris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.laris.Adapter.ColabAdapter;
import com.example.laris.Model.Colaborador;
import com.example.laris.Model.Endereco;
import com.example.laris.Notify.NotificationsActivity;
import com.example.laris.Profile.ProfileActivity;
import com.example.laris.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private GoogleMap mMap;
    String servico, montagem, contrato, dataVisita, dataEntrada, dataSaida, endereco, km, rua, num, userId;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ColabAdapter colabAdapter;
    private List<Colaborador> listColab = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.Theme_Laris);
        setContentView(binding.getRoot());

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMap, fragment).commit();


        Bundle extrasFilter = getIntent().getExtras();
        if (extrasFilter != null){
            servico = extrasFilter.getString("servico");
            binding.btnService.setText(servico);
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
            rua = extrasFilter.getString("rua");
            num = extrasFilter.getString("num");

            binding.addressSelected.setText(rua+", "+num);

            Bundle data = new Bundle();
            data.putString("rua",rua);
            data.putString("num",num);
            data.putString("endereco",endereco);
            fragment.setArguments(data);

            carregarLista();

        }

        binding.btnService.setOnClickListener(view -> newActivty(FilterHome.class));
        binding.btnNotify.setOnClickListener(view -> newActivty(NotificationsActivity.class));
        binding.verMais.setOnClickListener(view -> newActivty(ListActivity.class));
        binding.imgUser.setOnClickListener(view -> newActivty(ProfileActivity.class));
   }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

        if (usuarioAtual != null){
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null){
                        binding.nameUser.setText("Olá, "+value.getString("nome"));
                        if (value.getString("urlImagem").equals("") ||value.getString("urlImagem") == null){
                            return;
                        }else{
                            Picasso.get().load(value.getString("urlImagem"))
                                    .error(R.drawable.icon_profile)
                                    .placeholder(R.drawable.icon_contact)
                                    .into(binding.imgUser);
                        }
                    }
                }
            });
        }else{finish();}



    }

    private void newActivty(Class c ){
        Intent intent = new Intent(getApplicationContext(), c);
        startActivity(intent);
    }

    public void carregarLista(){

        Endereco endereco1 = new Endereco();
        endereco1.setCep("12061040");
        endereco1.setRua("Rua Paraná");
        endereco1.setBairro("Parque São Jorge");
        endereco1.setCidade("Taubaté");
        endereco1.setUf("SP");
        endereco1.setNumero("28");

        Endereco endereco2 = new Endereco();
        endereco2.setCep("12070200");
        endereco2.setRua("Rua José Milliet Filho");
        endereco2.setBairro("Jardim Ana Emilia");
        endereco2.setCidade("Taubaté");
        endereco2.setUf("SP");
        endereco2.setNumero("135");

        Colaborador colaborador1 = new Colaborador();
        colaborador1.setNome("João");
        colaborador1.setContrato("Diária");
        colaborador1.setValor(55.0);
        colaborador1.setProfissao("Pintor");
        colaborador1.setAvaliacao(5.0);
        colaborador1.setEndereco(endereco1);

        Colaborador colaborador2 = new Colaborador();
        colaborador2.setNome("Daniel");
        colaborador2.setContrato("A negociar");
        colaborador2.setValor(0.0);
        colaborador2.setProfissao("Montador");
        colaborador2.setAvaliacao(4.7);
        colaborador2.setEndereco(endereco1);

        listColab.add(colaborador1);
        listColab.add(colaborador2);

        colabAdapter = new ColabAdapter(listColab, "1");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerColab.setLayoutManager(layoutManager);
        binding.recyclerColab.setHasFixedSize(true);
        binding.recyclerColab.setAdapter(colabAdapter);
    }


}