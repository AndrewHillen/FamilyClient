package com.example.familyclient.Net;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ReqRes.LoginRequest;
import ReqRes.LoginResult;
import ReqRes.RegisterRequest;
import ReqRes.RegisterResult;

import static org.junit.Assert.*;

public class ServerProxyTest
{

    @Before
    public void setUp() throws Exception
    {
        ServerProxy.getInstance().setHostName("localhost");
        ServerProxy.getInstance().setPortNumber(8080);
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test

    public void registerUser()
    {
        assertEquals("localhost", ServerProxy.getInstance().getHostName());
        assertEquals(8080, ServerProxy.getInstance().getPortNumber());

        RegisterRequest request = new RegisterRequest();
        request.setUserName("username");
        request.setPassword("password");
        request.setFirstName("first");
        request.setLastName("last");
        request.setEmail("email");
        request.setGender("f");

        RegisterResult result = ServerProxy.getInstance().registerUser(request);

        assertNotNull(result);
        assertTrue(result.isSuccess());

    }

    @Test
    public void badRegister()
    {
        RegisterRequest request = new RegisterRequest();
        request.setUserName("username");
        request.setPassword("password");
        request.setFirstName("first");
        request.setLastName("last");
        request.setEmail("email");
        request.setGender("f");

        RegisterResult result = ServerProxy.getInstance().registerUser(request);

        assertFalse(result.isSuccess());
    }

    @Test
    public void loginUser()
    {
        LoginRequest request = new LoginRequest();
        request.setUserName("username");
        request.setPassword("password");

        LoginResult result = ServerProxy.getInstance().loginUser(request);

        assertTrue(result.isSuccess());

    }

    @Test
    public void badLogin()
    {
        LoginRequest request = new LoginRequest();
        request.setUserName("username");
        request.setPassword("password2");

        LoginResult result = ServerProxy.getInstance().loginUser(request);

        assertFalse(result.isSuccess());
    }

    @Test
    public void getPersons()
    {
    }

    @Test
    public void getEvents()
    {
    }
}