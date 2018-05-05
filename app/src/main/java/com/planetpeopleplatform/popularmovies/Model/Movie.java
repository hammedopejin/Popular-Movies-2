package com.planetpeopleplatform.popularmovies.Model;

/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class Movie {
    private String originalTitle;
    private String synopsis;
    private String ratings;
    private String releaseDate;
    private String thumdnailImage; //drawable reference id

    public Movie ( String origTitle, String synopsis, String ratings, String relDate, String thumdImage){
        this.originalTitle = origTitle;
        this.synopsis = synopsis;
        this.ratings = ratings;
        this.releaseDate = relDate;
        this.thumdnailImage = thumdImage;
    }

    public String getThumdnailImage () {
        return thumdnailImage;
    }
    public void setThumdnailImage (String thumdnailImage){
        this.thumdnailImage = thumdnailImage;
    }

    public String getOriginalTitle () {
        return originalTitle;
    }
    public void setOriginalTitle (String originalTitle){
        this.originalTitle = originalTitle;
    }

    public String getSynopsis () {
        return synopsis;
    }
    public void setSynopsis (String synopsis){
        this.synopsis = synopsis;
    }

    public String getRatings () {
        return ratings;
    }
    public void setRatings (String ratings){
        this.ratings = ratings;
    }

    public String getReleaseDate () {
        return releaseDate;
    }
    public void setReleaseDate (String releaseDate){
        this.releaseDate = releaseDate;
    }
}
