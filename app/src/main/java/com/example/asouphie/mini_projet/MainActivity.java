package com.example.asouphie.mini_projet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText titreSearch;
    private RadioButton films;
    private RadioButton series;
    public static Map<Integer, String> listeGenre = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Me permet d'initialiser listeGenre pour l'utiliser par la suite dans toute l'activité.
        searchIdGenres();

        titreSearch = (EditText) findViewById(R.id.editText);
        //Si je valide mon action directement du clavier
        titreSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent versFilmsView = implementIntent();
                if (actionId == EditorInfo.IME_ACTION_DONE && versFilmsView != null) {
                    startActivity(versFilmsView);
                }
                return false;
            }
        });

        //Sinon, si je clique directement sur le bouton rechercher.
        Button b = (Button) findViewById(R.id.search);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent versFilmsView = implementIntent();
                if (versFilmsView != null) {
                    startActivity(versFilmsView);
                }
            }
        });
    }

    /**
     * Permet d'implémenter notre Intent avec "titre" et "type" en extra.
     *
     * @return Intent implémenté pour lancer l'activité.
     */

    private Intent implementIntent() {
        //Je récupère la valeur des radiosButton
        films = (RadioButton) findViewById(R.id.radioButton);
        series =(RadioButton) findViewById(R.id.radioButton2);

        //Je créer mon Intent qui va me servir pour lancer ma deuxième activité et je l'initialise
        Intent versFilmsView = new Intent(MainActivity.this, ViewFilms.class);

        //Je vérifie que le titre n'est pas vide
        if(titreSearch.length() > 0) {
            versFilmsView.putExtra("titre", String.valueOf(titreSearch.getText()));
        } else {
            Toast.makeText(getApplicationContext(),
                    "Veuillez renseigner un titre de médias à rechercher.",
                    Toast.LENGTH_SHORT).show();
            return null;
        }

        //Pour le type à retourner :
        if (films.isChecked() && !series.isChecked()) { //On retourne seulement les films
            versFilmsView.putExtra("type", 1);
        } else if (!films.isChecked() && series.isChecked()) { //On retourne seulement les séries
            versFilmsView.putExtra("type", 0);
        //Je retourne un message d'erreur et ne lance pas l'activité si les deux ou aucunes des deux n'est selectionné.
        } else {
            Toast.makeText(getApplicationContext(),
                    "Veuillez renseigner un type de médias à rechercher.",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        return versFilmsView;
    }

    /**
     * Utilisé pour initialisé la map listeGenres pour les autres activités.
     */

    private void searchIdGenres() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlBase = "https://api.themoviedb.org/3/genre/";
        String urlFin = "/list?language=fr-FR&api_key=" + ViewFilms.KEY;

        String urlMovie = urlBase+"movie"+urlFin;
        String urlSerie = urlBase+"tv"+urlFin;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray jsonMap = json.getJSONArray("genres");
                    for(int i = 0; i < jsonMap.length(); i++) {
                        JSONObject obj = jsonMap.getJSONObject(i);
                        MainActivity.listeGenre.put((Integer) obj.get("id"), (String) obj.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "erreur dans la requête.", Toast.LENGTH_SHORT).show();
            }
        };
        //Je vais chercher les id et leur nom pour les séries et pour les movies
        //Car elles ont pas mal d'id en communs, certains sont differents, mais ne possède pas le même id entre série et film.
        StringRequest movieRequest = new StringRequest(Request.Method.GET, urlMovie, responseListener, errorListener );
        StringRequest serieRequest = new StringRequest(Request.Method.GET, urlSerie, responseListener, errorListener );

        queue.add(movieRequest);
        queue.add(serieRequest);
    }
}
