package com.yuri.quiora.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.credenciais.LoginActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;

public class SplashScreen extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseHelper.getAuth();

        if(auth.getCurrentUser() == null){
            //Não tem conta
            AndroidHelper helper = new AndroidHelper();
            helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
            finish();
        }else{
            //Tem conta
            AndroidHelper helper = new AndroidHelper();
            helper.TrocarIntent(getApplicationContext(), MainActivity.class);
            finish();
        }
    }


}