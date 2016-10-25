package bg.hotelmap.hotelmap.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.GalleryModel;
import bg.hotelmap.hotelmap.models.OfferModel;

/**
 * Created by Teo on 10-Oct-16.
 */

public class Offer extends ListFragment {

    private ArrayList<OfferModel> models;
    private OfferAdapter offerAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        models = new ArrayList<OfferModel>();
        models.add(new OfferModel("Hotel name","address","shortDesc","link1","link2","desc",40,20, "2016-11:1 12:00:00", OfferModel.Type.HOTEL));
        models.add(new OfferModel("Cherno more","Varna","Tova e edin luksozen hotel za 2ma","link1","link2","Tova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2ma",50,20, "2016-12-1 12:00:00", OfferModel.Type.EVENT));
        models.add(new OfferModel("Madara","Shumen","Tova e edin luksozen hotel za 2ma","link1","link2","desc",40,10, "2016-12-13 12:00:00", OfferModel.Type.SIGHT));
        models.add(new OfferModel("Test","Sofia","Tova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2ma","link1","link2","Tova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2maTova e edin luksozen hotel za 2ma",100,57, "2016-12-1 13:00:00", OfferModel.Type.SHOP));


        offerAdapter = new OfferAdapter(getActivity(),models);
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
