package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.adapter.AdapterProcurarVoo;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.VooModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProcurarVooActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<VooModel> listVoo = new ArrayList<>();
    private DatabaseReference reference;
    private AdapterProcurarVoo adapter;
    private TextView de, para;
    private String paraLugar, voltaDia, idaDia, deLugar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_voo);

        recyclerView = findViewById(R.id.recyclerView);
        de = findViewById(R.id.de);
        para = findViewById(R.id.para);

        reference = FirebaseDatabase.getInstance().getReference().child("voos");

        Intent intent = getIntent();
        pegarValoresIntent(intent);

        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);

        configurarAdapter();
        configurarToolbar(toolbar);
        configurarDados(paraLugar);
    }

    public void configurarToolbar(Toolbar toolbar){

        toolbar.setTitle("Quiora");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void pegarValoresIntent(Intent intent){
        String deLugar1 = intent.getStringExtra("deLugar");
        String paraLugar1 = intent.getStringExtra("paraLugar");
        String idaDia1 = intent.getStringExtra("idaDia");

        deLugar = deLugar1;
        paraLugar = paraLugar1;
        idaDia = idaDia1;

        de.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(deLugar1));
        para.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(paraLugar1));
    }

    public void configurarDados(String paraLugar){

        String dataAtual = idaDia.toString();

        reference.orderByChild("para")
                .startAt(paraLugar.toLowerCase())
                .endAt(paraLugar.toLowerCase() + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listVoo.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            VooModel vooModel = dataSnapshot.getValue(VooModel.class);

                            if(vooModel.getDataViagem().compareTo(dataAtual) >= 0){
                                listVoo.add(vooModel);

                            }

                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    public void configurarAdapter(){
        adapter = new AdapterProcurarVoo(listVoo, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}