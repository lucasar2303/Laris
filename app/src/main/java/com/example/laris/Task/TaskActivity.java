package com.example.laris.Task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.laris.Adapter.ColabAdapter;
import com.example.laris.Adapter.TaskAdapter;
import com.example.laris.MainActivity;
import com.example.laris.Model.Colaborador;
import com.example.laris.Model.Task;
import com.example.laris.databinding.ActivityTaskBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private ActivityTaskBinding binding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String userId;

    public int taskSup;
    public List<DocumentSnapshot> tasks;
    public List<Task> listTasks = new ArrayList<>();

    private TaskAdapter taskAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CollectionReference collectionReference = db.collection("Atividades");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                binding.etComplemento.setText(value.getDocuments().get(0).getString("nomeLocal"));
                listTasks.clear();
                taskSup = value.getDocuments().size();
                tasks = value.getDocuments();

                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                binding.imgBack.setOnClickListener(view -> finish());

                for (int i = 0; i <taskSup; i++){
                    if (!userId.equals(tasks.get(i).getString("idUser"))){

                    }else {
                        Task task = new Task();
                        task.setContrato(tasks.get(i).getString("contrato"));
                        task.setDataEntrada(tasks.get(i).getString("dataEntrada"));
                        task.setDataSaida(tasks.get(i).getString("dataSaida"));
                        task.setDataVisita(tasks.get(i).getString("dataVisita"));
                        task.setDescricao(tasks.get(i).getString("descricao"));
                        task.setEndereco(tasks.get(i).getString("endereco"));
                        task.setIdTask(tasks.get(i).getString("idTask"));
                        task.setIdColab(tasks.get(i).getString("idColab"));
                        task.setIdUser(tasks.get(i).getString("idUser"));
                        task.setNomeColab(tasks.get(i).getString("nomeColab"));
                        task.setNomeUser(tasks.get(i).getString("nomeUser"));
                        task.setPagamento(tasks.get(i).getString("pagamento"));
                        task.setServico(tasks.get(i).getString("servico"));
                        task.setStatus(tasks.get(i).getString("status"));
                        task.setValorDiaria(tasks.get(i).getDouble("valorDiaria"));
                        task.setValorTotal(tasks.get(i).getDouble("valorTotal"));

                        listTasks.add(task);
                    }

                }

                if (listTasks.size()==0){
                    Toast.makeText(TaskActivity.this, "Nenhuma atividade ativa", Toast.LENGTH_SHORT).show();
                }else{
                    carregarLista(listTasks);
                }


            }
        });
    }


    public void carregarLista(List<Task> listTasks){

            taskAdapter = new TaskAdapter(listTasks);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerTask.setLayoutManager(layoutManager);
        binding.recyclerTask.setHasFixedSize(true);
        binding.recyclerTask.setAdapter(taskAdapter);
    }
}