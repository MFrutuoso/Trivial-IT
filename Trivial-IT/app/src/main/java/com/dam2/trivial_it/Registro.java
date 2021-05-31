package com.dam2.trivial_it;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {
        EditText  etRegistroNick, etRegistroPass, etRegistroPass2;
        Button btnConfRegistro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        etRegistroNick=(EditText)findViewById(R.id.etRegistroNick);
        etRegistroPass=(EditText)findViewById(R.id.etRegistroPass);
        etRegistroPass2=(EditText)findViewById(R.id.etRegistroPass2);
        btnConfRegistro=(Button) findViewById(R.id.btnConfRegistro);

        btnConfRegistro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(etRegistroPass.getText().toString().equals(etRegistroPass2.getText().toString())) {
                    ejecutarRegistro("https://trivialit2021.000webhostapp.com/nuevoUsuario.php");
                        Intent IRegistro = new Intent(Registro.this, Login.class);
                        startActivity(IRegistro);
                }else{
                    Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ejecutarRegistro(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "¡REGISTRO COMPLETADO!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros=new HashMap<String, String>();
                parametros.put("nick", etRegistroNick.getText().toString());
                parametros.put("pass", etRegistroPass.getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}