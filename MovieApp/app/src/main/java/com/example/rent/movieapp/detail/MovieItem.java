package com.example.rent.movieapp.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RENT on 2017-03-13.
 */

public class MovieItem{
    @SerializedName("Title")
    private String title;
    @SerializedName("Year")
    private String year;
    @SerializedName("Rated")
    private String rated;
    @SerializedName("Released")
    private String released;
    @SerializedName("Genre")
    private String genre;
    @SerializedName("Director")
    private String director;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Actors")
    private String actors;
    @SerializedName("Poster")
    private String poster;
    @SerializedName("imdbID")
    private String id;
    private String imdbRating;
    @SerializedName("Awards")
    private String awards;
    @SerializedName("Plot")
    private String plot;
    @SerializedName("Response")
    private String response;


    public String getResponse() {
        return response;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getRated() {
        return rated;
    }

    public String getReleased() {
        return released;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return poster;
    }

    public String getId() {
        return id;
    }

    public String getAwards() {
        return awards;
    }

    public String getPlot() {
        return plot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieItem movieItem = (MovieItem) o;

        if (title != null ? !title.equals(movieItem.title) : movieItem.title != null) return false;
        if (year != null ? !year.equals(movieItem.year) : movieItem.year != null) return false;
        if (rated != null ? !rated.equals(movieItem.rated) : movieItem.rated != null) return false;
        if (released != null ? !released.equals(movieItem.released) : movieItem.released != null) return false;
        if (genre != null ? !genre.equals(movieItem.genre) : movieItem.genre != null) return false;
        if (director != null ? !director.equals(movieItem.director) : movieItem.director != null) return false;
        if (runtime != null ? !runtime.equals(movieItem.runtime) : movieItem.runtime != null) return false;
        if (actors != null ? !actors.equals(movieItem.actors) : movieItem.actors != null) return false;
        if (poster != null ? !poster.equals(movieItem.poster) : movieItem.poster != null) return false;
        if (id != null ? !id.equals(movieItem.id) : movieItem.id != null) return false;
        if (imdbRating != null ? !imdbRating.equals(movieItem.imdbRating) : movieItem.imdbRating != null) return false;
        if (awards != null ? !awards.equals(movieItem.awards) : movieItem.awards != null) return false;
        if (plot != null ? !plot.equals(movieItem.plot) : movieItem.plot != null) return false;
        return response != null ? response.equals(movieItem.response) : movieItem.response == null;

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (rated != null ? rated.hashCode() : 0);
        result = 31 * result + (released != null ? released.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (runtime != null ? runtime.hashCode() : 0);
        result = 31 * result + (actors != null ? actors.hashCode() : 0);
        result = 31 * result + (poster != null ? poster.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (imdbRating != null ? imdbRating.hashCode() : 0);
        result = 31 * result + (awards != null ? awards.hashCode() : 0);
        result = 31 * result + (plot != null ? plot.hashCode() : 0);
        result = 31 * result + (response != null ? response.hashCode() : 0);
        return result;
    }
}
