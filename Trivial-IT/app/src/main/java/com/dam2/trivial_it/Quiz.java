package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Quiz extends AppCompatActivity {

    TextView tvCategoria, tvPregunta, tvOpcion1, tvOpcion2, tvOpcion3, tvOpcion4, tvContador;
    //Button btnSiguiente, btnAbandonar;
    RequestQueue requestQueue;
    String[][] qPractica = new String[11][7];
    int qCont=-1;
    Boolean intentPractica=null;
    int numAleatorio=-1;
    MediaPlayer error;
    MediaPlayer correcto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        error = MediaPlayer.create(this, R.raw.error);
        correcto = MediaPlayer.create(this, R.raw.acierto);

        //btnSiguiente = findViewById(R.id.btn_siguiente);
        //btnAbandonar = findViewById(R.id.btn_abandonar);
        tvCategoria = findViewById(R.id.tv_categoria);
        tvPregunta = findViewById(R.id.tv_pregunta);
        tvOpcion1 = findViewById(R.id.tv_opcion1);
        tvOpcion2 = findViewById(R.id.tv_opcion2);
        tvOpcion3 = findViewById(R.id.tv_opcion3);
        tvOpcion4 = findViewById(R.id.tv_opcion4);
        tvContador = findViewById(R.id.tv_contador);

        String categoria="";
        String tablaCat="";

        //Parametros traídos de la actividad anterior a través del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            categoria = extras.getString("categoria");
            tablaCat = extras.getString("tablaCat");
            tvCategoria.setText(categoria);
            intentPractica = extras.getBoolean("intentPractica");
            Log.e("Bundle extras if", categoria);
            Log.e("intentPractica", ""+intentPractica);
           // if(!intentPractica) btnSiguiente.setText("Continuar");

        }else Log.e("Bundle extras", "null");

        addIndicesqPractica(); //Añadimos los indices al array para el modo practica

        String url = Locale.getDefault().getLanguage(); //Obtenemos el lenguaje del usuario

        if(url.equalsIgnoreCase("en")) url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat+"en";
        else url = "http://redcapcrd.es/trivialit/find.php?id="+tablaCat;

        buscarDatos(url);
        qCont=0;

    }

    //Añadimos indices al array
    private void addIndicesqPractica() {
        qPractica[0][0]= "id";
        qPractica[0][1]= "pregunta";
        qPractica[0][2]= "respuesta";
        qPractica[0][3]= "incorrecta1";
        qPractica[0][4]= "incorrecta2";
        qPractica[0][5]= "incorrecta3";
        qPractica[0][6]= "categoria";
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

                        if(intentPractica){ //quizLocal
                            if(i<5) {
                                quizLocal(i, jsonObject); //Si es el modo practica se ejecuta este método
                            }
                        }
                        else{ //quizRuleta
                            if(i==0){
                                int nResultados = response.length();
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
                Log.e("mostrarPregunta","OK");
                mostrarPregunta();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Ocurrió un error.", Toast.LENGTH_LONG).show();
                Log.e("onErrorResponse", "hubo un error: public void onErrorResponse(VolleyError error) {");
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //Printamos en la interfaz los resultados de la busqueda a la DB
    private void mostrarPregunta() {

        if(intentPractica){ //quizLocal
            qCont++;
            tvPregunta.setText(qPractica[qCont][1]);
            tvOpcion1.setText(qPractica[qCont][2]);
            tvOpcion2.setText(qPractica[qCont][3]);
            tvOpcion3.setText(qPractica[qCont][4]);
            tvOpcion4.setText(qPractica[qCont][5]);
            tvContador.setText(qCont+"/10");
            Log.e("contador: ",""+qCont);
        }

        else{ //quizRuleta
            tvPregunta.setText(qPractica[1][1]);
            tvOpcion1.setText(qPractica[1][2]);
            tvOpcion2.setText(qPractica[1][3]);
            tvOpcion3.setText(qPractica[1][4]);
            tvOpcion4.setText(qPractica[1][5]);
        }
    }

   /* public void btnNext(View view) {
        if(qCont<=10){
            if (qCont==9) btnSiguiente.setEnabled(false);
            mostrarPregunta();
        }
    }
    public void btnAtras(View view) {
        if (qCont==10) btnSiguiente.setEnabled(true);
        qCont-=2;
        Intent i = new Intent(this, Ruleta.class);
        startActivity(i);
    }
*/
    //Metodo de prueba - PRESCINDIBLE
    public void mostrarArray(){
        String row=""+qPractica.length; // 11
        String col=""+qPractica[0].length; // 7
        Log.e("length", "row="+row+" col="+col);

        for(int i=0; i<qPractica.length; i++){
            for(int j=0; j<qPractica[i].length; j++){
                Log.e("qPractica["+i+"]["+j+"]",""+qPractica[i][j]);
            }
        }
    }

    //Metodo si venimos del activity de practica
    public void quizLocal(int i, JSONObject jsonObject) throws JSONException {
        qPractica[i + 1][0] = jsonObject.getString("id");
        qPractica[i + 1][1] = jsonObject.getString("pregunta");
        qPractica[i + 1][2] = jsonObject.getString("respuesta");
        qPractica[i + 1][3] = jsonObject.getString("incorrecta1");
        qPractica[i + 1][4] = jsonObject.getString("incorrecta2");
        qPractica[i + 1][5] = jsonObject.getString("incorrecta3");
        qPractica[i + 1][6] = jsonObject.getString("categoria");
    }

    public void quizRuleta(JSONObject jsonObject) throws JSONException {
        qPractica[1][0] = jsonObject.getString("id");
        qPractica[1][1] = jsonObject.getString("pregunta");
        qPractica[1][2] = jsonObject.getString("respuesta");
        qPractica[1][3] = jsonObject.getString("incorrecta1");
        qPractica[1][4] = jsonObject.getString("incorrecta2");
        qPractica[1][5] = jsonObject.getString("incorrecta3");
        qPractica[1][6] = jsonObject.getString("categoria");
    }

    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void validarOpciones(View view) {
        int id = view.getId();

        switch (id){
            case R.id.tv_opcion1: //Base de datos
                //if(tvOpcion1.getText().toString().equals(qPractica[1][2]))
                    tvOpcion1.setBackgroundColor(Color.parseColor("#63F311"));
                    correcto.start();

                break;
            case R.id.tv_opcion2: //Programación
                tvOpcion2.setBackgroundColor(Color.parseColor("#F32611"));
                tvOpcion1.setBackgroundColor(Color.parseColor("#63F311"));
                error.start();
                break;
            case R.id.tv_opcion3: //Programación Web
                tvOpcion3.setBackgroundColor(Color.parseColor("#F32611"));
                tvOpcion1.setBackgroundColor(Color.parseColor("#63F311"));
                error.start();
                break;
            case R.id.tv_opcion4: //Sistemas informáticos
                tvOpcion4.setBackgroundColor(Color.parseColor("#F32611"));
                tvOpcion1.setBackgroundColor(Color.parseColor("#63F311"));
                error.start();
                break;
        }

        tvOpcion1.setText(qPractica[1][2]);
        tvOpcion2.setText(qPractica[1][3]);
        tvOpcion3.setText(qPractica[1][4]);
        tvOpcion4.setText(qPractica[1][5]);
    }
}