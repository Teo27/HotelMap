package bg.hotelmap.hotelmap;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import bg.hotelmap.hotelmap.fragments.Gallery;
import bg.hotelmap.hotelmap.fragments.Offer;

public class Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    SupportMapFragment supportMapFragment;
    private int item_selected = 4;
    private GoogleMap map;
    private int progress_value = 0;

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
                //TODO Make search
       //         Intent intent = new Intent();
       //         startActivity(intent);
            }
        });

        //Navigation Drawer code

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(item_selected).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);

        //Initializing object on screens
        spinnerInit();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_hotel) {
            Toast.makeText(this,"Покажи хотели",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_sights) {
            Toast.makeText(this,"Покажи забележителности",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_shops) {
            Toast.makeText(this,"Покажи магазини",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_events) {
            Toast.makeText(this,"Покажи събития",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_around) {
            aroundDialogInit().show();
        }else if (id == R.id.nav_scan) {
            Intent intent = new Intent(this,Code_Scanner.class);
            startActivityForResult(intent,0);
        } else if (id == R.id.nav_about) {
            Toast.makeText(this,"За нас",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_register) {
            Toast.makeText(this,"Регистрирай МИ",Toast.LENGTH_SHORT).show();
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

        switch (position){
            case 0:
                if (!supportMapFragment.isAdded())
                    sFm.beginTransaction().add(R.id.map, supportMapFragment).commit();
                else
                    sFm.beginTransaction().show(supportMapFragment).commit();
                break;
            case 1:
                fm.beginTransaction().replace(R.id.list,new Gallery()).commit();
                break;
            case 2:
                fm.beginTransaction().replace(R.id.list,new Offer()).commit();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Barcode not found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        UiSettings settings = map.getUiSettings();
        settings.setMapToolbarEnabled(false);
        LatLng sydney = new LatLng(-34, 151);
        LatLng sydney2 = new LatLng(-34, 150);
        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")).showInfoWindow();
        map.addMarker(new MarkerOptions().position(sydney2).title("Marker in Sydney2")).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    //TODO
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

    public Dialog aroundDialogInit(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Activity.this);
        LayoutInflater inflater = Navigation_Activity.this.getLayoutInflater();
        View dialog_layout = inflater.inflate(R.layout.around_me_dialog, null);

        builder.setView(dialog_layout)
                .setPositiveButton(R.string.around_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Navigation_Activity.this, "Success" + progress_value, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.around_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.around_dialog_title);

        seekInit(dialog_layout);

        return builder.create();
    }

    private void seekInit(View dialog_layout) {
        SeekBar seekBar = (SeekBar) dialog_layout.findViewById(R.id.around_me_seek);
        final TextView seekText = (TextView) dialog_layout.findViewById(R.id.around_me_text);
        final String text = "Search up to %s km";
        seekBar.setProgress(5);
        seekBar.setMax(29);
        seekText.setText(String.format(text,seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        progress_value = progress+1;
                        seekText.setText(String.format(text,progress_value));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seekText.setText(String.format(text,progress_value));
                    }
                }
        );

    }


}
