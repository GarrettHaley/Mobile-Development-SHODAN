package com.example.shodan.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.shodan.ShodanItem;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.TimeZone;

public class ShodanUtils {
    private final static String SHODAN_BASE_URL = "https://api.shodan.io/shodan/host/search";
    private final static String SHODAN_APPID = "hum6wgv0lrzu18g79j8Ot1uus9Lohy4i";

    /*
     * The below several classes are used only for JSON parsing with Gson of the shodan rest api response.
     */
    static class ShodanResults {
        ShodanListItem[] matches;
    }

    static class ShodanListItem {
        Long ip;
        Integer port;
        String transport;
        Location location;
        String product;
        Http http;
        String org;
        String timestamp;
        String isp;
    }
    static class Http{
        String title;
    }
    static class Location{
        String city;
        Double latitude;
        Double longitude;
        String country_code;
    }


    public static String buildShodanURL(String shodanQuery) {
        String query = "title:\"hacked by\" city:\"" + shodanQuery +"\"";
        return Uri.parse(SHODAN_BASE_URL).buildUpon()
                .appendQueryParameter("key", SHODAN_APPID)
                .appendQueryParameter("query", query)
                .build()
                .toString();
    }

    public static ArrayList<ShodanItem> parseShodanJSON(String shodanJSON) {
        Gson gson = new Gson();
        ShodanResults results = gson.fromJson(shodanJSON, ShodanResults.class);
        if (results != null && results.matches != null) {
            ArrayList<ShodanItem> shodanItems = new ArrayList<>();
            /*
             * Loop through all results parsed from JSON and condense each one into one
             * single-level ShodanItem object.
             */
            SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            dateParser.setTimeZone(TimeZone.getTimeZone("UTC"));
            for (ShodanListItem listItem : results.matches) {
                ShodanItem shodanItem = new ShodanItem();
                shodanItem.isp = listItem.isp;
                try {
                    shodanItem.timestamp = DateFormat.getDateTimeInstance().format(dateParser.parse(listItem.timestamp));
                }catch(Exception e){
                    e.printStackTrace();
                    shodanItem.timestamp = null;
                }
                shodanItem.organization = listItem.org;
                shodanItem.ip = listItem.ip;
                shodanItem.port = listItem.port;
                shodanItem.product = listItem.product.toLowerCase();
                shodanItem.transport = listItem.transport;
                shodanItem.title = listItem.http.title.toUpperCase();
                shodanItem.countryCode = listItem.location.country_code;
                shodanItem.city = listItem.location.city;
                shodanItem.longitude = listItem.location.longitude;
                shodanItem.latitude = listItem.location.latitude;

                shodanItems.add(shodanItem);
            }
            return shodanItems;
            }
            else {
                return null;
            }
    }
}

