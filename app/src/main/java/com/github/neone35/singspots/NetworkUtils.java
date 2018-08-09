package com.github.neone35.singspots;

import com.github.neone35.singspots.models.PlacesItem;
import com.github.neone35.singspots.models.PlacesResponse;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {

    private static final String MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/";
    private static final int REQUEST_LIMIT = 20;

    // Fetch and parse recipes into Recipe model
    public static List<PlacesItem> getPlacesResponse(String searchString) {
        MusicBrainzEndpointInterface mbApiEndpointInterface = getApiService();
        Call<PlacesResponse> retroCall = mbApiEndpointInterface.getPlaces(searchString, REQUEST_LIMIT, 0);
        List<PlacesItem> placesList = new ArrayList<>();
        try {
            // get main response object
            PlacesResponse placesResponse = retroCall.execute().body();
            if (placesResponse != null) {
                // get number of results
                int mResultNum = placesResponse.getCount();
                // if got more results than specified by limit
                if (mResultNum > REQUEST_LIMIT) {
                    // do additional requests as pages
                    int pagesNum = mResultNum / REQUEST_LIMIT;
                    for (int i = 0; i < pagesNum; i++) {
                        Call<PlacesResponse> retroPageCall = mbApiEndpointInterface.getPlaces(searchString, REQUEST_LIMIT, REQUEST_LIMIT * i);
                        PlacesResponse placesPageResponse = retroPageCall.execute().body();
                        if (placesPageResponse != null) {
                            List<PlacesItem> placesItemPageList = placesPageResponse.getPlaces();
                            placesList.addAll(placesItemPageList);
                        }
                    }
                }
            } else {
                Logger.d("No initial response received.");
            }
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
        return placesList;
    }


    private static MusicBrainzEndpointInterface getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MusicBrainzEndpointInterface.class);
    }


}
