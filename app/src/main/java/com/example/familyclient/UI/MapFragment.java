package com.example.familyclient.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private ArrayList<Polyline> polyLines;
    private HashMap<String, List<Event>> personEvents;

    ImageView iconView;
    TextView eventText;
    LinearLayout infoBox;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Event passInEvent;

    public MapFragment()
    {
        // Required empty public constructor
    }

    public MapFragment(Event e)
    {
        passInEvent = e;
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

        iconView = (ImageView) v.findViewById(R.id.boxIcon);
        eventText = (TextView) v.findViewById(R.id.eventText);

        Drawable icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_android).color(4900419).sizeDp(40);

        iconView.setImageDrawable(icon);

        infoBox = (LinearLayout) v.findViewById(R.id.infoBox);

        return v;
    }

    @Override
    public void onMapLoaded()
    {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener () {

            @Override
            public boolean onMarkerClick(Marker marker)
            {
                Event markerEvent = (Event) marker.getTag();
                clearPolyLines();
                drawPolyLines(markerEvent);

                centerEvent(markerEvent);


                return true;
            }
        });
    }

    private void centerEvent(Event markerEvent)
    {
        Person markerPerson = persons.get(markerEvent.getPersonID());

        String newText = markerPerson.getFirstName() + " " + markerPerson.getLastName() + "\n";
        newText = newText + markerEvent.getEventType() + ": " + markerEvent.getCity() + ", "
                + markerEvent.getCountry() + " (" + markerEvent.getYear() + ")";

        eventText.setText(newText);

        Drawable icon;

        if(markerPerson.getGender().equals("m"))
        {
            icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).color(2847675).sizeDp(40);
        }
        else
        {
            icon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).color(12743611).sizeDp(40);
        }

        iconView.setImageDrawable(icon);

        infoBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                personActivityOpener();
                //TODO PAss in a person I suppose
            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(markerEvent.getLatitude(), markerEvent.getLongitude())));
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        //Log.d("MapReady", "onMapReadyCalled");
        map = googleMap;
        map.setOnMapLoadedCallback(this);

        populateMap();


        if(passInEvent != null)
        {
            centerEvent(passInEvent);
        }


    }

    private void clearPolyLines()
    {
        if(polyLines != null)
        {
            for(int i = 0; i < polyLines.size(); i++)
            {
                polyLines.get(i).remove();
            }
        }

        polyLines = new ArrayList<>();
    }

    private void drawPolyLines(Event event)
    {
        if(!event.getPersonID().equals(DataCache.getInstance().getUserID()))
        {
            drawFamilyTreeLines(event, 1);
        }

        else
        {
            //TODO Ask about line specifics. father side/ no male means don't draw unconnected lines to grandmothers on fathers side?
            handleUserEvent(event);
        }
        drawSpouseLine(event);
    }

    private void handleUserEvent(Event event)
    {
        String userID = DataCache.getInstance().getUserID();
        Person user = persons.get(userID);
        Event fatherEvent = null;
        Event motherEvent = null;

        int fatherWidth = 0;
        int motherWidth = 0;

        if(user.getFatherID() != null && settings.isFatherSide())
        {
            ArrayList<Event> fatherEvents = (ArrayList<Event>) personEvents.get(user.getFatherID());

            if(fatherEvents.size() > 0)
            {
                fatherEvent = fatherEvents.get(0);
                fatherWidth = drawFamilyTreeLines(fatherEvent, 1);
            }
        }

        if(user.getMotherID() != null && settings.isMotherSide())
        {
            ArrayList<Event> motherEvents = (ArrayList<Event>) personEvents.get(user.getMotherID());

            if(motherEvents.size() > 0)
            {
                motherEvent = motherEvents.get(0);
                motherWidth = drawFamilyTreeLines(motherEvent, 1);
            }
        }

        int thisWidth = fatherWidth > motherWidth ? fatherWidth : motherWidth;

        if(fatherEvent != null && settings.isShowMale())
        {
            Polyline fatherLine = map.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(event.getLatitude(), event.getLongitude()),
                            new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude()))
                    .width(thisWidth)
                    .color(Color.BLUE)
                    .visible(true));


            polyLines.add(fatherLine);
        }

        if(motherEvent != null && settings.isShowFemale())
        {
            Polyline motherLine = map.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(event.getLatitude(), event.getLongitude()),
                            new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude()))
                    .width(thisWidth)
                    .color(Color.BLUE)
                    .visible(true));

            polyLines.add(motherLine);
        }
    }

    private void drawSpouseLine(Event event)
    {
        Person person = persons.get(event.getPersonID());
        Event spouseEvent = null;
        if(person.getSpouseID() != null)
        {
            Person spouse = persons.get(person.getSpouseID());

            if( (spouse.getGender().equals("m") && settings.isShowMale() ) || ( spouse.getGender().equals("f") && settings.isShowFemale() ) )
            {
                ArrayList<Event> spouseEvents = (ArrayList<Event>) personEvents.get(spouse.getId());

                if(spouseEvents.size() > 0)
                {
                    spouseEvent = spouseEvents.get(0);
                }
            }
        }

        if(spouseEvent != null)
        {
            if(DataCache.getInstance().getFatherSide().contains(spouseEvent.getPersonID()) && settings.isFatherSide())
            {
                Polyline spouseLine = map.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(event.getLatitude(), event.getLongitude()),
                                new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
                        .width(7)
                        .color(Color.RED)
                        .visible(true));


                polyLines.add(spouseLine);
            }

            if(DataCache.getInstance().getMotherSide().contains(spouseEvent.getPersonID()) && settings.isMotherSide())
            {
                Polyline spouseLine = map.addPolyline(new PolylineOptions()
                        .clickable(true)
                        .add(
                                new LatLng(event.getLatitude(), event.getLongitude()),
                                new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude()))
                        .width(7)
                        .color(Color.RED)
                        .visible(true));


                polyLines.add(spouseLine);
            }
        }

    }


    private int drawFamilyTreeLines(Event event, int width)
    {
        int thisWidth = 0;
        Person child = persons.get(event.getPersonID());

        Event fatherEvent = null;
        Event motherEvent = null;

        int fatherWidth = 0;
        int motherWidth = 0;

        if(child.getFatherID() != null)
        {
            ArrayList<Event> fatherEvents = (ArrayList<Event>) personEvents.get(child.getFatherID());

            if(fatherEvents.size() > 0)
            {
                fatherEvent = fatherEvents.get(0);
                int returnWidth = drawFamilyTreeLines(fatherEvent, width);

                if(returnWidth > fatherWidth)
                {
                    fatherWidth = returnWidth;
                }
            }
        }

        if(child.getMotherID() != null)
        {
            ArrayList<Event> motherEvents = (ArrayList<Event>) personEvents.get(child.getMotherID());

            if(motherEvents.size() > 0)
            {
                motherEvent = motherEvents.get(0);
                int returnWidth = drawFamilyTreeLines(motherEvent, width);

                if(returnWidth > motherWidth)
                {
                    motherWidth = returnWidth;
                }
            }
        }

        thisWidth = fatherWidth > motherWidth ? fatherWidth : motherWidth;
        thisWidth = thisWidth > width ? thisWidth : width;

        if(fatherEvent != null && settings.isShowMale())
        {
            Polyline fatherLine = map.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                    new LatLng(event.getLatitude(), event.getLongitude()),
                    new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude()))
            .width(fatherWidth)
            .color(Color.BLUE)
            .visible(true));


            polyLines.add(fatherLine);
        }

        if(motherEvent != null && settings.isShowFemale())
        {
            Polyline motherLine = map.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(event.getLatitude(), event.getLongitude()),
                            new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude()))
                    .width(motherWidth)
                    .color(Color.BLUE)
                    .visible(true));

            polyLines.add(motherLine);
        }

        return thisWidth + 3;


        //Find parent events if exist
        //pass width, and parent event into this function recursively. return width + 3 to pass into child width line
        //
    }

    private void drawLifeStoryLines(Event event)
    {

    }



    private void settingsActivityOpener()
    {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent);
    }

    private void personActivityOpener()
    {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        startActivity(intent);
    }

    private void populateMap()
    {
        map.clear();

        persons = DataCache.getInstance().getPersons();
        events = DataCache.getInstance().getEvents();
        settings = DataCache.getInstance().getSettings();
        eventColors = DataCache.getInstance().getEventColors();
        personEvents = DataCache.getInstance().getPersonEvents();

        if(settings.isFatherSide())
        {
            addSide(DataCache.getInstance().getFatherSide());
        }

        if(settings.isMotherSide())
        {
            addSide(DataCache.getInstance().getMotherSide());
        }

        allOtherEvents();
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

    private void allOtherEvents()
    {
        Set<String> fatherSide = DataCache.getInstance().getFatherSide();
        Set<String> motherSide = DataCache.getInstance().getMotherSide();

        for(Map.Entry<String, List<Event>> entry : personEvents.entrySet())
        {
            if(!fatherSide.contains(entry.getKey()) && !motherSide.contains(entry.getKey()))
            {
                ArrayList<Event> eventList = (ArrayList) entry.getValue();
                for(int i = 0; i < eventList.size(); i++)
                {
                    genderCheck(eventList.get(i));
                }
            }
        }
    }

}