package com.yuri.quiora.helper;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ChatActivity;
import com.yuri.quiora.activity.GerenciarChatsActivity;
import com.yuri.quiora.model.UsuariosModel;
import com.yuri.quiora.model.VooModel;

import java.util.List;

public class AndroidHelper {
    public void TrocarIntent(Context context, Class <?> cls){
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean verifInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public static void VerifFaleConosco(DatabaseReference reference, FirebaseAuth auth, Context context){
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
                        helper.TrocarIntent(context, GerenciarChatsActivity.class);
                    }else{
                        UsuariosModel usuariosModel = new UsuariosModel();
                        usuariosModel.setUid("RgXBfjdRPteRNyXyG6d9KS7Szff1");
                        usuariosModel.setNome("atendimentoQuiora");
                        usuariosModel.setEmail("atendimentoQuiora@gmail.com");

                        Intent intent = new Intent(context, ChatActivity.class);
                        helper.passarDadosDeUmUsuarioPorIntent(intent, usuariosModel);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    public void MostrarToast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static String formatarPrimeiraLetraMaiuscula(String texto){
        if(texto == null || texto.isEmpty()){
            return texto;
        }
        return texto.substring(0,1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    public static String setChatRoomIdLexi(String idUsu1, String idUsu2){
        if(idUsu1.compareTo(idUsu2) < 0){
            return idUsu1 + "_" + idUsu2;
        }else{
            return idUsu2 + "_" + idUsu1;
        }
    }

    public UsuariosModel receberDadosDeUmUsuarioPorIntent(Intent intent){
        UsuariosModel usuariosModel = new UsuariosModel();
        usuariosModel.setNome(intent.getStringExtra("nomeUsu").toString());
        usuariosModel.setEmail(intent.getStringExtra("emailUsu").toString());
        usuariosModel.setUid(intent.getStringExtra("uidUsu").toString());
        return usuariosModel;
    }

    public void passarDadosDeUmUsuarioPorIntent(Intent intent, UsuariosModel usuariosModel){
        intent.putExtra("nomeUsu", usuariosModel.getNome());
        intent.putExtra("uidUsu", usuariosModel.getUid());
        intent.putExtra("emailUsu", usuariosModel.getEmail());
    }

    public void passarDadosDeVooPorIntent(Intent intent, VooModel vooModel){
        intent.putExtra("uidViagem", vooModel.getUidViagem());
        intent.putExtra("para", vooModel.getPara());
        intent.putExtra("dataViagem", vooModel.getDataViagem());
        intent.putExtra("horarioSaida", vooModel.getHorarioSaida());
        intent.putExtra("horarioChegada", vooModel.getHorarioChegada());
        intent.putExtra("nomeCompanhia", vooModel.getNomeCompanhia());
        intent.putExtra("preco", vooModel.getPreco());
    }

    public VooModel receberDadosDeVooPorIntent(Intent intent){
        VooModel vooModel = new VooModel();
        vooModel.setUidViagem(intent.getStringExtra("uidViagem"));
        vooModel.setPara(intent.getStringExtra("para"));
        vooModel.setDataViagem(intent.getStringExtra("dataViagem"));
        vooModel.setHorarioSaida(intent.getStringExtra("horarioSaida"));
        vooModel.setHorarioChegada(intent.getStringExtra("horarioChegada"));
        vooModel.setNomeCompanhia(intent.getStringExtra("nomeCompanhia"));
        vooModel.setPreco(intent.getStringExtra("preco"));
        return vooModel;
    }

    public static void configurarToolbarFechar(String mensagemTopBar, TextView textView){
        textView.setText("Comprar Voos");
    }
    
    public void errorMsgToast(Context context){
        Toast.makeText(context, "Houve um erro ao realizar a ação, tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
    }
}
