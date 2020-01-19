package com.example.android.quakereport;

import android.app.DownloadManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Creates URL object and makes an HTTP request to API
     * @param requestUrl
     * @return list of earthquakes
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        String jsonResponse = null;
        URL url = createUrl(requestUrl);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform HTTP request to the url and receive a jsonResponse
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e("QueryUtils", "Error closing input stream", e);
        }

        // Extract relevant information from the JSON response
        // and create a list of Earthquake events
        List<Earthquake> earthquakes = extractFeaturesFromJson(jsonResponse);
        return earthquakes;
    }

    private static List<Earthquake> extractFeaturesFromJson(String jsonResponse) {
        // if JSON string empty or null, return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        double magnitude;
        String location, URL;
        Long time;
        JSONObject feature, properties;
        List<Earthquake> earthquakes = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONArray features = baseJsonResponse.getJSONArray("features");

            // Get feature by feature, data and create earthquake event from it.
            for (int i = 0; i < features.length(); i++) {
                feature = features.getJSONObject(i);
                properties = feature.getJSONObject("properties");

                //Get magnitude, location, and time
                magnitude = properties.getDouble("mag");
                location = properties.getString("place");
                time = properties.getLong("time");
                URL = properties.getString("url");
                earthquakes.add(new Earthquake(magnitude, location, time, URL));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    /**
     * Make an HTTP Request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request is successful
            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("QueryUtils", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch(IOException e) {
            Log.e("QueryUtils", "Problem getting data from URL", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains
     * the whole JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Returns URL object from given string URL
     */
    private static URL createUrl(String requestUrl){
        URL url = null;
        try{
            url = new URL(requestUrl);
        } catch(MalformedURLException e){
            Log.e("QueryUtils", "Problem forming URL", e);
        }
        return url;
    }

}