package bg.hotelmap.hotelmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

/**
 * Created by Teo on 05-Oct-16.
 */

public class Splash_Screen extends Activity {

    //private static int SPLASH_STATIC_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen_layout);

        /*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this,Navigation_Activity.class);
                startActivity(intent);

                finish();
            }
        }, SPLASH_STATIC_DELAY);
        */

        ImageButton btn_hotel = (ImageButton) findViewById(R.id.splash_hotels);
        ImageButton btn_sights = (ImageButton) findViewById(R.id.splash_sights);
        ImageButton btn_shops = (ImageButton) findViewById(R.id.splash_shops);
        ImageButton btn_events = (ImageButton) findViewById(R.id.splash_events);
        ImageButton btn_all = (ImageButton) findViewById(R.id.splash_all);

        btn_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(0);
            }
        });

        btn_sights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(1);
            }
        });

        btn_shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(2);
            }
        });

        btn_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(3);
            }
        });

        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(4);
            }
        });
    }

    private void start(int value){
        Intent i = new Intent(getApplicationContext(), Navigation_Activity.class);
        i.putExtra("search_value",value);
        startActivity(i);
        finish();
    }

}
