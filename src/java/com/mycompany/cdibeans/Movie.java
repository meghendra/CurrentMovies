/*
 * Created by Meghendra Singh on 2016.10.26  * 
 * Copyright © 2016 Meghendra Singh. All rights reserved. * 
 */
package com.mycompany.cdibeans;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Singh
 */
public class Movie {
    
    /*
    ================================================================
    Instance variables representing the attributes of a Movie.
    ================================================================
     */
    private long id;
    private String imdbId;
    private String name;
    private String releaseDate;
    private String mpaaRating;
    private String imdbRating;
    private String imageUrl;
    private List<String> cast;
    private String runtime;
    private String synopsis;

    /**
     * Constructor for instantiating a Movie object
     * @param id - Id of the movie as obtained from Rotten Tomatoes
     * @param name - Name of the movie
     * @param releaseDate - release date for the movie as a java.util.Date object
     * @param mpaaRating - mpaa rating of the movie
     * @param criticsRating - critics rating for the movie
     * @param audienceRating - audience rating for the movie
     * @param imageUrl - image url corresponding to the thumbnail poster of the movie obtained from Rotten Tomatoes
     * @param cast - List of cast names for the movie
     * @param runtime - Runtime of the movie in the appropriate format as a String
     * @param synopsis - Synopsis of the movie
     */
    public Movie(long id, String imdbId, String name, String releaseDate, String mpaaRating, String imdbRating, String imageUrl, List<String> cast, String runtime, String synopsis) {
        this.id = id;
        this.imdbId = imdbId;
        this.name = name;
        this.releaseDate = releaseDate;
        this.mpaaRating = mpaaRating;
        this.imdbRating = imdbRating;
        this.imageUrl = imageUrl;
        this.cast = cast;
        this.runtime = runtime;
        this.synopsis=synopsis;
    }

    /**
     * A constructor for Movie objects with just the movie id
     * could be useful for search and retrieval
     * @param id - Id of the movie as obtained from Rotten Tomatoes
     */
    public Movie(long id) {
        this.id = id;
    }

    /**
     * Empty constructor for initialization without any attribute values
     */
    public Movie() {
    }

    
    /*
    =========================
    Getter and Setter Methods
    =========================
    */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getCast() {
        return cast;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /**
     * Overridden toString method good for debugging as it returns a string 
     * containing all attribute values for a movie object which can be printed
     * on console or written to a log
     * @return - String containing all instance values of a movie object's attributes
     */
    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", name=" + name + ", releaseDate=" + releaseDate + ", mpaaRating=" + mpaaRating + ", imdbRating=" + imdbId + ", imdbId=" + imdbId + ", imageUrl=" + imageUrl + ", cast=" + cast + ", runtime=" + runtime + ", synopsis=" + synopsis + '}';
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(String imdbRating) {
        this.imdbRating = imdbRating;
    }
    
    
}
