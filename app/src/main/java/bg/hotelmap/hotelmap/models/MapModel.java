package bg.hotelmap.hotelmap.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import bg.hotelmap.hotelmap.R;

/**
 * Created by Teo on 28-Oct-16.
 */

public class MapModel implements ClusterItem {

    private String address, subtype, name, url, phone;
    private int id, star;
    private double price, lat, lng;
    private Type type;

    public enum Type {HOTEL, SHOP, SIGHT, EVENT}

    public MapModel(String address, String subtype, String name, String url, int star, String phone, double lat, double lng, double price, Type type) {
        this.address = address;
        this.subtype = subtype;
        this.name = name;
        this.url = url;
        this.star = star;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
        this.price = price;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getId() {
        return id;
    }

    public int getStar() {
        return star;
    }

    public String getPhone() {
        return phone;
    }

    public double getPrice() {
        return price;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public LatLng getPosition() {

        LatLng position = new LatLng(getLat(),getLng());

        return position;
    }

    public Type getType() {
        return type;
    }

    private static int typeToDrawable(MapModel.Type type){

        switch (type){
            case HOTEL:
                return R.drawable.ic_hotel;
            case SHOP:
                return R.drawable.ic_shop;
            case SIGHT:
                return R.drawable.ic_sight;
            case EVENT:
                return R.drawable.ic_event;
        }
        return R.drawable.ic_hotel;
    }

    public int getAssociatedDrawable(){
        return typeToDrawable(type);
    }
}
