package com.yuri.quiora.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.yuri.quiora.R;
import com.yuri.quiora.activity.ProcurarVooActivity;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;

import org.w3c.dom.Text;

public class ComprarFragment extends Fragment {

    private Button procurarVooBtn, ida, idaEvolta;
    private TextView erro, faleConosco;
    private DatabaseReference reference;
    private FirebaseAuth auth = FirebaseHelper.getAuth();
    private TextInputEditText origemInp, destinoInp, idaInp, voltaInp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comprar, container, false);

        procurarVooBtn = view.findViewById(R.id.procurarVooBtn);
        ida = view.findViewById(R.id.ida);
        idaEvolta = view.findViewById(R.id.idaEvolta);
        origemInp = view.findViewById(R.id.origemInp);
        erro = view.findViewById(R.id.erro);
        destinoInp = view.findViewById(R.id.destinoInp);
        idaInp = view.findViewById(R.id.idaInp);
        faleConosco = view.findViewById(R.id.faleConosco);
        voltaInp = view.findViewById(R.id.voltaInp);

        faleConosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.VerifFaleConosco(reference, auth, getContext());
            }
        });

        ida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trocarBtn(1);
            }
        });

        idaEvolta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trocarBtn(2);
            }
        });

        // Adicionando o TextWatcher para idaInp e voltaInp
        idaInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nada a fazer antes da mudança de texto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nada a fazer durante a mudança de texto
            }

            @Override
            public void afterTextChanged(Editable s) {
                formatarData(s, idaInp);
            }
        });

        voltaInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nada a fazer antes da mudança de texto
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nada a fazer durante a mudança de texto
            }

            @Override
            public void afterTextChanged(Editable s) {
                formatarData(s, voltaInp);
            }
        });

        procurarVooBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper helper = new AndroidHelper();
                String origem = origemInp.getText().toString();
                String destino = destinoInp.getText().toString();
                String ida = idaInp.getText().toString();
                String volta = voltaInp.getText().toString();

                if(origem.isEmpty() && destino.isEmpty() && ida.isEmpty() && volta.isEmpty()){
                    helper.MostrarToast(getContext(), "Preencha todos os campos!");
                } else {
                    Intent intent = new Intent(getContext(), ProcurarVooActivity.class);
                    intent.putExtra("deLugar", origem);
                    intent.putExtra("paraLugar", destino);
                    intent.putExtra("idaDia", ida);
                    intent.putExtra("voltaDia", volta);
                    startActivity(intent);

                }
            }
        });

        return view;
    }

    public void trocarBtn(int numero){
        switch (numero){
            case 1:
                ida.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkBlue));
                ida.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                idaEvolta.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                idaEvolta.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkBlue));
                erro.setVisibility(View.GONE);

                voltaInp.setVisibility(View.GONE);
                break;
            case 2:
                ida.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.white));
                ida.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkBlue));
                erro.setVisibility(View.VISIBLE);

                idaEvolta.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.darkBlue));
                idaEvolta.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

                voltaInp.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void formatarData(Editable s, TextInputEditText input) {
        if (s.length() == 2 && !s.toString().contains("/")) { // Se o texto tiver 2 caracteres e não contiver uma barra
            input.setText(new StringBuilder(s).insert(2, "/").toString()); // Adiciona uma barra após os dois primeiros caracteres
            input.setSelection(input.length()); // Coloca o cursor no final do texto
        } else if (s.length() == 5 && !s.toString().substring(3, 4).equals("/")) { // Se o texto tiver 5 caracteres e a quarta posição não for uma barra
            input.setText(new StringBuilder(s).insert(5, "/").toString()); // Adiciona uma barra após os quatro primeiros caracteres
            input.setSelection(input.length()); // Coloca o cursor no final do texto
        }
    }
}