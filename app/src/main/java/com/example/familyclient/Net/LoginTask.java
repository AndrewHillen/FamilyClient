package com.example.familyclient.Net;

import android.os.AsyncTask;

import ReqRes.LoginRequest;
import ReqRes.LoginResult;

public class LoginTask extends AsyncTask<LoginRequest, Integer, String[]>
{
    private Listener listener;

    public LoginTask(Listener l)
    {
        listener = l;
    }

    protected String[] doInBackground(LoginRequest... requests)
    {
        String[] returnStrings = new String[2];

        LoginResult result = ServerProxy.getInstance().loginUser(requests[0]);

        if(result == null)
        {
            returnStrings[0] = "Login failed";
            return returnStrings;
        }
        if(!result.isSuccess())
        {
            returnStrings[0] = "Login failed";
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
