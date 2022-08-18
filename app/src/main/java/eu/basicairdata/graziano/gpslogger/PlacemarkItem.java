package eu.basicairdata.graziano.gpslogger;

public class PlacemarkItem {
    String name;
    String description;
    int type;

    PlacemarkItem(String name, String description, int type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
