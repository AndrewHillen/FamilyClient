package com.example.familyclient.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familyclient.Model.DataCache;
import com.example.familyclient.Model.Settings;
import com.example.familyclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import Model.Event;
import Model.Person;

public class PersonActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Bundle b = getIntent().getExtras();

        if(b!= null)
        {
            String personID = b.getString("personID");
            HashMap<String, String> relationships = new HashMap<>();

            List<Person> family = buildFamily(relationships, personID);
            //Call functions to create personList and eventList
            ExpandableListView expandableListView = findViewById(R.id.expandableListView);

            List<Event> events = DataCache.getInstance().getPersonEvents().get(personID);

            expandableListView.setAdapter(new ExpandableListAdapter(family, events, relationships));
        }



    }

    private List<Person> buildFamily(HashMap<String, String> relationships, String personID)
    {
        ArrayList<Person> family = new ArrayList<>();
        List<Person> children = DataCache.getInstance().getPersonChildren().get(personID);

        Person person = DataCache.getInstance().getPersons().get(personID);

        if(person.getFatherID() != null)
        {
            relationships.put(person.getFatherID(), "Father");
            family.add(DataCache.getInstance().getPersons().get(person.getFatherID()));
        }

        if(person.getMotherID() != null)
        {
            relationships.put(person.getMotherID(), "Mother");
            family.add(DataCache.getInstance().getPersons().get(person.getMotherID()));
        }

        if(person.getSpouseID() != null)
        {
            relationships.put(person.getSpouseID(), "Spouse");
            family.add(DataCache.getInstance().getPersons().get(person.getSpouseID()));
        }

        for(int i = 0; i < children.size(); i++)
        {
            relationships.put(children.get(i).getId(), "Child");
            family.add(children.get(i));
        }

        return family;
    }



    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {

        private static final int FAMILY_POSITION = 0;
        private static final int EVENT_POSITION = 1;

        private final List<Person> family;
        private final HashMap<String, String> relationships;
        private final List<Event> events;

        public ExpandableListAdapter(List<Person> family, List<Event> events, HashMap<String, String> relationships)
        {
            this.family = family;
            this.events = events;
            this.relationships = relationships;
        }

        @Override
        public int getGroupCount()
        {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition)
        {
            switch (groupPosition)
            {
                case FAMILY_POSITION:
                    return family.size();
                case EVENT_POSITION:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Invalid group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition)
        {
            switch (groupPosition)
            {
                case FAMILY_POSITION:
                    return "Family";
                case EVENT_POSITION:
                    return "Events";
                default:
                    throw new IllegalArgumentException("Invalid group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition)
        {
            switch (groupPosition)
            {
                case FAMILY_POSITION:
                    return family.get(childPosition);
                case EVENT_POSITION:
                    return events.get(childPosition);
                default:
                    throw new IllegalArgumentException("Invalid group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition)
        {
            //return i?
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition)
        {
            //return i1?
            return childPosition;
        }

        @Override
        public boolean hasStableIds()
        {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup)
        {
            if(view == null)
            {
                view = getLayoutInflater().inflate(R.layout.list_item_group, viewGroup, false);
            }

            TextView title = view.findViewById(R.id.listTitle);
            switch (groupPosition)
            {
                case FAMILY_POSITION:
                    title.setText("Family");
                    break;
                case EVENT_POSITION:
                    title.setText("Events");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid group position: " + groupPosition);
            }

            return view;
        }

        //TODO fix this one
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup)
        {
            View itemView;
            switch (groupPosition)
            {
                case FAMILY_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, viewGroup, false);
                    initializePerson(itemView, childPosition);
                    break;
                case EVENT_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, viewGroup, false);
                    initializeEvent(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePerson(View personView, final int childPosition)
        {
            final Person person = family.get(childPosition);

            if(!settingsChecker(person.getId()))
            {
                personView = null;
                return;
            }

            Drawable icon;

            if(person.getGender().equals("m"))
            {
                icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).color(2847675).sizeDp(30);
            }
            else
            {
                icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).color(12743611).sizeDp(30);
            }

            ImageView genderIcon = personView.findViewById(R.id.genderIcon);

            genderIcon.setImageDrawable(icon);
            String name = person.getFirstName() + " " + person.getLastName();
            String relationship = relationships.get(person.getId());

            TextView nameText = personView.findViewById(R.id.personName);
            nameText.setText(name);

            TextView relationshipText = personView.findViewById(R.id.personRelationship);
            relationshipText.setText(relationship);

            LinearLayout newPersonClick = personView.findViewById(R.id.newPerson);

            newPersonClick.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    personActivityOpener(person.getId());
                }
            });


        }

        private void initializeEvent(View eventView, final int childPosition)
        {
            final Event event = events.get(childPosition);
            Person person = DataCache.getInstance().getPersons().get(event.getPersonID());

            Drawable icon;


            icon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).color(2847675).sizeDp(30);


            ImageView eventIcon = eventView.findViewById(R.id.eventIcon);

            eventIcon.setImageDrawable(icon);
            String info = event.getEventType() + ": " + event.getCity() + ", "
                    + event.getCountry() + " (" + event.getYear() + ")";

            String name = person.getFirstName() + " " + person.getLastName();

            TextView infoText = eventView.findViewById(R.id.eventInfo);
            infoText.setText(info);

            TextView nameText = eventView.findViewById(R.id.eventName);
            nameText.setText(name);

            LinearLayout newEventClick = eventView.findViewById(R.id.newEvent);

            newEventClick.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    eventActivityOpener(event.getId());
                }
            });
        }

        private void personActivityOpener(String personID)
        {
            Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
            Bundle b = new Bundle();

            //Find family and eventList with personID
            b.putString("personID", personID);
            //Bundle b = new Bundle();
            //if only allows primitive values, personID and eventID is enough to find stuff
            //b.put("family", familyList);
            //b.put("events", eventList);
            //intent.putExtras(b)
            intent.putExtras(b);
            startActivity(intent);
        }

        private void eventActivityOpener(String eventID)
        {

            Intent intent = new Intent(PersonActivity.this, EventActivity.class);
            Bundle b = new Bundle();

            b.putString("eventID", eventID);
            intent.putExtras(b);
            startActivity(intent);
        }

        private boolean settingsChecker(String personID)
        {
            Person person = DataCache.getInstance().getPersons().get(personID);
            Set<String> fatherSide = DataCache.getInstance().getFatherSide();
            Set<String> motherSide = DataCache.getInstance().getMotherSide();
            Settings settings = DataCache.getInstance().getSettings();

            if(person.getGender().equals("m") && !settings.isShowMale())
            {
                return false;
            }

            if(person.getGender().equals("f") && !settings.isShowFemale())
            {
                return false;
            }

            if(fatherSide.contains(personID) && !settings.isFatherSide())
            {
                return false;
            }
            if(motherSide.contains(personID) && !settings.isMotherSide())
            {
                return false;
            }


            return true;
        }


        @Override
        public boolean isChildSelectable(int i, int i1)
        {
            //Change to true?
            return false;
        }
    }
}