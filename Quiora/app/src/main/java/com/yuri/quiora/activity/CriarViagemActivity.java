package com.yuri.quiora.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuri.quiora.R;
import com.yuri.quiora.helper.AndroidHelper;
import com.yuri.quiora.helper.FirebaseHelper;
import com.yuri.quiora.model.UsuariosModel;
import com.yuri.quiora.model.VooModel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CriarViagemActivity extends AppCompatActivity {
    private Button btnAdd;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private ImageButton backChat;
    private EditText partida, chegada, qntMax, chegadaAerop, saidaAerop, precoVoo, dataChegada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_viagem);

        partida = findViewById(R.id.origemInp);
        chegada = findViewById(R.id.destinoInp);
        qntMax = findViewById(R.id.qntPessoasInp);
        chegadaAerop = findViewById(R.id.horarioChegadaInp);
        saidaAerop = findViewById(R.id.horarioSaidaInp);
        precoVoo = findViewById(R.id.precoInp);
        btnAdd = findViewById(R.id.btnAdd);
        dataChegada = findViewById(R.id.dataViagemInp);
        backChat = findViewById(R.id.backChat);

        configurarFormatoData(dataChegada);
        configurarFormatoHorario(chegadaAerop);
        configurarFormatoHorario(saidaAerop);
        configurarFormatoPreco(precoVoo);

        reference = FirebaseHelper.getReference().child("voos");
        auth = FirebaseHelper.getAuth();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String partidaInput = partida.getText().toString();
                String chegadaInput = chegada.getText().toString();
                String qntMaxInput = qntMax.getText().toString();
                String chegadaAeroporto = chegadaAerop.getText().toString();
                String saidaAeroporto = saidaAerop.getText().toString();
                String dataChegadaInput = dataChegada.getText().toString();
                String preco = precoVoo.getText().toString();


                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                try {
                    Date dataChegadaFormatada = inputFormat.parse(dataChegadaInput);

                    String dataChegadaFormatted = inputFormat.format(dataChegadaFormatada);
                    String chegadaAeroportoFormatted = outputTimeFormat.format(inputFormat.parse(chegadaAeroporto));
                    String saidaAeroportoFormatted = outputTimeFormat.format(inputFormat.parse(saidaAeroporto));

                    cadastrar(partidaInput, chegadaInput, qntMaxInput, chegadaAeroportoFormatted, saidaAeroportoFormatted, dataChegadaFormatted, preco);
                } catch (ParseException e) {
                    e.printStackTrace();
                    // Lida com a exceção de formatação de data
                }

                cadastrar(partidaInput, chegadaInput, qntMaxInput, chegadaAeroporto, saidaAeroporto, dataChegadaInput ,preco);

                AndroidHelper helper = new AndroidHelper();
                helper.TrocarIntent(getApplicationContext(), MainActivity.class);
                helper.MostrarToast(getApplicationContext(), "Vôo adicionado com sucesso!");
                finish();
            }
        });

        TextView nomeUsu = findViewById(R.id.nomeUsu);
        nomeUsu.setText("Criar viagem");
        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void configurarFormatoHorario(EditText campoHorario) {
        campoHorario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (s.length() % 3) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (c == ':') {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                if (s.length() > 0 && (s.length() % 3) == 0) {
                    char c = s.charAt(s.length() - 1);
                    if (Character.isDigit(c) && s.length() < 5) {
                        s.insert(s.length() - 1, ":");
                    }
                }
            }
        });
    }


    // Método para configurar o formato do preço
    private void configurarFormatoPreco(EditText campoPreco) {
        campoPreco.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    campoPreco.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[R$.,]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted.replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), ""); // Remove o símbolo da moeda
                    campoPreco.setText(current);
                    campoPreco.setSelection(current.length());

                    campoPreco.addTextChangedListener(this);
                }
            }
        });
    }


    private void configurarFormatoData(EditText campoData) {
        campoData.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final String DATE_FORMAT = "##/##/####";
            private final Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }

                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + DATE_FORMAT.substring(clean.length());
                    } else {
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;

                        cal.set(Calendar.YEAR, year);

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format(Locale.getDefault(), "%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format(Locale.getDefault(), "%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(0, Math.min(sel, clean.length()));
                    current = clean;
                    campoData.setText(current);
                    campoData.setSelection(sel < current.length() ? sel : current.length());
                }
            }
        });
    }


    public void cadastrar(String partida, String chegada, String qntMax, String chegadaAeroporto, String saidaAeroporto, String dataChegada, String preco ){
        VooModel vooModel = new VooModel();

        vooModel.setDe(partida.toLowerCase());
        vooModel.setUidGerenteVoo(auth.getCurrentUser().getUid());
        vooModel.setPara(chegada.toLowerCase());
        vooModel.setQntPassageiros(Integer.parseInt(qntMax));
        vooModel.setUidViagem(reference.push().getKey());
        vooModel.setHorarioChegada(chegadaAeroporto);
        vooModel.setHorarioSaida(saidaAeroporto);
        vooModel.setDataViagem(dataChegada);

        vooModel.setPreco(preco);

        String usuUid = auth.getCurrentUser().getUid();
        DatabaseReference usuarioReference = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuUid);
        usuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UsuariosModel usuariosModel = snapshot.getValue(UsuariosModel.class);

                    if(usuariosModel != null){
                        String nomeUsu = usuariosModel.getNome();
                        vooModel.setNomeCompanhia(nomeUsu); // Definindo o nome da companhia no modelo de voo

                        reference.child(vooModel.getUidViagem()).setValue(vooModel); // Adicionando o modelo de voo ao banco de dados de referência
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}