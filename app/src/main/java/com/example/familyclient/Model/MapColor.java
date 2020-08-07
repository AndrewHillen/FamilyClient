package com.example.familyclient.Model;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapColor
{
    private float colorFloat;
    private static int modifier = 0;

    public MapColor()
    {

    }

    public float getColorFloat()
    {
        colorFloat = 0 + modifier;

        modifier = (modifier + 35) % 360;
        return colorFloat;
    }

    public int getModifier()
    {
        return modifier;
    }
}
