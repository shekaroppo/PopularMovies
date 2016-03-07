
package shekar.com.popularmovies.ui.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import shekar.com.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_INFO = "info";
    public static final String EXTRA_URL = "url";

    @Bind(R.id.backdrop)
    ImageView mbackdropImage;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.movie_info)
    TextView mMovieInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (intent != null && intent.hasExtra(EXTRA_TITLE) &&
                intent.hasExtra(EXTRA_INFO) && intent.hasExtra(EXTRA_URL)) {
            String movieTitleString = intent.getStringExtra(EXTRA_TITLE);
            String movieInfoString = intent.getStringExtra(EXTRA_INFO);
            String moviePosterString = intent.getStringExtra(EXTRA_URL);
            collapsingToolbar.setTitle(movieTitleString);
            mMovieInfo.setText(movieInfoString);
            Picasso.with(this).load(moviePosterString).into(mbackdropImage);
        }
    }
}
