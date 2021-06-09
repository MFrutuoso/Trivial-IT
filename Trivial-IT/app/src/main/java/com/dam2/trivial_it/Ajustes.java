package com.dam2.trivial_it;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class Ajustes extends AppCompatActivity {
    Button btnPlayMusica, cerrar;
    static Boolean efectosEncendidos=true;
    static Boolean musicaFondoEncendida = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        btnPlayMusica = findViewById(R.id.btn_PlayMusica);

        cerrar = (Button)findViewById(R.id.btnCerrarSesion);

        cerrar.setOnClickListener(v -> {
            SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            preferences.edit().clear().commit();
            guardarAjustes(); //Meter en un hilo en el futuro
            Intent intent=new Intent(getApplicationContext(), Login.class);
            startActivity(intent); //Nos lleva al activity_Login
            finish(); //Destruye la actividad actual
        });

        ////////////////Se indica al botón la imagen que debe tener según el estado (Encendida/apagada)
        if(Ajustes.musicaFondoEncendida){
            btnPlayMusica.setBackgroundResource(R.drawable.pause);
        }
        else{
            btnPlayMusica.setBackgroundResource(R.drawable.play);
        }
    }
    //Metodo para guardar los ajustes y restablecerlos al abrir la app de nuevo (Usar en un hilo a futuro)
    public void guardarAjustes(){
        SharedPreferences preferences=getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("musicaFondoEncendida", Ajustes.musicaFondoEncendida); //Guardamos el estado de la musica de fondo
        editor.putBoolean("efectosEncendidos", Ajustes.musicaFondoEncendida); //Guardamos el estado de los efectos sonoros
        editor.commit();
    }
    ////////////Cuando pulsa el botón de Musica.
    public void btnPlayMusica(View view) {
        if(Ajustes.musicaFondoEncendida){
            apagarMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.play);
            Toast.makeText(this, "Música Off", Toast.LENGTH_SHORT).show();

        }
        else{
            encenderMusica();
            btnPlayMusica.setBackgroundResource(R.drawable.pause);
            Toast.makeText(this, "Música On", Toast.LENGTH_SHORT).show();
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