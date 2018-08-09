package com.github.neone35.singspots.models;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Generated("com.robohorse.robopojogenerator")
public class PlacesItem {

    @SerializedName("area")
    private Area area;

    @SerializedName("score")
    private int score;

    @SerializedName("address")
    private String address;

    @SerializedName("life-span")
    private LifeSpan lifeSpan;

    @SerializedName("type-id")
    private String typeId;

    @SerializedName("name")
    private String name;

    @SerializedName("disambiguation")
    private String disambiguation;

    @SerializedName("coordinates")
    private Coordinates coordinates;

    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("aliases")
    private List<AliasesItem> aliases;

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LifeSpan getLifeSpan() {
        return lifeSpan;
    }

    public void setLifeSpan(LifeSpan lifeSpan) {
        this.lifeSpan = lifeSpan;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AliasesItem> getAliases() {
        return aliases;
    }

    public void setAliases(List<AliasesItem> aliases) {
        this.aliases = aliases;
    }

    @Override
    public String toString() {
        return
                "PlacesItem{" +
                        "area = '" + area + '\'' +
                        ",score = '" + score + '\'' +
                        ",address = '" + address + '\'' +
                        ",life-span = '" + lifeSpan + '\'' +
                        ",type-id = '" + typeId + '\'' +
                        ",name = '" + name + '\'' +
                        ",disambiguation = '" + disambiguation + '\'' +
                        ",coordinates = '" + coordinates + '\'' +
                        ",id = '" + id + '\'' +
                        ",type = '" + type + '\'' +
                        ",aliases = '" + aliases + '\'' +
                        "}";
    }
}