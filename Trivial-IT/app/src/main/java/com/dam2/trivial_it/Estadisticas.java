package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Estadisticas extends ActivityBase {
    EstadisticasLocal estadisticasLocal;
    EstadisticasPaseYJuegue estadisticasPaseYJuegue;

    public Estadisticas() {}

    public Estadisticas(EstadisticasLocal estadisticasLocal, EstadisticasPaseYJuegue estadisticasPaseYJuegue) {
        this.estadisticasLocal = estadisticasLocal;
        this.estadisticasPaseYJuegue = estadisticasPaseYJuegue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        
        
    }
    public void btnAtras(View view) {
        Intent intent=new Intent(this, Principal.class);
        startActivity(intent);
        
        if (Ajustes.efectosEncendidos){
            MediaPlayer mp = MediaPlayer.create(this, R.raw.boton_sound);
            mp.start();
        }
        finish();
    }
}
//Clase para las estadísticas de Pase y Juegue
class EstadisticasPaseYJuegue{
    int partidasJugadas;
    int partidasGanadas;
    int partidasPerdidas;
    int totalPreguntasAcertadas;
    int totalPreguntasFalladas;

    public EstadisticasPaseYJuegue() {}

    public EstadisticasPaseYJuegue(int partidasJugadas, int partidasGanadas, int partidasPerdidas, int totalPreguntasAcertadas, int totalPreguntasFalladas) {
        this.partidasJugadas = partidasJugadas;
        this.partidasGanadas = partidasGanadas;
        this.partidasPerdidas = partidasPerdidas;
        this.totalPreguntasAcertadas = totalPreguntasAcertadas;
        this.totalPreguntasFalladas = totalPreguntasFalladas;
    }
}

//Clase para las estadísticas en Local
class EstadisticasLocal{
    float sistemasInformaticos;
    float programacion;
    float baseDeDatos;
    float programacionWeb;
    float entornosDeDesarrollo;
    float hardware;

    public EstadisticasLocal(){}

    public EstadisticasLocal(float sistemasInformaticos, float programacion, float baseDeDatos, float programacionWeb, float entornosDeDesarrollo, float hardware) {
        this.sistemasInformaticos = sistemasInformaticos;
        this.programacion = programacion;
        this.baseDeDatos = baseDeDatos;
        this.programacionWeb = programacionWeb;
        this.entornosDeDesarrollo = entornosDeDesarrollo;
        this.hardware = hardware;
    }

}