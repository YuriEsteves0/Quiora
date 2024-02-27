package com.yuri.quiora.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ComprarVooActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.model.VooModel;

import java.util.List;

public class AdapterProcurarVoo extends  RecyclerView.Adapter<AdapterProcurarVoo.MyViewHolder> {

    private List<VooModel> listVoo;
    private Context context;


    public AdapterProcurarVoo(List<VooModel> listVoo, Context context) {
        this.listVoo = listVoo;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.voo_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d("TAG", "onBindViewHolder: " + listVoo.size());
        VooModel vooModel = listVoo.get(position);

        if (vooModel != null) {
            if (vooModel.getNomeCompanhia().equals("atendimentoQuiora")) {
                holder.economico.setVisibility(View.VISIBLE);
            } else {
                holder.economico.setVisibility(View.GONE);
            }
            Log.d("TAG", "onBindViewHolder: NAO É NULO NADA CRIA");
            // Restante do seu código para definir os atributos do layout
        } else {
            Log.e("TAG", "VooModel na posição " + position + " é nulo.");
        }

        holder.horarioIda.setText("Saída: " + vooModel.getHorarioSaida());

        String[] partsSaida = vooModel.getHorarioSaida().split(":");
        int horaSaida = Integer.parseInt(partsSaida[0]);
        int minutoSaida = Integer.parseInt(partsSaida[1]);

        String[] partsChegada = vooModel.getHorarioChegada().split(":");
        int horaChegada = Integer.parseInt(partsChegada[0]);
        int minutoChegada = Integer.parseInt(partsChegada[1]);

        int totalMinutosSaida = horaSaida * 60 + minutoSaida;
        int totalMinutosChegada = horaChegada * 60 + minutoChegada;

        int diferencaMinutos = totalMinutosChegada - totalMinutosSaida;
        int horas = diferencaMinutos / 60;
        int minutos = diferencaMinutos % 60;

        String tempoViagem = horas + "h " + minutos + "min";
        holder.tempoViagem.setText(tempoViagem);
        holder.horarioChegada.setText("Chegada: " + vooModel.getHorarioChegada());
//        Log.d("TAG", "onBindViewHolder: " + vooModel.getNomeCompanhia());
        holder.operadora.setText("Operado pela " + vooModel.getNomeCompanhia());
        holder.precoBRLVoo.setText("BRL " + vooModel.getPreco());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComprarVooActivity.class);
                AndroidHelper helper = new AndroidHelper();
                helper.passarDadosDeVooPorIntent(intent, vooModel);
                Log.d("TAG", "onClick: " + vooModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVoo.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        TextView economico, horarioIda, horarioChegada, operadora, precoBRLVoo, tempoViagem;

        public MyViewHolder(View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            economico = itemView.findViewById(R.id.economico);
            operadora = itemView.findViewById(R.id.operadora);
            horarioIda = itemView.findViewById(R.id.horarioIda);
            horarioChegada = itemView.findViewById(R.id.horarioChegada);
            precoBRLVoo = itemView.findViewById(R.id.precoBRLVoo);
            tempoViagem = itemView.findViewById(R.id.tempoViagem);
        }
    }
}
