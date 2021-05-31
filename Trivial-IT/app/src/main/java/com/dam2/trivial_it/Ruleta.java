package com.dam2.trivial_it;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Ruleta extends AppCompatActivity implements Animation.AnimationListener {
    boolean blnButtonRotation = true;
    int intNumber = 7;
    long IngDegrees = 0;
    SharedPreferences sharedPreferences;
    MediaPlayer mp;
    ImageView selected, imageRoulette;
    String [] tablaCatArray = {"programacionweb", "Comodín", "hardware", "entornosdedesarrollo", "basededatos", "programacion", "sistemasinformaticos" };
    String [] sectors ={"Programación Web", "Comodín", "Hardware", "Entornos","Bases de Datos","Programación", "Sistemas Informáticos"};
    TextView textView;
    Button b_start;
    String res = null;
    String tablaCat="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        b_start = (Button)findViewById(R.id.buttonStart);
        textView = findViewById(R.id.txtv);
        selected = (ImageView)findViewById(R.id.imageSelected);
        imageRoulette = (ImageView)findViewById(R.id.rouletteImage);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.intNumber = this.sharedPreferences.getInt("INT NUMBER",7);
        //setImageRoulette(this.intNumber);
    }

    @Override
    public void onAnimationStart(Animation animation){
        this.blnButtonRotation=false;
        b_start.setVisibility(View.VISIBLE);
        mp = MediaPlayer.create(this, R.raw.sonido);
        mp.start();
    }

    @Override
    public void onAnimationEnd(Animation animation){
        @SuppressLint("WrongConstant") Toast toast = Toast.makeText(this, " " + String.valueOf((int)(((double)this.intNumber)
                - Math.floor(((double)this.IngDegrees) / (360.0d / ((double)this.intNumber))))) + "  ",0);
        toast.setGravity(49,0,0);
        //toast.show();
        this.blnButtonRotation = true;
        b_start.setGravity(View.VISIBLE);
        CalculatePoint(IngDegrees);
    }
    @Override
    public void onAnimationRepeat(Animation animation){
    }

    public void onClickButtonRotation (View v){
        if(this.blnButtonRotation){
            int ran = new Random().nextInt(360)+3600;
            RotateAnimation rotateAnimation = new RotateAnimation((float)this.IngDegrees, (float)
                    (this.IngDegrees + ((long)ran)),1,0.5f,1,0.515f);
            this.IngDegrees = (this.IngDegrees + ((long)ran)) % 360;
            rotateAnimation.setDuration((long)ran);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setInterpolator(new DecelerateInterpolator());
            rotateAnimation.setAnimationListener(this);
            imageRoulette.setAnimation(rotateAnimation);
            imageRoulette.startAnimation(rotateAnimation);
        }

    }

    private void iniciarPregunta() {
        Intent i = new Intent(this, Quiz.class);
        Log.e("iniciarPregunta()putExt", res); //Comprobamos que el parametro no va vacio. PRESCINDIBLE
        i.putExtra("categoria",res); //Titulo de a categoría
        i.putExtra("tablaCat" ,tablaCat); //Nombre de la tabla que consultaremos en la DB
        i.putExtra("intentPractica",false);
        startActivity(i);
    }

    public void CalculatePoint(long degree){
        double initialPoint = 0;
        double endPoint=51.42857142857143;
        int i=0;

        do{
            if(degree > initialPoint && degree < endPoint){
                res=sectors[i]; //res = Categoría resultante de la Ruleta
                tablaCat = tablaCatArray[i]; //Guardamos el nombre de la tabla de la base de datos

                if(sectors[i] != "Comodín")
                    iniciarPregunta(); //Intents a ventana con pregunta sector[i];
                else {return;} //"Comodín" -> Iniciamos emergente preguntando categoría a elegir
            }
            initialPoint+=51.42857142857143; endPoint+=51.42857142857143;
            i++;
        }while(res == null);
        textView.setText(res); //TextView prescindible, muestra categoría elegida.
    }

    public void btnAtras(View view) {
        Intent i = new Intent(this, ModoDeJuego.class);
        startActivity(i);
    }
}