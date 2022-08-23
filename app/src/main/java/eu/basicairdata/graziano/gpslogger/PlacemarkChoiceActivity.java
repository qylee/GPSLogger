package eu.basicairdata.graziano.gpslogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlacemarkChoiceActivity extends AppCompatActivity {
    public static final String TAG = "PlacemarkChoiceActivity";

    public static List<PlacemarkItem> items = new ArrayList<>(Arrays.asList(
        new PlacemarkItem("미분류", "미분류된 장소입니다.", LocationExtended.UNCATEGORIZED_AREA),
        new PlacemarkItem("어린이보호구역", "어린이보호구역입니다.", LocationExtended.CHILD_SANCTUARY),
        new PlacemarkItem("비보호좌회전 교차로", "비보호좌회전 신호가 있는 교차로 구역입니다.", LocationExtended.LEFT_TURN_AT_YOUR_OWN_RISK),
        new PlacemarkItem("불법주정차구역", "주정차하면 안되는 구역입니다.", LocationExtended.ILLEGAL_PARKING_AREA),
        new PlacemarkItem("학교 주변구역", "학교 주변구역입니다.", LocationExtended.SCHOOL_AREA),
        new PlacemarkItem("아파트 진출입로", "아파트 차량이 출입하는 구역입니다.", LocationExtended.APARTMENT_ENTRANCE_AREA),
        new PlacemarkItem("보행자전용 도로", "보행자만 다닐 수 있는 보도입니다.", LocationExtended.PEDESTRIAN_ROAD),
        new PlacemarkItem("지하철역 주변구역", "지하철 출입구역입니다.", LocationExtended.SUBWAY_STATION_AREA),
        new PlacemarkItem("보도 단차 지점", "보도 단차가 있는 지점입니다..", LocationExtended.EDGE_HIGH_AREA)
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Log.w("myApp", "[#] " + this + " - onCreate()");

        AppCompatDelegate.setDefaultNightMode(Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("prefColorTheme", "2")));
        setTitle(R.string.annotations);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placemark_choice);

        ListView listView = findViewById(R.id.id_placemarks_list);

        PlacemarksListViewAdapter adapter = new PlacemarksListViewAdapter(items, getBaseContext());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            // Shows the Placemark Dialog for description
            FragmentManager fm = getSupportFragmentManager();
            FragmentPlacemarkDialog placemarkDialog = new FragmentPlacemarkDialog(this, items.get(i));
            placemarkDialog.show(fm, "");
        });
    }
}