package bg.hotelmap.hotelmap;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;

import bg.hotelmap.hotelmap.fragments.DatePick;
import bg.hotelmap.hotelmap.fragments.Gallery;
import bg.hotelmap.hotelmap.fragments.Offer;
import bg.hotelmap.hotelmap.models.ObjectOfInterest;


public class Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener, GoogleMap.OnMarkerClickListener, DatePickerDialog.OnDateSetListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private SupportMapFragment supportMapFragment;
    private int item_selected = 4;
    private GoogleMap map;
    private double progress_value;
    private GoogleApiClient googleApiClient;
    private AlertDialog alertDialog = null;
    private ClusterManager<ObjectOfInterest> mClusterManager;
    private TextView arrival, departure, hotelCounter, shopCounter, sightCounter, eventCounter, allCounter;
    private NavigationView navigationView;
    private List<Marker> markers = new ArrayList<>();
    private Boolean error = false;
    private LatLng myLocation;
    private String searchString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //get splash screen selection
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            item_selected = extras.getInt("search_value");
        }

        //create map
        supportMapFragment = SupportMapFragment.newInstance();
        supportMapFragment.getMapAsync(this);

        //create toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Search button stuff

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialogInit().show();
            }
        });

        //Navigation Drawer code

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(item_selected).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        //Navigation Drawer counters
        hotelCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_hotels));
        eventCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_events));
        shopCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_shops));
        sightCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_sights));
        allCounter = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_all));

        counterConfig();

        //Initializing object on screens
        spinnerInit();
    }

    private void counterConfig() {

        hotelCounter.setGravity(Gravity.CENTER_VERTICAL);
        hotelCounter.setTypeface(null, Typeface.BOLD);

        eventCounter.setGravity(Gravity.CENTER_VERTICAL);
        eventCounter.setTypeface(null, Typeface.BOLD);

        sightCounter.setGravity(Gravity.CENTER_VERTICAL);
        sightCounter.setTypeface(null, Typeface.BOLD);

        shopCounter.setGravity(Gravity.CENTER_VERTICAL);
        shopCounter.setTypeface(null, Typeface.BOLD);

        allCounter.setGravity(Gravity.CENTER_VERTICAL);
        allCounter.setTypeface(null, Typeface.BOLD);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void spinnerInit() {
        Spinner spinner = (Spinner) findViewById(R.id.fragment_select);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fragment_selection_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nav_settings) {
            Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "За нас", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_register) {
            Toast.makeText(this, "Регистрирай МИ", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_hotels) {
            showMarker(ObjectOfInterest.Type.HOTEL);
            setTitle("Hotels");
        } else if (id == R.id.nav_sights) {
            showMarker(ObjectOfInterest.Type.SIGHT);
            setTitle("Sights");
        } else if (id == R.id.nav_shops) {
            showMarker(ObjectOfInterest.Type.SHOP);
            setTitle("Shops");
        } else if (id == R.id.nav_events) {
            showMarker(ObjectOfInterest.Type.EVENT);
            setTitle("Events");
        } else if (id == R.id.nav_all) {
            showAllMarkers();
            setTitle("Hotel Map");
        } else if (id == R.id.nav_discount) {
            Toast.makeText(this, "Резервирай с отстъпка", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_around) {
            checkGPS();
        } else if (id == R.id.nav_scan) {
            Intent intent = new Intent(this, Code_Scanner.class);
            startActivityForResult(intent, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (supportMapFragment.isAdded())
            sFm.beginTransaction().hide(supportMapFragment).commit();

        switch (position) {
            case 0:
                if (!supportMapFragment.isAdded())
                    sFm.beginTransaction().add(R.id.map, supportMapFragment).commit();
                else
                    sFm.beginTransaction().show(supportMapFragment).commit();
                break;
            case 1:
                fm.beginTransaction().replace(R.id.list, new Gallery()).commit();
                break;
            case 2:
                fm.beginTransaction().replace(R.id.list, new Offer()).commit();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Barcode not found", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == 1000) {
            if (resultCode == -1) {
                aroundDialogInit().show();
            } else {
                Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkGPS() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {

                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();

                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        aroundDialogInit().show();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    Navigation_Activity.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(Navigation_Activity.this, "Unable to change turn GPS on, please do so manually", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });

    }

    private Dialog aroundDialogInit() {

        error = false;
        getLocation();
        if (error)
            throw new EmptyStackException();

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
        LayoutInflater inflater = Navigation_Activity.this.getLayoutInflater();
        View dialog_layout = inflater.inflate(R.layout.around_me_dialog, null);

        builder.setView(dialog_layout)
                .setPositiveButton(R.string.around_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                     //   Toast.makeText(Navigation_Activity.this, "Success" + progress_value, Toast.LENGTH_SHORT).show();
                        aroundMe();
                    }
                })
                .setNegativeButton(R.string.around_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.around_dialog_title);

        seekInit(dialog_layout);
        alertDialog = builder.create();
        return alertDialog;
    }

    private void seekInit(View dialog_layout) {

        SeekBar seekBar = (SeekBar) dialog_layout.findViewById(R.id.around_me_seek);
        final TextView seekText = (TextView) dialog_layout.findViewById(R.id.around_me_text);
        final String text = "Search up to %s km";
        seekBar.setProgress(30);
        progress_value = 3.0;
        seekBar.setMax(45);
        seekText.setText(String.format(text, seekBar.getProgress() / 10.0));

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = (progress / 10.0) + 0.5;
                        seekText.setText(String.format(text, progress_value));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekText.setText(String.format(text, progress_value));
                    }
                }
        );

    }

    private void getLocation() {
        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (ActivityCompat.checkSelfPermission(Navigation_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Navigation_Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    error = true;
                    return;
                }
                locationManager.removeUpdates(this);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            error = true;
            Toast.makeText(this, "There was a problem with getting your location", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void aroundMe() {

        int distance = (int) (progress_value*1000);
        //Toast.makeText(this, Integer.toString(distance), Toast.LENGTH_SHORT).show();
        if(myLocation == null) {
            Toast.makeText(this, "Could not get location", Toast.LENGTH_SHORT).show();
            return;
        }
        hideAllMarkers();

        for (Marker marker : markers) {
            if (SphericalUtil.computeDistanceBetween(myLocation, marker.getPosition()) < distance) {
                marker.setVisible(true);
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setOnMarkerClickListener(this);
        UiSettings settings = map.getUiSettings();
        settings.setMapToolbarEnabled(false);
        map.getUiSettings().setRotateGesturesEnabled(false);

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ObjectOfInterest>(this,map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(mClusterManager);
     //   map.setOnMarkerClickListener(mClusterManager);

        ObjectLoad ol = new ObjectLoad();
        ol.setup();
        ArrayList<ObjectOfInterest> objects = ol.getObjects();

        int hotelScore,eventScore,shopScore,sightScore;
        hotelScore = eventScore = shopScore = sightScore = 0;

        for(ObjectOfInterest model : objects){
            Marker marker = map.addMarker(new MarkerOptions().position(model.getPosition()).icon(BitmapDescriptorFactory.fromBitmap(infoWindowInit(model))));
            marker.setTag(model);
            markers.add(marker);

            //  mClusterManager.addItem(model);

            switch (model.getType()){
                case HOTEL:
                    hotelScore++;
                    hotelCounter.setText(String.valueOf(hotelScore));
                    break;
                case EVENT:
                    eventScore++;
                    eventCounter.setText(String.valueOf(eventScore));
                    break;
                case SHOP:
                    shopScore++;
                    shopCounter.setText(String.valueOf(shopScore));
                    break;
                case SIGHT:
                    sightScore++;
                    sightCounter.setText(String.valueOf(sightScore));
                    break;
            }
        }

        allCounter.setText(String.valueOf(markers.size()));

        onNavigationItemSelected(navigationView.getMenu().getItem(item_selected));

        //set camera start destination
        LatLngBounds.Builder b = new LatLngBounds.Builder();
        b.include(new LatLng(44.214555,22.67459));
        b.include(new LatLng(41.236022,25.288167));
        b.include(new LatLng(42.312700,22.360067));
        b.include(new LatLng(43.539550,28.607050));
        LatLngBounds bounds = b.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        googleMap.moveCamera(cu);
    }

    private Bitmap infoWindowInit(ObjectOfInterest model){

        View v = getLayoutInflater().inflate(R.layout.map_info_window, null);

        TextView price = (TextView) v.findViewById(R.id.map_info_price);
        LinearLayout picLL = (LinearLayout) v.findViewById(R.id.map_info_star);
        int stars = model.getStar();
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (12 * scale + 0.5f);
        if(stars>1){
            for(int i=0;i<stars-1;i++){
                ImageView myImage = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(pixels,pixels);
                myImage.setImageResource(R.drawable.ic_golden_star);
                myImage.setLayoutParams(params);
                picLL.addView(myImage);
            }
        }

        int temp_price  = (int) model.getPrice();
        price.setText(String.valueOf(temp_price));


        if (v.getMeasuredHeight() <= 0) {
            v.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;

        }else{

            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
            v.draw(c);

            return b;
        }


    }

    private void hideAllMarkers(){

        for (Marker marker : markers) {
            marker.setVisible(false);
        }
    }

    private void showAllMarkers(){

        for (Marker marker : markers) {
            marker.setVisible(true);
        }
    }

    private void showMarker(ObjectOfInterest.Type oiType){
        for (Marker marker : markers) {
            ObjectOfInterest oi = (ObjectOfInterest) marker.getTag();
            if (oi.getType().equals(oiType))
                marker.setVisible(true);
            else
                marker.setVisible(false);
        }
    }

    public boolean onMarkerClick(final Marker marker) {
        ObjectOfInterest obj = (ObjectOfInterest) marker.getTag();
        mapDialogInit(obj).show();
        return false;
    }

    private Dialog mapDialogInit(ObjectOfInterest tag){

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
        LayoutInflater inflater = Navigation_Activity.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.map_short_desc_dialog, null);

        builder.setView(dialog_layout);

        TextView address = (TextView) dialog_layout.findViewById(R.id.map_desc_address);
        TextView star = (TextView) dialog_layout.findViewById(R.id.map_desc_star);
        TextView subtype = (TextView) dialog_layout.findViewById(R.id.map_desc_subtype);
        TextView name = (TextView) dialog_layout.findViewById(R.id.map_desc_name);
        TextView price = (TextView) dialog_layout.findViewById(R.id.map_desc_price);
        TextView phone = (TextView) dialog_layout.findViewById(R.id.map_desc_phone);

        ImageView type = (ImageView) dialog_layout.findViewById(R.id.map_desc_type);
        ImageView building = (ImageView) dialog_layout.findViewById(R.id.map_desc_image);
        ImageView currency = (ImageView) dialog_layout.findViewById(R.id.map_desc_currency);

        Button btn_around = (Button) dialog_layout.findViewById(R.id.map_desc_btn_around);
        Button btn_info = (Button) dialog_layout.findViewById(R.id.map_desc_btn_info);
        Button btn_site = (Button) dialog_layout.findViewById(R.id.map_desc_btn_site);
        Button btn_reserve = (Button) dialog_layout.findViewById(R.id.map_desc_btn_reserve);
        ImageButton btn_close = (ImageButton) dialog_layout.findViewById(R.id.map_desc_btn_close);

        address.setText(tag.getAddress());
        star.setText(String.valueOf(tag.getStar()));
        subtype.setText(tag.getSubtype());
        name.setText(tag.getName());
        price.setText(String.valueOf(tag.getPrice()));
        phone.setText(String.valueOf(tag.getPhone()));

        type.setImageResource(tag.getAssociatedDrawable());

        //TODO GENERIC, change later
        building.setImageResource(R.drawable.test_building);
        currency.setImageResource(R.drawable.ic_euro);

        btn_around.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Navigation_Activity.this, "Around", Toast.LENGTH_SHORT).show();
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Navigation_Activity.this, "Info", Toast.LENGTH_SHORT).show();
            }
        });
        btn_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Navigation_Activity.this, "Site", Toast.LENGTH_SHORT).show();
            }
        });
        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                mapReserveDialogInit().show();
            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog = builder.create();
        return alertDialog;
    }

    private Dialog mapReserveDialogInit(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
        LayoutInflater inflater = Navigation_Activity.this.getLayoutInflater();
        final View dialog_layout = inflater.inflate(R.layout.map_reserve, null);

        builder.setView(dialog_layout);
        alertDialog = builder.create();

        arrival = (TextView) dialog_layout.findViewById(R.id.map_res_date_arrive);
        departure = (TextView) dialog_layout.findViewById(R.id.map_res_date_depart);

        ImageView arrive_btn = (ImageView) dialog_layout.findViewById(R.id.map_res_date_arrive_btn);
        ImageView depart_btn = (ImageView) dialog_layout.findViewById(R.id.map_res_date_depart_btn);


        final Spinner adults = (Spinner) dialog_layout.findViewById(R.id.map_res_adults);
        final Spinner children = (Spinner) dialog_layout.findViewById(R.id.map_res_children);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.people_count, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adults.setAdapter(adapter);
        adults.setSelection(1);
        children.setAdapter(adapter);

        final EditText age = (EditText) dialog_layout.findViewById(R.id.map_res_age);

        final EditText names = (EditText) dialog_layout.findViewById(R.id.map_res_names);
        final EditText email = (EditText) dialog_layout.findViewById(R.id.map_res_email);
        final EditText phone = (EditText) dialog_layout.findViewById(R.id.map_res_phone);

        final CheckBox checkBox = (CheckBox) dialog_layout.findViewById(R.id.map_res_check);
        Button reserve_btn = (Button) dialog_layout.findViewById(R.id.map_res_reserve);

        arrive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePick();

                Bundle args = new Bundle();
                args.putBoolean("state", true);
                if(departure != null){
                    args.putString("max_date",departure.getText().toString());
                }
                newFragment.setArguments(args);

                newFragment.show(getSupportFragmentManager(), "dateArrive");
            }
        });

        depart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrival.getText().length() > 0) {
                    DialogFragment newFragment = new DatePick();

                    Bundle args = new Bundle();
                    args.putBoolean("state", false);
                    args.putString("min_date", arrival.getText().toString());
                    newFragment.setArguments(args);

                    newFragment.show(getSupportFragmentManager(), "dateDepart");
                }else
                    Toast.makeText(Navigation_Activity.this, "Please select a date of arrival first", Toast.LENGTH_SHORT).show();
            }
        });

        reserve_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ageCheck = true;
                final int adults_number = Integer.parseInt(adults.getSelectedItem().toString());
                final int childrens_number = Integer.parseInt(children.getSelectedItem().toString());

                if(childrens_number != 0 && age.getText().length() <= 0)
                    ageCheck = false;

                if(arrival.getText().length() > 0 && departure.getText().length() > 0 && names.getText().length() > 0 && isValidEmail(email.getText()) && phone.getText().length() > 0){

                    if(ageCheck){

                        if(checkBox.isChecked()){
                            alertDialog.dismiss();
                            Toast.makeText(Navigation_Activity.this, "Reserve with Adults: " + adults_number + " and children " + childrens_number, Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(Navigation_Activity.this, "You have to agree to the terms and conditions", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(Navigation_Activity.this, "Please write your childrens ages", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(Navigation_Activity.this, "Please check that all fields are correct", Toast.LENGTH_SHORT).show();


            }
        });

        return alertDialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date dateRepresentation = cal.getTime();

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        Fragment dateArrive = getSupportFragmentManager().findFragmentByTag("dateArrive");
        Fragment dateDepart = getSupportFragmentManager().findFragmentByTag("dateDepart");

        if(dateArrive != null)
            arrival.setText(simpleDate.format(dateRepresentation));
        else if(dateDepart != null)
            departure.setText(simpleDate.format(dateRepresentation));
        else
            Toast.makeText(this, "There was an error in the application, please report this", Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private Dialog searchDialogInit(){

        //TODO Receive destination
        String[] Cities = new String[] { "Varna",
                "Shumen", "Burgas", "Plovdiv", "Sofia" };

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
        LayoutInflater inflater = Navigation_Activity.this.getLayoutInflater();
        View dialog_layout = inflater.inflate(R.layout.menu_search, null);

        builder.setView(dialog_layout);
        alertDialog = builder.create();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Cities);
        final AutoCompleteTextView textView = (AutoCompleteTextView) dialog_layout.findViewById(R.id.menu_search_edit);
        textView.setAdapter(adapter);

        arrival = (TextView) dialog_layout.findViewById(R.id.menu_search_date_arrive);
        departure = (TextView) dialog_layout.findViewById(R.id.menu_search_date_depart);

        ImageView arrive_btn = (ImageView) dialog_layout.findViewById(R.id.menu_search_date_arrive_btn);
        ImageView depart_btn = (ImageView) dialog_layout.findViewById(R.id.menu_search_date_depart_btn);

        Button searchBtn = (Button) dialog_layout.findViewById(R.id.menu_search_btn);
        final Spinner people = (Spinner) dialog_layout.findViewById(R.id.menu_search_people);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.people_count, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        people.setAdapter(adapterSpinner);

        arrive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePick();

                Bundle args = new Bundle();
                args.putBoolean("state", true);
                if(departure != null){
                    args.putString("max_date",departure.getText().toString());
                }
                newFragment.setArguments(args);

                newFragment.show(getSupportFragmentManager(), "dateArrive");
            }
        });

        depart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(arrival.getText().length() > 0) {
                    DialogFragment newFragment = new DatePick();

                    Bundle args = new Bundle();
                    args.putBoolean("state", false);
                    args.putString("min_date", arrival.getText().toString());
                    newFragment.setArguments(args);

                    newFragment.show(getSupportFragmentManager(), "dateDepart");
                }else
                    Toast.makeText(Navigation_Activity.this, "Please select a date of arrival first", Toast.LENGTH_SHORT).show();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ppl = "people";
                int peopleNum = Integer.parseInt(people.getSelectedItem().toString());
                if(peopleNum == 1)
                    ppl = "person";
                int days = daysDifference();
                if(days == -1)
                    Toast.makeText(Navigation_Activity.this, "There was an error with the dates", Toast.LENGTH_SHORT).show();

                //TODO switch to %s
                searchString = textView.getText().toString() + ", " + peopleNum + " " + ppl + ", " + days + " days";

                setTitle(searchString);

                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {

                search(textView.getText().toString());

            }
        });

        return alertDialog;
    }

    private int daysDifference(){

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        Calendar calA = Calendar.getInstance();
        Calendar calD = Calendar.getInstance();

        try {
            calA.setTime(simpleDate.parse(arrival.getText().toString()));
            calD.setTime(simpleDate.parse(departure.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

        // Get the represented date in milliseconds
        long milis1 = calA.getTimeInMillis();
        long milis2 = calD.getTimeInMillis();

        // Calculate difference in milliseconds
        long diff = Math.abs(milis2 - milis1);
        int daysDifference = (int)(diff / (24 * 60 * 60 * 1000));

        return daysDifference;
    }

    private void search(String city){

        for (Marker marker : markers) {
            ObjectOfInterest oi = (ObjectOfInterest) marker.getTag();
            if (oi.getAddress().equals(city))
                marker.setVisible(true);
            else
                marker.setVisible(false);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}