package bg.hotelmap.hotelmap.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.GalleryModel;

/**
 * Created by Teo on 10-Oct-16.
 */

public class Gallery extends ListFragment {

    private ArrayList<GalleryModel> models;
    private GalleryAdapter galleryAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        models = new ArrayList<GalleryModel>();
        models.add(new GalleryModel("Varna","Spa hotel","Hotel Varna",4,+359123456,40, GalleryModel.Type.HOTEL));
        models.add(new GalleryModel("Stara Zagora","Concert","Tsarevets 9",2,+359123456,40, GalleryModel.Type.EVENT));
        models.add(new GalleryModel("Shumen","Madarski konnik","Tova e edin po dulug text mnogo po dulug po dulug i dulug",4,+359123456,40, GalleryModel.Type.SIGHT));
        models.add(new GalleryModel("Sofia","Shop","Somewhere else",4,+359123456,40, GalleryModel.Type.SHOP));

        galleryAdapter = new GalleryAdapter(getActivity(),models);
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
