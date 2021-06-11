package com.dam2.trivial_it;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Ajustes extends ActivityBase {
    Button btnPlayMusica, btnPlayEfecto, cerrar, btnJugar, btnReglas, btnAtras;
    static Boolean efectosEncendidos=true;
    static Boolean musicaFondoEncendida = true;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mp = MediaPlayer.create(this, R.raw.boton_sound);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        btnPlayMusica = findViewById(R.id.btn_PlayMusica);
        btnPlayEfecto = findViewById(R.id.btn_PlayEfecto);
        btnReglas = findViewById(R.id.btnReglas);
        btnJugar = findViewById(R.id.btnJugar);
        btnAtras = findViewById(R.id.btnAtras);
        cerrar = (Button)findViewById(R.id.btnCerrarSesion);

        Log.e("efectosEncendidos",""+efectosEncendidos);

        cerrar.setOnClickListener(v -> {
            SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            guardarAjustes(); //Meter en un hilo en el futuro
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent); //Nos lleva al activity_Login
            finish(); //Destruye la actividad actual
        });

        btnJugar.setOnClickListener(v -> {
            Intent intent=new Intent(this, ComoJugar.class);
            startActivity(intent);
            if (Ajustes.efectosEncendidos) mp.start();
            finish();
        });
        btnAtras.setOnClickListener(v -> {
            Intent intent=new Intent(this, Principal.class);
            startActivity(intent);
            if (Ajustes.efectosEncendidos) mp.start();
            finish();
        });
        btnReglas.setOnClickListener(v -> {
            Intent intent=new Intent(this, Reglas.class);
            startActivity(intent);
            if (Ajustes.efectosEncendidos) mp.start();
            finish();
        });

        ////////////////Se indica al botón la imagen que debe tener según el estado (Encendida/apagada)
        if(Ajustes.musicaFondoEncendida) btnPlayMusica.setBackgroundResource(R.drawable.off);
        else btnPlayMusica.setBackgroundResource(R.drawable.on);

        ////////////// Lo mismo pero con efectos Sonoros
        if(Ajustes.efectosEncendidos) btnPlayEfecto.setBackgroundResource(R.drawable.off);
        else btnPlayEfecto.setBackgroundResource(R.drawable.on);
    }

    //Metodo para guardar los ajustes y restablecerlos al abrir la app de nuevo (Usar en un hilo a futuro)
    public void guardarAjustes(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("musicaFondoEncendida", Ajustes.musicaFondoEncendida); //Guardamos el estado de la musica de fondo
        editor.putBoolean("efectosEncendidos", Ajustes.efectosEncendidos); //Guardamos el estado de los efectos sonoros
        editor.commit();
    }

    public void BtnPlayEfecto(View view){
        if (Ajustes.efectosEncendidos) {
            Ajustes.efectosEncendidos = false;
            btnPlayEfecto.setBackgroundResource(R.drawable.on);
        }
        else{
            Ajustes.efectosEncendidos = true;
            btnPlayEfecto.setBackgroundResource(R.drawable.off);
        }
        System.out.println("isEnabled?"+efectosEncendidos);
        guardarAjustes();
    }

    ////////////Cuando pulsa el botón de Musica.
    public void btnPlayMusica(View view) {
        if(Ajustes.musicaFondoEncendida){
            apagarMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.on);
            Toast.makeText(this, "Música Off", Toast.LENGTH_SHORT).show();
            if (Ajustes.efectosEncendidos) mp.start();
        }
        else{
            encenderMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.off);
            Toast.makeText(this, "Música On", Toast.LENGTH_SHORT).show();
            if (Ajustes.efectosEncendidos) mp.start();
        }
        guardarAjustes();
    }

    ///////////////Metodo para encender musica
    public void encenderMusica(){
        if(!Ajustes.musicaFondoEncendida){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            startService(miReproductor);
            Ajustes.musicaFondoEncendida = true;
        }
    }

    ///////////Metodo para apagar musica
    public void apagarMusica(){
        if(Ajustes.musicaFondoEncendida){
            Intent miReproductor = new Intent(this, ServicioMusica.class);
            this.stopService(miReproductor);
            Ajustes.musicaFondoEncendida = false;

        }
    }

}