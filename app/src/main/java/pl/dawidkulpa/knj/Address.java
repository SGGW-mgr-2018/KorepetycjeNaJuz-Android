package pl.dawidkulpa.knj;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Address {
    private double lat;
    private double lng;
    private String city;
    private String street;

    public static Address create(JSONObject jObj){
        Address address=new Address();

        try{
            address.lat= jObj.getDouble("latitude");
            address.lng= jObj.getDouble("longitude");
            address.city= jObj.getString("city");
            address.street= jObj.getString("street");


        } catch (JSONException je){
            Log.e("Address", je.getMessage());
            address=null;
        }

        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }
}
