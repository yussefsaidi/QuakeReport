package com.example.android.quakereport;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class JsonViewModel extends AndroidViewModel {
    public static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private final MutableLiveData<List<Earthquake>> data = new MutableLiveData<>();
    public EarthquakeAdapter mAdapter;

    public JsonViewModel(@NonNull Application application) {
        super(application);
        loadData();
    }

    public LiveData<List<Earthquake>> getData(){
        return data;
    }

    private void loadData(){
        // Create a list of earthquakes.
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(REQUEST_URL);
    }

    private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            Log.d("Viewmodel", "Tracing viewmodel network call");
            if(urls == null || urls.length < 0)
                return null;
            List<Earthquake> earthquakes = QueryUtils.fetchEarthquakeData(urls[0]);
            return earthquakes;
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            // If no result, do nothing
            if(earthquakes != null && !earthquakes.isEmpty())
                //mAdapter.addAll(earthquakes);
               data.postValue(earthquakes);
             }
    }
}
