
package shekar.com.popularmovies.ui.moviedetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import shekar.com.popularmovies.BaseApplication;
import shekar.com.popularmovies.R;
import shekar.com.popularmovies.model.MovieData;
import shekar.com.popularmovies.model.Review;
import shekar.com.popularmovies.model.ReviewResults;
import shekar.com.popularmovies.model.Trailer;
import shekar.com.popularmovies.model.TrailersResults;
import shekar.com.popularmovies.services.ApiService;
import shekar.com.popularmovies.utils.Constants;
import shekar.com.popularmovies.utils.FavoriteMovieContentProvider;
import shekar.com.popularmovies.utils.NetworkConnectionUtils;

public class MovieDetailActivity extends AppCompatActivity {

    @Bind(R.id.backdrop)
    ImageView mbackdropImage;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.movie_info)
    TextView mMovieInfo;
    @Bind(R.id.empty_trailer_list)
    TextView mDetailMovieEmptyTrailers;
    @Bind(R.id.movie_detail_trailer_container)
    LinearLayout mTrailerLinearLayout;
    @Nullable
    @Bind(R.id.favorite_fab)
    FloatingActionButton mFavoriteFab;
    @Bind(R.id.detail_movie_reviews_container)
    LinearLayout mReviewLinearLayout;

    @Bind(R.id.empty_review_list)
    TextView mDetailMovieEmptyReviews;

    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    @Inject
    ApiService mApiService;
    @Inject
    NetworkConnectionUtils connectionUtils;
    private String movieId;
    private boolean mAddedInFavorite;
    private MovieData mMovieData;

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.FAVORITE, mAddedInFavorite);
        outState.putParcelableArrayList(Constants.TRAILERS, mTrailers);
        outState.putParcelableArrayList(Constants.REVIEWS, mReviews);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        (((BaseApplication) getApplication()).getComponent()).inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovieData = getIntent().getExtras().getParcelable(Constants.MOVIE_DETAIL_KEY);
        if (mMovieData != null) {
            mAddedInFavorite = FavoriteMovieContentProvider.getMovieData(this, mMovieData.id) != null;
        }
        movieId = mMovieData.id + "";
        mAddedInFavorite = FavoriteMovieContentProvider.getMovieData(this, mMovieData.id) != null;
        collapsingToolbar.setTitle(mMovieData.originalTitle);
        mMovieInfo.setText(mMovieData.overview);
        Picasso.with(this).load(mMovieData.posterPath).into(mbackdropImage);
        if (savedInstanceState != null) {
            mTrailers = savedInstanceState.getParcelableArrayList(Constants.TRAILERS);
            mReviews = savedInstanceState.getParcelableArrayList(Constants.REVIEWS);
            mAddedInFavorite = savedInstanceState.getBoolean(Constants.FAVORITE);
            addTrailerViews(mTrailers);
            addReviewViews(mReviews);
        } else {
            executeTasks(movieId);
        }
    }

    private void executeTasks(String id) {
        Call<TrailersResults> call = mApiService.getTrailer(id, getResources().getString(R.string.api_key));
        call.enqueue(new Callback<TrailersResults>() {
            @Override
            public void onResponse(Response<TrailersResults> response, Retrofit retrofit) {
                if (response != null && response.body() != null && response.isSuccess()) {
                    mTrailers = response.body().results;
                    addTrailerViews(mTrailers);
                    mDetailMovieEmptyTrailers.setVisibility(View.GONE);
                } else {
                    mDetailMovieEmptyTrailers.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDetailMovieEmptyTrailers.setVisibility(View.GONE);
            }
        });

        Call<ReviewResults> callReview = mApiService.getReviews(id, getResources().getString(R.string.api_key));
        callReview.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Response<ReviewResults> response, Retrofit retrofit) {
                if (response != null && response.body() != null && response.isSuccess()) {
                    mReviews = response.body().results;
                    addReviewViews(mReviews);
                    mDetailMovieEmptyReviews.setVisibility(View.GONE);
                } else {
                    mDetailMovieEmptyReviews.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mDetailMovieEmptyReviews.setVisibility(View.GONE);
            }
        });

    }

    private void addTrailerViews(List<Trailer> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(this);
        if (resultList != null && !resultList.isEmpty()) {
            for (Trailer trailer : resultList) {
                final String key = trailer.key;
                final View trailerView = inflater.inflate(R.layout.list_item_trailer, mTrailerLinearLayout, false);
                ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_poster_image_view);
                ImageView playImage = ButterKnife.findById(trailerView, R.id.play_trailer_image_view);
                playImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openYouTubeIntent(key);
                    }
                });

                Picasso.with(this)
                        .load(String.format(Constants.YOU_TUBE_IMG_URL, trailer.key))
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(trailerImage);
                mTrailerLinearLayout.addView(trailerView);
            }
        }
    }

    private void addReviewViews(List<Review> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(this);
        boolean emptyList = resultList == null || resultList.isEmpty();

        if (!emptyList) {
            for (Review review : resultList) {
                final View reviewView = inflater.inflate(R.layout.list_item_review, mReviewLinearLayout, false);
                TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.list_item_review_author_text_view);
                TextView reviewContent = ButterKnife.findById(reviewView, R.id.list_item_review_content_text_view);
                reviewAuthor.setText(review.author);
                reviewContent.setText(review.content);
                mReviewLinearLayout.addView(reviewView);
            }
        }
        mDetailMovieEmptyReviews.setVisibility(emptyList ? View.VISIBLE : View.GONE);
    }

    /**
     * Switch FAB icons
     */
    private void switchFabIcon() {
        mFavoriteFab.setImageResource(mAddedInFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline);
    }


    /**
     * Show or hide FAB button dependding on movie data. It is used only for two panel mode
     *
     * @param showFab identify if fab should be shown
     */
    private void toggleVisibleFab(boolean showFab) {
        if (mFavoriteFab != null) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFavoriteFab.getLayoutParams();
            p.setAnchorId(showFab ? R.id.appbar : View.NO_ID);
            mFavoriteFab.setLayoutParams(p);
            mFavoriteFab.setVisibility(showFab ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.favorite_fab)
    public void toggleFavorite() {
        int resultMsg;
        if (!mAddedInFavorite) {
            FavoriteMovieContentProvider.putMovieData(this, mMovieData);
            resultMsg = R.string.added_to_favorite;
        } else {
            FavoriteMovieContentProvider.deleteMovieData(this, mMovieData.id);
            resultMsg = R.string.removed_from_favorite;
        }
        mAddedInFavorite = !mAddedInFavorite;

        Snackbar.make(collapsingToolbar, resultMsg, Snackbar.LENGTH_SHORT).show();
        switchFabIcon();
    }


    private void openYouTubeIntent(String key) {
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOU_TUBE_VIDEO_URL + key));
        youTubeIntent.putExtra("force_fullscreen", true);
        startActivity(youTubeIntent);
    }
}

