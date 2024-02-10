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
        VooModel vooModel = listVoo.get(position);
        holder.data.setText(vooModel.getDataViagem());
        holder.lugarChegada.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(vooModel.getPara()));
        holder.horarioSaida.setText("Saída: " + vooModel.getHorarioSaida());
        holder.horarioChegada.setText("Chegada: " + vooModel.getHorarioChegada());
        holder.precoVoo.setText("R$ " + vooModel.getPreco());

        holder.layoutClickable.setOnClickListener(new View.OnClickListener() {
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

        TextView data, lugarChegada, horarioSaida, horarioChegada, precoVoo;
        LinearLayout layoutClickable;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutClickable = itemView.findViewById(R.id.layoutClickable);
            data = itemView.findViewById(R.id.data);
            lugarChegada = itemView.findViewById(R.id.lugarChegada);
            horarioSaida = itemView.findViewById(R.id.horarioSaida);
            horarioChegada = itemView.findViewById(R.id.horarioChegada);
            precoVoo = itemView.findViewById(R.id.precoVoo);
        }
    }
}
