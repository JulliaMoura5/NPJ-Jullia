package com.example.fasipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<String> record_id, record_nome, record_cpf, record_tipo_ocorrencia, record_tipo_ocorrencia_descricao, record_estagiario, record_coordenador;

    CustomAdapter(Activity activity, Context context, ArrayList<String> record_id, ArrayList<String> record_nome,
                  ArrayList<String> record_cpf, ArrayList<String> record_tipo_ocorrencia, ArrayList<String> record_tipo_ocorrencia_descricao,
                  ArrayList<String> record_estagiario, ArrayList<String> record_coordenador){
        this.activity = activity;
        this.context = context;
        this.record_id = record_id;
        this.record_nome = record_nome;
        this.record_cpf = record_cpf;
        this.record_tipo_ocorrencia = record_tipo_ocorrencia;
        this.record_tipo_ocorrencia_descricao = record_tipo_ocorrencia_descricao;
        this.record_estagiario = record_estagiario;
        this.record_coordenador = record_coordenador;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    public void filterList(ArrayList<String> filteredRecordId, ArrayList<String> filteredRecordNome,
                           ArrayList<String> filteredRecordCpf, ArrayList<String> filteredRecordTipoOcorrencia, ArrayList<String> filteredRecordTipoOcorrenciaDesccricao,
                           ArrayList<String> filteredRecordEstagiario, ArrayList<String> filteredRecordCoordenador) {
        record_id = filteredRecordId;
        record_nome = filteredRecordNome;
        record_cpf = filteredRecordCpf;
        record_tipo_ocorrencia = filteredRecordTipoOcorrencia;
        record_tipo_ocorrencia_descricao = filteredRecordTipoOcorrenciaDesccricao;
        record_estagiario = filteredRecordEstagiario;
        record_coordenador = filteredRecordCoordenador;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        if (record_id.isEmpty()) {
            return; // Exit early if data is not available
        }

        holder.record_id_txt.setText(record_id.get(position));
        holder.record_nome_txt.setText("Nome: " + record_nome.get(position));
        holder.record_cpf_txt.setText("CPF: " + formatarCPF(record_cpf.get(position)));
        holder.record_tipo_ocorrencia_txt.setText("Ocorrência: " + record_tipo_ocorrencia.get(position));
        holder.record_tipo_ocorrencia_descricao_txt.setText("Descrição: " + record_tipo_ocorrencia_descricao.get(position));
        holder.record_estagiario_txt.setText("Estagiário: " + record_estagiario.get(position));
        holder.record_coordenador_txt.setText("Professor: " + record_coordenador.get(position));

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", record_id.get(position));
                intent.putExtra("nome", record_nome.get(position));
                intent.putExtra("cpf", record_cpf.get(position));
                intent.putExtra("tipoOcorrencia", record_tipo_ocorrencia.get(position));
                intent.putExtra("tipoOcorrenciaDescricao", record_tipo_ocorrencia_descricao.get(position));
                intent.putExtra("estagiario", record_estagiario.get(position));
                intent.putExtra("coordenador", record_coordenador.get(position));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    private String formatarCPF(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    @Override
    public int getItemCount() {
        return record_id.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView record_id_txt, record_nome_txt, record_cpf_txt, record_tipo_ocorrencia_txt, record_tipo_ocorrencia_descricao_txt, record_estagiario_txt, record_coordenador_txt;
        LinearLayout mainLayout;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            record_id_txt = itemView.findViewById(R.id.record_id_txt);
            record_nome_txt = itemView.findViewById(R.id.record_nome_txt);
            record_cpf_txt = itemView.findViewById(R.id.record_cpf_txt);
            record_tipo_ocorrencia_txt = itemView.findViewById(R.id.record_tipo_ocorrencia_txt);
            record_tipo_ocorrencia_descricao_txt = itemView.findViewById(R.id.record_tipo_desricao_txt);
            record_estagiario_txt = itemView.findViewById(R.id.record_estagiario_txt);
            record_coordenador_txt = itemView.findViewById(R.id.record_coordenador_txt);
            mainLayout = itemView.findViewById(R.id.mainLayout);


            // Animação para o item do RecyclerView
            Animation translate_anim = AnimationUtils.loadAnimation(itemView.getContext(),  R.anim.translate_anim);

            mainLayout.setAnimation(translate_anim);
        }
    }

    public ArrayList<String> getFilteredRecordIds() {
        return record_id;
    }

    public ArrayList<String> getFilteredRecordNomes() {
        return record_nome;
    }

    public ArrayList<String> getFilteredRecordCpfs() {
        return record_cpf;
    }

    public ArrayList<String> getFilteredRecordTipoOcorrencias() {
        return record_tipo_ocorrencia;
    }

    public ArrayList<String> getFilteredRecordTipoOcorrenciaDescricoes() {
        return record_tipo_ocorrencia_descricao;
    }

    public ArrayList<String> getFilteredRecordEstagiarios() {
        return record_estagiario;
    }

    public ArrayList<String> getFilteredRecordCoordenadores() {
        return record_coordenador;
    }
}
