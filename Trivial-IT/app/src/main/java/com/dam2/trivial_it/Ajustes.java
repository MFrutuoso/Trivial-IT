package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Ajustes extends AppCompatActivity {
    Button musica;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        musica = (Button)findViewById(R.id.play);
        mp = MediaPlayer.create(this, R.raw.fondo);
        musica.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v){
                    if(mp.isPlaying()){
                        mp.pause();
                        musica.setBackgroundResource(R.drawable.pause);
                        Toast.makeText(Ajustes.this, "Pausa", Toast.LENGTH_SHORT).show();
                    }else{
                        mp.start();
                        musica.setBackgroundResource(R.drawable.play);
                        Toast.makeText(Ajustes.this, "Play", Toast.LENGTH_SHORT).show();
                    }
                }
        });
    }
}