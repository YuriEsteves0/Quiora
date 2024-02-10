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
import com.google.firebase.database.DatabaseReference;
import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.UsuariosModel;

public class CadastroActivity extends AppCompatActivity {

    private EditText nomeInput, emailInput, senhaInput, senhaInput2;
    private TextView loginTxt;
    private Button btnCadastrar;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private CheckBox preferenciasCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        loginTxt = findViewById(R.id.loginTxt);
        nomeInput = findViewById(R.id.nomeInput);
        emailInput = findViewById(R.id.emailInput);
        senhaInput = findViewById(R.id.senhaInput);
        senhaInput2 = findViewById(R.id.senhaInput2);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        preferenciasCheckBox = findViewById(R.id.preferenciasCheckBox);

        auth = FirebaseHelper.getAuth();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeInput.getText().toString();
                String email = emailInput.getText().toString();
                String senha = senhaInput.getText().toString();
                String senha2 = senhaInput2.getText().toString();
                if(senha.equals(senha2)){
                    auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                if(preferenciasCheckBox.isChecked()){
                                    CadastrarUsuarioRealTimeDatabase(nome, email, senha);
                                }else{
                                    AndroidHelper helper = new AndroidHelper();
                                    helper.MostrarToast(getApplicationContext(), "Aceite nossos termos de privacidade.");
                                }

                            }
                        }
                    });

                }
            }
        });

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
                finish();
            }
        });
    }

    private void CadastrarUsuarioRealTimeDatabase(String nome, String email, String senha){
        UsuariosModel usuariosModel = new UsuariosModel();
        usuariosModel.setNome(nome);
        usuariosModel.setEmail(email);
        usuariosModel.setUid(auth.getUid());
        usuariosModel.setNivelAcesso("usuario");
        usuariosModel.setPremium("basico");

        try {
            reference = FirebaseHelper.getReference().child("usuarios");
            reference.child(auth.getUid()).setValue(usuariosModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        AndroidHelper helper = new AndroidHelper();
                        helper.MostrarToast(getApplicationContext(), "Usuario" + usuariosModel.getNome() + " cadastrado com sucesso!");
                        helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
                        finish();
                    }
                }
            });
        }catch(Exception error){
            error.printStackTrace();
            error.getMessage();
        }
    }
}