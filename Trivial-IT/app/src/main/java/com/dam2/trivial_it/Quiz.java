package com.dam2.trivial_it;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Quiz extends AppCompatActivity {


    //Declaración de variables y objetos
    TextView tvCategoria, tvPregunta, tvOpcion1, tvOpcion2, tvOpcion3, tvOpcion4, tvContador;
    Button btnSiguiente, btnAbandonar;


    RequestQueue requestQueue;
    String[][] qPractica = new String[5][7];
    int qCont=-1;
    Boolean intentPractica=null; //Para comprobar el modo de juego
    int numAleatorio=-1; //Variable usada para generar un numero aleatorio en varias ocasiones

    String respuestaCorrecta=""; //Guardaremos la respuesta correcta para realizar la valdiación
    ArrayList<String> listaOpciones = new ArrayList<String>(); //Guardamos las respuestas a mostrar (4 opciones)
    String [] preguntaCompleta = new String[7]; //Guardamos la preugnta integra desde la base de datos
    boolean respondido=false; //Variable para saber si ya ha pulsado una de las respuestas

    int [] pos5Preguntas = {-1,-1,-1,-1,-1}; //Posiciones para las 5 preguntas que se mostraran en modo entrenamiento

	MediaPlayer error;
    MediaPlayer correcto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Enlazamos componentes del XML con variables
        //btnSiguiente = findViewById(R.id.btn_siguiente);
        //btnAbandonar = findViewById(R.id.btn_abandonar);
        tvCategoria = findViewById(R.id.tv_categoria);
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvOpcion1 = findViewById(R.id.tv_opcion1);
        tvOpcion2 = findViewById(R.id.tv_opcion2);
        tvOpcion3 = findViewById(R.id.tv_opcion3);
        tvOpcion4 = findViewById(R.id.tv_opcion4);
        tvContador = findViewById(R.id.tv_contador);
		error = MediaPlayer.create(this, R.raw.error);
        correcto = MediaPlayer.create(this, R.raw.acierto);

        listaOpciones.ensureCapacity(4); //Le indicamos el numero de posiciones para ahorrar recursos

        String categoria="";
        String tablaCat="";

        //Parametros traídos de la actividad anterior a través del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoria = extras.getString("categoria");
            tablaCat = extras.getString("tablaCat");
            tvCategoria.setText(categoria);
            intentPractica = extras.getBoolean("intentPractica");

            //if(!intentPractica) btnSiguiente.setText("Continuar");

        }else Log.e("Bundle extras", "null");

//        if (intentPractica){
//            addIndicesqPractica(); //Añadimos los indices al array para el modo practica
//        }

        String url = Locale.getDefault().getLanguage(); //Obtenemos el lenguaje del usuario

        //Comprobamos si el usuario navega en inglés para añadir el idioma a las tablas que se buscaran en la DB
        if(url.equalsIgnoreCase("en")) url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat+"en";
        else url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat;

        buscarDatos(url); //Llamamos al método de la consulta a la DB

        qCont=0;

    }

    //Método para buscar los datos en la DB
    public void buscarDatos(String URL){
        Log.e("URL", URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("OnResponse", "Dentro");
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) { //Int con el número total de las consultas. Solo necesitamos hasta 10 por el tamaño del array.
                    try {
                        jsonObject = response.getJSONObject(i);

                        int nResultados = response.length();

                        if(intentPractica){ //SI ES EL MODO ENTRENAMIENTO

                            if(i==0) cincoPreguntasAleatorias(nResultados);

                            if (i==pos5Preguntas[0])
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                            if (i==pos5Preguntas[1])
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                            if (i==pos5Preguntas[2])
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                            if (i==pos5Preguntas[3])
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                            if (i==pos5Preguntas[4])
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                        }
                        //-------------------------------------------------------------------------------------------
                        else{ //quizRuleta
                            if(i==0){
                                numAleatorio = (int) Math.floor(Math.random()*(nResultados-0+1)+0);  // Valor entre M y N, ambos incluidos.
                            }
                            if(i == numAleatorio)
                                quizRuleta(jsonObject);
                        }

                    } catch (JSONException e) {
                        Log.e("catch", "Error en JSON buscarDatos()");
                        Toast.makeText(Quiz.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                mostrarPregunta(); //Mostramos los datos en la interfaz
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_LONG).show();
                Log.e("onErrorResponse", "hubo un error: public void onErrorResponse(VolleyError error) {");
            }
        }
        );
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //Printamos en la interfaz los resultados de la busqueda a la DB
    private void mostrarPregunta() {

        if(intentPractica){ //quizLocal
            qCont++;
            listaOpcionesQLocal();
            tvPregunta.setText(qPractica[qCont][1]);
            mostarOpcionesAleatorias();
            tvContador.setText((qCont+1)+"/10");
        }

        else{ //quizRuleta
            tvPregunta.setText(preguntaCompleta[1]);
            mostarOpcionesAleatorias();
        }
    }

    //Obtenemos las posiciones de las 5 preguntas a mostrar en QLocal
    public void cincoPreguntasAleatorias(int numRegistros){
        int contador=0;
        boolean coincide = false;

        pos5Preguntas[0] = (int) Math.floor(Math.random()*(numRegistros-0+1)+0); //Obtenemos el primmer [0] fuera del bucle
        do{
            numAleatorio = (int) Math.floor(Math.random()*(numRegistros-0+1)+0);  // Valor entre M y N, ambos incluidos.
            for(int i=0; i<5; i++){
                if (i==0) break; //Salimos del bucle para asignar el primer valor a pos5Preguntas[0] ya que es el primero.
                if(pos5Preguntas[i]==numAleatorio) {
                    coincide = true;
                    break;
                }
            }
            if (!coincide){
                pos5Preguntas[contador] = numAleatorio;
                System.out.println("pos5Preguntas["+contador+"] = "+pos5Preguntas[contador]);
                contador++;
            }
            else coincide=false;
        }while(contador<5);
    }

    //Metodo si venimos del activity de QLocal
    public void quizLocal(int i, JSONObject jsonObject) throws JSONException {

        qPractica[i][0] = jsonObject.getString("id");
        qPractica[i][1] = jsonObject.getString("pregunta");
        qPractica[i][2] = jsonObject.getString("respuesta");
        qPractica[i][3] = jsonObject.getString("incorrecta1");
        qPractica[i][4] = jsonObject.getString("incorrecta2");
        qPractica[i][5] = jsonObject.getString("incorrecta3");
        qPractica[i][6] = jsonObject.getString("categoria");
    }

    public void listaOpcionesQLocal(){
        listaOpciones.add(qPractica[qCont][2]);
        listaOpciones.add(qPractica[qCont][3]);
        listaOpciones.add(qPractica[qCont][4]);
        listaOpciones.add(qPractica[qCont][5]);

        respuestaCorrecta = qPractica[qCont][2]; //Guardamos la respuesta correcta para su posterior validación
    }

    public void quizRuleta(JSONObject jsonObject) throws JSONException {

        //Obtenemos los campos de la Base de Datos
        preguntaCompleta[0] = jsonObject.getString("id");
        preguntaCompleta[1] = jsonObject.getString("pregunta");
        preguntaCompleta[2] = jsonObject.getString("respuesta");
        preguntaCompleta[3] = jsonObject.getString("incorrecta1");
        preguntaCompleta[4] = jsonObject.getString("incorrecta2");
        preguntaCompleta[5] = jsonObject.getString("incorrecta3");
        preguntaCompleta[6] = jsonObject.getString("categoria");

        //Añadimos a la lista las opciones para generar el orden aleatorio
        listaOpciones.add(preguntaCompleta[2]);
        listaOpciones.add(preguntaCompleta[3]);
        listaOpciones.add(preguntaCompleta[4]);
        listaOpciones.add(preguntaCompleta[5]);

        respuestaCorrecta = preguntaCompleta[2]; //Guardamos la respuesta correcta para su posterior validación

    }

    //onClick de las opciones/respuestas a la pregunta
    public void validarOpciones(View view) {
        int id = view.getId();

        switch (id){
            case R.id.tv_opcion1:
                if (!respondido){ //Si no ha seleccionado ninguna respuesta antes...
                    if(tvOpcion1.getText().toString().equals(respuestaCorrecta)){
                        tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta); //Verde
                        correcto.start();
                    }
                    else{
                        tvOpcion1.setBackgroundResource(R.color.colorRespuestaIncorrecta); //Rojo

                        error.start();
                        if(tvOpcion2.getText().toString().equals(respuestaCorrecta)) tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if(tvOpcion3.getText().toString().equals(respuestaCorrecta)) tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido=true;
                }
                break;
            case R.id.tv_opcion2:
                if (!respondido){
                    if(tvOpcion2.getText().toString().equals(respuestaCorrecta)){
                        tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        correcto.start();

                    }
                    else{
                        tvOpcion2.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        error.start();
                        if(tvOpcion1.getText().toString().equals(respuestaCorrecta)) tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if(tvOpcion3.getText().toString().equals(respuestaCorrecta)) tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido=true;
                }
                break;
            case R.id.tv_opcion3:
                if (!respondido){
                    if(tvOpcion3.getText().toString().equals(respuestaCorrecta)){
                        tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);

                        correcto.start();

                    }
                    else{
                        tvOpcion3.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        error.start();
                        if(tvOpcion1.getText().toString().equals(respuestaCorrecta)) tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if(tvOpcion2.getText().toString().equals(respuestaCorrecta)) tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido=true;
                }
                break;
            case R.id.tv_opcion4:
                if (!respondido){
                    if(tvOpcion4.getText().toString().equals(respuestaCorrecta)){
                        tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);

                        correcto.start();

                    }
                    else{
                        tvOpcion4.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        error.start();
                        if(tvOpcion1.getText().toString().equals(respuestaCorrecta)) tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if(tvOpcion2.getText().toString().equals(respuestaCorrecta)) tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido=true;
                }
                break;
        }
    }

    public void mostarOpcionesAleatorias(){

        numAleatorio = (int) Math.floor(Math.random()*((listaOpciones.size()-1)-0+1)+0);  // Genera un numero aleatorio entre 0 y el tamaño del ArrayList.
        //Log.e("NumAleatorioLista", numAleatorio+"");
        tvOpcion1.setText(listaOpciones.get(numAleatorio)); //Mostramos en la 1a opción la posición aleatoria del ArrayList
        listaOpciones.remove(numAleatorio); //Eliminamos de la lista la opción ya mostrada en TextView

        numAleatorio = (int) Math.floor(Math.random()*((listaOpciones.size()-1)-0+1)+0);
        tvOpcion2.setText(listaOpciones.get(numAleatorio));
        listaOpciones.remove(numAleatorio);

        numAleatorio = (int) Math.floor(Math.random()*((listaOpciones.size()-1)-0+1)+0);
        tvOpcion3.setText(listaOpciones.get(numAleatorio));
        listaOpciones.remove(numAleatorio);

        tvOpcion4.setText(listaOpciones.get(0));
        listaOpciones.remove(0);
    }

    public void btnNext(View view) {
        mostrarPregunta();
    }
    public void btnAtras(View view) {
        Intent i = new Intent(this, ModoDeJuego.class);
        startActivity(i);
    }
}