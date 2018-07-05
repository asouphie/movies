package com.example.asouphie.mini_projet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.ArrayList;

public class ViewFilms extends AppCompatActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    public static String KEY = "45475b3d701437bed154e2d0cdbcee2b";
    private Search search = null;
    private int type;
    private String titreSearch;
    private ListView listeMovies;
    private LigneAdapter listViewAdapter;
    private TextView textView;

    private int prelast = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_films);

        textView = (TextView) findViewById(R.id.text);

        //J'initialise mon ListView et ma LigneAdapter pour la gestion du scroll par la suite.
        listeMovies = (ListView) findViewById(R.id.listMovies);
        listViewAdapter = new LigneAdapter(new ArrayList<Movie>(),this);
        listeMovies.setAdapter(listViewAdapter);
        listeMovies.setOnItemClickListener(this);
        listeMovies.setOnScrollListener(this);

        //Je vérifie que j'ai bien des valeurs dans mes extras.
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        //Si ce n'est pas vide je récupère mon titre à rechercher et mon type de recherche : Serie ou Film.
        titreSearch = extras.getString("titre");
        type = extras.getInt("type");

        searchMovie();
    }

    /**
     * Méthode réalisant une requête vers themoviedb pour récupérer les movies correspondant aux différents critères de recherches
     */

    private void searchMovie() {
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://api.themoviedb.org/3/search/";
        url += type == 1 ? "movie" : "tv";
        url += "?api_key="+KEY+"&query="+ Uri.encode(titreSearch)+"&language=fr-FR&page=";
        url += (search != null) ? (search.getPage().intValue()+1) : 1;

        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                setSearch(new Gson().fromJson(response, Search.class));

                                //Permet à l'utilisateur de l'application combien de résultats il obtient pour sa recherche.
                                //Besoin d'être mis une fois seulement, et non à chaque nouvelle requête pour l'évènement du scroll.
                                if(textView.getText().length() == 0) {
                                    textView.setText(search.getTotal_results()+" résultats pour \"" + titreSearch + "\"");
                                }

                                ajoutMovies();
                            }},
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "erreur dans la requête.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
        queue.add(stringRequest);
    }

    /**
     * Permet de modifier la valeur du search
     * En particulier pour la requête car il n'est pas possible de modifier directement
     * à l'interieur de la requête, dans la méthode onResponse.
     * @param newSearch
     */

    private void setSearch(Search newSearch) {
        this.search = newSearch;
    }

    /**
     * Permet d'ajouter des movies dans notre ligneAdapter
     */

    private void ajoutMovies() {
        for(Movie movie : search.getResults()) {
            listViewAdapter.add(movie);
        }
    }

    /**
     * Quand on clique sur un des items, on ouvre une nouvelle activité qui correspond au détail de l'item en question.
     * @param parent
     * @param view
     * @param position
     * @param id
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailFIlms.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", (Movie)parent.getItemAtPosition(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //TODO
    }

    /**
     * Lors du scroll, si on arrive en bas de la liste, on ajoute les movies de la page suivantes, et ainsi de suite
     * jusqu'à arriver à la fin des résultats.
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(view.getId() == R.id.listMovies) {
            final int lastItem = firstVisibleItem + visibleItemCount;

            if(lastItem == totalItemCount && search != null && search.getPage() < search.getTotal_pages() && lastItem != prelast) {
                searchMovie();
                prelast = lastItem;
            }
        }
    }
}
