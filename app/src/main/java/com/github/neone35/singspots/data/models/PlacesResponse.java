package com.github.neone35.singspots.data.models;

import java.util.List;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class PlacesResponse {

    @SerializedName("places")
    private List<PlacesItem> places;

    @SerializedName("offset")
    private int offset;

    @SerializedName("created")
    private String created;

    @SerializedName("count")
    private int count;

    public List<PlacesItem> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlacesItem> places) {
        this.places = places;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return
                "PlacesResponse{" +
                        "places = '" + places + '\'' +
                        ",offset = '" + offset + '\'' +
                        ",created = '" + created + '\'' +
                        ",count = '" + count + '\'' +
                        "}";
    }
}