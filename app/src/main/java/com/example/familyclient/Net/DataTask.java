package com.example.familyclient.Net;

import android.os.AsyncTask;

import com.example.familyclient.Model.DataCache;

import ReqRes.EventUserResult;
import ReqRes.PersonUserResult;


public class DataTask extends AsyncTask<String, Integer, String>
{
    private Listener listener;

    public DataTask(Listener l)
    {
        listener = l;
    }

    @Override
    protected String doInBackground(String... inputs)
    {


        PersonUserResult personResult = ServerProxy.getInstance().getPersons(inputs[0]);

        EventUserResult eventResult = ServerProxy.getInstance().getEvents(inputs[0]);

        if(personResult == null || eventResult == null)
        {
            return "Unable to retrieve data";
        }



        DataCache.getInstance().setUserID(inputs[1]);
        DataCache.getInstance().processData(personResult, eventResult);
        //Grab person by personID to return first and last name here

        //TODO: Maybe change this, for now return unique token

        String returnString = DataCache.getInstance().getPersons().get(inputs[1]).getFirstName() + " " + DataCache.getInstance().getPersons().get(inputs[1]).getLastName();
        return returnString;
    }

    protected void onPostExecute(String result)
    {
        listener.secondStage(result);
    }


}
