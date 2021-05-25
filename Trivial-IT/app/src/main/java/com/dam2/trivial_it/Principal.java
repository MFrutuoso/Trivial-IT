package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }
    public void btnNuevaPartida(View view){
        Intent IRuleta = new Intent(this, ModoDeJuego.class);
        startActivity(IRuleta);
    }
    public void btnAjustes(View view){
        Intent IAjustes = new Intent(this, Ajustes.class);
        startActivity(IAjustes);
    }
}