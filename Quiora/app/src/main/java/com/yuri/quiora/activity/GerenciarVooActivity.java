package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar_voo);

        recyclerView = findViewById(R.id.recyclerView);

        configurarAdapter();
        configurarDados();
        configurarToolbar();
    }

    public void configurarDados(){
        uidUsu = FirebaseHelper.getUserUid();
        reference = FirebaseHelper.getReferenceVoo();
        Query query = reference.orderByChild("Passageiros/" + uidUsu + "/uid").equalTo(uidUsu);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vooList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    VooModel vooModel = dataSnapshot.getValue(VooModel.class);
                    vooList.add(vooModel);
                }

                recyclerView.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void configurarAdapter(){
        AdapterGerenciarVoo adapter = new AdapterGerenciarVoo(getApplicationContext(), vooList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    public void configurarToolbar(){
        AndroidHelper.configurarToolbar(this);
    }
}