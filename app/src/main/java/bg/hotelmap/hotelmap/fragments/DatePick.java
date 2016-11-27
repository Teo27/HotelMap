package bg.hotelmap.hotelmap.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import bg.hotelmap.hotelmap.Navigation_Activity;

/**
 * Created by Teo on 27-Nov-16.
 */

public class DatePick extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");

        boolean state = getArguments().getBoolean("state");

        String max_date = null;

        if (getArguments().containsKey("max_date"))
            max_date = getArguments().getString("max_date");

        String min_date = null;

        if (getArguments().containsKey("min_date"))
            min_date = getArguments().getString("min_date");

        DatePickerDialog dp = new DatePickerDialog(getActivity(), (Navigation_Activity)getActivity(), year, month, day);

        if(state){

            Date min = c.getTime();
            dp.getDatePicker().setMinDate(min.getTime());

            if(max_date != null){

                try {
                    Date max = simpleDate.parse(max_date);
                    dp.getDatePicker().setMaxDate(max.getTime());
                } catch (ParseException e) {
                    System.out.println("Error occurred "+ e.getMessage());
                }
            }

            return dp;

        }else{

            try {
                Date min = simpleDate.parse(min_date);
                dp.getDatePicker().setMinDate(min.getTime());
            } catch (ParseException e) {
                System.out.println("Error occurred "+ e.getMessage());
            }

            return dp;
        }

    }

}
