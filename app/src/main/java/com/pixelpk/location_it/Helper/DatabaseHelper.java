package com.pixelpk.location_it.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";
    // User table name
    private static final String TABLE_USER = "user";
    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_Full_Name = "full_name";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Category Table
    private static final String TABLE_CATEGORY = "categories";

    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";

    LatLng locationLatLng;

    Double longitude;
    Double latitude;

    // Location Table
    private static final String TABLE_Location = "location";
    private static final String COLUMN_Location_ID = "location_id";
    private static final String COLUMN_Location_Lat = "location_lat";
    private static final String COLUMN_Location_Long = "location_long";
    private static final String COLUMN_Location_Category_Name = "location_category_name";

    Location_Model locationModel;
    User user;

    //                        User Table Data

    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_Full_Name + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";
    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    //                           Category Table Data

    private String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CATEGORY_NAME + " TEXT" + ")";

    private String DROP_CATEGORY_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;

    //                           Location Table Data

    private String CREATE_Location_TABLE = "CREATE TABLE " + TABLE_Location + "("
            + COLUMN_Location_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_Location_Lat + " DOUBLE,"
            + COLUMN_Location_Long + " DOUBLE," + COLUMN_Location_Category_Name + " TEXT" + ")";
    // drop table sql query

    private String DROP_Location_TABLE  = "DROP TABLE IF EXISTS " + TABLE_Location;


    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_Location_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_CATEGORY_TABLE);
        db.execSQL(DROP_Location_TABLE);
        // Create tables again
        onCreate(db);
    }
    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_Full_Name, user.getFull_name());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }


    public void addCategories(Category_Model user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, user.getCategory_name());

        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public void addLocation_data(Location_Model user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_Location_Category_Name, user.getLocation_category_name());
        values.put(COLUMN_Location_Lat, user.getLocaation_lat());
        values.put(COLUMN_Location_Long, user.getLocation_long());

        // Inserting Row
        db.insert(TABLE_Location, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser()
    {
        // array of columns to fetch
        String[] columns =
        {
                COLUMN_USER_ID,
                COLUMN_USER_NAME,
                COLUMN_Full_Name,
                COLUMN_USER_PASSWORD
        };

        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setFull_name(cursor.getString(cursor.getColumnIndex(COLUMN_Full_Name)));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user list
        return userList;
    }

    public ArrayList<Category_Model> getAllcategory()
    {
        ArrayList<Category_Model> category_models = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Category_Model noteModel = new Category_Model();
                noteModel.setCategory_id(Integer.parseInt(cursor.getString(0)));
                noteModel.setCategory_name(cursor.getString(1));
                category_models.add(noteModel);
            }while (cursor.moveToNext());
        }
        return category_models;
    }


    public ArrayList<Location_Model> getAlllocation()
    {
        ArrayList<Location_Model> location_models = new ArrayList<>();

        // select all query
        String select_query= "SELECT *FROM " + TABLE_Location;

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location_Model noteModel = new Location_Model();
                noteModel.setLocation_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_Location_ID))));
                noteModel.setLocation_category_name(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Category_Name)));
                noteModel.setLocaation_lat(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))));
                noteModel.setLocation_long(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));
                location_models.add(noteModel);
            }while (cursor.moveToNext());
        }
        return location_models;
    }


    public ArrayList<Location_Model> getlocation_latlng(String category_str)
    {
//        String[] columns =
//                {
//                       COLUMN_Location_Category_Name
//                };
//        SQLiteDatabase db = this.getReadableDatabase();
//        // selection criteria
//        String selection = COLUMN_Location_Lat + " = ?" + " AND " + COLUMN_Location_Long + " = ?";
//        // selection arguments
//        String[] selectionArgs = {location_lat, location_long};
//        // query user table with conditions
//        /**
//         * Here query function is used to fetch records from user table this function works like we use sql query.
//         * SQL query equivalent to this query function is
//         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
//         */
//        Cursor cursor = db.query(TABLE_Location, //Table to query
//                columns,                    //columns to return
//                selection,                  //columns for the WHERE clause
//                selectionArgs,              //The values for the WHERE clause
//                null,                       //group the rows
//                null,                       //filter by row groups
//                null);                      //The sort order
//        int cursorCount = cursor.getCount();
//        cursor.close();
//        db.close();
//        if (cursorCount > 0)
//        {
//            return true;
//        }
//        return false;


        ArrayList<Location_Model> location_models = new ArrayList<>();

        // select all query
        String select_query= "SELECT location_lat,location_long FROM location WHERE location_category_name ='"+category_str + "'";

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);
        cursor.moveToFirst();
       // cursor.getCount();

        Log.d("cursor",String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))));
        Log.d("cursor",String.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));
        //Toast.makeText(, "", Toast.LENGTH_SHORT).show();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Location_Model noteModel = new Location_Model();
                noteModel.setLocaation_lat(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))));
                noteModel.setLocation_long(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));
                location_models.add(noteModel);
            }while (cursor.moveToNext());

         //   Log.d("tag_query",cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat)));
        }
        return location_models;
    }


    public List<String> getAllcategory_spinner()
    {
        List<String> userlist =new ArrayList<>();
        //get readable database
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT category_name FROM categories",null);
        if(cursor.moveToFirst())
        {
            do
            {
                userlist.add(cursor.getString(0));
            }

            while (cursor.moveToNext());

        }
        //close the cursor
        cursor.close();
        //close the database
        db.close();
        return userlist;
    }



    public ArrayList<LatLng> getlocation_latlng_map(String category_str)
    {
        ArrayList<LatLng> location_models = new ArrayList<>();

        String select_query = "SELECT location_lat,location_long FROM location WHERE location_category_name ='"+category_str + "'";

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);
        cursor.moveToFirst();
//Log.d("lat_cursor",cursor.getString(Integer.parseInt(COLUMN_Location_Lat)));

        if (cursor.moveToFirst())
        {
            do {
                Location_Model noteModel = new Location_Model();
                noteModel.setLocaation_lat(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))));
                noteModel.setLocation_long(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));

                locationLatLng = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));
                location_models.add(locationLatLng);
                Log.d("Latitude", String.valueOf(locationLatLng.latitude));
                Log.d("Longitude", String.valueOf(locationLatLng.longitude));
            } while (cursor.moveToNext());
        }
        return location_models;

    }

    public ArrayList<LatLng> getlocation_latlngall_map()
    {
        ArrayList<LatLng> location_models = new ArrayList<>();

        String select_query = "SELECT location_lat,location_long FROM location";

        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);
        cursor.moveToFirst();
//Log.d("lat_cursor",cursor.getString(Integer.parseInt(COLUMN_Location_Lat)));

        if (cursor.moveToFirst())
        {
            do {

                Location_Model noteModel = new Location_Model();
                noteModel.setLocaation_lat(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))));
                noteModel.setLocation_long(Double.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));

                locationLatLng = new LatLng(Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Lat))), Double.parseDouble(cursor.getString(cursor.getColumnIndex(COLUMN_Location_Long))));
                location_models.add(locationLatLng);
                Log.d("Latitude", String.valueOf(locationLatLng.latitude));
                Log.d("Longitude", String.valueOf(locationLatLng.longitude));
            }
            while (cursor.moveToNext());
        }
        return location_models;
    }



    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(String user,String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_password", pass);
        // updating row
        db.update(TABLE_USER, values, "user_name=?",new String[]{user});
        db.close();
    }

    public void update_category(Category_Model category_model)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category_model.getCategory_name());

        // updating row
        db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category_model.getCategory_id())});
        db.close();
    }


    public void update_category_new(String title, String ID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("category_name", title);
        //updating row
        sqLiteDatabase.update(TABLE_CATEGORY, values, "category_id=" + ID, null);
        sqLiteDatabase.close();
    }


    public void update_category_new_loc(String title, String name_category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("location_category_name", title);
        // updating row
        db.update(TABLE_Location, values, COLUMN_Location_Category_Name + " = ?",new String[]{name_category});
        db.close();
    }


//    private static final String COLUMN_USER_ID = "user_id";
//    private static final String COLUMN_Full_Name = "full_name";
//    private static final String COLUMN_USER_NAME = "user_name";
//    private static final String COLUMN_USER_PASSWORD = "user_password";

    public void update_password_new(String pass, String ID)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("user_password", pass);
        //updating row
        sqLiteDatabase.update(TABLE_USER, values, "user_name=" + ID, null);
        sqLiteDatabase.close();
    }



//    public void update_location_new(String title, String str_old)
//    {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues values =  new ContentValues();
//        values.put("location_category_name", title);
//        //updating row
//        sqLiteDatabase.update(TABLE_CATEGORY, values, "category_name=" + str_old, null);
//        sqLiteDatabase.close();
//    }


    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID +" = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public void delete_Category(Category_Model category_model)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category_model.getCategory_id())});
        db.close();
    }

    public void delete_location(String location_cat_name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_Location, COLUMN_Location_Category_Name + " = ?",new String[]{location_cat_name});
        db.close();
    }

//    public void delete_Category(String ID)
//    {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        //deleting row
//        sqLiteDatabase.delete(TABLE_CATEGORY, " COLUMN_CATEGORY_ID =" + ID, null);
//        sqLiteDatabase.close();
//    }

    /**
     * This method to check user exist or not
     *
     * @param username
     * @return true/false
     */

    public boolean checkUser(String username)
    {
        // array of columns to fetch
        String[] columns =
        {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        // query user table with condition


        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0)
        {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param username
     * @param password
     * @return true/false
     */
    public boolean checkUser(String username, String password)
    {
        // array of columns to fetch
        String[] columns =
        {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        // selection arguments
        String[] selectionArgs = {username, password};
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        if (cursorCount > 0)
        {
            return true;
        }
        return false;
    }
}