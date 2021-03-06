package com.github.neone35.singspots;

import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.util.ToastUtils;
import com.facebook.stetho.Stetho;
import com.github.neone35.singspots.data.models.Coordinates;
import com.github.neone35.singspots.data.models.PlacesItem;
import com.github.neone35.singspots.asynctasks.OnAsyncEventListener;
import com.github.neone35.singspots.asynctasks.PlacesSearchTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.common.base.Splitter;
import com.google.maps.android.ui.IconGenerator;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int REQUEST_LIMIT = 20;
    public static final int YEAR_FROM = 1990;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main_maps);
        Logger.addLogAdapter(new AndroidLogAdapter());
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            ToastUtils.showShort("Map initialization failed");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private LatLng getLatLong(Coordinates coords) {
        float latitude = Float.valueOf(coords.getLatitude());
        float longitude = Float.valueOf(coords.getLongitude());
        return new LatLng(latitude, longitude);
    }

    private Marker generateMarker(LatLng latLng, String placeName, String beginYear) {
        // Build an icon with place name
        IconGenerator iconGenerator = new IconGenerator(MainMapsActivity.this);
        iconGenerator.setStyle(IconGenerator.STYLE_RED);
        Bitmap bitmap = iconGenerator.makeIcon(placeName + "\n" + beginYear);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        // Build a marker
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(icon);
        return mMap.addMarker(markerOptions);
    }

    private LatLngBounds getMarkerBounds(List<Marker> markerList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            builder.include(marker.getPosition());
        }
        return builder.build();
    }

    private int getBeginYear(String beginDate) {
        // "2017-03"
        List<String> dateSplitted = Splitter
                .on("-")
                .trimResults()
                .splitToList(beginDate);
        // "2017"
        String yearString = dateSplitted.get(0);
        return Integer.valueOf(yearString);
    }

    private void removeMarkerAfterSeconds(Marker marker, int seconds) {
        final Handler handler = new Handler();
        handler.postDelayed(marker::remove,
                seconds * 1000);
    }

    private void fetchPlaces(String searchString) {
        Logger.d("Fetch places is called");
        PlacesSearchTask placesSearchTask = new PlacesSearchTask(this, new OnAsyncEventListener<List<PlacesItem>>() {
            @Override
            public void onSuccess(List<PlacesItem> placesItemList) {
                // check if response is not empty
                if (!placesItemList.isEmpty()) {
                    // remove all markers
                    mMap.clear();
                    // create new empty marker list
                    List<Marker> markerList = new ArrayList<>();
                    // iterate over places
                    for (PlacesItem placesItem : placesItemList) {
                        String beginDate = placesItem.getLifeSpan().getBegin();
                        // check if place has specified begin date
                        if (beginDate != null) {
                            int beginYear = getBeginYear(beginDate);
                            // narrow to places after YEAR_FROM
                            if (beginYear >= YEAR_FROM) {
                                // get place name
                                String placeName = placesItem.getName();
                                // get coordinates
                                Coordinates coords = placesItem.getCoordinates();
                                // check if place has specified coordinates
                                if (coords != null) {
                                    LatLng latLng = getLatLong(coords);
                                    Marker placeMarker = generateMarker(latLng, placeName, String.valueOf(beginYear));
                                    markerList.add(placeMarker);
                                    // move camera to single added marker
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                    // set removal timer
                                    removeMarkerAfterSeconds(placeMarker, beginYear - YEAR_FROM);
                                } else {
                                    Logger.d("No coords found for " + placeName);
                                }
                            } else {
                                Logger.d("Place began before 1990");
                            }
                        }
                    }
                    // check if markers exist
                    if (!markerList.isEmpty()) {
                        int padding = 0; // offset from edges of the map in pixels
                        LatLngBounds latLngBounds = getMarkerBounds(markerList);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, padding);
                        // move camera to fit all added markers
                        mMap.animateCamera(cu);
                    } else {
                        ToastUtils.showShort("No spots found");
                    }
                } else {
                    ToastUtils.showShort("No spots found");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Logger.e(e.getMessage());
            }
        });
        placesSearchTask.execute(searchString, null, null);
    }

    // Menu icons are inflated
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
        }

        // fetch data on query submit
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchPlaces(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                // Set activity title to search query
                MainMapsActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return true;
    }
}
