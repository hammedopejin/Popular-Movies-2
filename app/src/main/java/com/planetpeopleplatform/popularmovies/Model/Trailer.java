package com.planetpeopleplatform.popularmovies.Model;

/**
 * Created by Hammedopejin on 5/12/2018.
 */

public class Trailer {

    private String trailerId;
    private String key;
    private String name;
    private String site;
    private String size;
    private String type;
    private String thumbnail;


    public Trailer ( String trailerId, String key, String name, String site, String size, String type, String thumbnail){
        this.trailerId = trailerId;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    public String getTrailerId () {
        return trailerId;
    }
    public void setId (String trailerId){
        this.trailerId = trailerId;
    }

    public String getKey () {
        return key;
    }
    public void setKey (String key){
        this.key = key;
    }

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
