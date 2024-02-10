package com.yuri.quiora.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ChatActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.ChatRoomModel;
import com.yuri.quiora.model.UsuariosModel;

import java.util.ArrayList;
import java.util.List;

public class UsuariosAdapter extends RecyclerView.Adapter<UsuariosAdapter.MyViewHolder> {

    private List<UsuariosModel> usuarioList;
    private Context context;
    public UsuariosAdapter(List<UsuariosModel> usuarioList, Context context) {
        this.usuarioList = usuarioList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usuarios_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //AQUI TEM UM ERRO, CUIDE DELE

        UsuariosModel usuariosModel = usuarioList.get(position);

        if(FirebaseHelper.getUserUid().equals(usuariosModel.getUid())){
            holder.nomeUsuario.setText(usuariosModel.getNome() + " (Eu)");
            holder.relativeLayoutFull.setClickable(false);
        }else{
            holder.nomeUsuario.setText(usuariosModel.getNome());
        }

        holder.emailUsuario.setText(usuariosModel.getEmail());
        holder.mensagemNova.setVisibility(View.INVISIBLE);

        holder.relativeLayoutFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();

                Intent intent = new Intent(context, ChatActivity.class);

                helper.passarDadosDeUmUsuarioPorIntent(intent, usuariosModel);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout relativeLayoutFull;
        private ImageView mensagemNova, imagemUsuario;
        private TextView nomeUsuario, emailUsuario;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayoutFull = itemView.findViewById(R.id.relativeLayoutFull);
            nomeUsuario = itemView.findViewById(R.id.nomeUsuario);
            emailUsuario = itemView.findViewById(R.id.emailUsuario);
            mensagemNova = itemView.findViewById(R.id.mensagemNova);
            imagemUsuario = itemView.findViewById(R.id.imagemUsuario);

        }
    }
}
