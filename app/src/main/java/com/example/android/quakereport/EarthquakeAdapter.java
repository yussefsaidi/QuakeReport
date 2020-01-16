package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private Context mContext = null;

    private String formatDate (Date dateObject){
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM DD, yyyy");
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    private String formatTime (Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeToDisplay = timeFormat.format(dateObject);
        return timeToDisplay;
    }

    private String formatMagnitude (double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeResourceId;
        int magnitudeFloor = (int)Math.floor(magnitude);
        switch(magnitudeFloor){
            case 0:
            case 1:
                magnitudeResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeResourceId);
    }


    public EarthquakeAdapter(Activity context, ArrayList<Earthquake> EqList) {
        super(context, 0, EqList);
        mContext = context;
    }



    public View getView(int position, View convertView, ViewGroup parent) {


        String offsetText;
        String primaryLocation;
        Earthquake currentEarthquake = getItem(position);
        String locationToSplit = currentEarthquake.getLocation();






        // Check if existing view is getting reused, otherwise inflate the view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item,parent,false);
        }

        TextView magnitudeView = convertView.findViewById(R.id.magnitude);
        TextView location = convertView.findViewById(R.id.location);
        TextView date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);
        TextView locationOffset = convertView.findViewById(R.id.location_offset);

        // Processing color of magnitude icons
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        //Processing magnitude
        double mag = currentEarthquake.getMagnitude();
        magnitudeView.setText(formatMagnitude(mag));



        // Create date & time objects from time in milliseconds
        long timeInMilliseconds = currentEarthquake.getTime();
        Date dateObject = new Date(timeInMilliseconds);


        // Split location into 2 TextViews
        //Location is given in offset of primary location
        if(locationToSplit.contains(mContext.getString(R.string.of))) {
            int offset = locationToSplit.indexOf(mContext.getString(R.string.of));
            offsetText = locationToSplit.substring(0, offset+2);
            primaryLocation = locationToSplit.substring(offset + 2);
        }
        //Location is given with no offset
        else{
            offsetText = mContext.getString(R.string.near_the);
            primaryLocation = locationToSplit;
        }



        locationOffset.setText(offsetText);
        location.setText(primaryLocation);
        date.setText(formatDate(dateObject));
        time.setText(formatTime(dateObject));

        return convertView;

    }
}
