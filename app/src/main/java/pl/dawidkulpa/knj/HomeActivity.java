package pl.dawidkulpa.knj;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Fragments.AccountFragment;
import pl.dawidkulpa.knj.Fragments.CalendarFragment;
import pl.dawidkulpa.knj.Fragments.HistoryFragment;
import pl.dawidkulpa.knj.Fragments.LoginFragment;
import pl.dawidkulpa.knj.Fragments.MapFragment;
import pl.dawidkulpa.knj.Fragments.NotifFragment;
import pl.dawidkulpa.knj.Fragments.SettingsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, LoginFragment.OnLoginListener,
        AccountFragment.OnSaveListener {

    private static final int PERMISSIONS_REQUEST_ALL=1;
    private static final int MAP_FRAGMENT_ID=0;
    private static final int LOGIN_FRAGMENT_ID=1;
    private static final int ACCOUNT_FRAGMENT_ID=2;
    private static final int CALENDAR_FRAGMENT_ID=3;
    private static final int HISTORY_FRAGMENT_ID=4;
    private static final int SETTINGS_FRAGMENT_ID=5;
    private static final int NOTIF_FRAGMENT_ID=6;

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> appFragments;

    private User logedInUser;
    private ArrayList<LessonMapMarker> onMapLessons;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Prepare FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Prepare side navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getLocationPermission();

        //Prepare fragment manager
        fragmentManager= getSupportFragmentManager();

        //Create and init apps fragments
        appFragments= new ArrayList<>();
        appFragments.add(MapFragment.newInstance());
        ((SupportMapFragment)appFragments.get(0)).getMapAsync(this);
        appFragments.add(LoginFragment.newInstance());
        appFragments.add(AccountFragment.newInstance());
        appFragments.add(CalendarFragment.newInstance());
        appFragments.add(HistoryFragment.newInstance());
        appFragments.add(SettingsFragment.newInstance());
        appFragments.add(NotifFragment.newInstance());

        switchFragment(0);

        if(logedInUser!=null){
            fab.show();
        } else {
            fab.hide();
        }

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        onMapLessons= new ArrayList<>();

    }

    private void switchFragment(int id){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if(id!=MAP_FRAGMENT_ID){
            fab.hide();
        } else {
            fab.show();
        }

        FragmentTransaction transaction= fragmentManager.beginTransaction();

        transaction.replace(R.id.main_container, appFragments.get(id));
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void getLocationPermission() {
        String[] perms;
        boolean granted;

        //Check if already granted
        granted= ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;


        if (!granted) {
            perms= new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION};

            ActivityCompat.requestPermissions(this, perms, PERMISSIONS_REQUEST_ALL);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                switchFragment(0);
                break;
            case R.id.nav_log_sign:
                switchFragment(1);
                break;
            case R.id.nav_your_accont:
                switchFragment(2);
                break;
            case R.id.nav_calendar:
                switchFragment(3);
                break;
            case R.id.nav_history:
                switchFragment(4);
                break;
            case R.id.nav_settings:
                switchFragment(5);
                break;
            case R.id.nav_notif:
                switchFragment(6);
                break;
            case R.id.nav_find_lesson:
                onFindLessonClick();
                break;
            case R.id.nav_create:
                onCreateLessonClick();
                break;
            case R.id.nav_logout:
                onLogoutClick();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        map= googleMap;

        try {
            //Disable My Location button
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.e("HomeActivity", "Loc update");
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()), 10));
                }
            });

        } catch (SecurityException se){
            Log.e("Exception", se.getMessage());
        }

        onMapLessons.add(new LessonMapMarker(52.215518, 21.100844));
        onMapLessons.add(new LessonMapMarker(52.226711, 21.085355));
        onMapLessons.add(new LessonMapMarker(52.221301, 21.110810));
        onMapLessons.get(0).register(this, map);
        onMapLessons.get(1).register(this, map);
        onMapLessons.get(2).register(this, map);

    }

    public void onFindLessonClick(){

    }

    public void onCreateLessonClick(){

    }

    public void onLogoutClick(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        logedInUser=null;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer_guest);
        switchFragment(0);

        fab.hide();
        Snackbar.make(fab, R.string.info_successful_logout, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onLoginAcquired(User user) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        logedInUser= user;

        Log.e("HomeActivity", "Login aquire");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer_user);
        switchFragment(MAP_FRAGMENT_ID);

        fab.show();
        Snackbar.make(fab, R.string.info_successful_login, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ((AccountFragment)appFragments.get(ACCOUNT_FRAGMENT_ID)).setUser(logedInUser);
    }

    @Override
    public void onDataSaveSuccessful(User user) {
    }
}
