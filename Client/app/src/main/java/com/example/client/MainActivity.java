package com.example.client;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import Model.Film;
import Model.Kupovina;


public class MainActivity extends AppCompatActivity {

    private TableLayout kontejner;
    private TextView mTextView;
    private Kupovina kupovina;
    private TextView mUkupno;
    private User user;
    private List<String> neaktivnaDugmad;
    private List<Button> buttons = new ArrayList<>();
    private List<Integer> ids = new ArrayList<Integer>();
    private List<Integer> kliknuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View containter = findViewById( R.id.kontejner );
        /*DINAMICKO DODAVANJE ELEMENATA*/
        kontejner = findViewById(R.id.kontejner);
        mUkupno = findViewById(R.id.ukupno);

        user = new User();
        user.setUsername("Nicka");
        user.setPassword("12345");


        getData();
        Intent intent = getIntent();
        kupovina = intent.getParcelableExtra("kupovina");
        kliknuta = intent.getIntegerArrayListExtra("kliknuta");

        if(kupovina == null){
            kupovina = new Kupovina();
        }
        if(kliknuta == null){
            kliknuta = new ArrayList<>();
        }
        mUkupno.setText(String.valueOf(kupovina.getUkupno()));


    }

    private void getData(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //  String url ="https://www.google.com";
        String url = "http://10.0.2.2:8081/image";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String tekst = null;
                        String slika = null;
                        String ukupno = null;
                        Double cena = 0.0;

                        try {
                            ukupno = jsonResponse.getString("ukupno");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < Integer.parseInt(ukupno); i++){
                            try {
                                slika = jsonResponse.getString("slika"+i);
                                tekst = jsonResponse.getString("tekst"+i);
                                cena = Double.parseDouble(jsonResponse.getString("cena"+i));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            byte[] decodedString = Base64.decode(slika, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            TableRow tr = new TableRow(MainActivity.this);

                            TextView text = new TextView(MainActivity.this);
                            text.setText(tekst);
                            tr.addView(text);
//                            kontejner.addView(text);

                            ImageView img = new ImageView(MainActivity.this);
                            img.setImageBitmap(decodedByte);
                            tr.addView(img);

                            Button btn = new Button(MainActivity.this);
                            btn.setId(i);
                            if(kliknuta.size() !=0){
                                for (int j = 0; j < kliknuta.size(); j++){
                                    if(kliknuta.get(j) == i){
                                        btn.setEnabled(false);
                                    }
                                }
                            }
                            Log.e("ID:", btn.getId()+"");
                            LinearLayout ll = new LinearLayout(MainActivity.this);
                            ll.setOrientation(LinearLayout.VERTICAL);
                            TextView price = new TextView(MainActivity.this);
                            price.setText("Cena: "+String.format("%.2f", cena));

                            btn.setText("Dodaj u korpu");
                            Film film = new Film();
                            film.setCena(cena);
                            film.setNaziv(tekst);
                            film.setSlika(slika);
                            String finalTekst = tekst;
                            btn.setOnClickListener (new View.OnClickListener () {
                                @Override
                                public void onClick (View v)
                                {
                                    btn.setEnabled(false);
//                                    Log.e("nesto",neaktivnaDugmad.size()+"");
                                    kliknuta.add(btn.getId());
//                                    Log.e("nesto",neaktivnaDugmad.size()+"");
                                    racun(film);

                                }
                            });
                            buttons.add(btn);
                            ids.add(i);
                            ll.addView(price);
                            ll.addView(btn);

                            tr.addView(ll);
                            kontejner.addView(tr);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!"+ error.getMessage());
            }
        });


        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void noviProzor(){
        Intent intent = new Intent();
        intent.setClass( this,MainActivity2.class);
        intent.putExtra("user", user);
        intent.putExtra("kupovina", kupovina);
        Log.e("kupovina", kupovina.getFilmovi().size()+"");
        intent.putExtra("kliknuta", (ArrayList<Integer>) kliknuta);
//        intent.putExtra("kliknuta", (ArrayList<S>) buttons);
//        Log.e("idemo", neaktivnaDugmad.toString());
//        Log.e("nesto",neaktivnaDugmad.size()+"");
//        mUkupno.setText(neaktivnaDugmad.toString());
//        intent.putExtra()
        startActivity(intent);
    }

    private void racun(Film film){
        mUkupno = findViewById(R.id.ukupno);
        double ukupno = Double.parseDouble(String.valueOf(mUkupno.getText()));
        ukupno += film.getCena();
        kupovina.getFilmovi().add(film);
        kupovina.setUkupno(ukupno);
        kupovina.setKorisnik(user);
        mUkupno.setText(String.format("%.2f",ukupno));
    }

//    public void getImage(View view, int a) {
//        mTextViewResult1 = findViewById(R.id.text_view_result1);
//        mTextViewResult.setText("RADI");
//    }

    public void noviProzor(View view) {
        noviProzor();
    }
}