package com.example.laris.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laris.Model.Colaborador;
import com.example.laris.R;
import com.example.laris.RequestServiceActivity;

import java.util.List;

public class ColabAdapter extends RecyclerView.Adapter<ColabAdapter.MyViewHolder> {

    private List<Colaborador> listColab;
    private String activity;

    public ColabAdapter(List<Colaborador> list, String activity) {
        this.listColab = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Colaborador colaborador = listColab.get(position);

        holder.nome.setText(colaborador.getNome());
        holder.profissao.setText(colaborador.getProfissao());
        holder.contrato.setText(colaborador.getContrato());

        String contrato = colaborador.getContrato();

        if (contrato.equals("A negociar")){
            holder.contrato.setText("A negociar");
        }else{
            holder.contrato.setText("Diária: R$ " + colaborador.getValor());
        }

        View rootView2 = holder.rootView;

        holder.btnSolicitar.setOnClickListener(view -> {
            Intent intent = new Intent(rootView2.getContext(), RequestServiceActivity.class);
            intent.putExtra("colab", colaborador);
            rootView2.getContext().startActivity(intent);

        });

//        holder.avaliacao.setText(colaborador.getAvaliacao().toString());

    }

    @Override
    public int getItemCount() {
        if (activity.equals("1")){
            if (listColab.size()<=2){
                return listColab.size();
            }else {
                return 2;
            }
        }else{
            return listColab.size();
        }
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nome, profissao, contrato, avaliacao;
        Button btnSolicitar;
        View rootView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;

            nome = itemView.findViewById(R.id.tvNome);
            profissao = itemView.findViewById(R.id.tvProfissao);
            contrato = itemView.findViewById(R.id.tvContrato);
            avaliacao = itemView.findViewById(R.id.tvAvaliacao);
            btnSolicitar = itemView.findViewById(R.id.btnSolicitar);


        }
    }

}
