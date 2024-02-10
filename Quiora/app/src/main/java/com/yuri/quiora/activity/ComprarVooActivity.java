package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.VooModel;

public class ComprarVooActivity extends AppCompatActivity {

    //AQUI TEM ALGUMA COISA ERRADA COM A FORMULA, CONSERTAR ISSO DPS

    private TextView nomeLugar, dataViagem, saidaAviao, chegadaDestino, nomeComp, preco, qntAssentosDisp;
    private Button btnComprar;
    private VooModel vooModel = new VooModel();
    private AndroidHelper helper = new AndroidHelper();
    private DatabaseReference reference;
    private Long qntPassageirosSuportado;
    private Long qntPassageirosAtual;
    private String uidUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_voo);

        btnComprar = findViewById(R.id.idComprar);
        nomeLugar = findViewById(R.id.nomeLugar);
        dataViagem = findViewById(R.id.dataViagem);
        saidaAviao = findViewById(R.id.saidaAviao);
        chegadaDestino = findViewById(R.id.chegadaDestino);
        nomeComp = findViewById(R.id.nomeComp);
        preco = findViewById(R.id.preco);
        qntAssentosDisp = findViewById(R.id.qntAssentosDisp);

        receberDadosIntent();
        configurarDados();

        AndroidHelper.configurarToolbar(this);

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckQntPassageiros();
            }
        });

    }

    public void CheckQntPassageiros(){

        DatabaseReference qntPassageirosSuportadosRef = FirebaseHelper.getReferenceVoo().child(vooModel.getUidViagem()).child("qntPassageiros");

        //VER A QUANTIDADE DE PASSAGEIROS SUPORTADOS

        qntPassageirosSuportadosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    qntPassageirosSuportado = snapshot.getValue(Long.class);

                    //VER A QUANTIDADE DE PASSAGEIROS ATUAL

                    DatabaseReference qntPassageirosAtualRef = FirebaseHelper.getReferenceVoo().child(vooModel.getUidViagem()).child("Passageiros");

                    qntPassageirosAtualRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            qntPassageirosAtual = snapshot.getChildrenCount();

                            if(qntPassageirosAtual < qntPassageirosSuportado){

                                uidUsu = FirebaseHelper.getUserUid();
                                helper.MostrarToast(getApplicationContext(), "Passagem comprada com sucesso!");

                                qntPassageirosAtualRef.child(uidUsu).child("uid").setValue(uidUsu);
                            }else{
                                helper.MostrarToast(getApplicationContext(), "Não há mais assentos disponíveis!");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            error.getMessage();
                            helper.errorMsgToast(getApplicationContext());
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
                helper.errorMsgToast(getApplicationContext());
            }
        });
    }

    public Long contaQntPassageiros(Long qntPassageirosAtual, Long qntPassageirosSuportado){
        Long formula = qntPassageirosSuportado - qntPassageirosAtual;
        return formula;
    }

    public void configurarDados(){
        nomeLugar.setText(AndroidHelper.formatarPrimeiraLetraMaiuscula(vooModel.getPara()));
        dataViagem.setText("Data: " + vooModel.getDataViagem());
        saidaAviao.setText("Horário Saída: " + vooModel.getHorarioSaida() + "h");
        chegadaDestino.setText("Horário chegada: " + vooModel.getHorarioChegada() + "h");
        nomeComp.setText(vooModel.getNomeCompanhia());
        preco.setText("R$ " + vooModel.getPreco());

        DatabaseReference qntPassageirosSuportadosRef = FirebaseHelper.getReferenceVoo().child(vooModel.getUidViagem()).child("qntPassageiros");

        qntPassageirosSuportadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qntPassageirosSuportado = snapshot.getValue(Long.class);

                DatabaseReference qntPassageirosAtualRef = FirebaseHelper.getReferenceVoo().child(vooModel.getUidViagem()).child("Passageiros");

                qntPassageirosAtualRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        qntPassageirosAtual = snapshot.getChildrenCount();

                        Long formula = contaQntPassageiros(qntPassageirosAtual, qntPassageirosSuportado);

                        qntAssentosDisp.setText(formula.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public VooModel receberDadosIntent(){
        Intent intent = getIntent();
        vooModel = helper.receberDadosDeVooPorIntent(intent);
        return vooModel;
    }
}