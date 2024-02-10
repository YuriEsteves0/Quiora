package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.adapter.ChatRoomAdapter;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.ChatRoomModel;
import com.yuri.quiora.model.MensagensModel;
import com.yuri.quiora.model.UsuariosModel;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private UsuariosModel usuariosModel;
    private RecyclerView recyclerView;
    private EditText campoMsg;
    private List<MensagensModel> msgList = new ArrayList<>();
    private ChatRoomModel chatRoomModel;
    private ImageButton enviarBtn;
    private String idSalaChat;
    private ChatRoomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerView);
        campoMsg = findViewById(R.id.campoMsg);
        enviarBtn = findViewById(R.id.enviarBtn);

        pegarDadosUsuario();

        idSalaChat = AndroidHelper.setChatRoomIdLexi(FirebaseHelper.getUserUid(), usuariosModel.getUid());
        criarSala(idSalaChat);

        configurarDados();

        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = campoMsg.getText().toString();
                enviarMsg(msg);
            }
        });

        configurarAdapter();
        configurarToolbar();
    }

    public void configurarDados(){
        DatabaseReference reference = FirebaseHelper.getChatRoomReference();
        reference.child(idSalaChat).child("chatMsg").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                msgList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MensagensModel model = dataSnapshot.getValue(MensagensModel.class);
                    Log.d("TAG", "onDataChange: FOI ATÉ AQUI" + model.getMsg());
                    msgList.add(model);
                }
                adapter.notifyDataSetChanged();
                if(!msgList.isEmpty()){
                    recyclerView.smoothScrollToPosition(msgList.size() - 1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    public void enviarMsg(String msg){
        DatabaseReference reference = FirebaseHelper.getChatRoomReference();
        MensagensModel model = new MensagensModel(FirebaseHelper.getUserUid(), msg);

        String msgUid = reference.push().getKey();

        reference.child(idSalaChat).child("chatMsg").child(msgUid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                campoMsg.setText("");
            }
        });
    }

    public void configurarAdapter(){
        adapter = new ChatRoomAdapter(msgList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    public void criarSala(String idSalaChat){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("chatRooms").child(idSalaChat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatRoomModel = snapshot.getValue(ChatRoomModel.class);
                }else{
                    String uid = FirebaseHelper.getUserUid();

                    List<String> usuariosIds = new ArrayList<>();
                    usuariosIds.add(uid);
                    usuariosIds.add(usuariosModel.getUid());

                    chatRoomModel = new ChatRoomModel(
                            idSalaChat,
                            usuariosIds
                    );

                    reference.child("chatRooms").child(chatRoomModel.getChatRoomUid()).setValue(chatRoomModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public UsuariosModel pegarDadosUsuario(){
        Intent intent = getIntent();

        AndroidHelper helper = new AndroidHelper();
        usuariosModel = helper.receberDadosDeUmUsuarioPorIntent(intent);

        return usuariosModel;
    }

    public void configurarToolbar(){
        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);
        AndroidHelper helper = new AndroidHelper();
        String nomeCapitalizado = helper.formatarPrimeiraLetraMaiuscula(usuariosModel.getNome());
        toolbar.setTitle(nomeCapitalizado);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}