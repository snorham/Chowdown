package com.example.chowdown.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.chowdown.R;
import com.example.chowdown.adapters.StableArrayAdapter;
import com.example.chowdown.models.Vote;
import com.example.chowdown.network.ParsePutter;
import com.example.chowdown.network.VoteParseGrabber;
import com.example.chowdown.views.DynamicListView;
import com.google.common.collect.Multimap;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RankingActivity extends Activity {


    public static final String CHOSEN_LUNCH_EVENT_ID = "CHOSEN_LUNCH_EVENT_ID";
    public static final String ORCHID_THAI_OBJECT_ID = "doLgBRhEzo";
    public static final String TAQO_OBJECT_ID = "YqLy4jHA2T";
    public static final String SLICE_OBJECT_ID = "A1ItP7AEuy";
    String lunchEventID;

    //List<ParseObject> pOL;
    Multimap<String, String> voteResultsMultimap = null;
    ParseObject testLunchEvent;

    StableArrayAdapter restaurantAdaptor;

    VoteParseGrabber voteParseGrabber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Intent intent = getIntent();
        lunchEventID = intent.getStringExtra(CHOSEN_LUNCH_EVENT_ID);
        TextView testTextView1 = (TextView) findViewById(R.id.title_text_view);
        testTextView1.setText(lunchEventID);

        voteParseGrabber = new VoteParseGrabber(this);

        voteParseGrabber.testPostToParse(lunchEventID);

        //
        // THIS CODE GRABS ALL THE VOTES SUBMITTED FOR A PARTICULAR LUNCH EVENT
        // It should probably belong in a different place, some kind of utility.
        // We'll move it later
        voteResultsMultimap = voteParseGrabber.getVotesByLunchID(lunchEventID);
        ArrayList<String> firstChoiceRestaurants = getArrayListsOfRestaurantVotes(voteResultsMultimap, "first");
        Log.d("firstChoiceRestaurants", firstChoiceRestaurants.toString());
        // WHY DOES THE LOG HAPPEN BEFORE I GET MY RESULTS?! HOW CAN I MAKE IT HAPPEN AFTERWARD?


        // END OF VOTE MANIPULATION CODE
        //

        DynamicListView topRestaurantsListView = (DynamicListView) findViewById(R.id.ranked_restaurants_listview);

        ArrayList<String> restaurants = new ArrayList<String>();
        restaurants.add("Slice");
        restaurants.add("Orchid Thai");
        restaurants.add("TAQO");
        restaurantAdaptor = new StableArrayAdapter(this, R.layout.list_item_restaurant, restaurants);

        topRestaurantsListView.setCheeseList(restaurants);
        topRestaurantsListView.setAdapter(restaurantAdaptor);
        topRestaurantsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Button submitButton = (Button) findViewById(R.id.submit_vote_button);
        final Activity thisActivity = this;

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vote newVote = new Vote(lunchEventID, restaurantAdaptor.getItem(0), restaurantAdaptor.getItem(1), restaurantAdaptor.getItem(2));
//                System.out.println(newVote);
                ParsePutter parsePutter = new ParsePutter(thisActivity);
                parsePutter.saveVote(newVote);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ranking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<String> getArrayListsOfRestaurantVotes(Multimap<String, String> voteResultsMultimap, String rank) {
        if (voteResultsMultimap == null) {
            Log.d("NoMap", "NG");
            return null;
        }
        if (rank.equals("first")) {
            ArrayList<String> firstChoiceRestaurants = getArrayListFromCollection(voteResultsMultimap.get("firstChoice"));
            Log.d("firstChoiceRestaurants", firstChoiceRestaurants.toString());
            return firstChoiceRestaurants;
        }
        if (rank.equals("second")) {
            ArrayList<String> secondChoiceRestaurants = getArrayListFromCollection(voteResultsMultimap.get("secondChoice"));
            Log.d("secondChoiceRestaurants", secondChoiceRestaurants.toString());
            return secondChoiceRestaurants;
        }
        if (rank.equals("third")) {
            ArrayList<String> thirdChoiceRestaurants = getArrayListFromCollection(voteResultsMultimap.get("thirdChoice"));
            Log.d("thirdChoiceRestaurants", thirdChoiceRestaurants.toString());
            return thirdChoiceRestaurants;
        }
        return null;
    }

    public ArrayList<String> getArrayListFromCollection(Collection<String> collection) {
        String[] stringArray = collection.toArray(new String[collection.size()]);
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(stringArray));
        return arrayList;
    }
}
