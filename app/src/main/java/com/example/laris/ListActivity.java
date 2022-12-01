package com.example.laris;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.laris.Adapter.ColabAdapter;
import com.example.laris.Model.Colaborador;
import com.example.laris.databinding.ActivityListBinding;
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

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;
    private ColabAdapter colabAdapter;
    public List<Colaborador> listColab = new ArrayList<>();
    public List<Colaborador> listColab2 = new ArrayList<>();

    String servico, contrato;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public List<DocumentSnapshot> colabs;
    public int colabsSup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Laris);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(view -> finish());



        CollectionReference collectionReference = db.collection("Colaboradores");


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value!=null){

                    listColab.clear();

                    colabsSup = value.getDocuments().size();
                    colabs = value.getDocuments();

                    for (int i = 0; i <colabsSup; i++){

                        Colaborador colaborador1 = new Colaborador();
                        colaborador1.setNome(colabs.get(i).getString("nome"));
                        colaborador1.setContrato(colabs.get(i).getString("contrato"));
                        colaborador1.setValor(colabs.get(i).getDouble("valor"));
                        colaborador1.setProfissao(colabs.get(i).getString("profissao"));

                        listColab.add(colaborador1);
                    }
                    carregarLista(listColab, "2");

                }

            }
        });
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