package com.yuri.quiora.activity.credenciais;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.MainActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;

import org.checkerframework.checker.units.qual.A;

public class LoginActivity extends AppCompatActivity {

    private TextView CadTxt;
    private EditText emailInput, senhaInput;
    private Button btnEntrar;
    private CheckBox preferenciasCheckBox;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CadTxt = findViewById(R.id.CadTxt);
        emailInput = findViewById(R.id.emailInput);
        senhaInput = findViewById(R.id.senhaInput);
        btnEntrar = findViewById(R.id.btnEntrar);
        preferenciasCheckBox = findViewById(R.id.preferenciasCheckBox);

        auth = FirebaseHelper.getAuth();

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String senha = senhaInput.getText().toString();

                if(preferenciasCheckBox.isChecked()){
                    entrar(email, senha);
                }else{
                    AndroidHelper helper = new AndroidHelper();
                    helper.MostrarToast(getApplicationContext(), "Aceite nossos termos de privacidade.");
                }
            }
        });

        CadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), CadastroActivity.class);
                finish();
            }
        });
    }

    public void entrar(String email, String senha){
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    AndroidHelper helper = new AndroidHelper();
                    helper.TrocarIntent(getApplicationContext(), MainActivity.class);
                    finish();
                }else{
                    AndroidHelper helper = new AndroidHelper();
                    helper.MostrarToast(getApplicationContext(), "Usuário não encontrado, faça cadastro");
                }
            }
        });
    }
}