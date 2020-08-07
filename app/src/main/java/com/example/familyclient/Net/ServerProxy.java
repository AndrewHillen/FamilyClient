package com.example.familyclient.Net;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import ReqRes.EventUserResult;
import ReqRes.LoginRequest;
import ReqRes.LoginResult;
import ReqRes.PersonUserResult;
import ReqRes.RegisterRequest;
import ReqRes.RegisterResult;

public class ServerProxy
{
    private static String hostName;
    private static int portNumber;
    private static ServerProxy instance;
    Gson gson = new Gson();

    public static ServerProxy getInstance()
    {
        if(instance == null)
        {
            instance = new ServerProxy();
        }
        return instance;
    }

    private ServerProxy()
    {

    }
    public String getHostName()
    {
        return hostName;
    }

    public void setHostName(String hostName)
    {
        ServerProxy.hostName = hostName;
    }

    public int getPortNumber()
    {
        return portNumber;
    }

    public void setPortNumber(int portNumber)
    {
        ServerProxy.portNumber = portNumber;
    }


    //https://drive.google.com/drive/folders/1UTxyfAp2rXm7AbiffPJ3rOS6UaPUGj8B
    //Client portion for formula

    //Register



    public RegisterResult registerUser(RegisterRequest request)
    {
        RegisterResult result = new RegisterResult();
        try
        {
            URL url = new URL("http://" + hostName + ":" + portNumber + "/user/register");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.connect();

            String requestString = DeSerialize(request);

            OutputStream requestBody = http.getOutputStream();
            requestWriter(requestString, requestBody);
            requestBody.close();


            InputStream responseBody = http.getInputStream();

            String responseString = readReponseBody(responseBody);

            result = (RegisterResult) Serialize(responseString, result);


        }
        catch (MalformedURLException ex)
        {
            result = null;
        }
        catch (ProtocolException ex)
        {
            result = null;
        }
        catch (IOException ex)
        {
            result = null;
        }
        return result;
    }

    //Login
    public LoginResult loginUser(LoginRequest request)
    {
        LoginResult result = new LoginResult();
        try
        {
            URL url = new URL("http://" + hostName + ":" + portNumber + "/user/login");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.connect();

            String requestString = DeSerialize(request);

            OutputStream requestBody = http.getOutputStream();
            requestWriter(requestString, requestBody);
            requestBody.close();


            InputStream responseBody = http.getInputStream();

            String responseString = readReponseBody(responseBody);

            result = (LoginResult) Serialize(responseString, result);


        }
        catch(IOException ex)
        {
            result = null;
        }
        return result;
    }

    //GetPeople
    public PersonUserResult getPersons(String token)
    {
        PersonUserResult result = new PersonUserResult();
        try
        {
            URL url = new URL("http://" + hostName + ":" + portNumber + "/person");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", token);

            http.connect();


            if(http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = http.getInputStream();

                String responseString = readReponseBody(responseBody);

                result = (PersonUserResult) Serialize(responseString, result);

            }

            else
            {
                result = null;
            }
        }
        catch(IOException ex)
        {
            result = null;
        }
        return result;
    }

    //GetEvents
    public EventUserResult getEvents(String token)
    {
        EventUserResult result = new EventUserResult();
        try
        {
            URL url = new URL("http://" + hostName + ":" + portNumber + "/event");

            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setRequestMethod("GET");
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", token);

            http.connect();


            if(http.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                InputStream responseBody = http.getInputStream();

                String responseString = readReponseBody(responseBody);

                result = (EventUserResult) Serialize(responseString, result);

            }

            else
            {
                result = null;
            }
        }
        catch(IOException ex)
        {
            result = null;
        }
        return result;
    }






    //Put in a serialize/deserializer && string readers and writers

    public Object Serialize(String jsonString, Object o)
    {

        o = gson.fromJson(jsonString, o.getClass());


        return o;
    }

    public String DeSerialize(Object o)
    {
        String jsonString = gson.toJson(o);

        return jsonString;
    }

    public String readReponseBody(InputStream in) throws IOException
    {
        StringBuilder returnString = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(in);
        char[] buffer = new char[1024];
        int len;
        while((len = reader.read(buffer)) > 0)
        {
            returnString.append(buffer, 0, len);
        }
        return returnString.toString();
    }

    public void requestWriter(String request, OutputStream out) throws IOException
    {
        OutputStreamWriter write = new OutputStreamWriter(out);
        write.write(request);
        write.flush();
    }
}
