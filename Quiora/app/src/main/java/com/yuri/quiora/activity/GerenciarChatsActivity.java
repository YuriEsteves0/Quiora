package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.adapter.UsuariosAdapter;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.UsuariosModel;

import java.util.ArrayList;
import java.util.List;

public class GerenciarChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UsuariosAdapter adapter;
    private List<UsuariosModel> usuList = new ArrayList<>();
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_chats);

        recyclerView = findViewById(R.id.recyclerView);

        configurarToolbar();
        configurarAdapter();

        //CRIA UMA THREAD SEPARADA PARA CARREGAR OS DADOS, FAZENDO COM QUE A INTERFACE DO USUARIO NAO TRAVE.
        new Thread(new Runnable() {
            @Override
            public void run() {
                configurarDados();
            }
        }).start();
    }

    public void configurarDados(){
        reference = FirebaseHelper.getReferenceUsuarios();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UsuariosModel usuariosModel = dataSnapshot.getValue(UsuariosModel.class);

                    usuList.add(usuariosModel);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void configurarAdapter(){
        adapter = new UsuariosAdapter(usuList, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    public void configurarToolbar(){
        TextView nomeUsu = findViewById(R.id.nomeUsu);
        ImageButton backChat = findViewById(R.id.backChat);
        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nomeUsu.setText("Quiora");
    }
}