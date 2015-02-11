package com.iems5722.translateapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chan on 2/11/2015.
 */
public class TranslateHistoriesAdapter extends ArrayAdapter<TranslateHistory> {
    public TranslateHistoriesAdapter(Context context, ArrayList<TranslateHistory> translateHistories){
        super(context, 0, translateHistories);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TranslateHistory translatehistory = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_translate_histories, parent, false);
        }
        // Lookup view for data population
//        TextView translateWord = (TextView) convertView.findViewById(R.id.translateWord);
//        TextView translateResult = (TextView) convertView.findViewById(R.id.translateResult);

        // Populate the data into the template view using the data object
//        translateWord.setText(translatehistory.word);
//        translateResult.setText(translatehistory.result);
        // Return the completed view to render on screen
        return convertView;
    }
}
