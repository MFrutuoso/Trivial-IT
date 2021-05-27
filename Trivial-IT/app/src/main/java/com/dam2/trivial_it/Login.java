package com.dam2.trivial_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    static Boolean encendida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(encendida==null){ //Enciende la musica por primera vez para toda la app
            encenderMusica();
        }
    }

    //Inicia sesión y nos lleva al menú principal
    public void intentPrincipal(View view){
        Intent IPrincipal = new Intent(this, Principal.class);
        startActivity(IPrincipal);
    }

    public void encenderMusica(){
        if(encendida==null){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.startService(miReproductor);
            encendida = true;
        }
    }
  
}