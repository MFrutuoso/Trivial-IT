package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModoDeJuego extends AppCompatActivity {

    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_de_juego);

        if (Ajustes.efectosEncendidos)
            mp = MediaPlayer.create(this, R.raw.boton_sound);

        Button btnMultijugador = findViewById(R.id.btn_Multijugador);
        btnMultijugador.setEnabled(false); //Desabilitado el botón Multijugador hasta que esté disponible el modo de juego
    }

    public void btnRuleta(View view){
        Intent intent = new Intent(this, Ruleta.class);
        startActivity(intent);
        if (Ajustes.efectosEncendidos) mp.start();
    }

    public void btnQLocal(View view) {
        Intent intent = new Intent(this, QLocal.class);
        startActivity(intent);
        if (Ajustes.efectosEncendidos) mp.start();
    }

    public void btn_Atras(View view) {
        Intent i = new Intent(this, Principal.class);
        startActivity(i);
    }
}