package bg.hotelmap.hotelmap.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.GalleryModel;

/**
 * Created by Teo on 10-Oct-16.
 */

public class Offer extends ListFragment {

    private ArrayList<GalleryModel> models;
    private ArrayAdapter galleryAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        models = new ArrayList<GalleryModel>();
        models.add(new GalleryModel("Sofia","Spa hotel","Hotel Varna",4,+359123456,40, GalleryModel.Type.HOTEL));
        models.add(new GalleryModel("Stara ","Concert","Tsarevets 9",2,+359123456,40, GalleryModel.Type.EVENT));
        models.add(new GalleryModel("hhh","Madarski konnik","Tova e edin po dulug text mnogo po dulug po dulug i dulug",4,+359123456,40, GalleryModel.Type.SIGHT));
        models.add(new GalleryModel("asd","Shop","Somewhere else",4,+359123456,40, GalleryModel.Type.SHOP));

        galleryAdapter = new GalleryAdapter(getActivity(),models);
        setListAdapter(galleryAdapter);
    }
}
