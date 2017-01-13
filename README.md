# FlixBook

App to help users discover popular and highly rated movies on the web. A scrolling grid of movie posters is used to display the movies however the user decides to sort them. When clicked, a details screen is shown for the particular movie is selected. This app utilizes core Android user interface components and grabs movie information using themoviedb.org web API.

## Concepts Covered

1) Network Requests (via the Volley API)
2) Image Caching (via the Volley API)
3) Activities/Fragments
4) RecyclerView
5) JSON (via the Movie DB API)
6) Intents
7) Parcelable (via the Parceler API)

## Installation

1. Download Android Studio.
2. Pull down the project files and open the them in Android Studio.

## API Keys

1. The Movie DB:        Place own key in the SingleFragmentActivity class (in package com.alvinsvitzer.flixbook.extensions) on line 33:

setMovieDBApiKey("INSERT YOUR KEY HERE")

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D
