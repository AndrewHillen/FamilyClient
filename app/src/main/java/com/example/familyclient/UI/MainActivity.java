package com.example.familyclient.UI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import com.example.familyclient.Net.Listener;
import com.example.familyclient.R;


public class MainActivity extends AppCompatActivity implements Listener
{
    private LoginFragment loginFragment;
    private MapFragment mapFragment;
    private MenuItem settingsItem;
    boolean onLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        onLogin = true;
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

        Log.d("Oncreatecalled", "here");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        Log.d("onActivityResult", "here");
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                if(data.getBooleanExtra("Restart", true))
                {
                    onLogin = true;
                    FragmentManager fm = this.getSupportFragmentManager();



                    loginFragment = new LoginFragment(this);
                    Bundle args = new Bundle();
                    loginFragment.setArguments(args);

                    fm.beginTransaction()
                            .replace(R.id.fragmentContainer, loginFragment)
                            .commit();

                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        Log.d("Oncreatemenucalled", "here");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        settingsItem = menu.findItem(R.id.mapFragmentSettings);

        Drawable settingsIcon = new IconDrawable(this, FontAwesomeIcons.fa_gear).sizeDp(40);
        settingsItem.setIcon(settingsIcon);
        settingsItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                settingsActivityOpener();
                return false;
            }
        });
        if(onLogin)
        {
            settingsItem.setVisible(false);
        }
        return true;
    }

    private void settingsActivityOpener()
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void firstStage(String[] redundant)
    {
        onLogin = true;
        FragmentManager fm = this.getSupportFragmentManager();



        loginFragment = new LoginFragment(this);
        Bundle args = new Bundle();
        loginFragment.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.fragmentContainer, loginFragment)
                .commit();
    }

    @Override
    public void secondStage(String toastMessage)
    {
        onLogin = false;
        FragmentManager fm = this.getSupportFragmentManager();

        mapFragment = new MapFragment();
        Bundle args = new Bundle();
        mapFragment.setArguments(args);

        fm.beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit();


        Log.d("Secondstage", "here");
    }


}