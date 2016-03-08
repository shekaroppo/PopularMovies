package shekar.com.popularmovies.ui.movielist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import shekar.com.popularmovies.BaseApplication;
import shekar.com.popularmovies.R;
import shekar.com.popularmovies.model.ResultsPage;
import shekar.com.popularmovies.services.ApiService;
import shekar.com.popularmovies.utils.NetworkConnectionUtils;
import shekar.com.popularmovies.utils.OffsetDecoration;

public class MoviesListActivity extends AppCompatActivity {

    @Bind(R.id.movie_list_recyclerView)
    RecyclerView mRecyclerView;

    @Bind(R.id.loadingView)
    ProgressBar mProgressBar;

    @Bind(R.id.errorView)
    TextView mErrorText;

    MoviesListAdapter mAdapter;

    @Inject
    ApiService mApiService;
    @Inject
    NetworkConnectionUtils connectionUtils;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        (((BaseApplication) getApplication()).getComponent()).inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUI();
        loadContent();
    }

    private void loadContent() {
        if (connectionUtils.isConnected()) {
            showProgress();
            getMovies();
        } else {
            showErrorMsg(getString(R.string.no_network));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.topRated:
                mSortOrder = getResources().getString(R.string.sort_order_top_rated);
                loadContent();
                return true;
            case R.id.popular:
                mSortOrder = getResources().getString(R.string.sort_order_popular);
                loadContent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setUI() {
        mSortOrder = getResources().getString(R.string.sort_order_popular);
        mAdapter = new MoviesListAdapter();
        final int spacing = getResources()
                .getDimensionPixelSize(R.dimen.spacing_nano);
        mRecyclerView.addItemDecoration(new OffsetDecoration(spacing));
         mRecyclerView.setAdapter(mAdapter);
    }

    private void getMovies() {
        Call<ResultsPage> call = mApiService.getMovies(mSortOrder, getResources().getString(R.string.api_key));
        call.enqueue(new Callback<ResultsPage>() {
            @Override
            public void onResponse(Response<ResultsPage> response, Retrofit retrofit) {
                hideProgress();
                if(response!=null&&response.body()!=null&&response.isSuccess()) {
                    mAdapter.setData(response.body().getResults());
                }
                else{
                    showErrorMsg(getResources().getString(R.string.data_error));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                showErrorMsg(getResources().getString(R.string.data_error));
            }
        });
    }

    private void showErrorMsg(String errorMsg) {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(errorMsg);
    }

    private void hideProgress() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mErrorText.setVisibility(View.GONE);
        }
    }

    private void showProgress() {
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mErrorText.setVisibility(View.GONE);
        }
    }
}
