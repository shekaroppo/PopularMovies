package shekar.com.popularmovies.ui.movielist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import shekar.com.popularmovies.R;
import shekar.com.popularmovies.model.MovieData;
import shekar.com.popularmovies.ui.moviedetail.MovieDetailActivity;
import shekar.com.popularmovies.utils.Constants;
import shekar.com.popularmovies.utils.SquareImageView;


/**
 * Created by Sekhar on 03/07/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieListViewHolder> {
    private List<MovieData> mData = new ArrayList<>();

    public void setData(List<MovieData> data) {
        this.mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public MovieListViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies_list, parent, false);
        return new MovieListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieListViewHolder holder, int position) {
        final MovieData selectedMovie = getItem(position);
        holder.movieTitle.setText(selectedMovie.originalTitle);
        Picasso.with(holder.movieArt.getContext()).load(holder.movieArt.getContext().getResources().getString(R.string.image_endpoint) +
                selectedMovie.posterPath).into(holder.movieArt);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                String title = selectedMovie.originalTitle;

                String detail_text = "Release Date: " + selectedMovie.releaseDate + "\n" +
                        "Vote Average: " + selectedMovie.voteAverage + "\n" +
                        "Plot Synopsis: " + selectedMovie.overview;

                String posterPath = "http://image.tmdb.org/t/p/w500" + selectedMovie.posterPath;

                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra(Constants.MOVIE_DETAIL_KEY, selectedMovie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public MovieData getItem(int position) {
        return mData.get(position);
    }

    public static class MovieListViewHolder extends RecyclerView.ViewHolder {

        //@Bind(R.id.album_title)
        public TextView movieTitle;
        //@Bind(R.id.album_art)
        public SquareImageView movieArt;
        //@Bind(R.id.content)
        public View mView;

        public MovieListViewHolder(final View view) {
            super(view);
           // ButterKnife.bind(this, view);
            movieTitle=(TextView)view.findViewById(R.id.album_title);
            movieArt=(SquareImageView)view.findViewById(R.id.album_art);
            mView=view;
        }
    }
}