package bg.hotelmap.hotelmap.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import bg.hotelmap.hotelmap.R;

/**
 * Created by Teo on 27-Nov-16.
 */

public class ObjectOfInterest implements ClusterItem {

    private String name,address,shortDesc, desc, subtype, url, period;
    private int price, discountPrice,discount,star, phone;
    private Type type;
    private double lat, lng;


    public ObjectOfInterest(String name, String address, Type type, String subtype, int price, int star, int phone, String shortDesc, String desc, String url, double lat, double lng) {

        this.name = name;
        this.address = address;
        this.type = type;
        this.subtype = subtype;
        this.price = price;
        this.star = star;
        this.phone = phone;

        this.shortDesc = shortDesc;
        this.desc = desc;
        this.url = url;

        this.lat = lat;
        this.lng = lng;
    }

    //with discount offer
    public ObjectOfInterest(String name, String address, Type type, String subtype, int price, int star, int phone, String shortDesc, String desc, String url, double lat, double lng, String period, int discountPrice) {

        this.name = name;
        this.address = address;
        this.type = type;
        this.subtype = subtype;
        this.price = price;
        this.star = star;
        this.phone = phone;

        this.shortDesc = shortDesc;
        this.desc = desc;
        this.url = url;

        this.lat = lat;
        this.lng = lng;

        this.period = period;
        this.discountPrice = discountPrice;
        this.discount = (int) ((1 - (double) discountPrice / (double) price)*100);
    }

    public enum Type {HOTEL, SHOP, SIGHT, EVENT}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getUrl() {
        return url;
    }

    public String getPeriod() {
        return period;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public int getStar() {
        return star;
    }

    public int getPhone() {
        return phone;
    }

    public Type getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    private double getLat() {
        return lat;
    }

    private double getLng() {
        return lng;
    }

    public LatLng getPosition() {
        return new LatLng(getLat(),getLng());
    }

    private static int typeToDrawable(ObjectOfInterest.Type type){

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
