package pl.dawidkulpa.knj;

import android.Manifest;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.dawidkulpa.knj.Dialogs.CreateLesson.CreateLessonActivity;
import pl.dawidkulpa.knj.Dialogs.FilterDialog;
import pl.dawidkulpa.knj.Dialogs.LessonDescriptionDialog;
import pl.dawidkulpa.knj.Fragments.AccountFragment;
import pl.dawidkulpa.knj.Fragments.CalendarFragment;
import pl.dawidkulpa.knj.Fragments.MessagesFragment;
import pl.dawidkulpa.knj.Fragments.HistoryFragment;
import pl.dawidkulpa.knj.Fragments.LoginFragment;
import pl.dawidkulpa.knj.Fragments.MapFragment;
import pl.dawidkulpa.knj.Fragments.ConversationFragment;
import pl.dawidkulpa.knj.Fragments.NotifFragment;
import pl.dawidkulpa.knj.Fragments.SigninFragment;
import pl.dawidkulpa.knj.Lessons.LessonFilters;
import pl.dawidkulpa.knj.Lessons.LessonMapMarker;
import pl.dawidkulpa.knj.Lessons.LessonsManager;
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
    private static final int NOTIF_FRAGMENT_ID=5;
    private static final int SIGNIN_FRAGMENT_ID=6;
    private static final int MESSAGES_FRAGMENT_ID =7;
    private static final int CONVERSATION_FRAGMENT_ID =8;

    public static final String SERVER_NAME="http://api.meteomap.pl:7181/api";
    public static final String[] LEVELS_LIST={"Szkoła podstawowa",
                                                "Liceum podstawa",
                                                "Liceum rozszerzenie",
                                                "Studia"};
    public static final String[] STATUS_NAME_LIST={
            "Czeka na studenta",
            "Czeka na potwierdzenie",
            "Potwierdzona",
            "Odrzucona",
            "Anulowana"
    };


    private FragmentManager fragmentManager;
    private int fragmentOnId;
    private ArrayList<Fragment> appFragments;

    private User logedInUser;

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationClient;

    private LessonsManager lessonsManager;

    private String[] subjectLabels;
    private ArrayAdapter<CharSequence> levelsAdapter;
    private ArrayAdapter<CharSequence> subjectsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Prepare FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilterDialog();
            }
        });

        levelsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        subjectsAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

        getLessonSubjects();
        levelsAdapter.add("Wszystkie");
        for(int i=0; i<LEVELS_LIST.length; i++){
            levelsAdapter.add(LEVELS_LIST[i]);
        }

        //Prepare side navigation
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
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
        appFragments.add(NotifFragment.newInstance());
        appFragments.add(SigninFragment.newInstance());
        appFragments.add(MessagesFragment.newInstance());
        appFragments.add(ConversationFragment.newInstance());

        switchFragment(0);

        fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        lessonsManager= new LessonsManager(this);
    }

    private void switchFragment(int id){
        FloatingActionButton fab = findViewById(R.id.fab);

        if(id!=MAP_FRAGMENT_ID){
            fab.hide();
        } else {
            fab.show();
        }

        FragmentTransaction transaction= fragmentManager.beginTransaction();

        transaction.replace(R.id.main_container, appFragments.get(id));
        transaction.addToBackStack(null);

        fragmentOnId= id;

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(fragmentOnId==CONVERSATION_FRAGMENT_ID)
                super.onBackPressed();
        }
    }

    public void setDrawerTitle(String name, String email){
        DrawerLayout drawer= findViewById(R.id.drawer_layout);

        ((TextView)drawer.findViewById(R.id.name_text)).setText(name);
        ((TextView)drawer.findViewById(R.id.email_text)).setText(email);
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
            case R.id.nav_notif:
                switchFragment(NOTIF_FRAGMENT_ID);
                break;
            case  R.id.nav_signin:
                switchFragment(SIGNIN_FRAGMENT_ID);
                break;
            case R.id.nav_mess:
                switchFragment(MESSAGES_FRAGMENT_ID);
                break;
            case R.id.nav_create:
                if(logedInUser!=null && logedInUser.isLoggedin()) {
                    Intent intent = new Intent(this, CreateLessonActivity.class);
                    intent.putExtra("subjects", subjectLabels);
                    intent.putExtra("userLoginToken", logedInUser.getLoginToken());
                    startActivity(intent);
                } else {
                    putSnackbar(getString(R.string.info_first_login));
                }
                break;
            case R.id.nav_logout:
                onLogoutClick();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                lessonsManager.refreshLessonMarkers(map);
            }
        });
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                HomeActivity.this.onMarkerClick(marker);
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lessonsManager.hideAllInfoWindows();
            }
        });

        lessonsManager.refreshLessonMarkers(map);
    }

    public void onMarkerClick(Marker marker){
        LessonMapMarker lessonMapMarker= lessonsManager.findLessonObject(marker);

        if(lessonMapMarker!=null){
            LessonDescriptionDialog lessonDescriptionDialog= new LessonDescriptionDialog(this,lessonMapMarker, logedInUser);
            lessonDescriptionDialog.show();
        }
    }

    public void onLogoutClick(){
        FloatingActionButton fab = findViewById(R.id.fab);
        logedInUser=null;

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer_guest);
        switchFragment(0);

        setDrawerTitle("Korepetycje Na Już", "Jakiś slogan!");

        Snackbar.make(fab, R.string.info_successful_logout, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onLoginAcquired(User user) {
        FloatingActionButton fab = findViewById(R.id.fab);
        logedInUser= user;

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.activity_home_drawer_user);
        switchFragment(MAP_FRAGMENT_ID);

        Snackbar.make(fab, R.string.info_successful_login, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        setDrawerTitle(user.getName()+" "+user.getSname(), user.getEmail());

        logedInUser.refreshLessonEntries(null);
    }



    @Override
    public void onSignInSuccess() {
        FloatingActionButton fab = findViewById(R.id.fab);
        switchFragment(MAP_FRAGMENT_ID);
        Snackbar.make(fab, R.string.info_success_signin, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onDataSaveSuccessful(User user) {

    }

    private void openFilterDialog(){
        FilterDialog filterDialog= new FilterDialog(this, new FilterDialog.OnClickListener() {
            @Override
            public void onPositiveClick(LessonFilters filters) {
                lessonsManager.updateFilters(filters);
                lessonsManager.refreshLessonMarkers(map);
            }

            @Override
            public void onNegativeClick() {

            }
        });

        filterDialog.show(levelsAdapter, subjectsAdapter, lessonsManager.getFilters());
    }

    private void getLessonSubjects(){
        ServerConnectionManager scm= new ServerConnectionManager(new ServerConnectionManager.OnFinishListener() {
            @Override
            public void onFinish(int respCode, JSONObject jObject) {
                onGetSubjectsFinished(respCode, jObject);
            }
        }, Query.BuildType.Pairs);
        scm.setMethod(ServerConnectionManager.METHOD_GET);
        scm.start(SERVER_NAME+"/LessonSubjects/GetAll");
    }

    private void onGetSubjectsFinished(int rCode, JSONObject jObj){
        try{
            JSONArray jArray= jObj.getJSONArray("array");
            subjectLabels= new String[jArray.length()];
            subjectsAdapter.add("Wszystkie");
            for(int i=0; i<jArray.length(); i++){
                subjectLabels[i]=jArray.getJSONObject(i).getString("name");
                subjectsAdapter.add(subjectLabels[i]);
            }
        } catch (JSONException je){
            Log.e("HomeActivity", je.getMessage());
        }
    }

    public User getLogedInUser(){
        return logedInUser;
    }

    public void putSnackbar(String text){
        FloatingActionButton fab = findViewById(R.id.fab);
        Snackbar.make(fab, text, Snackbar.LENGTH_SHORT).show();
    }

    public void showConversation(int withId, String withName){
        ((ConversationFragment)appFragments.get(CONVERSATION_FRAGMENT_ID)).setWithId(withId);
        ((ConversationFragment)appFragments.get(CONVERSATION_FRAGMENT_ID)).setWithName(withName);
        switchFragment(CONVERSATION_FRAGMENT_ID);
    }


}
