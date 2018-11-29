package pl.dawidkulpa.knj;

import android.Manifest;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import com.facebook.FacebookSdk;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.dawidkulpa.knj.Fragments.AccountFragment;
import pl.dawidkulpa.knj.Fragments.CalendarFragment;
import pl.dawidkulpa.knj.Fragments.HistoryFragment;
import pl.dawidkulpa.knj.Fragments.LoginFragment;
import pl.dawidkulpa.knj.Fragments.MapFragment;
import pl.dawidkulpa.knj.Fragments.NotifFragment;
import pl.dawidkulpa.knj.Fragments.SettingsFragment;
import pl.dawidkulpa.knj.Fragments.SigninFragment;
import pl.dawidkulpa.serverconnectionmanager.Query;
import pl.dawidkulpa.serverconnectionmanager.ServerConnectionManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, LoginFragment.OnLoginListener,
        AccountFragment.OnSaveListener, SigninFragment.OnSignInListener {

    private static final int PERMISSIONS_REQUEST_ALL=1;
    private static final int MAP_FRAGMENT_ID=0;
    private static final int LOGIN_FRAGMENT_ID=1;
    private static final int ACCOUNT_FRAGMENT_ID=2;
    private static final int CALENDAR_FRAGMENT_ID=3;
    private static final int HISTORY_FRAGMENT_ID=4;
    private static final int SETTINGS_FRAGMENT_ID=5;
    private static final int NOTIF_FRAGMENT_ID=6;
    private static final int SIGNIN_FRAGMENT_ID=7;

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> appFragments;

    private User logedInUser;
    private ArrayList<LessonMapMarker> onMapLessons;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;


    //MAP filters
    private int radiusFilter;
    private LatLng mapCenter;
    private Date today;
    private int timeFilter;
    private Date dateToFilter;
    private int subjectFilter;
    private int levelFilter;
    private int coachIdFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //FacebookSdk.sdkInitialize(getApplicationContext());

        //Prepare FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterDialog();
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
        appFragments.add(SigninFragment.newInstance());

        switchFragment(0);

        if(logedInUser!=null){
            fab.show();
        } else {
            fab.hide();
        }

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        onMapLessons= new ArrayList<>();

        subjectFilter=-1;
        levelFilter=-1;
        coachIdFilter=-1;
    }

    private void setSubjectsBox(){

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
                switchFragment(MAP_FRAGMENT_ID);
                break;
            case R.id.nav_login:
                switchFragment(LOGIN_FRAGMENT_ID);
                break;
            case R.id.nav_your_accont:
                switchFragment(ACCOUNT_FRAGMENT_ID);
                break;
            case R.id.nav_calendar:
                switchFragment(CALENDAR_FRAGMENT_ID);
                break;
            case R.id.nav_history:
                switchFragment(HISTORY_FRAGMENT_ID);
                break;
            case R.id.nav_settings:
                switchFragment(SETTINGS_FRAGMENT_ID);
                break;
            case R.id.nav_notif:
                switchFragment(NOTIF_FRAGMENT_ID);
                break;
            case  R.id.nav_signin:
                switchFragment(SIGNIN_FRAGMENT_ID);
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

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                double latLength=0;
                LatLngBounds latLngBounds;
                mapCenter= map.getCameraPosition().target;
                latLngBounds= map.getProjection().getVisibleRegion().latLngBounds;
                latLength= latLngBounds.northeast.latitude-latLngBounds.southwest.latitude;
                latLength= Math.abs(latLength);
                radiusFilter= (int)((latLength*110.574)/5);

                startMapRefresh();
            }
        });


        mapCenter=map.getCameraPosition().target;
        radiusFilter=50;

        startMapRefresh();
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

        Snackbar.make(fab, R.string.info_successful_logout, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onLoginAcquired(User user) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        logedInUser= user;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer_user);
        switchFragment(MAP_FRAGMENT_ID);

        Snackbar.make(fab, R.string.info_successful_login, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ((AccountFragment)appFragments.get(ACCOUNT_FRAGMENT_ID)).setUser(logedInUser);
    }

    @Override
    public void onSignInSuccess() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        switchFragment(MAP_FRAGMENT_ID);
        Snackbar.make(fab, R.string.info_success_signin, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDataSaveSuccessful(User user) {
    }

    private void openFilterDialog(){
        AlertDialog.Builder adbuilder= new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        adbuilder.setTitle("Filtry");
        adbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        adbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adbuilder.setView(inflater.inflate(R.layout.dialog_filters, null));

        adbuilder.create().show();
    }

    private void startMapRefresh(){
        today= Calendar.getInstance().getTime();
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'", Locale.US);

        Calendar c=Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DAY_OF_MONTH, timeFilter);
        dateToFilter= c.getTime();

        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                refreshMap(respCode, jObject);
            }
        }, Query.BuildType.Pairs);

        //Testowe
        //scm.addPOSTPair("Latitude", "52.327286");
        //scm.addPOSTPair("Longitude", "21.103956");
        //scm.addPOSTPair("Radius", "100");
        scm.addPOSTPair("DateFrom", "2018-11-23T14:00:00");
        scm.addPOSTPair("DateTo", "2018-11-23T16:00:00");

        //Prawidlowe
        scm.addPOSTPair("Latitude", String.valueOf(mapCenter.latitude));
        scm.addPOSTPair("Longitude", String.valueOf(mapCenter.longitude));
        scm.addPOSTPair("Radius", String.valueOf(radiusFilter));
        //scm.addPOSTPair("DateFrom", sdf.format(today));
        //scm.addPOSTPair("DateTo", sdf.format(dateToFilter));

        if(subjectFilter>=0)
            scm.addPOSTPair("SubjectId", String.valueOf(subjectFilter));
        if(levelFilter>=0)
            scm.addPOSTPair("LevelId", String.valueOf(levelFilter));
        if(coachIdFilter>=0)
            scm.addPOSTPair("CoachId", String.valueOf(coachIdFilter));

        scm.setMethod(ServerConnectionManager.METHOD_GET);

        scm.start("https://korepetycjenajuzapi.azurewebsites.net/api/CoachLesson/CoachLessonsByFilters");
    }

    private void refreshMap(int rCode, JSONObject jObj){
        if(rCode==200){
            boolean[] occurMap= new boolean[onMapLessons.size()];

            try {
                JSONArray lessonsArray = jObj.getJSONArray("array");

                //Update or add lesson marker
                for(int i=0; i<lessonsArray.length(); i++){
                    boolean occured=false;
                    JSONObject lessonJObj= lessonsArray.getJSONObject(i);

                    //Search for occurrence
                    for(int j=0; i<onMapLessons.size(); j++){
                        if(onMapLessons.get(j).equals(lessonJObj)){
                            occurMap[j]=true;
                            occured= true;
                            onMapLessons.get(j).update(lessonJObj);
                            break;
                        }
                    }

                    //If absent -> add
                    if(!occured){
                        onMapLessons.add(LessonMapMarker.create(lessonJObj));
                        onMapLessons.get(onMapLessons.size()-1).register(this, map);
                    }
                }
            } catch (JSONException jsonE){
                Log.e("Refresh Map", jsonE.getMessage());
            }

            //Remove absent elements
            for(int i=0; i<occurMap.length; i++){
                if(!occurMap[i]){
                    onMapLessons.get(i).unregister();
                    onMapLessons.remove(i);
                }
            }
        }

        Log.e("Home Activity", "Map refreshed");
    }


}
