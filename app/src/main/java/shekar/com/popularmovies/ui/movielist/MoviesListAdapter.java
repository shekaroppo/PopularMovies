package shekar.com.popularmovies.ui.movielist;

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
import shekar.com.popularmovies.utils.RecyclerViewItemClickListener;


/**
 * Created by Sekhar on 4/6/15.
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.GenresViewHolder> {
    private RecyclerViewItemClickListener mlistener;
    private List<Results> mData = new ArrayList<>();

    public MoviesListAdapter(RecyclerViewItemClickListener listener) {
        mlistener = listener;
    }

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
        GenresViewHolder vh = new GenresViewHolder(v, mlistener);
        return vh;
    }

    @Override
    public void onBindViewHolder(GenresViewHolder holder, int position) {
        Results movie = getItem(position);
        holder.movieTitle.setText(movie.getTitle());
        Picasso.with(holder.movieArt.getContext()).load(holder.movieArt.getContext().getResources().getString(R.string.image_endpoint) +
                movie.getPoster_path()).into(holder.movieArt);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Results getItem(int position) {
        return (Results) mData.get(position);
    }

    public static class GenresViewHolder extends RecyclerView.ViewHolder {

        private final RecyclerViewItemClickListener mListener;
        @Bind(R.id.album_title)
        public TextView movieTitle;
        @Bind(R.id.album_art)
        public ImageView movieArt;

        public GenresViewHolder(final View view, RecyclerViewItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            mListener = listener;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null)
                        mListener.onItemClick(view, getLayoutPosition());
                }
            });
        }
    }
}