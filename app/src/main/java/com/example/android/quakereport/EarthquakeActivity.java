/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.android.quakereport.R;
import com.example.android.quakereport.databinding.EarthquakeActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public String URL;
    private EarthquakeAdapter mAdapter;
    JsonViewModel jsonViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        final EarthquakeActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.earthquake_activity);
        //Get Viewmodel instance
        jsonViewModel = new ViewModelProvider(this).get(JsonViewModel.class);
        // LiveData should call setValue() to update UI via binding
        binding.setViewModel(jsonViewModel);
        // Required to update UI with LiveData
        binding.setLifecycleOwner(this);

        // Find a reference to the {@link ListView} in the layout
        final ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setEmptyView(findViewById(R.id.empty));
        jsonViewModel.loadData();

        /**
         * ViewModel to update UI when data changes
         */
        jsonViewModel.getData().observe(this, new Observer<List<Earthquake>>() {
            @Override
            public void onChanged(List<Earthquake> earthquakes) {
                Log.d("MainActivity", "Tracing mainActivity observe method");
                if(earthquakes != null){
                    mAdapter = new EarthquakeAdapter(getApplicationContext(), earthquakes);
                    // Set the adapter on the {@link ListView}
                    // so the list can be populated in the user interface
                    earthquakeListView.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        //Set click lsitener for list items
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get URL from item
                Earthquake selectedItem =  (Earthquake) parent.getItemAtPosition(position);
                URL = selectedItem.getURL();

                // Send url in implicit intent
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });
    }
}
