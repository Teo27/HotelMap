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

public class Gallery extends ListFragment {

    private GalleryAdapter galleryAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ObjectLoad ol = new ObjectLoad();
        ol.setup();
        ArrayList<ObjectOfInterest> objects = ol.getObjects();


        galleryAdapter = new GalleryAdapter(getActivity(), objects);
        setListAdapter(galleryAdapter);
        getListView().setDivider(getResources().getDrawable(R.drawable.divider));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        galleryAdapter.selectedItem(position);
        ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
    }
}
