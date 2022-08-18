package eu.basicairdata.graziano.gpslogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMaps extends Fragment {
    private GoogleMap googleMap;
    private final LatLng cityHallAnsan = new LatLng(37.3218, 126.8309);
    private final float defaultZoomLevel = 15.0f;
    private final float recordingZoomLevel = 19.0f;
    private LatLng lastPos = cityHallAnsan;
    private Map<LocationExtended, Marker> markerList = new HashMap<>();

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap map) {
            googleMap = map;
            UiSettings mapUiSettings = googleMap.getUiSettings();
            mapUiSettings.setZoomControlsEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cityHallAnsan, defaultZoomLevel));

            GPSActivity gpsActivity = (GPSActivity) getActivity();
            if (ActivityCompat.checkSelfPermission(gpsActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(gpsActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
                mapUiSettings.setMyLocationButtonEnabled(true);
            } else {
                gpsActivity.checkLocationPermission();
            }
            updatePlacemarks();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        updatePlacemarks();
        update();
    }

    @Override
    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        clearPlacemarks();
        super.onPause();
    }

    /**
     * The EventBus receiver for Short Messages.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Short msg) {
        if (msg == EventBusMSG.UPDATE_TRACK) {
            update();
        } else if (msg == EventBusMSG.ADD_PLACEMARK) {
            LocationExtended currentPlacemark = GPSApplication.getInstance().getCurrentPlacemark();
            addPlacemark(currentPlacemark);
        } else if (msg == EventBusMSG.FINALIZE_TRACK || msg == EventBusMSG.STOP_RECORDING) {
            clearPlacemarks();
        } else if (msg == EventBusMSG.START_RECORDING) {
            updatePlacemarks();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    /**
     * Updates the user interface of the fragment.
     * It takes care of visibility and value of each tile, and Track Status widgets.
     */
    public void update() {
        Track track = GPSApplication.getInstance().getCurrentTrack();
        if (track.isValid()) {
            lastPos = new LatLng(track.getLatitudeEnd(), track.getLongitudeEnd());
        }
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastPos, recordingZoomLevel));
        }
    }

    private void clearPlacemarks() {
        // clear all markers
        for (Marker marker: markerList.values()) {
            marker.remove();
        }
        markerList.clear();
    }

    private void updatePlacemarks() {
        GPSApplication app = GPSApplication.getInstance();
        if (app.getIsRecording()) {
            Track track = app.getCurrentTrack();
            if (track.isValid()) {
                List<LocationExtended> placemarkList = app.gpsDataBase.getPlacemarksList(track.getId(), 0, 1500);
                for (LocationExtended placemark: placemarkList) {
                    markerList.put(placemark,
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(placemark.getLatitude(), placemark.getLongitude()))
                                    .title(placemark.getDescription()))
                    );
                }
            }
        }
    }

    private void addPlacemark(LocationExtended placemark) {
        markerList.put(placemark,
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(placemark.getLatitude(), placemark.getLongitude()))
                        .title(placemark.getDescription()))
        );
    }
}