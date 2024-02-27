package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.adapter.AdapterGerenciarVoo;
import com.yuri.quiora.activity.adapter.AdapterProcurarVoo;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.VooModel;

import java.util.ArrayList;
import java.util.List;

public class GerenciarVooActivity extends AppCompatActivity {

    private List<VooModel> vooList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private String uidUsu;
    private TextView msgVazio;
    private ImageButton backChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_voo);

        backChat = findViewById(R.id.backChat);
        msgVazio = findViewById(R.id.msgVazio);

        recyclerView = findViewById(R.id.recyclerView);

        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        configurarAdapter();
        configurarDados();
        configurarToolbar();
    }

    public void configurarDados(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                uidUsu = FirebaseHelper.getUserUid();
                reference = FirebaseHelper.getReferenceVoo();
                Query query = reference.orderByChild("Passageiros/" + uidUsu + "/uid").equalTo(uidUsu);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        vooList.clear();
                        if (snapshot.getChildrenCount() == 0){
                            //CRIAR UMA NOVA THREAD E MUDAR A THREAD PRINCIPAL LÁ :)
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    msgVazio.setVisibility(View.VISIBLE);

                                }
                            });

                        }else{
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                VooModel vooModel = dataSnapshot.getValue(VooModel.class);
                                vooList.add(vooModel);
                            }
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.getAdapter().notifyDataSetChanged();

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }).start();

    }

    public void configurarAdapter(){
        AdapterGerenciarVoo adapter = new AdapterGerenciarVoo(getApplicationContext(), vooList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    public void configurarToolbar(){
        TextView nomeUsu = findViewById(R.id.nomeUsu);
        AndroidHelper.configurarToolbarFechar("Comprar Voos", nomeUsu);
    }
}