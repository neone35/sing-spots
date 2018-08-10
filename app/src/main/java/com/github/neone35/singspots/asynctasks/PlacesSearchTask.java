package com.github.neone35.singspots.asynctasks;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.github.neone35.singspots.R;
import com.github.neone35.singspots.data.models.PlacesItem;
import com.github.neone35.singspots.data.network.NetworkUtils;
import com.orhanobut.logger.Logger;

import java.util.List;


public class PlacesSearchTask extends AsyncTask<String, Void, List<PlacesItem>> {

    private static final String MUSIC_BRAINZ_BASE_URL = "https://musicbrainz.org/ws/2/";
    private static final int REQUEST_LIMIT = 20;

    private ProgressDialog dialog;
    private OnAsyncEventListener<List<PlacesItem>> mCallBack;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private Exception mException;

    public PlacesSearchTask(Context context, OnAsyncEventListener<List<PlacesItem>> callback) {
        mContext = context;
        mCallBack = callback;
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
            return NetworkUtils.getPlacesResponse(searchString);
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
