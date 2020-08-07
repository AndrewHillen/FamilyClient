package com.example.familyclient.UI;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.familyclient.Model.DataCache;
import com.example.familyclient.Model.Settings;
import com.example.familyclient.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Model.Event;
import Model.Person;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback
{
    private GoogleMap map;
    MenuItem settingsItem;
    private HashMap<String, Event> events;
    private HashMap<String, Person> persons;
    private Settings settings;
    private HashMap<String, Float> eventColors;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2)
    {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_map, menu);
        settingsItem = menu.findItem(R.id.mapFragmentSettings);
        Drawable settingsIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).sizeDp(40);
        settingsItem.setIcon(settingsIcon);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                settingsActivityOpener();
                return false;
            }
        });
    }
    @Override
    public void onResume()
    {

        super.onResume();
        //Log.d("onResume", "Onresume called");
        if(map != null)
        {
            populateMap();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onMapLoaded()
    {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //Log.d("MapReady", "onMapReadyCalled");
        map = googleMap;
        map.setOnMapLoadedCallback(this);


        populateMap();


    }

    private void settingsActivityOpener()
    {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    private void populateMap()
    {
        map.clear();

        persons = DataCache.getInstance().getPersons();
        events = DataCache.getInstance().getEvents();
        settings = DataCache.getInstance().getSettings();
        eventColors = DataCache.getInstance().getEventColors();
        String userID = DataCache.getInstance().getUserID();

        if(settings.isFatherSide())
        {
            addSide(DataCache.getInstance().getFatherSide());
        }

        if(settings.isMotherSide())
        {
            addSide(DataCache.getInstance().getMotherSide());
        }

        List<Event> userEvents = DataCache.getInstance().getPersonEvents().get(userID);

        for(int i = 0; i < userEvents.size(); i++)
        {
            genderCheck(userEvents.get(i));
        }
    }

    private void addSide(Set<String> side)
    {
        for(String personID : side)
        {
            List<Event> personEvents = DataCache.getInstance().getPersonEvents().get(personID);

            for(int i = 0; i < personEvents.size(); i++)
            {
                genderCheck(personEvents.get(i));
            }
        }
    }

    private void genderCheck(Event event)
    {
        Person associatedPerson = persons.get(event.getPersonID());
        String gender = associatedPerson.getGender();

        if(gender.equals("f") && settings.isShowFemale())
        {
            addMarker(event);
        }

        if(gender.equals("m") && settings.isShowMale())
        {
            addMarker(event);
        }
    }

    private void addMarker(Event event)
    {
        float markerColor = eventColors.get(event.getEventType());
        LatLng markerLocation = new LatLng(event.getLatitude(), event.getLongitude());

        Marker newMarker = map.addMarker(
                new MarkerOptions()
                .position(markerLocation)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));

        newMarker.setTag(event);
    }

    //TODO Add polylines to stuff. All Events should be bound to markers for click events. Maybe can set click listeners on the fly?

    //Get all events
    //if father, add all father events to map (if male, if female)
    //if mother, add all mother events to map (if male, if female)
}