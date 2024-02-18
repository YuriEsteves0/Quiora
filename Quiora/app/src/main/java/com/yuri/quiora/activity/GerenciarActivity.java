package com.yuri.quiora.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;

public class GerenciarActivity extends AppCompatActivity {

    private Button gerenciarViagem, criarViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerenciar);

        gerenciarViagem = findViewById(R.id.gerenciarViagem);
        criarViagem = findViewById(R.id.criarViagem);

        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);
        toolbar.setTitle("Quiora");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        criarViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), CriarViagemActivity.class);
            }
        });

        gerenciarViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), GerenciarVooActivity.class);
            }
        });
    }
}