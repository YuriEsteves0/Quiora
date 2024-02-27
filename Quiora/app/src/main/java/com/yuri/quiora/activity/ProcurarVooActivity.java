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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProcurarVooActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<VooModel> listVoo = new ArrayList<>();
    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseHelper.getAuth();
    private AdapterProcurarVoo adapter;
    private TextView origem, destino, faleConoscoTxt, tipoVoo;
    public ImageButton backChat;
    private String paraLugar, voltaDia, idaDia, deLugar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procurar_voo);

        recyclerView = findViewById(R.id.recyclerView);
        origem = findViewById(R.id.origem);
        destino = findViewById(R.id.destino);
        tipoVoo = findViewById(R.id.tipoVoo);
        backChat = findViewById(R.id.backChat);
        faleConoscoTxt = findViewById(R.id.faleConoscoTxt);

        faleConoscoTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.VerifFaleConosco(reference, auth, getApplicationContext());
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("voos");

        Intent intent = getIntent();
        pegarValoresIntent(intent);

        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);

        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        configurarAdapter();
        configurarToolbar(toolbar);
        configurarDados(paraLugar);
    }

    public void configurarToolbar(Toolbar toolbar){
        TextView nomeUsu = findViewById(R.id.nomeUsu);
        AndroidHelper.configurarToolbarFechar("Comprar Voos", nomeUsu);
    }

    public void pegarValoresIntent(Intent intent){
        String deLugar1 = intent.getStringExtra("deLugar");
        String paraLugar1 = intent.getStringExtra("paraLugar");
        String idaDia1 = intent.getStringExtra("idaDia");
        Log.d("TAG", "pegarValoresIntent: " + deLugar1 + " " + paraLugar1 +  deLugar1 + " " + idaDia1);
        deLugar = deLugar1;
        paraLugar = paraLugar1;
        idaDia = idaDia1;

        origem.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(deLugar1));
        destino.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(paraLugar1));
    }

    public void configurarDados(String paraLugar) {
        Log.d("THREAD", "configurarDados: fora da thread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                String dataAtual = idaDia.toString();
                Log.d("THREAD", "configurarDados: dentro da thread");
                reference.orderByChild("para")
                        .startAt(paraLugar.toLowerCase())
                        .endAt(paraLugar.toLowerCase() + "\uf8ff")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                listVoo.clear();
                                Log.d("THREAD", "onDataChange: " + snapshot.getChildrenCount());
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    VooModel vooModel = dataSnapshot.getValue(VooModel.class);
                                    Log.d("TAG", "onDataChange: " + dataSnapshot.getChildrenCount());
                                    if (vooModel.getDataViagem().compareTo(dataAtual) >= 0) {
                                        listVoo.add(vooModel);

                                    }

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
                                Log.d("ERRO", "onCancelled: " + error.getMessage());
                            }
                        });
            }
        }).start();


    }

    public void configurarAdapter(){
        adapter = new AdapterProcurarVoo(listVoo, getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}