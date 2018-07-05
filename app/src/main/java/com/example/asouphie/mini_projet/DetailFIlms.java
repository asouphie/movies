package com.example.asouphie.mini_projet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailFIlms extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail);
            Intent intent = getIntent();

            //On souhaite récupérer directement l'objet Movie plutôt que de refaire une requête.
            Bundle bundle = intent.getExtras();
            Movie movie = (Movie)(bundle.getSerializable("movie"));

            //Si cet objet n'est pas null, alors on peut implémenter la vue activity_detail
            if(movie != null) {
                //Tout les composants de la vue
                TextView titre = (TextView) findViewById(R.id.detail_title);
                TextView years = (TextView) findViewById(R.id.detail_years);
                TextView categories = (TextView) findViewById(R.id.detail_categories);
                TextView resume = (TextView) findViewById(R.id.detail_resume);
                ImageView image = (ImageView) findViewById(R.id.detail_picture);
                RatingBar ratingBar = (RatingBar) findViewById(R.id.detail_popularite);

                //Dans un premier temps, avec Picasso, on récupère l'image.
                if(movie.getPoster_path()==null){
                    Picasso.with(this).load("http://www.publicdomainpictures.net/pictures/140000/velka/question-mark-1443688403FVL.jpg").into(image);
                }
                else {
                    Picasso.with(this).load("https://image.tmdb.org/t/p/w640/"+movie.getPoster_path()).into(image);
                }

                //Ensuite on récupère la date pour la remettre au format souhaité
                //Exemple : "2016-04-01" donnera "01 avril 2016"
                try {
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                    SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMMM yyyy");
                    Date date = originalFormat.parse(movie.getRelease_date());
                    years.setText(targetFormat.format(date));
                } catch (ParseException e) {
                    years.setText(movie.getRelease_date());
                }

                //Puis nous récupérons le nom des id genres contenu dans notre objet Movie
                //Pour les afficher de la manière suivante : nom1 / nom2 / nom3 / ...
                List<String> genres = new ArrayList<>();
                Map<Integer, String> list = MainActivity.listeGenre;
                for(Integer id : movie.getGenre_ids()) {
                    if(list.containsKey(id)) {
                        genres.add(list.get(id));
                    }
                }
                categories.setText(TextUtils.join(" / ",genres));

                titre.setText(movie.getTitle());

                String overview = movie.getOverview();
                resume.setText(overview.length() == 0 ? "Aucune description disponible" : overview);

                ratingBar.setRating(movie.getVote_average()/2);
            }else{
                Toast.makeText(this, "Erreur", Toast.LENGTH_LONG).show();
            }
        }
}
