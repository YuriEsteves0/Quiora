package com.yuri.quiora.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.adapter.AdapterNotificacoes;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.NotificacaoModel;

import java.util.ArrayList;
import java.util.List;

public class NotificacoesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView noNotifications;
    private DatabaseReference reference;
    private List<NotificacaoModel> notificacaoModelList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificacoes, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        noNotifications = view.findViewById(R.id.noNotifications);

//        configurarDados();
//        configurarAdapter();

        return view;
    }

    public void configurarDados(){
        Log.d("TAG", "configurarDados: CONFIG DADOS");
        reference = FirebaseHelper.getReferenceNotificacao();
        Log.d("TAG", "configurarDados: " + reference);
        reference.child(FirebaseHelper.getUserUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", "onDataChange: DENTRO DO ONDATACHANGE");
                if(snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Log.d("TAG", "onDataChange: " + dataSnapshot.getKey());
                        String idNotificacao = dataSnapshot.getKey();
                        Log.d("TAG", "onDataChange: " + idNotificacao);

                        reference.child(FirebaseHelper.getUserUid()).child(idNotificacao).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){

                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                        String msg = dataSnapshot1.child("msg").getValue(String.class);
                                        String msgNova = dataSnapshot1.child("msgNova").getValue(String.class);
                                        String uidUsuRemetente = dataSnapshot1.child("uidUsuRemetente").getValue(String.class);
                                        String uidUsuDestinatario = dataSnapshot1.child("uidUsuDestinatario").getValue(String.class);

                                        NotificacaoModel model = new NotificacaoModel(msg, msgNova, uidUsuRemetente, uidUsuDestinatario);

                                        Log.d("TAG", "onDataChange1: " + model);


                                        notificacaoModelList.add(model);
                                    }
//                                    Log.d("TAG", "onDataChangeAAA: " + snapshot.getChildrenCount());

                                    recyclerView.getAdapter().notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }

    public void configurarAdapter(){
        AdapterNotificacoes adapterNotificacoes = new AdapterNotificacoes(notificacaoModelList, getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterNotificacoes);
    }

}