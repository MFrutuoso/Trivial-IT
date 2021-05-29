package com.dam2.trivial_it;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;

public class Principal extends AppCompatActivity {
    MediaPlayer mp;
    MediaPlayer fondo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        mp = MediaPlayer.create(this, R.raw.boton_sound);
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