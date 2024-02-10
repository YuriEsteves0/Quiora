package com.yuri.quiora.activity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.ChatRoomModel;
import com.yuri.quiora.model.MensagensModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    private List<MensagensModel> msgList = new ArrayList<>();

    public ChatRoomAdapter(List<MensagensModel> msgList){
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_rv, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MensagensModel mensagensModel = msgList.get(position);
        if(mensagensModel.getUidRemetente().equals(FirebaseHelper.getUserUid())){
            holder.layout_green_dir.setVisibility(View.VISIBLE);
            holder.msg_dir.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula( mensagensModel.getMsg()));
            holder.layout_blue_esq.setVisibility(View.GONE);
        }else{
            holder.layout_blue_esq.setVisibility(View.VISIBLE);
            holder.msg_esq.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula( mensagensModel.getMsg()));
            holder.layout_green_dir.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout_blue_esq,layout_green_dir;
        private TextView msg_esq, msg_dir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_blue_esq = itemView.findViewById(R.id.layout_blue_esq);
            layout_green_dir = itemView.findViewById(R.id.layout_green_dir);
            msg_esq = itemView.findViewById(R.id.msg_esq);
            msg_dir= itemView.findViewById(R.id.msg_dir);
        }
    }
}
