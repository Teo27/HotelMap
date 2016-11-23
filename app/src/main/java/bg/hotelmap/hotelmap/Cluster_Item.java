package bg.hotelmap.hotelmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Teo on 07-Nov-16.
 */

public class Cluster_Item implements ClusterItem {

    private final LatLng mPosition;

    public Cluster_Item(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}