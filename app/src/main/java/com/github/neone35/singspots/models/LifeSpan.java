package com.github.neone35.singspots.models;

import javax.annotation.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class LifeSpan {

    @SerializedName("ended")
    private Object ended;

    @SerializedName("begin")
    private String begin;

    @SerializedName("end")
    private String end;

    public Object getEnded() {
        return ended;
    }

    public void setEnded(Object ended) {
        this.ended = ended;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return
                "LifeSpan{" +
                        "ended = '" + ended + '\'' +
                        ",begin = '" + begin + '\'' +
                        ",end = '" + end + '\'' +
                        "}";
    }
}