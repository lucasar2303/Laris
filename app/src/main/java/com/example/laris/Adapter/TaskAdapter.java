package com.example.laris.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laris.Model.Colaborador;
import com.example.laris.Model.Task;
import com.example.laris.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> listTasks;
    public TaskAdapter(List<Task> listTasks) {
        this.listTasks = listTasks;
    }
    public Boolean supMore = false;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);

        return new TaskAdapter.MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Task task = listTasks.get(position);

        holder.nome.setText(task.getNomeColab());
        holder.servico.setText(task.getServico());
        String idCurto = task.getIdTask();
        holder.id.setText("#"+idCurto.substring(0,6));
        holder.status.setText(task.getStatus());
        if (task.getContrato().equals("A negociar")){
            holder.layoutDataEntrada.setVisibility(View.GONE);
            holder.textDataSaida.setText("Data da visita");
            holder.dataSaida.setText(task.getDataVisita());
        }else{
            holder.dataEntrada.setText(task.getDataEntrada());
            holder.dataSaida.setText(task.getDataSaida());
        }
        holder.contrato.setText(task.getContrato());
        if (task.getValorTotal()==0){
            holder.valor.setText("--");
        }else{
            holder.valor.setText(task.getValorTotal().toString());
        }



        holder.btnMore.setOnClickListener(view -> {
            if (supMore==false) {
                holder.layoutDados.setVisibility(View.VISIBLE);
                supMore = true;
            }else if (supMore==true){
                holder.layoutDados.setVisibility(View.GONE);
                supMore = false;
            }
        });

        if (task.getStatus().equals("A confirmar")){
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_circle_orange, 0, 0, 0);

        }

        if (task.getStatus().equals("A negociar valor")){
            holder.textConfirm.setVisibility(View.VISIBLE);
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_circle_blue, 0, 0, 0);

        }

        if (task.getStatus().equals("A confirmar valor")){
            holder.btnEnviar.setVisibility(View.VISIBLE);
            holder.btnCancelar.setVisibility(View.VISIBLE);
            holder.btnEnviar.setText("Aceitar valor");
            holder.btnCancelar.setText("Negar valor");
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_circle_blue, 0, 0, 0);
        }

        if (task.getStatus().equals("Confirmado")){
            holder.btnEnviar.setVisibility(View.VISIBLE);
            holder.btnCancelar.setVisibility(View.VISIBLE);
            holder.btnEnviar.setText("Finalizar");
            holder.btnCancelar.setText("Cancelar");
            holder.status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_circle_green, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
        return listTasks.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, avaliacao, servico, id, status, dataEntrada, dataSaida, textDataSaida, contrato, valor, textConfirm;
        ConstraintLayout layoutDados, layoutDataEntrada;
        ImageButton btnMore;
        Button btnCancelar, btnEnviar;
        View rootView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            nome = itemView.findViewById(R.id.tvNome);
            avaliacao = itemView.findViewById(R.id.tvAvaliacao);
            servico = itemView.findViewById(R.id.tvServico);
            id = itemView.findViewById(R.id.tvId);
            status = itemView.findViewById(R.id.tvStatus);
            dataEntrada = itemView.findViewById(R.id.tvDataEntrada);
            dataSaida = itemView.findViewById(R.id.tvDataSaida);
            textDataSaida = itemView.findViewById(R.id.textViewDataSaida);
            contrato = itemView.findViewById(R.id.tvContrato);
            valor = itemView.findViewById(R.id.tvValor);
            textConfirm = itemView.findViewById(R.id.tvTextoConfirmar);
            layoutDados = itemView.findViewById(R.id.layoutDados);
            layoutDataEntrada = itemView.findViewById(R.id.layoutDataEntrada);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnCancelar = itemView.findViewById(R.id.btnCancelar);
            btnEnviar = itemView.findViewById(R.id.btnEnviar);

        }
    }

}
