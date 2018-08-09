package com.github.neone35.singspots;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.github.neone35.singspots.models.PlacesItem;
import com.github.neone35.singspots.models.PlacesResponse;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PlacesSearchTask extends AsyncTask<String, Void, List<PlacesItem>> {

    private static final String MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/";
    private static final int REQUEST_LIMIT = 20;

    private ProgressDialog dialog;
    private OnAsyncEventListener<List<PlacesItem>> mCallBack;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Exception mException;

    PlacesSearchTask(Context context, OnAsyncEventListener<List<PlacesItem>> callback) {
        mContext = context;
        mCallBack = callback;
    }

    private static MusicBrainzEndpointInterface getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MUSIC_BRAINZ_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MusicBrainzEndpointInterface.class);
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        String gettingPlaces = mContext.getResources().getString(R.string.gettings_places);
        dialog.setTitle(gettingPlaces);
        String pleaseWait = mContext.getResources().getString(R.string.please_wait);
        dialog.setMessage(pleaseWait);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected List<PlacesItem> doInBackground(String... strings) {
        Logger.d("PlacesSearchTask started");
        String searchString = strings[0];
        try {
            MusicBrainzEndpointInterface mbApiEndpointInterface = getApiService();
            Call<PlacesResponse> retroCall = mbApiEndpointInterface.getPlaces(searchString, REQUEST_LIMIT, 0);
            List<PlacesItem> placesList = new ArrayList<>();
            // get main response object
            PlacesResponse placesResponse = retroCall.execute().body();
            Logger.d(placesResponse);
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
            return placesList;
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<PlacesItem> placesItems) {
        dialog.dismiss();
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(placesItems);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

}
