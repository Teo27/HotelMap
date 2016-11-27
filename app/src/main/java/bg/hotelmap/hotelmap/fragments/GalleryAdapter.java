package bg.hotelmap.hotelmap.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.ObjectOfInterest;

/**
 * Created by Teo on 19-Oct-16.
 */

class GalleryAdapter extends ArrayAdapter<ObjectOfInterest> {

    private int pos = -1;

    GalleryAdapter(Context context, ArrayList<ObjectOfInterest> models) {
        super(context, 0, models);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final ObjectOfInterest model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row, parent, false);
        }
        if (pos == position) {
            View view2 = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row_clicked, parent, false);

            Button btn_around = (Button) view2.findViewById(R.id.gallery_btn_around);
            Button btn_info = (Button) view2.findViewById(R.id.gallery_btn_info);
            Button btn_map = (Button) view2.findViewById(R.id.gallery_btn_map);
            Button btn_reserve = (Button) view2.findViewById(R.id.gallery_btn_reserve);
            Button btn_site = (Button) view2.findViewById(R.id.gallery_btn_site);

            btn_around.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Around", Toast.LENGTH_SHORT).show();
                }
            });
            btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Info", Toast.LENGTH_SHORT).show();
                }
            });
            btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Map", Toast.LENGTH_SHORT).show();
                }
            });
            btn_reserve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Reserve", Toast.LENGTH_SHORT).show();
                }
            });
            btn_site.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Site", Toast.LENGTH_SHORT).show();
                }
            });

            return view2;
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_row, parent, false);
        }

        TextView city = (TextView) convertView.findViewById(R.id.gallery_row_address);
        TextView subtype = (TextView) convertView.findViewById(R.id.gallery_row_subtype);
        TextView name = (TextView) convertView.findViewById(R.id.gallery_row_name);
        TextView stars = (TextView) convertView.findViewById(R.id.gallery_row_star);
        TextView phone = (TextView) convertView.findViewById(R.id.gallery_row_phone);
        TextView price = (TextView) convertView.findViewById(R.id.gallery_row_box_price);
        ImageView type = (ImageView) convertView.findViewById(R.id.gallery_row_box_type);

        Resources res = convertView.getResources();
        assert model != null;
        city.setText(model.getAddress());
        subtype.setText(model.getSubtype());
        name.setText(model.getName());
        stars.setText(String.valueOf(model.getStar()));
        phone.setText(res.getString(R.string.gal_phone, String.valueOf(model.getPhone())));
        price.setText(String.valueOf(model.getPrice()));
        type.setImageResource(model.getAssociatedDrawable());

        ImageButton btn_call = (ImageButton) convertView.findViewById(R.id.gallery_row_call);

        final View finalView = convertView;
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + String.valueOf(model.getPhone())));
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    finalView.getContext().startActivity(callIntent);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        return convertView;
    }

    void selectedItem(int position)
    {
        this.pos = position; //position must be a global variable
    }

}
