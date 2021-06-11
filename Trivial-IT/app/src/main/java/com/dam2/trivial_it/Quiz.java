package com.dam2.trivial_it;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;

public class Quiz extends ActivityBase {

    //Declaración de variables y objetos
    TextView tvCategoria, tvPregunta, tvOpcion1, tvOpcion2, tvOpcion3, tvOpcion4, tvContador, tiempo;
    int contadorPregunta=0;

//    CountDownTimer countDownTimer;
//    private long timeMili=11000;
//    public boolean timeRunning;

    RequestQueue requestQueue;
    String[][] qPractica = new String[5][7];
    int qCont=-1;
    Boolean intentPractica=null; //Para comprobar el modo de juego
    int numAleatorio=-1; //Variable usada para generar un numero aleatorio en varias ocasiones

    String respuestaCorrecta=""; //Guardaremos la respuesta correcta para realizar la valdiación
    ArrayList<String> listaOpciones = new ArrayList<String>(); //Guardamos las respuestas a mostrar (4 opciones)
    String [] preguntaCompleta = new String[7]; //Guardamos la preugnta integra desde la base de datos
    boolean respondido=false; //Variable para saber si ya ha pulsado una de las respuestas
    Partida partida; //Objeto para controlar el flujo de la partida
    int [] pos5Preguntas = {-1,-1,-1,-1,-1}; //Posiciones para las 5 preguntas que se mostraran en modo entrenamiento
    int catPregunta=-1;
    int aciertos=0; //Variable para contabilizar los aciertos en el modo entrenamiento
    boolean acierto=false; //variable para validar si se acierta una pregunta;
	MediaPlayer error;
    MediaPlayer correcto;

    ImageButton btnSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Enlazamos componentes del XML con variables
        tvCategoria = findViewById(R.id.tv_categoria);
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvOpcion1 = findViewById(R.id.tv_opcion1);
        tvOpcion2 = findViewById(R.id.tv_opcion2);
        tvOpcion3 = findViewById(R.id.tv_opcion3);
        tvOpcion4 = findViewById(R.id.tv_opcion4);
        tvContador = findViewById(R.id.tv_contador);
		error = MediaPlayer.create(this, R.raw.error);
        correcto = MediaPlayer.create(this, R.raw.acierto);
        btnSiguiente = findViewById(R.id.btn_Siguiente);
        listaOpciones.ensureCapacity(4); //Le indicamos el numero de posiciones para ahorrar recursos

        btnSiguiente.setEnabled(false); //Por defecto no puede pasar la pregunta

        String categoria="";
        String tablaCat="";

        //Parametros traídos de la actividad anterior a través del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoria = extras.getString("categoria");
            tablaCat = extras.getString("tablaCat");
            tvCategoria.setText(categoria);
            intentPractica = extras.getBoolean("intentPractica");
            partida = (Partida) getIntent().getSerializableExtra("partida");
        }else Log.e("Bundle extras", "null");

        String url = Locale.getDefault().getLanguage(); //Obtenemos el lenguaje del usuario

        //Comprobamos si el usuario navega en inglés para añadir el idioma a las tablas que se buscaran en la DB
        if(url.equalsIgnoreCase("en")) url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat+"en";
        else url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat;

        buscarDatos(url); //Llamamos al método de la consulta a la DB
    }

    //TIEMPO
//    public void startTimer(){
//        countDownTimer = new CountDownTimer(timeMili, 1000){
//            @Override
//            public void onTick(long millisUntilFinished) {
//                tiempo.setText(String.valueOf((millisUntilFinished / 1000)));
//            }
//            @Override
//            public void onFinish() {
//
//            }
//        }.start();
//        timeRunning=true;
//    }

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

                        int nResultados = response.length()-1; //Total de registros, le restamos 1 ya que la DB empieza en 0 y no en 1

                        if(intentPractica){ //SI ES EL MODO ENTRENAMIENTO
                            if(i==0) cincoPreguntasAleatorias(nResultados); //Generamos las 5 entradas aleatorias

                            for (int j=0; j<pos5Preguntas.length; j++){
                                if (i==pos5Preguntas[j]) quizLocal(jsonObject); //Metodo para guardar la pregunta completa
                            }
                        }
                        //-------------------------------------------------------------------------------------------
                        else{ //quizRuleta
                            if(i==0){
                                numAleatorio = (int) Math.floor(Math.random()*(nResultados-0+1)+0);  // Valor entre M y N, ambos incluidos.
                            }
                            if(i == numAleatorio) quizRuleta(jsonObject);
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
            tvContador.setText((qCont+1)+"/5");
        }
        else{ //quizRuleta
            tvPregunta.setText(preguntaCompleta[1]);
            mostarOpcionesAleatorias();
        }
    }

    //Obtenemos las posiciones de las 5 preguntas a mostrar en QLocal
    public void cincoPreguntasAleatorias(int numRegistros){
        int contador=0;
        do{
            boolean encontrado=false;
            numAleatorio = (int) Math.floor(Math.random()*(numRegistros-0+1)+0);  // Valor entre M y N, ambos incluidos.
            for(int i=0; i<pos5Preguntas.length; i++){
                if (pos5Preguntas[i]==numAleatorio){
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado){
                pos5Preguntas[contador]=numAleatorio;
                contador++;
            }
        }while(contador < 5);
        Arrays.sort(pos5Preguntas);
        String contenido="";
    }

    //Metodo si venimos del activity de QLocal
    public void quizLocal(JSONObject jsonObject) throws JSONException {

        qPractica[contadorPregunta][0] = jsonObject.getString("id");
        qPractica[contadorPregunta][1] = jsonObject.getString("pregunta");
        qPractica[contadorPregunta][2] = jsonObject.getString("respuesta");
        qPractica[contadorPregunta][3] = jsonObject.getString("incorrecta1");
        qPractica[contadorPregunta][4] = jsonObject.getString("incorrecta2");
        qPractica[contadorPregunta][5] = jsonObject.getString("incorrecta3");
        qPractica[contadorPregunta][6] = jsonObject.getString("categoria");

        contadorPregunta++;
        if (contadorPregunta==5) contadorPregunta=0; //Si ya ha llegado a 5 (cumplido su cometido) restablecemos la variable a 0.
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
    public void validarOpciones(View view) throws InterruptedException {
        int id = view.getId();
        switch (id) {
            case R.id.tv_opcion1:
                if (!respondido) { //Si no ha seleccionado ninguna respuesta antes...
                    btnSiguiente.setEnabled(true); //Activamos el botón Siguiente
                    btnSiguiente.setImageDrawable(ContextCompat.getDrawable(Quiz.this, R.drawable.next)); //Mostramos aspecto activado
                    if (tvOpcion1.getText().toString().equals(respuestaCorrecta)) {
                        tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta); //Verde
                        if (Ajustes.efectosEncendidos) correcto.start();
                        acierto=true;
                    } else {
                        tvOpcion1.setBackgroundResource(R.color.colorRespuestaIncorrecta); //Rojo
                        if (Ajustes.efectosEncendidos) error.start();
                        if (tvOpcion2.getText().toString().equals(respuestaCorrecta))
                            tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if (tvOpcion3.getText().toString().equals(respuestaCorrecta))
                            tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido = true;
                }
                break;

            case R.id.tv_opcion2:
                if (!respondido) { //Si no ha seleccionado ninguna respuesta antes...
                    btnSiguiente.setEnabled(true); //Activamos el botón Siguiente
                    btnSiguiente.setImageDrawable(ContextCompat.getDrawable(Quiz.this, R.drawable.next)); //Mostramos aspecto activado

                    if (tvOpcion2.getText().toString().equals(respuestaCorrecta)) {
                        tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        if (Ajustes.efectosEncendidos) correcto.start();
                        acierto=true;
                    } else {
                        tvOpcion2.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        if (Ajustes.efectosEncendidos) error.start();
                        if (tvOpcion1.getText().toString().equals(respuestaCorrecta))
                            tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if (tvOpcion3.getText().toString().equals(respuestaCorrecta))
                            tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido = true;
                }
                break;

            case R.id.tv_opcion3:
                if (!respondido) { //Si no ha seleccionado ninguna respuesta antes...
                    btnSiguiente.setEnabled(true); //Activamos el botón Siguiente
                    btnSiguiente.setImageDrawable(ContextCompat.getDrawable(Quiz.this, R.drawable.next)); //Mostramos aspecto activado

                    if (tvOpcion3.getText().toString().equals(respuestaCorrecta)) {
                        tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        acierto=true;
                        if (Ajustes.efectosEncendidos) correcto.start();
                    } else {
                        tvOpcion3.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        if (Ajustes.efectosEncendidos) error.start();
                        if (tvOpcion1.getText().toString().equals(respuestaCorrecta))
                            tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if (tvOpcion2.getText().toString().equals(respuestaCorrecta))
                            tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido = true;
                }
                break;

            case R.id.tv_opcion4:
                if (!respondido) { //Si no ha seleccionado ninguna respuesta antes...
                    btnSiguiente.setEnabled(true); //Activamos el botón Siguiente
                    btnSiguiente.setImageDrawable(ContextCompat.getDrawable(Quiz.this, R.drawable.next)); //Mostramos aspecto activado

                    if (tvOpcion4.getText().toString().equals(respuestaCorrecta)) {
                        tvOpcion4.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        acierto=true;
                        if (Ajustes.efectosEncendidos) correcto.start();
                    } else {
                        tvOpcion4.setBackgroundResource(R.color.colorRespuestaIncorrecta);
                        if (Ajustes.efectosEncendidos) error.start();
                        if (tvOpcion1.getText().toString().equals(respuestaCorrecta))
                            tvOpcion1.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else if (tvOpcion2.getText().toString().equals(respuestaCorrecta))
                            tvOpcion2.setBackgroundResource(R.color.colorRespuestaCorrecta);
                        else tvOpcion3.setBackgroundResource(R.color.colorRespuestaCorrecta);
                    }
                    respondido = true;
                }
                break;
            }

    }

    public void mostarOpcionesAleatorias(){

        numAleatorio = (int) Math.floor(Math.random()*((listaOpciones.size()-1)-0+1)+0);  // Genera un numero aleatorio entre 0 y el tamaño del ArrayList.
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
        if (intentPractica){ //Si estamos en modo practica
            if (acierto){
                aciertos++;
                acierto=false;
            }
            if (qCont<4){
                btnSiguiente.setEnabled(false);
                btnSiguiente.setImageDrawable(ContextCompat.getDrawable(Quiz.this, R.drawable.next_disable)); //Mostramos aspecto desactivado
                respondido=false;
                //Restablecemos el color natural de las respuestas (Eliminamos el verde y rojo del acierto-fallo)
                tvOpcion1.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.bordes));
                tvOpcion2.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.bordes));
                tvOpcion3.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.bordes));
                tvOpcion4.setBackground(ContextCompat.getDrawable(Quiz.this, R.drawable.bordes));
                mostrarPregunta();
                //startTimer();

            }else mostrarPuntuacion(); //si no quedan más preguntas mostramos puntuación
        }else{ //Si estamos en partida de Ruleta
            if (respondido){
                if (acierto){ //Si la pregunta es acertada
                    acierto=false;
                    if (partida.turno == 1){
                        partida.j1.setPreguntasAcertadas(); //Se suma un acierto
                        if (preguntaCompleta[6] != null) { //Validamos que en el momento del Parsear el Integer no sea nulo
                            try {
                                partida.j1.setQuesitos(Integer.parseInt(preguntaCompleta[6]), true); //Se añade quesito
                            } catch(NumberFormatException e) {
                                Log.e("Integer.parseInt","NumberFormatException, debe estar recibiendo un id Nulo, revisar");
                            }
                        }
                    }
                    else{
                        partida.j2.setPreguntasAcertadas(); //Se suma un acierto
                        if (preguntaCompleta[6] != null) { //Validamos que en el momento del Parsear el Integer no sea nulo
                            try {
                                partida.j2.setQuesitos(Integer.parseInt(preguntaCompleta[6]), true); //Se añade quesito
                            } catch(NumberFormatException e) {
                                Log.e("Integer.parseInt","NumberFormatException, debe estar recibiendo un id Nulo, revisar");
                            }
                        }
                    }

                }else{ //Si falla la pregunta
                    partida.continuaTurno = false;
                    if (partida.turno == 1){
                        partida.turno = 2;  //Cambia el turno
                        partida.j1.setPreguntasFalladas(); //Se suma un fallo
                        partida.j1.setQuesitos(Integer.parseInt(preguntaCompleta[6]), false); //Se quita quesito
                    }
                    else{
                        partida.turno = 1; //Cambia el turno
                        partida.j2.setPreguntasFalladas(); //Se suma un fallo
                        partida.j2.setQuesitos(Integer.parseInt(preguntaCompleta[6]), false); //Se quita quesito
                    }
                }
                Intent intent = new Intent(this, Ruleta.class);
                intent.putExtra("partida",partida);
                startActivity(intent);
                finish();
            }
        }
    }

    private void mostrarPuntuacion() {
        String mensajeAlert="Aciertos: "+aciertos+"\nFallos: "+(5-aciertos);
        String tituloAlert ="Resultado del entrenamiento";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(mensajeAlert)
                .setTitle(tituloAlert)
                .setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Quiz.this, QLocal.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}