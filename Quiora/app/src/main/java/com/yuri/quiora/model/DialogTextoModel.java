package com.yuri.quiora.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

public class DialogTextoModel {

    public interface TextoListener{
        void textoListener(String texto);
    }

    public static void mostrarDialogComCaixaDeTexto(Context context, String tituloDialog, final TextoListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(tituloDialog);

        //ADICIONAR A CAIXA DE TEXTO

        final EditText inputEditText = new EditText(context);
        inputEditText.setHint("Escreva o por que você quer receber o PGDV");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                50,
                LinearLayout.LayoutParams.WRAP_CONTENT // Altura do EditText ajustada automaticamente
        );
        layoutParams.setMargins(50, 0, 50, 0); // Margens esquerda e direita para espaçamento

        inputEditText.setLayoutParams(layoutParams); // Aplica os parâmetros de layout ao EditText

        builder.setView(inputEditText);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto = inputEditText.getText().toString();
                listener.textoListener(texto);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        builder.show();

    }
}
