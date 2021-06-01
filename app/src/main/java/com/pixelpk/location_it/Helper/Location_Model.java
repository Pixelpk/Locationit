package com.pixelpk.location_it.Helper;

public class Location_Model
{
    private int location_id;
    String location_category_name;
    Double locaation_lat;
    Double location_long;

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public Double getLocaation_lat() {
        return locaation_lat;
    }

    public void setLocaation_lat(Double locaation_lat)
    {
        this.locaation_lat = locaation_lat;
    }

    public Double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(Double location_long) {
        this.location_long = location_long;
    }

    public String getLocation_category_name() {
        return location_category_name;
    }

    public void setLocation_category_name(String location_category_name) {
        this.location_category_name = location_category_name;
    }
}
