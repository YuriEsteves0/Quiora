package com.yuri.quiora.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ChatActivity;
import com.yuri.quiora.activity.CriarViagemActivity;
import com.yuri.quiora.activity.GerenciarChatsActivity;
import com.yuri.quiora.activity.GerenciarVooActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.DialogTextoModel;
import com.yuri.quiora.model.PedidoModel;
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
    private ImageButton voosComprados;
    private TextView criarContaTit, textoMotivador;
    private Button criarViagem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        faleConoscoLayout = view.findViewById(R.id.faleConoscoLayout);
        voosComprados = view.findViewById(R.id.voosComprados);
        criarViagem = view.findViewById(R.id.criarViagem);
        criarContaTit = view.findViewById(R.id.criarContaTit);
        textoMotivador = view.findViewById(R.id.textoMotivador);

        configSecao();

        criarViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = FirebaseHelper.getReferenceUsuarios();
                Log.d("TAG", "onClick: CLICOU");
                reference.child(FirebaseHelper.getUserUid()).child("nivelAcesso").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Log.d("TAG", "onDataChange: " + snapshot.getValue(String.class) + "UID: " + FirebaseHelper.getUserUid());

                            if(snapshot.getValue(String.class).equals("usuario")){
                                Log.d("TAG", "onDataChange: TA AQ DENTRO");
                                try {
                                    DialogTextoModel.mostrarDialogComCaixaDeTexto(getContext(), "Pedir PGDV", new DialogTextoModel.TextoListener() {
                                        @Override
                                        public void textoListener(String texto) {
                                            DatabaseReference reference1 = FirebaseHelper.getReferencePGDV();
                                            Log.d("TAG", "onDataChange: TA AQ DENTRO2");
                                            PedidoModel pedidoModel = new PedidoModel();
                                            pedidoModel.setIdUsuPedido(FirebaseHelper.getUserUid());
                                            pedidoModel.setMsgPedido(texto);

                                            String idPushAleatorio = reference1.push().getKey();

                                            reference1.child(FirebaseHelper.getUserUid()).child(idPushAleatorio).setValue(pedidoModel);
                                            AndroidHelper helper = new AndroidHelper();
                                            helper.MostrarToast(getContext(), "Mensagem enviada com sucesso!");
                                        }
                                    });
                                }catch (Exception error){
                                    error.printStackTrace();
                                }

                            }else{
                                AndroidHelper helper = new AndroidHelper();
                                helper.TrocarIntent(getContext(), CriarViagemActivity.class);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//
            }
        });

        faleConoscoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.VerifFaleConosco(reference, auth, getContext());
            }
        });

        voosComprados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getContext(), GerenciarVooActivity.class);
            }
        });
        return view;
    }

    public void configSecao(){
        reference = FirebaseHelper.getReferenceUsuarios();

        reference.child(FirebaseHelper.getUserUid()).child("nivelAcesso").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getValue(String.class).equals("gerenteVoo")){
                        criarContaTit.setText("Quer criar uma nova viagem?");
                        textoMotivador.setText("Boas notícias! Você fio aceito para ser um gerente de vôo, logo, você tem as permissões de criar e gerenciar suas viagens!");
                        criarViagem.setText("Criar viagem agora");
                    }else if (snapshot.getValue(String.class).equals("usuario")){
                        criarContaTit.setText("Quer criar uma viagem?");
                        textoMotivador.setText("Com uma conta Quiora MGFly você pode criar seus próprios voos e receber por isso! Peça já seu PGDV (Permissão de Gerente De Voo)");
                        criarViagem.setText("Saiba mais");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}