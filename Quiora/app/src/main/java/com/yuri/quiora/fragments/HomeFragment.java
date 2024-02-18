package com.yuri.quiora.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ChatActivity;
import com.yuri.quiora.activity.GerenciarChatsActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.UsuariosModel;

public class HomeFragment extends Fragment {

    private LinearLayout faleConoscoLayout;
    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseHelper.getAuth();
    private Handler handler;
    private int[] imagens = {R.drawable.paisagem, R.drawable.paisagemdois};
    private int indexAtual = 0;
    private static final long SWITCH_DELAY = 3000; //A CADA 3 SEGUNDOS A IMAGEM TROCA.
    private ViewSwitcher viewSwitcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        faleConoscoLayout = view.findViewById(R.id.faleConoscoLayout);




        faleConoscoLayout.setOnClickListener(new View.OnClickListener() {
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
                                helper.TrocarIntent(view.getContext(), GerenciarChatsActivity.class);
                            }else{
                                UsuariosModel usuariosModel = new UsuariosModel();
                                usuariosModel.setUid("RgXBfjdRPteRNyXyG6d9KS7Szff1");
                                usuariosModel.setNome("atendimentoQuiora");
                                usuariosModel.setEmail("atendimentoQuiora@gmail.com");

                                Intent intent = new Intent(view.getContext(), ChatActivity.class);
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
//                helper.TrocarIntent(getApplicationContext(), );
            }
        });
        return view;
    }
}