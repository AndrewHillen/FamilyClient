package com.example.familyclient.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyclient.Model.DataCache;
import com.example.familyclient.Model.Settings;
import com.example.familyclient.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class SearchActivity extends AppCompatActivity
{
    private static final int PERSON_POSITION = 0;
    private static final int EVENT_POSITION = 1;
    private Settings settings;
    private HashMap<String, Person> filteredPersons;
    private HashMap<String, Event> filteredEvents;
    private List<Person> personResult;
    private List<Event> eventResult;
    ImageView searchButton;
    EditText searchBar;
    ImageView clearSearch;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        personResult = new ArrayList<>();
        eventResult = new ArrayList<>();
        filteredPersons = new HashMap<>();
        filteredEvents = new HashMap<>();

        settings = DataCache.getInstance().getSettings();
        filterOptions();

        searchButton = findViewById(R.id.searchButton);
        Drawable searchIcon = new IconDrawable(this, FontAwesomeIcons.fa_search).sizeDp(30);
        searchButton.setImageDrawable(searchIcon);

        searchBar = findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                String searchText = searchBar.getText().toString();
                fillResults(searchText);
                searchBar.clearFocus();
                updateView();
                return;
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                /*String searchText = searchBar.getText().toString();
                fillResults(searchText);

                updateView();*/
            }
        });

        clearSearch = findViewById(R.id.clearSearch);
        Drawable clearIcon = new IconDrawable(this, FontAwesomeIcons.fa_times).sizeDp(30);
        clearSearch.setImageDrawable(clearIcon);
        clearSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                searchBar.setText("");
                personResult = new ArrayList<>();
                eventResult = new ArrayList<>();
                updateView();
            }
        });

        updateView();


    }
    private void updateView()
    {
        if(searchBar.getText().toString().equals(""))
        {
            personResult = new ArrayList<>();
            eventResult = new ArrayList<>();
        }
        SearchAdapter adapter = new SearchAdapter(personResult, eventResult);
        recyclerView.setAdapter(adapter);
    }

    private void fillResults(String search)
    {
        personResult = new ArrayList<>();
        eventResult = new ArrayList<>();
        fillPersons(search.toLowerCase());
        fillEvents(search.toLowerCase());
    }

    private void fillPersons(String search)
    {
        for(Map.Entry<String, Person> entry : filteredPersons.entrySet())
        {
            Person person = entry.getValue();
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();

            if(firstName.contains(search) || lastName.contains(search))
            {
                personResult.add(person);
            }

        }
    }
    private void fillEvents(String search)
    {
        for(Map.Entry<String, Event> entry : filteredEvents.entrySet())
        {
            Event event = entry.getValue();

            Person person = filteredPersons.get(event.getPersonID());
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();
            String eventType = event.getEventType().toLowerCase();
            String city = event.getCity().toLowerCase();
            String country = event.getCountry().toLowerCase();
            String year = Integer.toString(event.getYear()).toLowerCase();

            if(eventType.contains(search) || city.contains(search) || country.contains(search) || year.contains(search) || firstName.contains(search) || lastName.contains(search))
            {
                eventResult.add(event);
            }
        }
    }

    private void filterOptions()
    {
        HashMap<String, List<Event>> personEvents = DataCache.getInstance().getPersonEvents();

        HashMap<String, Person> persons = DataCache.getInstance().getPersons();
        for(Map.Entry<String, Person> entry : persons.entrySet())
        {
            filteredPersons.put(entry.getKey(), entry.getValue());
            if(personChecker(entry.getValue()))
            {
                //filteredPersons.put(entry.getKey(), entry.getValue());
                List<Event> individualEvents = personEvents.get(entry.getKey());

                if(individualEvents != null)
                {
                    for(int i = 0; i < individualEvents.size(); i++)
                    {
                        filteredEvents.put(individualEvents.get(i).getId(), individualEvents.get(i));
                    }
                }
            }
        }
    }

    private boolean personChecker(Person person)
    {
        String personID = person.getId();
        if(person.getGender().equals("m") && !settings.isShowMale())
        {
            return false;
        }
        if(person.getGender().equals("f") && !settings.isShowFemale())
        {
            return false;
        }
        if(DataCache.getInstance().getFatherSide().contains(personID) && !settings.isFatherSide())
        {
            return false;
        }

        if(DataCache.getInstance().getMotherSide().contains(personID) && !settings.isMotherSide())
        {
            return false;
        }

        return true;
    }



    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder>
    {

        private final List<Person> persons;
        private final List<Event> events;

        SearchAdapter(List<Person> persons, List<Event> events)
        {
            this.persons = persons;
            this.events = events;
        }

        @Override
        public int getItemViewType(int position)
        {
            return position < persons.size() ? PERSON_POSITION : EVENT_POSITION;
        }


        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view;

            if(viewType == PERSON_POSITION)
            {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            }
            else
            {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            return new SearchHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchHolder holder, int position)
        {
            if(position < persons.size())
            {
                holder.bind(persons.get(position));
            }
            else
            {
                holder.bind(events.get(position - persons.size()));
            }
        }


        @Override
        public int getItemCount()
        {
            return persons.size() + events.size();
        }
    }


    private class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        //person
        private ImageView genderIcon;
        private TextView personName;
        private TextView personRelationship;

        //Event
        private ImageView eventIcon;
        TextView infoText;
        TextView nameText;

        private final int viewType;
        private Person person;
        private Event event;
        public SearchHolder(View itemView, int viewType)
        {
            super(itemView);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == PERSON_POSITION)
            {
                genderIcon = itemView.findViewById(R.id.genderIcon);
                personName = itemView.findViewById(R.id.personName);
                personRelationship = itemView.findViewById(R.id.personRelationship);
                eventIcon = null;
                infoText = null;
                nameText = null;
            }
            if(viewType == EVENT_POSITION)
            {
                genderIcon = null;
                personName = null;
                personRelationship = null;
                eventIcon = itemView.findViewById(R.id.eventIcon);
                infoText = itemView.findViewById(R.id.eventInfo);
                nameText = itemView.findViewById(R.id.eventName);
            }
        }

        private void bind(Person person)
        {
            this.person = person;
            Drawable icon;

            if(person.getGender().equals("m"))
            {
                icon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).color(2847675).sizeDp(30);
            }
            else
            {
                icon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).color(12743611).sizeDp(30);
            }
            genderIcon.setImageDrawable(icon);

            String name = person.getFirstName() + " " + person.getLastName();
            personName.setText(name);
            personRelationship.setText("");
        }

        private void bind(Event event)
        {
            this.event = event;

            Person eventPerson = filteredPersons.get(event.getPersonID());
            Drawable icon;
            icon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).color(2847675).sizeDp(30);
            eventIcon.setImageDrawable(icon);

            String info = event.getEventType() + ": " + event.getCity() + ", "
                    + event.getCountry() + " (" + event.getYear() + ")";

            String name = eventPerson.getFirstName() + " " + eventPerson.getLastName();

            infoText.setText(info);
            nameText.setText(name);



        }

        @Override
        public void onClick(View view)
        {
            if(viewType == PERSON_POSITION)
            {
                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                Bundle b = new Bundle();

                b.putString("personID", person.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
            if(viewType == EVENT_POSITION)
            {
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                Bundle b = new Bundle();

                b.putString("eventID", event.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        }
    }
}