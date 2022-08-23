package eu.basicairdata.graziano.gpslogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PlacemarksListViewAdapter extends BaseAdapter {

    List<PlacemarkItem> itemList;
    Context context;

    public PlacemarksListViewAdapter(List<PlacemarkItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.layout_placemark_item, viewGroup, false);

        TextView tv_name = view.findViewById(R.id.textview_name);
        tv_name.setText(itemList.get(i).name);

        TextView tv_description = view.findViewById(R.id.textview_description);
        tv_description.setText(itemList.get(i).description);

        TextView tv_category = view.findViewById(R.id.textview_category);
        tv_category.setText(Integer.toString(itemList.get(i).category));

        return view;
    }
}
