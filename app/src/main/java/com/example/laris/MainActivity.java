package com.example.laris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.laris.Adapter.ColabAdapter;
import com.example.laris.Address.AddAddressActivity;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
    public List<Colaborador> listColab = new ArrayList<>();

    public int colabsSup;
    public List<DocumentSnapshot> colabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setTheme(R.style.Theme_Laris);
        setContentView(binding.getRoot());

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMap, fragment).commit();

        CollectionReference collectionReference = db.collection("Colaboradores");

        Bundle extrasFilter = getIntent().getExtras();
        if (extrasFilter != null){
            //Recebendo o filtro
            servico = extrasFilter.getString("servico");
            binding.btnService.setText(servico);
            if(servico.equals("Consertos") || servico.equals("Montagem")){
                montagem = extrasFilter.getString("Montagem");
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

            //Maps
            Bundle data = new Bundle();
            data.putString("rua",rua);
            data.putString("num",num);
            data.putString("endereco",endereco);
            fragment.setArguments(data);






            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                binding.etComplemento.setText(value.getDocuments().get(0).getString("nomeLocal"));
                    colabsSup = value.getDocuments().size();
                    colabs = value.getDocuments();

                    for (int i = 0; i <colabsSup; i++){
                        if (!servico.equals(colabs.get(i).getString("profissao"))){

                        }else
                        if (!contrato.equals(colabs.get(i).getString("contrato"))){

                        }else {
                            Colaborador colaborador1 = new Colaborador();
                            colaborador1.setNome(colabs.get(i).getString("nome"));
                            colaborador1.setContrato(colabs.get(i).getString("contrato"));
                            colaborador1.setValor(colabs.get(i).getDouble("valor"));
                            colaborador1.setProfissao(colabs.get(i).getString("profissao"));

                            listColab.add(colaborador1);
                        }

                    }

                    if (listColab.size()==0){
                        Toast.makeText(MainActivity.this, "Nenhum colaborador disponível", Toast.LENGTH_SHORT).show();
                    }else{
                        carregarLista(listColab, "1");
                    }


                }
            });

        }else{

            FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

            if (usuarioAtual!=null){
                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                binding.etComplemento.setText(value.getDocuments().get(0).getString("nomeLocal"));
                        if (value!=null){
                            colabsSup = value.getDocuments().size();
                            colabs = value.getDocuments();

                            for (int i = 0; i <colabsSup; i++){

                                Colaborador colaborador1 = new Colaborador();
                                colaborador1.setNome(colabs.get(i).getString("nome"));
                                colaborador1.setContrato(colabs.get(i).getString("contrato"));
                                colaborador1.setValor(colabs.get(i).getDouble("valor"));
                                colaborador1.setProfissao(colabs.get(i).getString("profissao"));
                                colaborador1.setId(colabs.get(i).getId());

                                listColab.add(colaborador1);
                            }
                            carregarLista(listColab, "1");

                        }

                    }
                });
            }



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

    public void carregarLista(List<Colaborador> listColaba, String activity){

        if (!activity.equals("1")){
            colabAdapter = new ColabAdapter(listColab, "2");
        }else{
            colabAdapter = new ColabAdapter(listColab, "1");
        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerColab.setLayoutManager(layoutManager);
        binding.recyclerColab.setHasFixedSize(true);
        binding.recyclerColab.setAdapter(colabAdapter);
    }


}