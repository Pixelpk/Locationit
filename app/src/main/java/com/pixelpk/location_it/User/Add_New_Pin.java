package com.pixelpk.location_it.User;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.pixelpk.location_it.Helper.Category_Model;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.Location_Model;
import com.pixelpk.location_it.LocationTrack;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

import java.security.KeyPairGenerator;
import java.security.KeyPairGeneratorSpi;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Add_New_Pin extends FragmentActivity implements

        OnMapReadyCallback,
        NavigationView.OnNavigationItemSelectedListener
{

    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;//    TextView test;
//    Spinner category_spinner;
    FusedLocationProviderClient mFusedLocationClient;

    DatabaseHelper databaseHelper;
    Category_Model categoryModel;
    Location location;

    String category_str;
    Location_Model location_model;

    LatLng locationLatLng;

    Marker locationMarker;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack = new LocationTrack(Add_New_Pin.this);

    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;


    Location loc;
    double latitude;
    double longitude;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;

//    ArrayList<Category_Model> arrayList;

    List<String> users = new ArrayList<>();

    ArrayAdapter<String> adapter;

    ViewDialog_Add_Map viewDialog_add_map;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    TextView username_drawer;
    String str_username_drawer;

    View headerView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout contentView;
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__pin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        // Construct a FusedLocationProviderClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationLatLng = new LatLng(0.0,0.0);

        drawerLayout   = findViewById(R.id.drawer_layout_add_location);
        navigationView = findViewById(R.id.navigation_view_add_location);
        contentView    = findViewById(R.id.content_add_location);
        menuIcon       = findViewById(R.id.menu_icon_add_location);
        headerView = navigationView.getHeaderView(0);

        username_drawer = findViewById(R.id.username_drawer_add_location);

        navigationView.setItemIconTintList(null);
        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_LOCATION);

        str_username_drawer = sharedPreferences.getString("user_sign_in","0");

        if(str_username_drawer == "0")
        {
            username_drawer.setText("Guest");
            Menu nav_log_out = navigationView.getMenu();
            Menu nav_change_pass = navigationView.getMenu();
            Menu nav_login = navigationView.getMenu();
            nav_log_out.findItem(R.id.nav_logout).setVisible(false);
            nav_change_pass.findItem(R.id.nav_change_pass).setVisible(false);
            nav_login.findItem(R.id.nav_login).setVisible(true);
        }

        else
        {
            username_drawer.setText(str_username_drawer);
            Menu nav_login = navigationView.getMenu();
            nav_login.findItem(R.id.nav_login).setVisible(false);
        }


        viewDialog_add_map = new ViewDialog_Add_Map();
        databaseHelper = new DatabaseHelper(this);
        categoryModel = new Category_Model();
        locationTrack = new LocationTrack(Add_New_Pin.this);
        location_model = new Location_Model();

        naviagtionDrawer();

        mapFragment.getMapAsync(this);

    }

    private void naviagtionDrawer()
    {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_pin_location);

        menuIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    private void animateNavigationDrawer()
    {
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener()
        {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



    }

    LocationCallback mLocationCallback = new LocationCallback()
    {
        @Override
        public void onLocationResult(LocationResult locationResult)
        {
            for(Location location : locationResult.getLocations())
            {
                if(getApplicationContext()!=null){
                    mLastLocation = location;

                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        }
    };




    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED)
                    {
//                        Toast.makeText(Add_New_Pin.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }}
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.nav_home:
            {
                Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
                startActivity(intent);
                finish();
            }
            break;


            case R.id.nav_pin_location:
            {
                Intent intent = new Intent(getApplicationContext(),Add_New_Pin.class);
                startActivity(intent);
                finish();
            }
            break;
            case R.id.nav_change_pass:
            {

                Intent intent = new Intent(getApplicationContext(),User_Change_Pass.class);
                startActivity(intent);
                finish();

            }
            break;
            case R.id.nav_view_pin_location:
            {

                Intent intent = new Intent(getApplicationContext(),View_Pin.class);
                startActivity(intent);
                finish();

            }
            break;

            case R.id.nav_login:
            {

                Intent intent = new Intent(getApplicationContext(),Sign_in.class);
                startActivity(intent);
                finish();

            }
            break;

            case R.id.nav_logout:
            {

                new AlertDialog.Builder(Add_New_Pin.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout?")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                logoutUser();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
            break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutUser()
    {
        editor.putString("login_id","0").apply();
        editor.putString("user_sign_in","0").apply();
        Intent intent = new Intent(getApplicationContext(), Welcome_Page.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    public void save_location_data(View view)
//    {
////        locationTrack = new LocationTrack(Add_New_Pin.this);
////
////        if (locationTrack.canGetLocation())
////        {
////
////            double longitude = locationTrack.getLongitude();
////            double latitude = locationTrack.getLatitude();
////
////            location_model.setLocation_category_name(viewDialog_add_map.category_str);
////            location_model.setLocaation_lat(latitude);
////            location_model.setLocation_long(longitude);
////            databaseHelper.addLocation_data(location_model);
////
////
////            locationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
////
//////            Toast.makeText(Add_New_Pin.this, category_str, Toast.LENGTH_SHORT).show();
////            locationMarker = mMap.addMarker(new MarkerOptions().position(locationLatLng).title("Location Here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));
////
////            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
////        }
////
////        else
////
////        {
////            locationTrack.showSettingsAlert();
////        }
//    }

//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//        locationManager.removeUpdates(LocationTrack.this);
//    }

    public void show_dialog_location_data(View view)
    {
//        categoryModel.getCategory_name().equals()

        viewDialog_add_map.showDialog(Add_New_Pin.this);
    }

    public void open_drawer_navigation_add_location(View view)
    {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.navigation_view_add_category);

        menuIcon.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animateNavigationDrawer();
    }

    public class ViewDialog_Add_Map
    {
        //        Dialog dialog;
        ImageView image_cancel;
        Button submit;
        Spinner category_spinner;
        LocationTrack locationTrack;

        DatabaseHelper databaseHelper;
        Category_Model categoryModel;
        LatLng locationLatLng;

        Marker locationMarker;

        ArrayList<Category_Model> arrayList;

        List<String> users=new ArrayList<>();

        ArrayAdapter<String> adapter;

        String category_str;
        String category_size;

        public void InitializeView(Dialog dialog)
        {
            category_spinner =  dialog.findViewById(R.id.spinner_select_category_map);
            image_cancel     =  dialog.findViewById(R.id.img_cancel_dialog_add);
            submit           =  dialog.findViewById(R.id.save_select_category);
            databaseHelper   = new DatabaseHelper(getApplicationContext());
            categoryModel    = new Category_Model();
            location_model   = new Location_Model();

        }

        public void showDialog(Activity activity)
        {

            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            dialog.setContentView(R.layout.dialog_map);
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            InitializeView(dialog);

            users = databaseHelper.getAllcategory_spinner();

            //adapter for spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_New_Pin.this,android.R.layout.simple_spinner_dropdown_item,android.R.id.text1,users);
            //attach adapter to spinner
            category_spinner.setAdapter(adapter);

            category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                  category_str = adapter.getItem(position);
                  category_size = String.valueOf(adapter.getCount());
                  

//                test.setText(category_str);
//                  dialog.dismiss();

//                databaseHelper.addLocation_data(location_model);
//                Toast.makeText(Add_New_Pin.this, category_str, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });

            submit.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v)
                {
                    locationTrack = new LocationTrack(Add_New_Pin.this);

                    if(category_size == null)
                    {
                        new AlertDialog.Builder(Add_New_Pin.this)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("No Categories Found")
                                .setMessage("Want to Add Categories click Yes")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Intent intent = new Intent(getApplicationContext(),Add_Category.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();

                        dialog.dismiss();
                    }

                    else
                    {
                        if (locationTrack.canGetLocation())
                        {

                            double longitude = locationTrack.getLongitude();
                            double latitude = locationTrack.getLatitude();

                            location_model.setLocation_category_name(category_str);
                            location_model.setLocaation_lat(latitude);
                            location_model.setLocation_long(longitude);
                            databaseHelper.addLocation_data(location_model);


                            locationLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

//            Toast.makeText(Add_New_Pin.this, category_str, Toast.LENGTH_SHORT).show();
                            locationMarker = mMap.addMarker(new MarkerOptions().position(locationLatLng).title("Location Here").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)));

//                            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Location Pinned Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            locationTrack.stopUsingGPS();
                        }

                        else

                        {
                            locationTrack.stopUsingGPS();
                            locationTrack.showSettingsAlert();
                        }
                    }
                }
            });

            image_cancel.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        locationTrack.stopUsingGPS();
    }
}