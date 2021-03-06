package com.example.familyclient.Net;
import android.os.AsyncTask;

import ReqRes.RegisterResult;
import ReqRes.RegisterRequest;

public class RegisterTask extends AsyncTask<RegisterRequest, Integer, String[]>
{

    private Listener listener;

    public RegisterTask(Listener l)
    {
        listener = l;
    }

    protected String[] doInBackground(RegisterRequest... requests)
    {

        String[] returnStrings = new String[2];

        RegisterResult result = ServerProxy.getInstance().registerUser(requests[0]);

        if(result == null)
        {
            returnStrings[0] = "Register failed";
            return returnStrings;
        }
        if(!result.isSuccess())
        {
            returnStrings[0] = "Register failed";
            return returnStrings;
        }

        returnStrings[0] =  result.getAuthToken();
        returnStrings[1] = result.getPersonID();

        return returnStrings;

    }

    protected void onPostExecute(String[] result)
    {
        listener.firstStage(result);
    }

}
