package eu.basicairdata.graziano.gpslogger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class PlacemarkChoiceActivity extends AppCompatActivity {
    public static final String TAG = "PlacemarkChoiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placemark_choice);

        ListView listView = findViewById(R.id.id_placemarks_list);

        ArrayList<PlacemarkItem> items = new ArrayList<>();
        items.add(new PlacemarkItem("미분류", "미분류된 장소입니다.", LocationExtended.UNCATEGORIZED_AREA));
        items.add(new PlacemarkItem("어린이보호구역", "어린이보호구역입니다.", LocationExtended.CHILD_SANCTUARY));
        items.add(new PlacemarkItem("비보호좌회전 교차로", "비보호좌회전 신호가 있는 교차로 구역입니다.", LocationExtended.LEFT_TURN_AT_YOUR_OWN_RISK));
        items.add(new PlacemarkItem("불법주정차구역", "주정차하면 안되는 구역입니다.", LocationExtended.ILLEGAL_PARKING_AREA));
        items.add(new PlacemarkItem("학교 주변구역", "학교 주변구역입니다.", LocationExtended.SCHOOL_AREA));
        items.add(new PlacemarkItem("아파트 진출입로", "아파트 차량이 출입하는 구역입니다.", LocationExtended.APARTMENT_ENTRANCE_AREA));
        items.add(new PlacemarkItem("보행자전용 도로", "보행자만 다닐 수 있는 보도입니다.", LocationExtended.PEDESTRIAN_ROAD));
        items.add(new PlacemarkItem("지하철역 주변구역", "지하철 출입구역입니다.", LocationExtended.SUBWAY_STATION_AREA));
        items.add(new PlacemarkItem("보도 단차 지점", "보도 단차가 있는 지점입니다..", LocationExtended.EDGE_HIGH_AREA));

        PlacemarksListViewAdapter adapter = new PlacemarksListViewAdapter(items, getApplicationContext());

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getBaseContext(), GPSActivity.class);
            intent.putExtra(getString(R.string.key_placemark), items.get(i));
            Log.d(TAG, items.get(i).toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}