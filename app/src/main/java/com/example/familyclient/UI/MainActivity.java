package com.example.familyclient.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.example.familyclient.Net.Listener;
import com.example.familyclient.R;


public class MainActivity extends AppCompatActivity implements Listener
{
    private LoginFragment loginFragment;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = this.getSupportFragmentManager();

        loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragmentContainer);
        if(loginFragment == null)
        {
            loginFragment = new LoginFragment(this);
            Bundle args = new Bundle();
            loginFragment.setArguments(args);

            fm.beginTransaction()
                    .add(R.id.fragmentContainer, loginFragment)
                    .commit();
        }

    }

    @Override
    public void firstStage(String[] redundant)
    {
    }

    @Override
    public void secondStage(String toastMessage)
    {
        FragmentManager fm = this.getSupportFragmentManager();

        mapFragment = new MapFragment();
        Bundle args = new Bundle();
        mapFragment.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit();

    }


}