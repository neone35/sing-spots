package com.github.neone35.singspots.data.models;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class AliasesItem {

    @SerializedName("begin-date")
    private String beginDate;

    @SerializedName("end-date")
    private String endDate;

    @SerializedName("type-id")
    private String typeId;

    @SerializedName("name")
    private String name;

    @SerializedName("sort-name")
    private String sortName;

    @SerializedName("locale")
    private Object locale;

    @SerializedName("type")
    private String type;

    @SerializedName("primary")
    private Object primary;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public Object getLocale() {
        return locale;
    }

    public void setLocale(Object locale) {
        this.locale = locale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getPrimary() {
        return primary;
    }

    public void setPrimary(Object primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return
                "AliasesItem{" +
                        "begin-date = '" + beginDate + '\'' +
                        ",end-date = '" + endDate + '\'' +
                        ",type-id = '" + typeId + '\'' +
                        ",name = '" + name + '\'' +
                        ",sort-name = '" + sortName + '\'' +
                        ",locale = '" + locale + '\'' +
                        ",type = '" + type + '\'' +
                        ",primary = '" + primary + '\'' +
                        "}";
    }
}