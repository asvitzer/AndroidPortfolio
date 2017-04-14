package com.alvinsvitzer.flixbook.moviedetail.pagerfragments;

/**
 * Created by Alvin on 2/22/17.
 */
/*
@RunWith(PowerMockRunner.class)
public class MovieOverviewPresenterTest implements MovieDataStoreInMemory.GetMovieCallback {

    private Movie mMovie;

    @Mock
    private AppRepository mAppRepository;

    @Mock
    private MovieOverviewContract.View mView;

    @Mock
    private Logger mLogger;

    private MovieOverviewPresenter mPresenter;

    @Before
    public void setupObject() {

        MockitoAnnotations.initMocks(this);

        mPresenter = new MovieOverviewPresenter(mView, mAppRepository, mLogger);

        mMovie = new Movie();
        mMovie.setMovieTitle("The Best Movie");
    }

    @Test
    public void verifyMovieDetailLoadOnStart() {

        mPresenter.start();

        verify(mAppRepository).getMovie(mPresenter);

    }

    @Test
    public void notifyUserMovieDataNotAvailable() {

        mPresenter.onMovieDataNotAvailable();
        verify(mView).notifyNoMovieData();

    }

    @Test
    public void detachViewSetsViewToNull() {

        mPresenter.detachView();
        assertNull(mPresenter.mView);

    }

    @PrepareForTest({MovieDBUtils.class})
    @Test
    public void verifyMovieDataLoaded() {

        PowerMockito.mockStatic(MovieDBUtils.class);

        PowerMockito.when(MovieDBUtils.getLocalDate(anyString()))
                    .thenReturn(anyString());

        mPresenter.onMovieLoaded(mMovie);

        verify(mView).setPlot(anyString());
        verify(mView).setReleaseDate(anyString());
        verify(mView).setVoteAverage(anyDouble());
    }

    *//**
     * The following methods are all blank since they are methods implemented
     * from callback interfaces that are passed into the AppRepository when making
     * calls to it. They do not need to be filled out since the SUT is the Presenter
     * class. The AppRepository will be tested in other classes to see if it is correctly
     * calling a callback.
 *//*
    @Override
    public void onMovieLoaded(Movie movie) {

    }

    @Override
    public void onMovieDataNotAvailable() {



}
    }*/