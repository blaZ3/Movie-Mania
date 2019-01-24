package com.example.moviemania.app.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

//
//{
//    "Title": "The Big Bang Theory",
//    "Year": "2007–",
//    "Rated": "TV-14",
//    "Released": "01 May 2006",
//    "Runtime": "22 min",
//    "Genre": "Comedy, Romance",
//    "Director": "N/A",
//    "Writer": "Chuck Lorre, Bill Prady",
//    "Actors": "Johnny Galecki, Jim Parsons, Kaley Cuoco, Simon Helberg",
//    "Plot": "A woman who moves into an apartment across the hall from two brilliant but socially awkward physicists shows them how little they know about life outside of the laboratory.",
//    "Language": "English, Hindi, Italian, Russian, Mandarin, Klingon",
//    "Country": "USA",
//    "Awards": "Won 1 Golden Globe. Another 66 wins & 222 nominations.",
//    "Poster": "https://m.media-amazon.com/images/M/MV5BY2FmZTY5YTktOWRlYy00NmIyLWE0ZmQtZDg2YjlmMzczZDZiXkEyXkFqcGdeQXVyNjg4NzAyOTA@._V1_SX300.jpg",
//    "Ratings": [
//    {
//        "Source": "Internet Movie Database",
//        "Value": "8.2/10"
//    }
//    ],
//    "Metascore": "N/A",
//    "imdbRating": "8.2",
//    "imdbVotes": "626,251",
//    "imdbID": "tt0898266",
//    "Type": "series",
//    "totalSeasons": "12",
//    "Response": "True"
//}

@Entity
data class Movie(

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @SerializedName("imdbID")
    var imdbID: String? = "",

    @SerializedName("imdbVotes")
    var imdbVotes: String? = "",

    @SerializedName("imdbRating")
    var imdbRating: String? = "",

    @SerializedName("Title")
    var title: String? = "",

    @SerializedName("Year")
    var year: String? = "",

    @SerializedName("Rated")
    var rated: String? = "",

    @SerializedName("Released")
    var released: String? = "",

    @SerializedName("Runtime")
    var runtime: String? = "",

    @SerializedName("Genre")
    var genre: String? = "",

    @SerializedName("Director")
    var director: String? = "",

    @SerializedName("Writer")
    var writer: String? = "",

    @SerializedName("Actors")
    var actors: String? = "",

    @SerializedName("Plot")
    var plot: String? = "",

    @SerializedName("Language")
    var language: String? = "",

    @SerializedName("Ratings")
    @Ignore
    var ratings: List<Rating>? = listOf(),

    @SerializedName("Poster")
    var poster: String? = "",

    @SerializedName("Response")
    @Ignore
    var response: Boolean = false
)

data class Rating(
    @SerializedName("Source")
    var source: String? = "",

    @SerializedName("Value")
    var value: String? = ""
)

data class SearchResult(
    @SerializedName("Search")
    var searchItems: List<SearchResultItem>? = listOf(),

    @SerializedName("totalResults")
    var totalResults: Int = 0,

    @SerializedName("Response")
    var response: Boolean = false
)

data class SearchResultItem(
    @SerializedName("Title")
    var title: String? = "",

    @SerializedName("Year")
    var year: String? = "",

    @SerializedName("imdbID")
    var imdbID: String? = "",

    @SerializedName("Type")
    var type: String? = "",

    @SerializedName("Poster")
    var poster: String? = "",

    var favorited: Boolean = false
)