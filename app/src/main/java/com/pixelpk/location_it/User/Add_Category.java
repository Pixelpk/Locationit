package com.pixelpk.location_it.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.pixelpk.location_it.Adapter.Category_Adapter;
import com.pixelpk.location_it.Helper.Category_Model;
import com.pixelpk.location_it.Helper.DatabaseHelper;
import com.pixelpk.location_it.Helper.User;
import com.pixelpk.location_it.R;
import com.pixelpk.location_it.Welcome_Page;

import java.util.ArrayList;
import java.util.List;

public class Add_Category extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Category_Adapter category_adapter;
    RecyclerView recyclerView_category;
    ArrayList<Category_Model> arrayList;
     DatabaseHelper databaseHelper;
//    TextInputLayout category_txt;
    String category_str;
    Category_Model categoryModel;
    Category_Adapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    View headerView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    LinearLayout contentView;
    ImageView menuIcon;
    static final float END_SCALE = 0.7f;

    TextView username_drawer;
    String str_username_drawer;

//    RecyclerView recyclerView;
//    DatabaseHelper database_helper;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
//        category_txt = findViewById(R.id.category_add_category);
        recyclerView_category = findViewById(R.id.category_recyclerview);
        databaseHelper = new DatabaseHelper(this);
        categoryModel = new Category_Model();

        sharedPreferences = getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        drawerLayout = findViewById(R.id.drawer_layout_add_category);
        navigationView = findViewById(R.id.navigation_view_add_category);
        contentView    = findViewById(R.id.content_add_category);
        menuIcon       = findViewById(R.id.menu_icon_add_category);
        headerView = navigationView.getHeaderView(0);

        username_drawer = findViewById(R.id.username_drawer_add_category);

        navigationView.setItemIconTintList(null);

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


        LoadData();

        naviagtionDrawer();



    }

    public void open_drawer_navigation_add_category(View view)
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

    public void insert_data_to_category(View view)
    {
        ViewDialog alert = new ViewDialog();
        alert.showDialog(Add_Category.this);

//        category_str = category_txt.getEditText().getText().toString();


//        if(!validateCategory())
//        {
//            return;
//        }
//        else
//        {
//            addData();
//        }
    }

//    private Boolean validateCategory()
//    {
//        String val_name = category_txt.getEditText().getText().toString();
//
//        if(val_name.isEmpty())
//        {
//            category_txt.setError("Field is Empty");
//            return false;
//        }
//
//        else
//        {
//            category_txt.setError(null);
//            category_txt.setErrorEnabled(false);
//            return true;
//        }
//    }



    private void LoadData()
    {
        arrayList = new ArrayList<>(databaseHelper.getAllcategory());
        recyclerView_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        recyclerView_category.setItemAnimator(new DefaultItemAnimator());
        Category_Adapter adapter = new Category_Adapter(arrayList,this);
        recyclerView_category.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home_Dashboard.class);
        startActivity(intent);
        finish();
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

                new AlertDialog.Builder(Add_Category.this)
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

    private class ViewDialog
    {
//        Dialog dialog;
        ImageView image_cancel;
        EditText category_name;
        Button submit;
        String category_str;

        public void InitializeView(Dialog dialog)
        {

            category_name =  dialog.findViewById(R.id.title);
            submit        =  dialog.findViewById(R.id.submit);
            image_cancel  =  dialog.findViewById(R.id.img_cancel_dialog);

        }

        public void showDialog(Activity activity)
        {
            Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            dialog.setContentView(R.layout.dialog);
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(params);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            InitializeView(dialog);

            submit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    category_str = category_name.getText().toString();

                    if (category_name.getText().toString().isEmpty())
                    {
                        category_name.setError("Please Enter Category name");
                    }
                    else
                    {
                        categoryModel.setCategory_name(category_str);
                        databaseHelper.addCategories(categoryModel);
                        Toast.makeText(getApplicationContext(), "Category Added", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        LoadData();
                        //notify list
                    }

                }
            });

            image_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    dialog.dismiss();
                }
            });

        }
    }
}