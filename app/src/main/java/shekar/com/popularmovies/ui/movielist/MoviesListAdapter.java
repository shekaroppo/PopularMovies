package shekar.com.popularmovies.ui.movielist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import shekar.com.popularmovies.R;
import shekar.com.popularmovies.model.Results;
import shekar.com.popularmovies.ui.moviedetail.MovieDetailActivity;


/**
 * Created by Sekhar on 03/07/16.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.GenresViewHolder> {
    private List<Results> mData = new ArrayList<>();

    public void setData(List<Results> data) {
        this.mData.clear();
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public GenresViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies_list, parent, false);
        GenresViewHolder vh = new GenresViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenresViewHolder holder, int position) {
        final Results selectedMovie = getItem(position);
        holder.movieTitle.setText(selectedMovie.getTitle());
        Picasso.with(holder.movieArt.getContext()).load(holder.movieArt.getContext().getResources().getString(R.string.image_endpoint) +
                selectedMovie.getPoster_path()).into(holder.movieArt);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();

                String title = selectedMovie.getTitle();

                String detail_text = "Release Date: " + selectedMovie.getRelease_date() + "\n" +
                        "Vote Average: " + selectedMovie.getVote_average() + "\n" +
                        "Plot Synopsis: " + selectedMovie.getOverview();

                String posterPath = "http://image.tmdb.org/t/p/w500" + selectedMovie.getPoster_path();

                Intent intent = new Intent(context, MovieDetailActivity.class)
                        .putExtra(MovieDetailActivity.EXTRA_TITLE, title)
                        .putExtra(MovieDetailActivity.EXTRA_INFO, detail_text)
                        .putExtra(MovieDetailActivity.EXTRA_URL, posterPath);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Results getItem(int position) {
        return (Results) mData.get(position);
    }

    public static class GenresViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.album_title)
        public TextView movieTitle;
        @Bind(R.id.album_art)
        public ImageView movieArt;
        public final View mView;

        public GenresViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}