package com.dam2.trivial_it;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PruebaBD extends AppCompatActivity {

    EditText edtnick, aciertos, fallos;
    Button buscar, editar, agregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_b_d);
        edtnick=findViewById(R.id.buscarUsuario);



    }
    /*
    queue = Volley.newRequestQueue(this);

     //l.ejecutarServicio("https://trivialit2021.000webhostapp.com/buscar.php?nick"+l.edtUsuario);
                                                             OBTENER DATOS
    private void obtenerDatos(){
        String url="https://trivialit2021.000webhostapp.com/datosUsuarios.php";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray JsonArray = response.getJSONArray("usuarios");

                    for(int i=0; i<JsonArray.length();i++) {
                        JSONObject JsonObject = JsonArray.getJSONObject(i);
                        String nick = JsonObject.getString("nick");
                        nombre.setText(nick);
                        Toast.makeText(Estadisticas.this, "Nombre: "+nick, Toast.LENGTH_SHORT).show();

                    }
                }catch(JSONException e){}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);


                                                                    !!!!!!!!!!!!  BUSCAR DATOS!!!!!!!
        private void buscarDatos(String URL){
        JsonArrayRequest JsonArrayRequest=new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre.setText(jsonObject.getString("nick"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(JsonArrayRequest);
    }

     */

}