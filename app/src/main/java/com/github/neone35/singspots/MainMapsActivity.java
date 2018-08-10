package com.github.neone35.singspots;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
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
import com.google.maps.android.ui.IconGenerator;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class MainMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public static final int REQUEST_LIMIT = 20;
    public static final int YEAR_FROM = 20;

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
        }

//        setSupportActionBar(toolbar);
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

    private void fetchPlaces(String searchString) {
        Logger.d("Fetch places is called");
        PlacesSearchTask placesSearchTask = new PlacesSearchTask(this, new OnAsyncEventListener<List<PlacesItem>>() {
            @Override
            public void onSuccess(List<PlacesItem> placesItemList) {
                Logger.d(placesItemList);
                if (!placesItemList.isEmpty()) {
                    mMap.clear();
                    List<Marker> markerList = new ArrayList<>();
                    int placesNum = placesItemList.size();
                    for (int i = 0; i < placesNum; i++) {
                        PlacesItem placesItem = placesItemList.get(i);
                        // get place name
                        String placeName = placesItem.getName();
                        // get coordinates
                        Coordinates coords = placesItem.getCoordinates();
                        if (coords != null) {
                            float latitude = Float.valueOf(coords.getLatitude());
                            float longitude = Float.valueOf(coords.getLongitude());
                            LatLng latLng = new LatLng(latitude, longitude);
                            Logger.d(latLng);
                            // Build an icon with place name
                            IconGenerator iconGenerator = new IconGenerator(MainMapsActivity.this);
                            iconGenerator.setStyle(IconGenerator.STYLE_RED);
                            Bitmap bitmap = iconGenerator.makeIcon(placeName);
                            BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
                            // Build a marker
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .icon(icon);
                            Marker placeMarker = mMap.addMarker(markerOptions);
                            markerList.add(placeMarker);
//                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        } else {
                            Logger.d("No coords received for place name " + placeName);
                        }
                    }
                    if (!markerList.isEmpty()) {
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markerList) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();
                        int padding = 0; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);
                    } else {
                        ToastUtils.showShort("No spots with coordinates found");
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
