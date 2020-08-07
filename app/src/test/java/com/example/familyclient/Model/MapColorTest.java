package com.example.familyclient.Model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MapColorTest
{

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void getColorFloat()
    {
        MapColor color1 = new MapColor();
        MapColor color2 = new MapColor();
        ArrayList<Float> floatValues = new ArrayList<>();

        for(int i = 0; i < 5; i++)
        {
            floatValues.add(color1.getColorFloat());
            floatValues.add(color2.getColorFloat());
        }

        Float[] floatArray = floatValues.toArray(new Float[5]);

    }

    @Test
    public void getModifier()
    {
    }
}