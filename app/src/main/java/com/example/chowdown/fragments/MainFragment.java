package com.example.chowdown.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.chowdown.R;
import com.example.chowdown.activities.CreateLunchActivity;
import com.example.chowdown.activities.LunchDetailActivity;
import com.example.chowdown.adapters.LunchEventAdapter;
import com.example.chowdown.models.LunchEvent;
import com.example.chowdown.models.ParseConverterObject;
import com.example.chowdown.models.Vote;
import com.example.chowdown.network.LunchEventParseGrabber;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {


    public static final String USERNAME_KEY = "USERNAME_KEY";
    LunchEventAdapter mLunchEventAdapter;
    LunchEventParseGrabber lunchEventParseGrabber;
    ParseConverterObject mParseConverterObject = new ParseConverterObject();
    public static ArrayList<LunchEvent> arrayOfLunches = new ArrayList<LunchEvent>();
    public static ArrayList<Vote> arrayOfVotes = new ArrayList<Vote>();
    public static final String CHOSEN_LUNCH_KEY = "CHOSEN_LUNCH_KEY";
    public static final String LOG_TAG = "MainActivity";
    public Button newLunchButton;


    public MainFragment () {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Log.d(LOG_TAG, "ITEM CLICKED IN ADAPTER VIEW");
        LunchEvent chosenLunch = mLunchEventAdapter.getItem(position);

        Intent detailIntent = new Intent(getActivity(), LunchDetailActivity.class);
        detailIntent.putExtra(CHOSEN_LUNCH_KEY,chosenLunch);
        Log.d(LOG_TAG, "EventID is " + chosenLunch.getEventID());
        startActivity(detailIntent);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        newLunchButton = (Button) rootView.findViewById(R.id.new_lunch_button);
        newLunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createIntent = new Intent(getActivity(), CreateLunchActivity.class);
                startActivity(createIntent);
            }
        });

        lunchEventParseGrabber = new LunchEventParseGrabber(getActivity());

        List<ParseObject> pOL = lunchEventParseGrabber.getLunchEvents();

        int i = 0;
        for (ParseObject pO: pOL) {
            arrayOfLunches.add(i, mParseConverterObject.parseObjectToLunchEvent(pO));
            i++;

        }

        mLunchEventAdapter = new LunchEventAdapter(getActivity(), arrayOfLunches);

        mLunchEventAdapter.addAll(arrayOfLunches);

        listView.setAdapter(mLunchEventAdapter);

        listView.setOnItemClickListener(this);



        return rootView;
    }

}
