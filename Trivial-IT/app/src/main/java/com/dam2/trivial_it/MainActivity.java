package com.dam2.trivial_it;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final int DURACION_SPLASH = 5400;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
                 */
                SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion=preferences.getBoolean("sesion", false);
                Login.nick = preferences.getString("nick", "Jugador 1");
                Login.encendida = preferences.getBoolean("encendida", true); //Obtenemos pref. del user en la musica
                if (Login.encendida) encenderMusica(); //Si tenía la musica ON, al entrar la encendemos
                Log.e("Login.encendida",""+Login.encendida);
                if(sesion){
                    Intent intent = new Intent(getApplicationContext(), Principal.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent=new Intent (getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                }
            };
        }, DURACION_SPLASH);
    }
    //Método para encender la música
    public void encenderMusica(){
        if(Login.encendida){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.startService(miReproductor);
            Login.encendida = true;
        }
    }
}