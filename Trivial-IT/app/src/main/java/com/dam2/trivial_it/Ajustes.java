package com.dam2.trivial_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Ajustes extends AppCompatActivity {
    Button btnPlayMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        btnPlayMusica = (Button)findViewById(R.id.btn_PlayMusica);

        //Se indica al botón la imagen que debe tener según el estado (Encendida/apagada)
        if(Login.encendida){
            btnPlayMusica.setBackgroundResource(R.drawable.pause);
        }
        else{
            btnPlayMusica.setBackgroundResource(R.drawable.play);
        }
    }

    //Cuando pulsa el botón de Musica.
    public void btnPlayMusica(View view) {
        if(Login.encendida){
            apagarMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.play);
            Toast.makeText(this, "Música Off", Toast.LENGTH_SHORT).show();
        }
        else{
            encenderMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.pause);
            Toast.makeText(this, "Música On", Toast.LENGTH_SHORT).show();
        }
    }

    //Metodo para encender musica
    public void encenderMusica(){
        if(Login.encendida==false){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.startService(miReproductor);
            Login.encendida = true;
        }
    }
    //Metodo para apagar musica
    public void apagarMusica(){
        if(Login.encendida==true){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.stopService(miReproductor);
            Login.encendida = false;
        }

    }
}