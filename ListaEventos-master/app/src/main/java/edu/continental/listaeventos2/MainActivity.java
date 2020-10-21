package edu.continental.listaeventos2;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import edu.continental.listaeventos2.Adaptador.EventosAdapter;
import edu.continental.listaeventos2.Entidades.Evento;

public class MainActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>{

    RecyclerView recyclerEventos;
    ArrayList<Evento> listaEventos;
    ProgressDialog progreso;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listaEventos=new ArrayList<>();
        recyclerEventos=findViewById(R.id.rvEventos);
        recyclerEventos.setLayoutManager(new LinearLayoutManager(this));
        recyclerEventos.setHasFixedSize(true);
        EventosAdapter adapter=new EventosAdapter(listaEventos);
        recyclerEventos.setAdapter(adapter);

        request= Volley.newRequestQueue(this);

        cargarWebService();

    }

    private void cargarWebService() {
        progreso=new ProgressDialog(this);
        progreso.setMessage("Conectando");
        progreso.show();
        String url="http://smartcityhuancayo.herokuapp.com/Evento/List_Evento.php";
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
        Log.i("Error",error.toString());
        progreso.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        Evento evento=null;
        JSONArray json=response.optJSONArray("records");
        try{
            for(int i=0;i<json.length();i++){
                evento=new Evento();
                JSONObject jsonObject=null;
                jsonObject=json.getJSONObject(i);
                evento.setEVE_Nombres(jsonObject.optString("EVE_Nombres"));
                evento.setEVE_Descripcion(jsonObject.optString("EVE_Descripcion"));
                // evento.setIddistrito(jsonObject.optString("ID_Distrito"));
                // evento.setDistrito(jsonObject.optString("Distrito"));
                evento.setEVE_Detalles(jsonObject.optString("EVE_Detalles"));
                //evento.setFotografia(jsonObject.optString("EVE_Fotografia"));
               // evento.setFechahora(jsonObject.optString("EVE_Fecha_Hora"));
               // evento.setLongitud(jsonObject.optString("EVE_Longitud"));
              //  evento.setLatitud(jsonObject.optString("EVE_Latitud"));
                listaEventos.add(evento);
            }
            progreso.hide();
            EventosAdapter adapter=new EventosAdapter(listaEventos);
            recyclerEventos.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(this, "No se pudo establecer la conexion con el servidor", Toast.LENGTH_SHORT).show();
            progreso.hide();
        }
    }
}