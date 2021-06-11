package com.dam2.trivial_it.dialogos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.dam2.trivial_it.R;

public class Jugador2NuevaPartidaDialog extends AppCompatDialogFragment {
    private EditText editTextUsername;
    private Jugador2NuevaPartidaDialogListener listener;
    Button btnAceptar;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.nombre_j2_dialog_layout, null);

        builder.setView(view).setCancelable(false);

        editTextUsername = view.findViewById(R.id.edit_username);
        btnAceptar = view.findViewById(R.id.btn_Aceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                listener.applyTexts(username);
                getDialog().dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (Jugador2NuevaPartidaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement Jugador2NuevaPartidaDialogListener");
        }
    }

    public interface Jugador2NuevaPartidaDialogListener {
        void applyTexts(String username);
    }
}

