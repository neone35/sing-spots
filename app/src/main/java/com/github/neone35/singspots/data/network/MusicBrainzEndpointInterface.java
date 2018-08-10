package com.github.neone35.singspots.data.network;

import com.github.neone35.singspots.data.models.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MusicBrainzEndpointInterface {

    @Headers({
            "User-Agent: SingSpots/1.0 (https://github.com/neone35/sing-spots)"
    })
    @GET("/ws/2/place?fmt=json")
    Call<PlacesResponse> getPlaces(
            @Query("query") String searchQuery, @Query("limit") int limit,
            @Query("offset") int offset);
}
