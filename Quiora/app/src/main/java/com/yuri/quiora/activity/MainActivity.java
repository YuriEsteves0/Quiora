package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.credenciais.CadastroActivity;
import com.yuri.quiora.activity.credenciais.LoginActivity;
import com.yuri.quiora.fragments.ComprarFragment;
import com.yuri.quiora.fragments.HomeFragment;
import com.yuri.quiora.fragments.NotificacoesFragment;
import com.yuri.quiora.fragments.PagamentoFragment;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.ChatRoomModel;
import com.yuri.quiora.model.UsuariosModel;

import org.checkerframework.checker.units.qual.A;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    private ImageButton faleConoscoAtendente, menuHamb;
    private DatabaseReference reference;
    ComprarFragment comprarFragment = new ComprarFragment();
    NotificacoesFragment notificacoesFragment = new NotificacoesFragment();
    PagamentoFragment pagamentoFragment = new PagamentoFragment();
    private FirebaseAuth auth = FirebaseHelper.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        menuHamb = findViewById(R.id.menuHamb);
        faleConoscoAtendente = findViewById(R.id.faleConoscoAtendente);

        menuHamb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), MenuActivity.class);
            }
        });

        if(auth.getCurrentUser().getUid().equals("RgXBfjdRPteRNyXyG6d9KS7Szff1")){
            faleConoscoAtendente.setVisibility(View.VISIBLE);
        }

        faleConoscoAtendente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();

                String uidUsu = FirebaseHelper.getUserUid();

                reference = FirebaseHelper.getReferenceUsuarios();

                reference.child("RgXBfjdRPteRNyXyG6d9KS7Szff1").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UsuariosModel model = snapshot.getValue(UsuariosModel.class);

                            Log.d("CHECKQUIORA", "onDataChange: " + model.getUid() + " " + model.getNome() + " " + auth.getCurrentUser().getUid());
                            if(auth.getCurrentUser().getUid().equals("RgXBfjdRPteRNyXyG6d9KS7Szff1")){
                                helper.TrocarIntent(getApplicationContext(), GerenciarChatsActivity.class);
                            }else{
                                UsuariosModel usuariosModel = new UsuariosModel();
                                usuariosModel.setUid("RgXBfjdRPteRNyXyG6d9KS7Szff1");
                                usuariosModel.setNome("atendimentoQuiora");
                                usuariosModel.setEmail("atendimentoQuiora@gmail.com");

                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                helper.passarDadosDeUmUsuarioPorIntent(intent, usuariosModel);
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.getMessage();
                    }
                });
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.Home){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, homeFragment).commit();
                    return true;
                }
                if(id == R.id.Comprar){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, comprarFragment).commit();
                    return true;
                }
                if(id == R.id.Pagamento){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, pagamentoFragment).commit();
                    return true;
                }
                if(id == R.id.Notificacoes){
                    getSupportFragmentManager().beginTransaction().replace(R.id.containerFrameLayout, notificacoesFragment).commit();
                    return true;
                }
                return false;
            }
        });
    }

//        auth = FirebaseHelper.getAuth();
//
//        Toolbar toolbar = findViewById(R.id.ToolbarPrincipal);
//        btnD = findViewById(R.id.btnD);
//        faleConoscoLayout = findViewById(R.id.faleConoscoLayout);
//        buscarVoo = findViewById(R.id.buscarVoo);
//        idaInp = findViewById(R.id.ida);
//        voltaInp= findViewById(R.id.voltaInp);
//        deInput = findViewById(R.id.deInput);
//        paraInput = findViewById(R.id.paraInput);
//
//        buscarVoo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String de = deInput.getText().toString();
//                String para = paraInput.getText().toString();
//                String ida = idaInp.getText().toString();
//                String volta = voltaInp.getText().toString();
//
//                if(para.isEmpty() || de.isEmpty() || ida.isEmpty() || volta.isEmpty()){
//                    AndroidHelper helper = new AndroidHelper();
//                    helper.MostrarToast(getApplicationContext(), "PREENCHA TODOS OS CAMPOS!");
//                }else{
//                    Intent intent = new Intent(MainActivity.this, ProcurarVooActivity.class);
//                    intent.putExtra("deLugar", de);
//                    intent.putExtra("paraLugar", para);
//                    intent.putExtra("idaDia", ida);
//                    intent.putExtra("voltaDia", volta);
//                    startActivity(intent);
//                }
//            }
//        });
//
//
//        checkNivelUsuario();
//
//        toolbar.setTitle("Quiora");
//        setSupportActionBar(toolbar);
//
//        idaInp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Nada a fazer antes da mudança de texto
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Nada a fazer durante a mudança de texto
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 2 && !s.toString().contains("/")) { // Se o texto tiver 2 caracteres e não contiver uma barra
//                    idaInp.setText(new StringBuilder(s).insert(2, "/").toString()); // Adiciona uma barra após os dois primeiros caracteres
//                    idaInp.setSelection(idaInp.length()); // Coloca o cursor no final do texto
//                } else if (s.length() == 5 && !s.toString().substring(3, 4).equals("/")) { // Se o texto tiver 5 caracteres e a quarta posição não for uma barra
//                    idaInp.setText(new StringBuilder(s).insert(5, "/").toString()); // Adiciona uma barra após os quatro primeiros caracteres
//                    idaInp.setSelection(idaInp.length()); // Coloca o cursor no final do texto
//                }
//            }
//        });
//
//        voltaInp.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Nada a fazer antes da mudança de texto
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // Nada a fazer durante a mudança de texto
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() == 2 && !s.toString().contains("/")) { // Se o texto tiver 2 caracteres e não contiver uma barra
//                    voltaInp.setText(new StringBuilder(s).insert(2, "/").toString()); // Adiciona uma barra após os dois primeiros caracteres
//                    voltaInp.setSelection(voltaInp.length()); // Coloca o cursor no final do texto
//                } else if (s.length() == 5 && !s.toString().substring(3, 4).equals("/")) { // Se o texto tiver 5 caracteres e a quarta posição não for uma barra
//                    voltaInp.setText(new StringBuilder(s).insert(5, "/").toString()); // Adiciona uma barra após os quatro primeiros caracteres
//                    voltaInp.setSelection(voltaInp.length()); // Coloca o cursor no final do texto
//                }
//            }
//        });
//    }
//
//    private String formatarData(String data) {
//        StringBuilder dataFormatada = new StringBuilder(data);
//        dataFormatada.insert(2, '/'); // Inserir '/' após os primeiros 2 caracteres
//        dataFormatada.insert(5, '/'); // Inserir '/' após os caracteres 5
//
//        return dataFormatada.toString();
//    }
//
//    public void checkNivelUsuario(){
//        reference = FirebaseHelper.getReferenceUsuarios();
//
//        uidUsu = FirebaseHelper.getUserUid();
//        Log.d("UIDUSU", "checkNivelUsuario: " + uidUsu);
//
//        reference.child(uidUsu).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                UsuariosModel usuariosModel = snapshot.getValue(UsuariosModel.class);
//
//                if (usuariosModel != null && usuariosModel.getNivelAcesso().equals("usuario")) {
//                    btnD.setText("Minhas viagens");
//                    btnD.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AndroidHelper helper = new AndroidHelper();
//                            helper.TrocarIntent(getApplicationContext(), GerenciarVooActivity.class);
//                        }
//                    });
//                }
//
//                if(usuariosModel != null && usuariosModel.getNivelAcesso().equals("gerenteVoo")){
//                    btnD.setText("Gerenciar meus vôos");
//                    btnD.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            AndroidHelper helper = new AndroidHelper();
//                            helper.TrocarIntent(getApplicationContext(), GerenciarActivity.class);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_sample, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int idItem = item.getItemId();
//
//        if(idItem == R.id.config){
//            AndroidHelper helper = new AndroidHelper();
//            helper.TrocarIntent(getApplicationContext(), ConfigActivity.class);
//        }
//        if(idItem == R.id.segViagem){
//
//        }
//        if(idItem == R.id.pacotes){
//
//        }
//        if(idItem == R.id.sairBtn){
//            auth.signOut();
//            AndroidHelper helper = new AndroidHelper();
//            helper.TrocarIntent(getApplicationContext(), LoginActivity.class);
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}