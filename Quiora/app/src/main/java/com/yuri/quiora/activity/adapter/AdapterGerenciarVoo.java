package com.yuri.quiora.activity.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ComprarVooActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.VooModel;

import java.io.File;
import java.util.List;

public class AdapterGerenciarVoo extends RecyclerView.Adapter<AdapterGerenciarVoo.MyViewHolder> {

    private Context context;
    private List<VooModel> vooList;
    final static int REQUEST_CODE = 1232;
    private Activity activity;

    public AdapterGerenciarVoo(Context context, List<VooModel> vooList, Activity activity) {
        this.context = context;
        this.vooList = vooList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gerenciar_voo_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VooModel vooModel = vooList.get(position);
        holder.data.setText(vooModel.getDataViagem());
        holder.lugarChegada.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(vooModel.getPara()));
        holder.horarioSaida.setText("Saída: " + vooModel.getHorarioSaida());
        holder.horarioChegada.setText("Chegada: " + vooModel.getHorarioChegada());
        holder.precoVoo.setText("R$ " + vooModel.getPreco());

        holder.verPassagensBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                createPDF();
            }
        });

        holder.cancelarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidUsu = FirebaseHelper.getUserUid();
                DatabaseReference reference = FirebaseHelper.getReferenceVoo().child(vooModel.getUidViagem()).child("Passageiros").child(uidUsu);
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                AndroidHelper helper = new AndroidHelper();
                                helper.MostrarToast(context, "Viagem cancelada com sucesso!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                AndroidHelper helper = new AndroidHelper();
                                helper.errorMsgToast(context);
                            }
                        });


            }
        });
    }

    @Override
    public int getItemCount() {
        return vooList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView data, lugarChegada, horarioSaida, horarioChegada, precoVoo;
        private Button verPassagensBtn, cancelarBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.data);
            lugarChegada = itemView.findViewById(R.id.lugarChegada);
            horarioSaida = itemView.findViewById(R.id.horarioSaida);
            horarioChegada = itemView.findViewById(R.id.horarioChegada);
            precoVoo = itemView.findViewById(R.id.precoVoo);
            verPassagensBtn = itemView.findViewById(R.id.verPassagensBtn);
            cancelarBtn = itemView.findViewById(R.id.cancelarBtn);
        }
    }

    private void createPDF(){
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(42);
        String text = "Hello, world";
        float x = 500;
        float y = 900;

        canvas.drawText(text, x, y, paint);
        document.finishPage(page);
//        File download
    }
}
