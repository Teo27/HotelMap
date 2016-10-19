package bg.hotelmap.hotelmap.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.GalleryModel;

/**
 * Created by Teo on 19-Oct-16.
 */

class GalleryAdapter extends ArrayAdapter<GalleryModel> {

    private int pos = -1;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> models){
        super(context, 0, models);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GalleryModel model = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row, parent, false);
        }
        if(pos == position)
        {
            View view2 = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row_clicked, parent, false);
            return view2;
        }else{
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row, parent, false);
        }

        TextView city = (TextView) convertView.findViewById(R.id.gallery_row_city);
        TextView subtype = (TextView) convertView.findViewById(R.id.gallery_row_subtype);
        TextView name = (TextView) convertView.findViewById(R.id.gallery_row_name);
        TextView stars = (TextView) convertView.findViewById(R.id.gallery_row_star);
        TextView phone = (TextView) convertView.findViewById(R.id.gallery_row_phone);
        TextView price = (TextView) convertView.findViewById(R.id.gallery_row_box_price);
        ImageView type = (ImageView) convertView.findViewById(R.id.gallery_row_box_type);

        Resources res = convertView.getResources();
        city.setText(model.getCity());
        subtype.setText(model.getSubtype());
        name.setText(model.getName());
        stars.setText(String.valueOf(model.getStar()));
        phone.setText(res.getString(R.string.gal_phone,String.valueOf(model.getPhone())));
        price.setText(String.valueOf(model.getPrice()));
        type.setImageResource(model.getAssociatedDrawable());



        return convertView;
    }

    public void selectedItem(int position)
    {
        this.pos = position; //position must be a global variable
    }

}
