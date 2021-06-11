package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class Reglas extends ActivityBase {
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglas);

        mp = MediaPlayer.create(this, R.raw.boton_sound);

        WebView wv = (WebView) findViewById(R.id.reglas);
        wv.loadUrl("file:///android_asset/reglas.html");
    }

    public void btnAtras(View view) {
        Intent intent=new Intent(this, Ajustes.class);
        startActivity(intent);
        if (Ajustes.efectosEncendidos) mp.start();
        finish();
    }
}