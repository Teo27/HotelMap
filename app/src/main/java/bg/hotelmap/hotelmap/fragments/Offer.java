package bg.hotelmap.hotelmap.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import bg.hotelmap.hotelmap.ObjectLoad;
import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.ObjectOfInterest;

/**
 * Created by Teo on 10-Oct-16.
 */

public class Offer extends ListFragment {

    private OfferAdapter offerAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ObjectLoad ol = new ObjectLoad();
        ol.setup();
        ArrayList<ObjectOfInterest> objects = ol.getOffers();

        offerAdapter = new OfferAdapter(getActivity(),objects);
        setListAdapter(offerAdapter);
        getListView().setDivider(getResources().getDrawable(R.drawable.divider));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        offerAdapter.selectedItem(position);
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }
}
