package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.credenciais.LoginActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.UsuariosModel;

public class ConfigActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextView configTitulo;
    private EditText senhaUsu, emailUsu, nomeUsu;
    private Button btnAtualizar;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        AndroidHelper helper = new AndroidHelper();
        helper.MostrarToast(getApplicationContext(), "ESTA PÁGINA NÃO ESTÁ FUNCIONANDO AINDA");

        auth = FirebaseHelper.getAuth();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseHelper.getReferenceUsuarios();

        btnAtualizar = findViewById(R.id.btnAtualizar);
        senhaUsu = findViewById(R.id.senhaUsu);
        configTitulo = findViewById(R.id.configTitulo);
        emailUsu = findViewById(R.id.emailUsu);
        nomeUsu = findViewById(R.id.nomeUsu);
        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);

        mostrarNomeUsuario();

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeUsu.getText().toString();
                String email = emailUsu.getText().toString();
                String senha = senhaUsu.getText().toString();
                Log.d("ERRORUPDATE", "onClick: " + nome + " " + email + " " + senha);
                atualizarUsuario(nome, email, senha);
            }
        });

        toolbar.setTitle("Quiora");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void atualizarUsuario(String nome, String email, String senha){
        //ATUALIZAR NOME
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(nome).build();

        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    AndroidHelper helper = new AndroidHelper();
                    helper.MostrarToast(getApplicationContext(), "Atualização do nome concluida com sucesso!");
                }else{
                    AndroidHelper helper = new AndroidHelper();
                    helper.MostrarToast(getApplicationContext(), "Atualização do nome não pôde ser concluida");
                }
            }
        });

        //ATUALIZAR EMAIL
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try{

                    if(task.isSuccessful()){
                        AndroidHelper helper = new AndroidHelper();
                        helper.MostrarToast(getApplicationContext(), "Atualização do email concluída com sucesso!");
                    }else{
                        AndroidHelper helper = new AndroidHelper();
                        helper.MostrarToast(getApplicationContext(), "Atualização do email não pôde ser concluída");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("ERROUPDATE", "onComplete: ", e);
                }
            }
        });

        //ATUALIZAR SENHA
        user.updatePassword(senha).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                try {
                    if(task.isSuccessful()){
                        AndroidHelper helper = new AndroidHelper();
                        helper.MostrarToast(getApplicationContext(), "Atualização do senha concluida com sucesso!");
                    }else{
                        AndroidHelper helper = new AndroidHelper();
                        helper.MostrarToast(getApplicationContext(), "Atualização do senha não pôde ser concluida");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void mostrarNomeUsuario(){
        String uidUsu = auth.getCurrentUser().getUid();
        reference.child(uidUsu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsuariosModel usuariosModel = snapshot.getValue(UsuariosModel.class);
                configTitulo.setText("Configurações- Editar - " + usuariosModel.getNome());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sample, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.config){
            AndroidHelper helper = new AndroidHelper();
            helper.TrocarIntent(getApplicationContext(), ConfigActivity.class);
            finish();
        }
        if(id == R.id.segViagem){

        }
        if(id == R.id.pacotes){

        }
        if(id == R.id.sairBtn){
            auth.signOut();
            AndroidHelper helper = new AndroidHelper();
            helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}