package com.example.asouphie.mini_projet;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LigneAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;
    private Context context;

    public LigneAdapter(ArrayList<Movie> movie, Context context) {
        super(context, R.layout.line_view_films, movie);
        this.movies = movie;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View ligne = inflater.inflate(R.layout.line_view_films, parent, false);

            //Le contenu de la ligneAdapter
            TextView title = (TextView) ligne.findViewById(R.id.title);
            TextView categories = (TextView) ligne.findViewById(R.id.categories);
            ImageView image = (ImageView) ligne.findViewById(R.id.image);
            RatingBar ratingBar = (RatingBar) ligne.findViewById(R.id.popularite);

            Movie movie = movies.get(position);

            if(movie.getPoster_path()==null){
                Picasso.with(context).load("http://www.publicdomainpictures.net/pictures/140000/velka/question-mark-1443688403FVL.jpg").into(image);
            }
            else {
                Picasso.with(context).load("https://image.tmdb.org/t/p/w640"+movie.getPoster_path()).into(image);
            }
            title.setText(movie.getTitle());
            List<String> genres = new ArrayList<>();


            Map<Integer, String> list = MainActivity.listeGenre;
            for(Integer id : movie.getGenre_ids()) {
                if(list.containsKey(id)) {
                    genres.add(list.get(id));
                }
            }
            categories.setText(TextUtils.join(" / ",genres));
            ratingBar.setRating(movie.getVote_average()/2);

        return ligne;
    }
}

/*
package com.example.asouphie.mini_projet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LigneAdapter extends ArrayAdapter<Movie> {
    private ArrayList<Movie> movies;
    private Context context;

    static class ViewHolder {
        TextView title;
        TextView categories;
        ImageView image;
        RatingBar ratingBar;
    }

    public LigneAdapter(ArrayList<Movie> movie, Context context) {
        super(context, R.layout.line_view_films, movie);
        this.movies = movie;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ligne = convertView;
        final ViewHolder holder;

        if(ligne == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ligne = inflater.inflate(R.layout.line_view_films, parent, false);

            holder = new ViewHolder();
            //Le contenu de la ligneAdapter
            holder.title = (TextView) ligne.findViewById(R.id.title);
            holder.categories = (TextView) ligne.findViewById(R.id.categories);
            holder.image = (ImageView) ligne.findViewById(R.id.image);
            holder.ratingBar = (RatingBar) ligne.findViewById(R.id.popularite);

            ligne.setTag(holder);
        } else {
            holder = (ViewHolder) ligne.getTag();
        }

        Movie movie = movies.get(position);

        if(movie.getPoster_path()==null){
            Picasso.with(context).load("http://www.publicdomainpictures.net/pictures/140000/velka/question-mark-1443688403FVL.jpg").into(holder.image);
        }
        else {
            Picasso.with(context).load("https://image.tmdb.org/t/p/w640"+movie.getPoster_path()).into(holder.image);
        }
        holder.title.setText(movie.getTitle());
        List<String> genres = new ArrayList<>();


        Map<Integer, String> list = MainActivity.listeGenre;
        for(Integer id : movie.getGenre_ids()) {
            if(list.containsKey(id)) {
                genres.add(list.get(id));
            }
        }
        holder.categories.setText(TextUtils.join(" / ",genres));
        holder.ratingBar.setRating(movie.getVote_average()/2);

        return ligne;
    }
}*/
