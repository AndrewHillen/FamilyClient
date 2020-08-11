package com.example.familyclient.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import Model.Event;
import Model.Person;
import ReqRes.EventResult;
import ReqRes.EventUserResult;
import ReqRes.PersonResult;
import ReqRes.PersonUserResult;

public class DataCache
{
    private static DataCache instance;
    private Settings settings;

    private HashMap<String, Person> persons;
    private HashMap<String, Event> events;
    private HashMap<String, List<Event>> personEvents;
    private Set<String> eventTypes;
    private String userID;
    private HashMap<String, Float> eventColors;
    private MapColor mapColorGenerator;
    //Add More holders here
    private HashMap<String, List<Person>> personChildren;
    private Set<String> fatherSide;
    private Set<String> motherSide;

    private DataCache()
    {
        persons = new HashMap<>();
        events = new HashMap<>();
        settings = new Settings();
        personEvents = new HashMap<>();
        eventTypes = new TreeSet<>();
        personChildren = new HashMap<>();
        fatherSide = new TreeSet<>();
        motherSide = new TreeSet<>();
        mapColorGenerator = new MapColor();
        eventColors = new HashMap<>();


    }

    public static DataCache getInstance()
    {
        if(instance == null)
        {
            instance = new DataCache();
        }
        return instance;
    }

    public void setUserID(String id)
    {
        userID = id;
    }
    public String getUserID()
    {
        return userID;
    }

    public HashMap<String, List<Event>> getPersonEvents()
    {
        return personEvents;
    }

    public Set<String> getEventTypes()
    {
        return eventTypes;
    }


    public HashMap<String, List<Person>> getPersonChildren()
    {
        return personChildren;
    }

    public Set<String> getFatherSide()
    {
        return fatherSide;
    }


    public Set<String> getMotherSide()
    {
        return motherSide;
    }

    public HashMap<String, Float> getEventColors()
    {
        return eventColors;
    }

    public Settings getSettings()
    {
        return settings;
    }


    public HashMap<String, Person> getPersons()
    {
        return persons;
    }

    public HashMap<String, Event> getEvents()
    {
        return events;
    }





    //Process all of the data
    public void processData(PersonUserResult persons, EventUserResult events)
    {
        processPersons(persons);
        processEvents(events);
    }

    private void processPersons(PersonUserResult data)
    {
        PersonResult[] resultArray = data.getData();

        for(int i = 0; i < resultArray.length; i++)
        {
            Person newPerson = new Person(resultArray[i].getPersonID(),
                    resultArray[i].getAssociatedUsername(),
                    resultArray[i].getFirstName(),
                    resultArray[i].getLastName(),
                    resultArray[i].getGender(),
                    resultArray[i].getFatherID(),
                    resultArray[i].getMotherID(),
                    resultArray[i].getSpouseID());

            persons.put(resultArray[i].getPersonID(), newPerson);
        }

        userProcessing();

        for(Map.Entry<String, Person> entry : persons.entrySet())
        {
            childrenListBuilder(entry.getKey());
        }


    }

    private void processEvents(EventUserResult data)
    {
        EventResult resultArray[] = data.getData();

        for(int i = 0; i < resultArray.length; i++)
        {
            Event newEvent = new Event(resultArray[i].getEventID(),
                    resultArray[i].getAssociatedUsername(),
                    resultArray[i].getPersonID(),
                    resultArray[i].getLatitude(),
                    resultArray[i].getLongitude(),
                    resultArray[i].getCountry(),
                    resultArray[i].getCity(),
                    resultArray[i].getEventType(),
                    resultArray[i].getYear());

            events.put(resultArray[i].getEventID(), newEvent);

        }

        colorGenerator();
        eventListBuilder();
    }

    private void addToSide(String childID, Set<String> side)
    {
        Person childPerson = persons.get(childID);
        if(childPerson.getFatherID() != null)
        {
            side.add(childPerson.getFatherID());
            addToSide(childPerson.getFatherID(), side);
        }
        if(childPerson.getMotherID() != null)
        {
            side.add(childPerson.getMotherID());
            addToSide(childPerson.getMotherID(), side);
        }

    }

    private void generateChildrenList(Person childPerson, String parentID)
    {
        if(personChildren.containsKey(parentID))
        {
            personChildren.get(parentID).add(childPerson);
        }
        else
        {
            List<Person> childList = new ArrayList();
            childList.add(childPerson);

            personChildren.put(parentID, childList);

        }
    }

    private void userProcessing()
    {
        Person userPerson = persons.get(userID);

        if(userPerson.getFatherID() != null)
        {
            fatherSide.add(userPerson.getFatherID());
            addToSide(userPerson.getFatherID(), fatherSide);
        }

        if(userPerson.getMotherID() != null)
        {
            motherSide.add(userPerson.getMotherID());
            addToSide(userPerson.getMotherID(), motherSide);
        }

    }

    private void childrenListBuilder(String childID)
    {
        Person childPerson = persons.get(childID);

        if(childPerson.getFatherID() != null)
        {
            generateChildrenList(childPerson, childPerson.getFatherID());
        }

        if(childPerson.getMotherID() != null)
        {
            generateChildrenList(childPerson, childPerson.getMotherID());
        }
    }

    private void colorGenerator()
    {
        for(Map.Entry<String, Event> entry : events.entrySet())
        {
            String eventType = entry.getValue().getEventType().toLowerCase();
            if(!eventColors.containsKey(eventType))
            {
                eventColors.put(eventType, mapColorGenerator.getColorFloat());
            }
        }
    }

    private void eventListBuilder()
    {
        for(Map.Entry<String, Person> entry : persons.entrySet())
        {
            String personID = entry.getValue().getId();

            ArrayList<Event> eventList = new ArrayList<>();

            for(Map.Entry<String, Event> eventEntry : events.entrySet())
            {
                Event possibleEvent = eventEntry.getValue();

                if(personID.equals(possibleEvent.getPersonID()))
                {
                    eventList.add(possibleEvent);
                }
            }
            eventList = eventListSorter(eventList);

            personEvents.put(personID, eventList);
        }
    }

    private ArrayList<Event> eventListSorter(ArrayList<Event> unsorted)
    {
        Event[] eventArray = unsorted.toArray(new Event[unsorted.size()]);

        if(unsorted.size() == 1)
        {
            return unsorted;
        }
        for(int i = 0; i < eventArray.length - 1; i++)
        {

            for(int j = i + 1; j < eventArray.length; j++)
            {
                Event tempEvent =  eventArray[i];

                if(eventArray[i].getYear() > eventArray[j].getYear())
                {
                    eventArray[i] = eventArray[j];
                    eventArray[j] = tempEvent;
                }
            }
        }

        ArrayList<Event> sortedList = new ArrayList<>();

        for(int i = 0; i < eventArray.length; i++)
        {
            sortedList.add(eventArray[i]);
        }

        return sortedList;
    }



}
