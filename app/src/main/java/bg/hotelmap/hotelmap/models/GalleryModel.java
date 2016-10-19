package bg.hotelmap.hotelmap.models;

import bg.hotelmap.hotelmap.R;

/**
 * Created by Teo on 19-Oct-16.
 */

public class GalleryModel {
    private String city, subtype, name;
    private int id, star, phone;
    private Type type;
    private double price;

    public enum Type {HOTEL, SHOP, SIGHT, EVENT}

    public GalleryModel(String city, String subtype, String name, int star, int phone, int price, Type type) {
        this.city = city;
        this.subtype = subtype;
        this.name = name;
        this.star = star;
        this.phone = phone;
        this.price = price;
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public static int typeToDrawable(Type type){

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
