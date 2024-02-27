package com.yuri.quiora.activity.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.NotificacaoModel;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterNotificacoes extends RecyclerView.Adapter<AdapterNotificacoes.MyViewHolder> {

    private List<NotificacaoModel> notificacaoList;
    private Context context;
    private DatabaseReference reference = FirebaseHelper.getReferenceUsuarios();

    public AdapterNotificacoes(List<NotificacaoModel> notificacaoList, Context context) {
        this.notificacaoList = notificacaoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificacao_rv, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NotificacaoModel notificacaoModel = notificacaoList.get(position);
        verificarNome(notificacaoModel.getUidUsuDestinatario(), new OnNomeVerificadoListener() {
            @Override
            public void onNomeVerificado(String nomeDestinatario) {
                holder.nomeUsuRemetente.setText(nomeDestinatario);
            }
        });

        if(notificacaoModel.getMsgNova() == "true"){
            holder.ImgMsgNova.setVisibility(View.VISIBLE);
        }else if (notificacaoModel.getMsgNova() == "false"){
            holder.ImgMsgNova.setVisibility(View.INVISIBLE);
        }

        holder.msg.setText(notificacaoModel.getMsg());

        holder.cardNotificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidUsu = FirebaseHelper.getUserUid();
                // Crie um mapa para armazenar os novos valores que deseja atualizar
                Map<String, Object> novosValores = new HashMap<>();
                novosValores.put("msgNova", false); // Define o valor para false

                // Obtenha uma referência para o nó "notificacao" dentro do nó do usuário
                DatabaseReference notificacaoRef = reference.child(uidUsu).child("notificacao");

                // Atualize os valores usando o método updateChildren()
                notificacaoRef.updateChildren(novosValores)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "onSuccess: FOI");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Tratamento de erro
                                Log.e("TAG", "Erro ao atualizar dados: " + e.getMessage());
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificacaoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView nomeUsuRemetente, msg;
        private ImageView ImgMsgNova;
        private RelativeLayout cardNotificacao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nomeUsuRemetente = itemView.findViewById(R.id.nomeUsuRemetente);
            this.msg = itemView.findViewById(R.id.msg);
            this.ImgMsgNova = itemView.findViewById(R.id.ImgMsgNova);
            this.cardNotificacao = itemView.findViewById(R.id.cardNotificacao);
        }
    }

    public interface OnNomeVerificadoListener {
        void onNomeVerificado(String nome);
    }

    private void verificarNome(String uidUsuario, OnNomeVerificadoListener listener){
        Log.d("UID", "verificarNome: " + uidUsuario);
        reference.child(uidUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nomeUsu = snapshot.child("nome").getValue(String.class);
                    listener.onNomeVerificado(nomeUsu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", "onCancelled: ERRO VERIFICAR NOME");
            }
        });
    }
}
