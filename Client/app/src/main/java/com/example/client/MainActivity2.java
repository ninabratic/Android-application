package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.Film;
import Model.Kupovina;

public class MainActivity2 extends AppCompatActivity {

    private TextView mtextView;
    private User user;
    private TableLayout kontejner;
    private Kupovina kupovina;
    private List<Film> uklonjeni = new ArrayList<>();
    private List<Integer> kliknuta = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        kontejner = findViewById(R.id.kontejner);
        mtextView = findViewById(R.id.textView);
        Intent intent = getIntent();
        User u = intent.getParcelableExtra("user");
        user = u;
        Kupovina shop = intent.getParcelableExtra("kupovina");
        Log.e("kupovina", shop.getFilmovi().size()+"");
        kupovina = shop;
        mtextView.setText("Ukupno: " +String.valueOf(kupovina.getUkupno()));



        kliknuta = intent.getIntegerArrayListExtra("kliknuta");
        Button kupi = new Button(this);
        kupi.setText("KUPI");


        for(int i = 0; i < kupovina.getFilmovi().size(); i++){
            TableRow tr = new TableRow(this);
            TextView naziv = new TextView(this);
            ImageView img = new ImageView(this);
            TextView cena = new TextView(this);
            Button btn = new Button(this);
            btn.setText("Izbaci iz korpe");
            btn.setId(i);


            int finalI = i;
            btn.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View v)
                {
                    izbaciIzKorpe(kupovina.getFilmovi().get(finalI), btn, kupi);
                }
            });

            naziv.setText(kupovina.getFilmovi().get(i).getNaziv());
//            double price = kupovina.getFilmovi().get(i).getCena());
            cena.setText(String.format("%.2f",kupovina.getFilmovi().get(i).getCena()));

            tr.setGravity(Gravity.CENTER_HORIZONTAL);
            naziv.setWidth(100);
            tr.addView(naziv);
            tr.addView(cena);
            tr.addView(btn);
            kontejner.addView(tr);
        }
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        TextInputEditText email = new TextInputEditText(this);
        email.setHint("EMAIL");
        email.setWidth(300);

        ll.addView(email);
        ll.addView(kupi);
        kupi.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v)
            {
                Log.e("OVO", "poslao zahtev");
                plati(kupi, email);
            }
        });
        if(kupovina.getFilmovi().size() == 0){
            kupi.setEnabled(false);
        }
        kontejner.addView(ll);
//        kliknuta = new ArrayList<>();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public void plati(Button btn, TextInputEditText email){
        if(kupovina.getFilmovi().size() == 0){
            btn.setEnabled(false);
        }
        Log.e("KUPI", "USAO" );

        String emailAdresa =email.getText().toString();
        if(!validate(emailAdresa)){
            DialogFragment dialog = new MyDialogFragment();
            dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
        }

        volleyPost(email.getText().toString());
    }

    public void izbaciIzKorpe(Film film, Button btn, Button kupi){

        kupovina.getFilmovi().remove(film);
        kupovina.setUkupno(kupovina.getUkupno() - film.getCena());
//        btn.setText("Dodaj u korpu");
        btn.setEnabled(false);
        uklonjeni.add(film);
        kliknuta.remove(btn.getId());
        Log.e("izbaciIzKorpe: ",""+kliknuta.size() );
        if(kupovina.getFilmovi().size() == 0){
            kupi.setEnabled(false);
        }
        mtextView.setText("Ukupno: " +String.valueOf(kupovina.getUkupno()));

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass( this,MainActivity.class);
        intent.putExtra("kupovina", kupovina);
        intent.putExtra("kliknuta", (ArrayList<Integer>)kliknuta);
        startActivity(intent);
    }

        String postUrl = "http://10.0.2.2:8081/kupi";
    public void volleyPost(String email){
        Log.e("OVO", "volleyPost");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("ukupno", kupovina.getUkupno());
            postData.put("ukupnoFilmova", kupovina.getFilmovi().size());
            postData.put("email", email);
            String filmovi = "";
            for(int i = 0; i <  kupovina.getFilmovi().size(); i++){

                filmovi += kupovina.getFilmovi().get(i).getNaziv();
                if(i+1 != kupovina.getFilmovi().size()){
                    filmovi +=", ";
                }
            }
            postData.put("filmovi", filmovi);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("asd",postData.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                System.out.println(response);
                DialogFragment pe = new PoslatEmail();
                pe.show(getSupportFragmentManager(), "MyDialogFragmentTag");
            }

//

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        Log.e("OVO", "volleyPost12");

        requestQueue.add(jsonObjectRequest);

    }

    public static class MyDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("App Title");
            builder.setMessage("Invalid email adress");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just
                    // want it dismissed when clicked
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    public static class PoslatEmail extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("App Title");
            builder.setMessage("Uspesno ste kupili filmove");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // You don't have to do anything here if you just
                    // want it dismissed when clicked
                }
            });

            // Create the AlertDialog object and return it
            return builder.create();
        }

    }
}

