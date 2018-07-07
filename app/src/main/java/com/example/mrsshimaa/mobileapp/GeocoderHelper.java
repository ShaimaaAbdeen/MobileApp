package com.example.mrsshimaa.mobileapp;

/**
 * Created by Mrs.shimaa on 4/11/2018.
 */
import java.util.List;
import java.util.Locale;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GeocoderHelper {
    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(GeocoderHelper.class.getName());

    private boolean running = false;
    String cityName = null;

    public void fetchCityName(final Context contex, final LatLng location)
    {
        if (running)
            return ;

        new AsyncTask<Void, Void, String>()
        {
            protected void onPreExecute()
            {
                running = true;
            };

            @Override
            protected String doInBackground(Void... params)
            {

                if (Geocoder.isPresent())
                {
                    try
                    {
                        Geocoder geocoder = new Geocoder(contex, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
                        if (addresses.size() > 0)
                        {
                            cityName = addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName();
                        }
                    }
                    catch (Exception ignored)
                    {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (cityName != null) // i.e., Geocoder succeed
                {
                    return cityName;
                }
                else // i.e., Geocoder failed
                {
                    return fetchCityNameUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchCityNameUsingGoogleMap()
            {
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.latitude + ","
                        + location.longitude + "&sensor=false&language=fr";
                Log.i(googleMapUrl,"url:");

                try
                {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results
                    JSONArray results = (JSONArray) googleMapResponse.get("results");

                        JSONObject result = results.getJSONObject(0);


                            String address = result.getString("formatted_address");
                    Log.i(address,"city:");

                    // loop among all address component to find a 'locality' or 'sublocality'

                            cityName=address;
                            if (cityName != null)
                            {
                                Log.i(cityName,"city:");

                                return cityName;
                            }



                }
                catch (Exception ignored)
                {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String cityName)
            {
                running = false;
                if (cityName != null)
                {
                    HospitalLocation.setCityName(cityName);
                    Log.i("GeocoderHelper", cityName);
                }
            };
        }.execute();

        Log.i(cityName,"City:");
    }

}
