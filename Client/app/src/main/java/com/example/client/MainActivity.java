package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;

import Model.Film;
import Model.Kupovina;


public class MainActivity extends AppCompatActivity {

    private TableLayout kontejner;
    private ScrollView layout;
    private TextView mTextView;
    private Kupovina kupovina;
    private TextView mUkupno;

    private List<String> neaktivnaDugmad;
    private List<Button> buttons = new ArrayList<>();
    private List<Integer> ids = new ArrayList<Integer>();
    private List<Integer> kliknuta;
    private String http = "http://";
    private String adress = "10.0.2.2"; //:8081/image
    private final String[] port = new String[1];
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        port[0] = "8081/image";


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View containter = findViewById( R.id.kontejner );
        layout = findViewById(R.id.scrollView);
        /*DINAMICKO DODAVANJE ELEMENATA*/
        kontejner = findViewById(R.id.kontejner);
        mUkupno = findViewById(R.id.ukupno);


        url = http+ adress +":"+port[0];
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


        Log.e("url", port[0] );



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("ResourceType")
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
                        TableRow del1 = findViewById( -10 );
                        kontejner.removeView(del1);
                        TableRow del2 = findViewById( -20 );
                        kontejner.removeView(del2);

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
                            tr.setId(i);

                            TextView text = new TextView(MainActivity.this);
                            text.setTextColor(Color.RED);
                            text.setTextSize(18);
                            text.setText(tekst);
                            tr.addView(text);
//                            kontejner.addView(text);


                            Bitmap bMapScaled = Bitmap.createScaledBitmap(decodedByte, 250, 250, true);

                            ImageView img = new ImageView(MainActivity.this);
                            img.setImageBitmap(bMapScaled);
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
                        TableRow tr1 = new TableRow(MainActivity.this);
                        TableRow tr2 = new TableRow(MainActivity.this);
                        TextInputEditText serverIP = new TextInputEditText(MainActivity.this);
                        Button saveIp = new Button(MainActivity.this);
                        TextInputEditText serverPort = new TextInputEditText(MainActivity.this);
                        Button savePort = new Button(MainActivity.this);

                        String finalUkupno = ukupno;
                        savePort.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v)
                            {
                                port[0] = serverPort.getText().toString();
                                Log.e("UCITANI PORT", port[0].toString() );
                                url = "";
                                url = http+ adress +":"+port[0];
                                Log.e("URLLLLL", url );
//                                layout.removeView(kontejner);
//                                kontejner = new TableLayout(MainActivity.this);
//                                layout.addView(kontejner);
                                for (int i = 0; i < Integer.parseInt(finalUkupno); i++){
                                    TableRow tr = findViewById( i );
                                    kontejner.removeView(tr);
                                }
                                getData();
                            }
                        });

                        saveIp.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v)
                            {
                                adress = serverIP.getText().toString();
                                Log.e("UCITANI PORT", port[0].toString() );
                                url = "";
                                url = http+ adress +":"+port[0];
                                Log.e("URLLLLL", url );
                                for (int i = 0; i < Integer.parseInt(finalUkupno); i++){
                                    TableRow tr = findViewById( i );
                                    kontejner.removeView(tr);
                                }
                                getData();
                            }
                        });

                        savePort.setText("Set port");
                        saveIp.setText("Set IP adress");
                        serverIP.setWidth(100);
                        serverIP.setHeight(120);
                        serverPort.setHeight(120);
                        serverPort.setWidth(100);
                        TextView IPLabel = new TextView(MainActivity.this);
                        IPLabel.setText("Server IP");

                        TextView portLabel = new TextView(MainActivity.this);
                        portLabel.setText("Server port");
                        tr1.addView(IPLabel);
                        tr1.setId(-20);
                        tr1.addView(serverIP);
                        tr1.addView(saveIp);
                        tr2.addView(portLabel);
                        tr2.setId(-10);
                        tr2.addView(serverPort);
                        tr2.addView(savePort);
                        kontejner.addView(tr1);
                        kontejner.addView(tr2);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                return;
//                mTextView.setText("That didn't work!"+ error.getMessage());
            }
        });


        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private void noviProzor(){
        Intent intent = new Intent();
        intent.setClass( this,MainActivity2.class);
        intent.putExtra("kupovina", kupovina);
        Log.e("kupovina", kupovina.getFilmovi().size()+"");
        intent.putExtra("kliknuta", (ArrayList<Integer>) kliknuta);
        startActivity(intent);
    }

    private void racun(Film film){
        mUkupno = findViewById(R.id.ukupno);
        double ukupno = Double.parseDouble(String.valueOf(mUkupno.getText()));
        ukupno += film.getCena();
        kupovina.getFilmovi().add(film);
        kupovina.setUkupno(ukupno);
        mUkupno.setText(String.format("%.2f",ukupno));
    }


    public void noviProzor(View view) {
        noviProzor();
    }
}