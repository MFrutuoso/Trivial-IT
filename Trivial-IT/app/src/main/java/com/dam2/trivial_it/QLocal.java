package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QLocal extends ActivityBase {
    MediaPlayer mp;
    Button atras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_local);
        if (Ajustes.efectosEncendidos) mp = MediaPlayer.create(this, R.raw.boton_sound);
    }

    public void btnTematica(View view) {
        int categoria=0;
        String tablaCat="";
        //String []tablaCatArray = {basededatos, programacion, programacionweb, sistemasinformaticos, entornosdedesarrollo, hardware}
        String[] categorias = {"Base de datos","Programación","Programación Web","Sistemas Informáticos","Entornos de Desarollo","Hardware"};
        int id = view.getId();
        Log.e("boton", "boton pulsado");
        switch (id){
            case R.id.btn1: //Base de datos
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=1;
                tablaCat="basededatos";
                break;
            case R.id.btn2: //Programación
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=2;
                tablaCat="programacion";
                break;
            case R.id.btn3: //Programación Web
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=3;
                tablaCat="programacionweb";
                break;
            case R.id.btn4: //Sistemas informáticos
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=4;
                tablaCat="sistemasinformaticos";
                break;
            case R.id.btn5: //Entornos de desarrollo
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=5;
                tablaCat="entornosdedesarrollo";
                break;
            case R.id.btn6: //Hardware
                if (Ajustes.efectosEncendidos) mp.start();
                categoria=6;
                tablaCat="hardware";
                break;
            default: categoria=0;
        }
//        Log.e("Categoria", categorias[-1]);
        String value="";

        if(categoria == 0) value="Prueba0";
        else value = categorias[categoria-1];

        Intent i = new Intent(this, Quiz.class);
        i.putExtra("intentPractica",true);
        i.putExtra("categoria", value);
        i.putExtra("numCat", categoria);
        i.putExtra("tablaCat",tablaCat);

        startActivity(i);
        finish();
    }

    public void btn_Atras(View view) {
        Intent i = new Intent(this, ModoDeJuego.class);
        startActivity(i);
        mp.start();
        finish();
    }
}
