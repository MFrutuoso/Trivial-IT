package com.dam2.trivial_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

public class Principal extends AppCompatActivity {
    MediaPlayer mp;
    MediaPlayer fondo;
    Button cerrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mp = MediaPlayer.create(this, R.raw.boton_sound);

        /*cerrar = (Button)findViewById(R.id.btnCerrarSesion);
        cerrar.setOnClickListener(v -> {
            SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });*/

    }
    public void btnNuevaPartida(View view){
        Intent IRuleta = new Intent(this, ModoDeJuego.class);
        startActivity(IRuleta);
        mp.start();
    }
    public void btnAjustes(View view){
        Intent IAjustes = new Intent(this, Ajustes.class);
        startActivity(IAjustes);
        mp.start();
    }
}