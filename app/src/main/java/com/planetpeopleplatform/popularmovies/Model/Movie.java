package com.planetpeopleplatform.popularmovies.Model;

/**
 * Created by Hammedopejin on 4/27/2018.
 */

public class Movie {
    private String originalTitle;
    private String id;
    private String synopsis;
    private String ratings;
    private String releaseDate;
    private String thumbnailImage; //drawable reference id

    public Movie ( String origTitle, String synopsis, String ratings, String relDate, String thumbnailImage, String id){
        this.originalTitle = origTitle;
        this.synopsis = synopsis;
        this.ratings = ratings;
        this.releaseDate = relDate;
        this.thumbnailImage = thumbnailImage;
        this.id = id;
    }

    public String getThumbnailImage () {
        return thumbnailImage;
    }
    public void setThumbnailImage (String thumbnailImage){
        this.thumbnailImage = thumbnailImage;
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

    public String getId () {
        return id;
    }
    public void setId (String id){
        this.id = id;
    }
}
