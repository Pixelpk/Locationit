package com.pixelpk.location_it.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.pixelpk.location_it.Helper.Category_Model;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.Location_Model;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

import java.util.ArrayList;

public class Home_Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//    ArrayList<LatLng> arrayList;
    TextView home_total_pins,home_total_categories;
    String str_total_pins,str_total_categories;
    Location_Model location_model;
    TextView username_drawer;
    String str_username_drawer;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    ArrayList<Location_Model> arrayList_model;
    ArrayList<Category_Model> arrayList_category_model;
    DatabaseHelper databaseHelper;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    View headerView;

    LinearLayout contentView;
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);

        home_total_pins = findViewById(R.id.total_pins);
        home_total_categories = findViewById(R.id.total_categories);

        databaseHelper   = new DatabaseHelper(getApplicationContext());

        drawerLayout = findViewById(R.id.drawer_layout_home);
        navigationView = findViewById(R.id.navigation_view_home);
        contentView    = findViewById(R.id.content_home);
        menuIcon       = findViewById(R.id.menu_icon_home);

        headerView = navigationView.getHeaderView(0);

        username_drawer = findViewById(R.id.username_drawer_home);



        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        navigationView.setItemIconTintList(null);

        str_username_drawer = sharedPreferences.getString("user_sign_in","0");

//        Toast.makeText(this, str_username_drawer, Toast.LENGTH_SHORT).show();

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

        location_model = new Location_Model();
        arrayList_model = new ArrayList<>(databaseHelper.getAlllocation());


        arrayList_category_model = new ArrayList<>(databaseHelper.getAllcategory());

        str_total_pins = String.valueOf(arrayList_model.size());
        str_total_categories = String.valueOf(arrayList_category_model.size());
//        Toast.makeText(this, str_total_categories, Toast.LENGTH_SHORT).show();


        home_total_pins.setText(str_total_pins+" Pin");
        home_total_categories.setText(str_total_categories+" Total");

        naviagtionDrawer();

    }

    public void open_drawer_navigation_home(View view)
    {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

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


    private void naviagtionDrawer()
    {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

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

    public void go_to_add_category(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Add_Category.class);
        startActivity(intent);
        finish();
    }

    public void go_to_add_new_pin(View view)
    {
        Intent intent = new Intent(getApplicationContext(),Add_New_Pin.class);
        startActivity(intent);
        finish();
    }

    public void go_to_view_pin(View view)
    {
        Intent intent = new Intent(getApplicationContext(),View_Pin.class);
        startActivity(intent);
        finish();
    }

    public void open_drawer_navigation(View view)
    {

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

                new AlertDialog.Builder(Home_Dashboard.this)
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


}