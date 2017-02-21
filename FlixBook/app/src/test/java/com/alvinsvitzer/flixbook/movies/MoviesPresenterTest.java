
package com.alvinsvitzer.flixbook.movies;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.alvinsvitzer.flixbook.utilities.MovieDBUtils;
import com.android.volley.toolbox.ImageLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class MoviesPresenterTest{

    @Mock
    private MovieDataStore mAppRepository;

    @Mock
    private ImageLoader mImageLoader;

    @Mock
    private MoviesContract.View mView;

    private MoviesPresenter mPresenter;

    private Movie mMovie;

    @Before
    public void setupMoviesPresenter(){

        MockitoAnnotations.initMocks(this);

        mPresenter = new MoviesPresenter(mAppRepository, mView, mImageLoader);

        mMovie = new Movie();
        mMovie.setMovieTitle("The Best Movie");

    }

    @Test
    public void verifyMoviesLoadOnStart(){

        MoviesPresenter moviesPresenterSpy = spy(mPresenter);

        moviesPresenterSpy.start();

        verify(mView, atLeastOnce()).hideNoDataTextView();
        verify(moviesPresenterSpy).loadMovies();

    }


    @Test
    public void loadPopularMovies(){

        MoviesFilterType moviesFilterType = MoviesFilterType.FAVORITE_MOVIES;

        when(mView.getSortingId()).thenReturn(moviesFilterType);

        mPresenter.loadMovies();

        verify(mAppRepository).getMovies(mPresenter, moviesFilterType);

    }

    @Test
    public void verifyViewIsDetached(){

        mPresenter.detachView();
        assertNull(mPresenter.mMoviesView);

    }

    @Test
    public void verifyMovieSavedToRepo(){

        mPresenter.saveMovie(mMovie);

        verify(mAppRepository).saveMovie(mMovie);

    }

    @Test
    public void verifyImageLoaderReturned(){

        ImageLoader imageLoader = mPresenter.getImageLoader();
        assertEquals(imageLoader, mImageLoader);

    }

    @PrepareForTest({ MovieDBUtils.class })
    @Test
    public void buildMoviePosterUrl(){

        URL moviePosterUrl = null;

        mMovie.setMoviePoster("/bbxtz5V0vvnTDA2qWbiiRC77Ok9.jpg");

        try {
            moviePosterUrl = new URL("http://image.tmdb.org/t/p/w185/bbxtz5V0vvnTDA2qWbiiRC77Ok9.jpg");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        PowerMockito.mockStatic(MovieDBUtils.class);

        PowerMockito.when(MovieDBUtils.buildMoviePosterURL(mMovie.getMoviePoster()))
                .thenReturn(moviePosterUrl);

        assertEquals("Movie URL does not match pattern.", moviePosterUrl.toString(), mPresenter.buildMovieUrl(mMovie));

    }

    @Test
    public void loadMoviesToScreen(){

        List<Movie> movieList = new ArrayList<>();
        movieList.add(mMovie);

        mPresenter.onMoviesLoaded(movieList);

        verify(mView).showMovies(movieList);

    }

    @Test
    public void showNoDataMessageIfNoMovies(){

        mPresenter.onMovieListDataNotAvailable();

        verify(mView).showNoDataTextView();
    }
}
