package com.example.moviemania.helpers

import com.example.moviemania.app.model.Movie
import com.example.moviemania.app.model.SearchResult
import com.google.gson.GsonBuilder

class TestHelper {

    companion object {

        val DUMMY_DELAY = 500L

        private val dummySearcResultJson = "{ \"Search\": [ { \"Title\": \"Friends\", \"Year\": \"1994–2004\", \"imdbID\": \"tt0108778\", \"Type\": \"series\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNDVkYjU0MzctMWRmZi00NTkxLTgwZWEtOWVhYjZlYjllYmU4XkEyXkFqcGdeQXVyNTA4NzY1MzY@._V1_SX300.jpg\" }, { \"Title\": \"Friends with Benefits\", \"Year\": \"2011\", \"imdbID\": \"tt1632708\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTQ2MzQ0NTk4N15BMl5BanBnXkFtZTcwMDc2NDYzNQ@@._V1_SX300.jpg\" }, { \"Title\": \"Just Friends\", \"Year\": \"2005\", \"imdbID\": \"tt0433400\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjA0Mzg2NjUzMl5BMl5BanBnXkFtZTcwNDg2ODUzMQ@@._V1_SX300.jpg\" }, { \"Title\": \"How to Lose Friends & Alienate People\", \"Year\": \"2008\", \"imdbID\": \"tt0455538\", \"Type\": \"movie\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMjY0MzFmMDgtY2ZiOC00M2QyLWFmOWMtOTBmZWY4OWE2YTYzXkEyXkFqcGdeQXVyMjA5MTIzMjQ@._V1_SX300.jpg\" }, { \"Title\": \"Friends with Kids\", \"Year\": \"2011\", \"imdbID\": \"tt1720616\", \"Type\": \"movie\", \"Poster\": \"https://ia.media-imdb.com/images/M/MV5BMTcyMDI2NjU2Ml5BMl5BanBnXkFtZTcwNjA4MzQzNw@@._V1_SX300.jpg\" }, { \"Title\": \"We Are Your Friends\", \"Year\": \"2015\", \"imdbID\": \"tt3787590\", \"Type\": \"movie\", \"Poster\": \"https://ia.media-imdb.com/images/M/MV5BMjE2NjIxODUxNF5BMl5BanBnXkFtZTgwMjI1MzM1NjE@._V1_SX300.jpg\" }, { \"Title\": \"Friends with Money\", \"Year\": \"2006\", \"imdbID\": \"tt0436331\", \"Type\": \"movie\", \"Poster\": \"https://ia.media-imdb.com/images/M/MV5BMjE3Mjc3NjQ5NV5BMl5BanBnXkFtZTYwMjY5MjE3._V1_SX300.jpg\" }, { \"Title\": \"Happy Tree Friends\", \"Year\": \"1999–\", \"imdbID\": \"tt0770762\", \"Type\": \"series\", \"Poster\": \"https://ia.media-imdb.com/images/M/MV5BMzRiMjRkNDYtNjNmZC00MTQwLThjNGQtZDEzZDA0OWVlOWUxXkEyXkFqcGdeQXVyMjAxODI1Nzk@._V1_SX300.jpg\" }, { \"Title\": \"Foster's Home for Imaginary Friends\", \"Year\": \"2004–2009\", \"imdbID\": \"tt0419326\", \"Type\": \"series\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BNjYyNGFjOTctYzFmNC00NzdmLThhMDgtNjEzZTRmNzA3ODc5XkEyXkFqcGdeQXVyNjk1Njg5NTA@._V1_SX300.jpg\" }, { \"Title\": \"Friends from College\", \"Year\": \"2017–\", \"imdbID\": \"tt5565334\", \"Type\": \"series\", \"Poster\": \"https://ia.media-imdb.com/images/M/MV5BOTY1MTUxOTY5MF5BMl5BanBnXkFtZTgwNTY2MzI2MjI@._V1_SX300.jpg\" } ], \"totalResults\": \"1735\", \"Response\": \"True\" }"
        private val dummyMovieDetailJson = "{ \"Response\": \"True\", \"Title\": \"Guardians of the Galaxy Vol. 2\", \"Year\": \"2017\", \"Rated\": \"PG-13\", \"Released\": \"05 May 2017\", \"Runtime\": \"136 min\", \"Genre\": \"Action, Adventure, Sci-Fi\", \"Director\": \"James Gunn\", \"Writer\": \"James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)\", \"Actors\": \"Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel\", \"Plot\": \"The Guardians must fight to keep their newfound family together as they unravel the mystery of Peter Quill's true parentage.\", \"Language\": \"English\", \"Country\": \"USA\", \"Awards\": \"Nominated for 1 Oscar. Another 12 wins & 42 nominations.\", \"Poster\": \"https://m.media-amazon.com/images/M/MV5BMTg2MzI1MTg3OF5BMl5BanBnXkFtZTgwNTU3NDA2MTI@._V1_SX300.jpg\", \"Ratings\": [ { \"Source\": \"Internet Movie Database\", \"Value\": \"7.7/10\" }, { \"Source\": \"Rotten Tomatoes\", \"Value\": \"83%\" }, { \"Source\": \"Metacritic\", \"Value\": \"67/100\" } ], \"Metascore\": \"67\", \"imdbRating\": \"7.7\", \"imdbVotes\": \"388,497\", \"imdbID\": \"tt3896198\", \"Type\": \"movie\", \"DVD\": \"22 Aug 2017\", \"BoxOffice\": \"\$389,804,217\", \"Production\": \"Walt Disney Pictures\", \"Website\": \"https://marvel.com/guardians\" }"

        fun getDummySearchResult(): SearchResult{
            return GsonBuilder().serializeNulls().create()
                .fromJson(dummySearcResultJson, SearchResult::class.java)
        }

        fun getDummyMovieDetail(): Movie{
            return GsonBuilder().serializeNulls().create()
                .fromJson(dummyMovieDetailJson, Movie::class.java)
        }

    }

}