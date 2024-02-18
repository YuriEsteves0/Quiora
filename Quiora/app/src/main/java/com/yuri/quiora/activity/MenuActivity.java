package com.yuri.quiora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.credenciais.LoginActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;

public class MenuActivity extends AppCompatActivity {

    private ImageView menuHamb, fecharMenu;
    private Button btnSair;
    private FirebaseAuth auth = FirebaseHelper.getAuth();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        menuHamb = findViewById(R.id.menuHamb);
        fecharMenu = findViewById(R.id.fecharMenu);
        btnSair = findViewById(R.id.btnSair);
        menuHamb.setVisibility(View.GONE);
        fecharMenu.setVisibility(View.VISIBLE);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
                finish();
            }
        });

        fecharMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}