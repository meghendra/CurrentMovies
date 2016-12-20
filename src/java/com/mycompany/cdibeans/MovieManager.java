/*
 * Created by Meghendra Singh on 2016.10.26  * 
 * Copyright © 2016 Meghendra Singh. All rights reserved. * 
 */
package com.mycompany.cdibeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

/**
 *
 * @author Singh
 */

/*
The @SessionScoped annotation preserves the values of the MovieManager
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
---------------------------------------------------------------------------
JSF Managed Beans annotated with @ManagedBean from javax.faces.bean
is in the process of being deprecated in favor of CDI Beans. Therefore, 
you should use @Named from javax.inject package to designate a managed
bean as a JSF controller class. Contexts and Dependency Injection (CDI) 
beans annotated with @Named is the preferred approach, because CDI 
enables Java EE-wide dependency injection. CDI bean is a bean managed
by the CDI container. 

Within JSF XHTML pages, this bean will be referenced by using the name
'MovieManager'. Actually, the default name is the class name starting
with a lower case letter and value = "MovieManager" is optional;
However, we spell it out to make our code more readable and understandable.
---------------------------------------------------------------------------
 */
@Named(value = "movieManager")

/*
---------------------------------------------------------------------------
Marking the MovieManager class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
---------------------------------------------------------------------------
 */
public class MovieManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String tmdbRESTfulWebService_url = "https://api.themoviedb.org/3/movie/now_playing?api_key=dd424332d7b9f6b37f3aeaab413fbca7";
    private String tmdbRESTfulImageService_url = "http://image.tmdb.org/t/p/w500//";
    private String tmdbRESTfulCastServicePart1_url = "https://api.themoviedb.org/3/movie/";
    private String tmdbRESTfulCastServicePart2_url = "?api_key=dd424332d7b9f6b37f3aeaab413fbca7&append_to_response=credits";
    private String omdbRESTfulRatingServicePart1_url = "http://www.omdbapi.com/?i=";
    private String omdbRESTfulRatingServicePart2_url = "&plot=full&r=json ";
    
    private List<Movie> movies;
    private String statusMessage;
    private String jsonCategoryData;

    /*
    "The PostConstruct annotation is used on a method that needs to be executed after dependency injection is done
    to perform any initialization. This method MUST be invoked before the class is put into service." [Oracle]
     */
    @PostConstruct
    public void init() {

        statusMessage = "";

        /*
        This constructor method creates movies and movieNames
        by using the JSON file returned from the RESTful web services at moviesRESTfulWebServices_url
         */
        JSONArray jsonArray;
        movies = new ArrayList<>();

        /*
        ----------------------------------------------------------
        Create the movies list containing movie objects
        ----------------------------------------------------------
         */
        try {
            /*
            JSON data use the following notation:
            { }    represents a JavaScript object (i.e., a current movie) with its properties as Key:Value pairs
            [ ]    represents Array
            [{ }]  represents an Array of JavaScript objects
              :    separates Key from the Value
            
            moviesRESTfulWebServices_url = "*************" 
            returns the entire content of current movies on rottentomatoes.com in JSON format as 
            one Array [] containing 50 movie objects each within a {}.
            
            jasonObject is a *******
            jsonArray is an array containing 50 movie objects with properties given as Key:Value pairs.
             */
            JSONObject jsonObject1 = new JSONObject(readUrlContent(tmdbRESTfulWebService_url + "&page=1"));
            JSONObject jsonObject2 = new JSONObject(readUrlContent(tmdbRESTfulWebService_url + "&page=2"));
            JSONObject jsonObject3 = new JSONObject(readUrlContent(tmdbRESTfulWebService_url + "&page=3"));
            JSONObject jsonObject4 = new JSONObject(readUrlContent(tmdbRESTfulWebService_url + "&page=4"));

            JSONArray jsonArray1 = new JSONArray(jsonObject1.getJSONArray("results").toString());
            JSONArray jsonArray2 = new JSONArray(jsonObject2.getJSONArray("results").toString());
            JSONArray jsonArray3 = new JSONArray(jsonObject3.getJSONArray("results").toString());
            JSONArray jsonArray4 = new JSONArray(jsonObject4.getJSONArray("results").toString());

            jsonArray = concatArray(jsonArray1, jsonArray2, jsonArray3, jsonArray4);

            int index = 0;

            // The length is 50 and numberOfCharactersRead = 0,1,2,...,49
            if (jsonArray.length() > index) {

                // ********** Created jsonArrayOfMovies has content
                // ********** Create a Movie object corresponding to each JSON object in jsonArrayOfMovies
                while (jsonArray.length() > index) {

                    // Get the JSONObject at numberOfCharactersRead
                    JSONObject jsonObject = jsonArray.getJSONObject(index);

                    // Obtain fields: id, name, synopsis, mpaarating and runtime of a Movie object
                    Long id = jsonObject.optLong("id",0);
                    String name = jsonObject.optString("title","");
                    String synopsis = jsonObject.optString("overview","");

                    // Obtain the thumbnail image url
                    
                    String imageUrl = jsonObject.optString("poster_path","not found");
                    if(imageUrl.equals("not found")){
                        imageUrl = "resources/images/poster.png";
                    }
                    else{
                        imageUrl = tmdbRESTfulImageService_url + imageUrl;
                    }

                    JSONObject movieJsonObject = new JSONObject(readUrlContent(tmdbRESTfulCastServicePart1_url + id + tmdbRESTfulCastServicePart2_url));
                    if (! movieJsonObject.toString().equals("{}")) {                        
                        String imdbId = movieJsonObject.optString("imdb_id","N.A.");
                        JSONObject creditsJsonObject = movieJsonObject.optJSONObject("credits");
                        JSONArray abridgedCastJsonArray = new JSONArray(creditsJsonObject.optJSONArray("cast").toString());
                        // Obtain list of actor names from the abridged_cast JSON Array                    
                        List<String> cast = getListOfCastNames(abridgedCastJsonArray);

                        JSONObject omdbJsonObject = new JSONObject(readUrlContent(omdbRESTfulRatingServicePart1_url + imdbId + omdbRESTfulRatingServicePart2_url));
                        String mpaaRating = omdbJsonObject.optString("Rated","");
                        String runtime = omdbJsonObject.optString("Runtime","");

                        String releaseDate = omdbJsonObject.optString("Released","");
                        String imdbRating = omdbJsonObject.optString("imdbRating","N/A");
                        Movie movie = new Movie(id, imdbId, name, releaseDate, mpaaRating, imdbRating, imageUrl, cast, runtime, synopsis);
                        movies.add(movie);
                    }
                    else
                    {
                        List<String> cast = new ArrayList<>();
                        cast.add("");
                        Movie movie = new Movie(id, "", name, "", "", "N/A", imageUrl, cast, "", synopsis);
                        movies.add(movie);
                    }

                    // Construct a new movie object usin the information gathered above
                    
                    // Add the newly created movie object to the list of movie objects
                    
                    index++;
                }

            } else {
                statusMessage = "The TMDB and OMDB web services are unreachable!";
                return;
            }
        } catch (Exception ex) {
            Logger.getLogger(MovieManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        
    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getJsonCategoryData() {
        return jsonCategoryData;
    }

    public void setJsonCategoryData(String jsonCategoryData) {
        this.jsonCategoryData = jsonCategoryData;
    }

    /*
    ================
    Instance Methods
    ================
     */
    /**
     * Return the content of a given URL as String
     *
     * @param webServiceURL to retrieve the JSON data from
     * @return JSON data from the given URL as String
     * @throws Exception
     */
    public String readUrlContent(String webServiceURL) {
        /*
        reader is an object reference pointing to an object instantiated from the BufferedReader class.
        Currently, it is "null" pointing to nothing.
         */
        BufferedReader reader = null;

        try {
            // Create a URL object from the webServiceURL given
            URL url = new URL(webServiceURL);
            /*
            The BufferedReader class reads text from a character-input stream, buffering characters
            so as to provide for the efficient reading of characters, arrays, and lines.
             */
            reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // Create a mutable sequence of characters and store its object reference into buffer
            StringBuilder buffer = new StringBuilder();

            // Create an array of characters of size 10240
            char[] chars = new char[10240];

            int numberOfCharactersRead;
            /*
            The read(chars) method of the reader object instantiated from the BufferedReader class
            reads 10240 characters as defined by "chars" into a portion of a buffered array.

            The read(chars) method attempts to read as many characters as possible by repeatedly
            invoking the read method of the underlying stream. This iterated read continues until
            one of the following conditions becomes true:

                (1) The specified number of characters have been read, thus returning the number of characters read.
                (2) The read method of the underlying stream returns -1, indicating end-of-file, or
                (3) The ready method of the underlying stream returns false, indicating that further input requests would block.

            If the first read on the underlying stream returns -1 to indicate end-of-file then the read(chars) method returns -1.
            Otherwise the read(chars) method returns the number of characters actually read.
             */
            while ((numberOfCharactersRead = reader.read(chars)) != -1) {
                buffer.append(chars, 0, numberOfCharactersRead);
            }

            // Return the String representation of the created buffer
            return buffer.toString();

        } catch (Exception e) {
            return "{}";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    Logger.getLogger(MovieManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Return the list of cast names of a movie as a list of strings for JSON
     * array of cast details received from the API
     *
     * @param abridgedCastJsonArray to retrieve the JSON data about the cast
     * @return cast names of the movie as a List of Strings
     */
    private List<String> getListOfCastNames(JSONArray abridgedCastJsonArray) {

        //Initialize an empty list of strings and an index
        List<String> castNamesList = new ArrayList<>();
        int index = 0;

        // Check if the JSON array of cast details is not empty
        if (abridgedCastJsonArray.length() > index) {

            // loop over each JSON object in the JSON array
            while (abridgedCastJsonArray.length() > index) {

                /*
                Extract the cast name from each JSON object
                and append it to the list of cast names
                 */
                JSONObject jsonObject = abridgedCastJsonArray.getJSONObject(index);
                castNamesList.add(jsonObject.getString("name"));

                // increment the index for the loop
                index++;
            }
        }

        // return the list of cast names
        return castNamesList;
    }

    private JSONArray concatArray(JSONArray... arrs)
            throws JSONException {
        JSONArray result = new JSONArray();
        for (JSONArray arr : arrs) {
            for (int i = 0; i < arr.length(); i++) {
                result.put(arr.get(i));
            }
        }
        return result;
    }
    
}
