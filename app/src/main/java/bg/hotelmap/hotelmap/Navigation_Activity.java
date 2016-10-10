package bg.hotelmap.hotelmap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import bg.hotelmap.hotelmap.fragments.Gallery;
import bg.hotelmap.hotelmap.fragments.Offer;

public class Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, AdapterView.OnItemSelectedListener {

    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

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
        navigationView.setNavigationItemSelectedListener(this);



        //Initializing object on screens
        spinnerInitialize();
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

    private void spinnerInitialize() {
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
            Toast.makeText(this,"Покажи около мен",Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_scan) {
            Toast.makeText(this,"Сканирай",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,test_barcode.class);
            startActivity(intent);
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
    public void onMapReady(GoogleMap googleMap) {

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
}
