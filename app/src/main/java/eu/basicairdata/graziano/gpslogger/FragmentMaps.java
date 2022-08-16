package eu.basicairdata.graziano.gpslogger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FragmentMaps extends Fragment {
    final GPSApplication gpsApp = GPSApplication.getInstance();
    private GoogleMap googleMap;
    private final LatLng cityHallAnsan = new LatLng(37.3218, 126.8309);
    private final float defaultZoomLevel = 15.0f;
    private LatLng lastPos = cityHallAnsan;

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
            update();
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

        update();
    }

    @Override
    public void onPause() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onPause();
    }

    /**
     * The EventBus receiver for Short Messages.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Short msg) {
        if (msg == EventBusMSG.UPDATE_TRACK) {
            update();
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
        Track track = gpsApp.getCurrentTrack();
        if (track.getLatitudeEnd() != GPSApplication.NOT_AVAILABLE && track.getLongitudeEnd() != GPSApplication.NOT_AVAILABLE) {
            lastPos = new LatLng(track.getLatitudeEnd(), track.getLongitudeEnd());
        }
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(defaultZoomLevel));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(lastPos));
        }
    }
}