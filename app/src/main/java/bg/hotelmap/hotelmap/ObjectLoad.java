package bg.hotelmap.hotelmap;

import android.app.Application;

import java.util.ArrayList;

import bg.hotelmap.hotelmap.models.ObjectOfInterest;

/**
 * Created by Teo on 27-Nov-16.
 */

public class ObjectLoad{

    private ArrayList<ObjectOfInterest> objects, offers;

    public ArrayList<ObjectOfInterest> getObjects(){
        return objects;
    }

    public ArrayList<ObjectOfInterest> getOffers(){
        return offers;
    }

    public void setup(){

        ObjectOfInterest obj1 = new ObjectOfInterest("Hotel Cherno more", "Varna", ObjectOfInterest.Type.HOTEL,"Spa hotel",39, 3, 12345678, "Maluk hotel za dvama", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",43.2193138, 27.8997223);
        ObjectOfInterest obj2 = new ObjectOfInterest("Detskikat", "Varna", ObjectOfInterest.Type.EVENT,"Kids Park",0, 1, 12345678, "Park za deca", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",43.2140504, 27.9147333);
        ObjectOfInterest obj3 = new ObjectOfInterest("Madarski konnik", "Shumen", ObjectOfInterest.Type.SIGHT,"Monument",0, 2, 12345678, "Kamenni chudesa", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",43.2774110,27.1189170);
        ObjectOfInterest obj4 = new ObjectOfInterest("Malibu", "Burgas", ObjectOfInterest.Type.SHOP,"Restaurant",10, 3, 12345678, "Restorant v centura na grada", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",42.4948020,27.4725480);

        ObjectOfInterest obj5 = new ObjectOfInterest("St Stefan Hotel", "Nessebar", ObjectOfInterest.Type.HOTEL,"Vintage hotel",48, 5, 12345678, "Star hotel za dvama ili poveche, +jivotni i deca", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",42.6577384,27.7332619,"2016-12-1 12:00:00",24);
        ObjectOfInterest obj6 = new ObjectOfInterest("Detska Jeleznica", "Plovdiv", ObjectOfInterest.Type.EVENT,"Kids Park",15, 1, 12345678, "Patuvaite s vlakche sus svoite deca", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",42.1395500,24.7291340,"2016-12-2 13:00:00",10);
        ObjectOfInterest obj7 = new ObjectOfInterest("Sofia Zoo", "Sofia", ObjectOfInterest.Type.SIGHT,"Zoo",30, 2, 12345678, "Nai golemiqt zookut v Sofia", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",42.6583395,23.3320773,"2017-1-12 12:00:00",25);
        ObjectOfInterest obj8 = new ObjectOfInterest("Mall Sofia", "Sofia", ObjectOfInterest.Type.SHOP,"Mall",100, 3, 12345678, "Mall v centura na grada", "Long description,Long description,Long description,Long description,Long description,Long description,Long description,Long description","http://hotelmap.bg",42.6982040,23.3084970,"2018-12-1 12:00:00", 75);

        objects = new ArrayList<>();
        offers = new ArrayList<>();

        objects.add(obj1);
        objects.add(obj2);
        objects.add(obj3);
        objects.add(obj4);

        objects.add(obj5);
        objects.add(obj6);
        objects.add(obj7);
        objects.add(obj8);

        offers.add(obj5);
        offers.add(obj6);
        offers.add(obj7);
        offers.add(obj8);
    }
}
