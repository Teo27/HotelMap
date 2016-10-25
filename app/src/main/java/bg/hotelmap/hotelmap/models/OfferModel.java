package bg.hotelmap.hotelmap.models;


/**
 * Created by Teo on 24-Oct-16.
 */

public class OfferModel {

    private String name,address,shortDesc, link1, link2, desc;
    private int id,price, discountPrice,discount;
    private String period;
    private Type type;

    public enum Type {HOTEL, SHOP, SIGHT, EVENT}

    public OfferModel(String name, String address, String shortDesc, String link1, String link2, String desc, int price, int discountPrice, String period, Type type) {
        this.name = name;
        this.address = address;
        this.shortDesc = shortDesc;
        this.link1 = link1;
        this.link2 = link2;
        this.desc = desc;
        this.price = price;
        this.discountPrice = discountPrice;
        this.period = period;
        this.type = type;

        double discTemp = (1 - (double) discountPrice/ (double) price)*100;
        this.discount = (int) discTemp;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLink1() {
        return link1;
    }

    public String getLink2() {
        return link2;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public String getPeriod() {
        return period;
    }

    public Type getType() {
        return type;
    }

}
