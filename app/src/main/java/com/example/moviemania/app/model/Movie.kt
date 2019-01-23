package com.example.moviemania.app.model

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

data class Movie(
    @SerializedName("imdbID")
    val imdbID: String? = "",

    @SerializedName("imdbVotes")
    val imdbVotes: String? = "",

    @SerializedName("imdbRating")
    val imdbRating: String? = "",

    @SerializedName("Title")
    val title: String? = "",

    @SerializedName("Year")
    val year: String? = "",

    @SerializedName("Rated")
    val rated: String? = "",

    @SerializedName("Released")
    val released: String? = "",

    @SerializedName("Runtime")
    val runtime: String? = "",

    @SerializedName("Genre")
    val genre: String? = "",

    @SerializedName("Director")
    val director: String? = "",

    @SerializedName("Writer")
    val writer: String? = "",

    @SerializedName("Actors")
    val actors: String? = "",

    @SerializedName("Plot")
    val plot: String? = "",

    @SerializedName("Language")
    val language: String? = "",

    @SerializedName("Ratings")
    val ratings: Rating? = Rating("", ""),

    @SerializedName("Poster")
    val poster: String? = "",

    @SerializedName("Response")
    val response: Boolean = false
)

data class Rating(
    @SerializedName("Source")
    val source: String? = "",

    @SerializedName("Value")
    val value: String? = ""
)

data class SearchResult(
    @SerializedName("Search")
    val searchItems: List<SearchResultItem>? = listOf(),

    @SerializedName("totalResults")
    val totalResults: Int = 0,

    @SerializedName("Response")
    val response: Boolean = false
)

data class SearchResultItem(
    @SerializedName("Title")
    val title: String? = "",

    @SerializedName("Year")
    val year: String? = "",

    @SerializedName("imdbID")
    val imdbID: String? = "",

    @SerializedName("Type")
    val type: String? = "",

    @SerializedName("Poster")
    val poster: String? = ""
)