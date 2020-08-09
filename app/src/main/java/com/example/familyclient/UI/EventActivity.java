package com.example.familyclient.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.familyclient.Model.DataCache;
import com.example.familyclient.R;

import android.os.Bundle;

import Model.Event;

public class EventActivity extends AppCompatActivity
{

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle b = getIntent().getExtras();

        if(b!= null)
        {
            String eventID = b.getString("eventID");

            Event event = DataCache.getInstance().getEvents().get(eventID);

            FragmentManager fm = this.getSupportFragmentManager();

            mapFragment = new MapFragment(event);
            Bundle args = new Bundle();
            mapFragment.setArguments(args);

            fm.beginTransaction()
                    .add(R.id.eventContainer, mapFragment)
                    .commit();
        }
    }
}