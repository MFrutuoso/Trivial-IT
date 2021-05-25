package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModoDeJuego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_de_juego);

        Button btnMultijugador = findViewById(R.id.btn_Multijugador);
        btnMultijugador.setEnabled(false); //Desabilitado el botón Multijugador hasta que esté disponible el modo de juego
    }

    public void btnRuleta(View view){
        Intent intent = new Intent(this, Ruleta.class);
        startActivity(intent);
    }

    public void btnQLocal(View view) {
        Intent intent = new Intent(this, QLocal.class);
        startActivity(intent);
    }
}