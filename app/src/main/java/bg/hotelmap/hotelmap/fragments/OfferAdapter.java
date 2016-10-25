package bg.hotelmap.hotelmap.fragments;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import bg.hotelmap.hotelmap.R;
import bg.hotelmap.hotelmap.models.OfferModel;

/**
 * Created by Teo on 25-Oct-16.
 */

public class OfferAdapter extends ArrayAdapter<OfferModel> {

    private int pos = -1;
    private Handler handler;
    private Runnable runnable;
    private String currency = " Leva";

    public OfferAdapter(Context context, ArrayList<OfferModel> models) {
        super(context, 0, models);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        OfferModel model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_row, parent, false);
        }
        if (pos == position) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_row_clicked, parent, false);

            /*
            final View finalConvertView = convertView;
            new Handler().post(new Runnable() {
                public void run() {
                    finalConvertView.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
                }
            });

            */

            TextView desc = (TextView) convertView.findViewById(R.id.offer_row_desc);
            Button link1 = (Button) convertView.findViewById(R.id.offer_row_link1);
            Button link2 = (Button) convertView.findViewById(R.id.offer_row_link2);
            final String url = model.getLink1();
            desc.setText(String.valueOf(model.getDesc()));

            link1.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                }
            });

            link2.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Info", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_row, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.offer_row_name);
            TextView address = (TextView) convertView.findViewById(R.id.offer_row_address);
            TextView discount = (TextView) convertView.findViewById(R.id.offer_row_discount);
            TextView period = (TextView) convertView.findViewById(R.id.offer_row_period);

            name.setText(String.valueOf(model.getName()));
            address.setText(String.valueOf(model.getAddress()));

            Resources res = convertView.getResources();

          //  int discountInt = Double.valueOf(model.getDiscount()).intValue();
            discount.setText("-" + String.valueOf(model.getDiscount()) + "%");

            try {
                countDownStart(period, model.getPeriod(), res);
            } catch (ParseException e) {
                period.setText(res.getString(R.string.error));
            }

        }

        //TODO change currency variable

        TextView shortDesc = (TextView) convertView.findViewById(R.id.offer_row_shortDesc);
        TextView price = (TextView) convertView.findViewById(R.id.offer_row_price);
        TextView discountPrice = (TextView) convertView.findViewById(R.id.offer_row_discountPrice);

        shortDesc.setText(String.valueOf(model.getShortDesc()));
        price.setText(String.valueOf(model.getPrice()) + currency);
        discountPrice.setText(String.valueOf(model.getDiscountPrice()) + currency);

        return convertView;
    }

    void selectedItem(int position) {
        this.pos = position; //position must be a global variable
    }

    private void countDownStart(final TextView period, String date, final Resources res) throws ParseException {
        final Date dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                try {

                    Date currentDate = new Date();

                    long diff = dateFormat.getTime() - currentDate.getTime();
                    long days = diff / (24 * 60 * 60 * 1000);
                    diff -= days * (24 * 60 * 60 * 1000);
                    long hours = diff / (60 * 60 * 1000);
                    diff -= hours * (60 * 60 * 1000);
                    long minutes = diff / (60 * 1000);
                    diff -= minutes * (60 * 1000);
                    long seconds = diff / 1000;

                    period.setText(res.getString(R.string.off_period,String.valueOf(days),String.valueOf(hours),String.valueOf(minutes),String.valueOf(seconds)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
