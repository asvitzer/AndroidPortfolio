
package com.alvinsvitzer.flixbook.movies;

import com.alvinsvitzer.flixbook.data.MovieDataStore;
import com.alvinsvitzer.flixbook.data.model.Movie;
import com.android.volley.toolbox.ImageLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MoviesPresenterTest{

    @Mock
    private MovieDataStore mAppRepository;

    @Mock
    private ImageLoader mImageLoader;

    @Mock
    private MoviesContract.View mView;

    private MoviesPresenter mPresenter;

    @Before
    public void setupMoviesPresenter(){

        MockitoAnnotations.initMocks(this);

        mPresenter = new MoviesPresenter(mAppRepository, mView, mImageLoader);

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

        Movie movie = new Movie();
        movie.setMovieTitle("The Best Movie");

        mPresenter.saveMovie(movie);

        verify(mAppRepository).saveMovie(movie);

    }

    @Test
    public void verifyImageLoaderReturned(){

        ImageLoader imageLoader = mPresenter.getImageLoader();
        assertEquals(imageLoader, mImageLoader);

    }
}
