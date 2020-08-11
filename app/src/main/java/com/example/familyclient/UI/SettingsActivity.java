package com.example.familyclient.UI;

import androidx.appcompat.app.AppCompatActivity;

import com.example.familyclient.Model.DataCache;
import com.example.familyclient.Model.Settings;
import com.example.familyclient.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity
{

    Switch lifeStoryLines;
    Switch familyTreeLines;
    Switch spouseLines;
    Switch fatherSide;
    Switch motherSide;
    Switch maleEvents;
    Switch femaleEvents;
    LinearLayout logout;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        lifeStoryLines = findViewById(R.id.lifeStoryLines);
        familyTreeLines = findViewById(R.id.familyTreeLines);
        spouseLines = findViewById(R.id.spouseLines);
        fatherSide = findViewById(R.id.fatherSide);
        motherSide = findViewById(R.id.motherSide);
        maleEvents = findViewById(R.id.maleEvents);
        femaleEvents = findViewById(R.id.femaleEvents);
        logout = findViewById(R.id.logoutClick);
        initializeSwitches();

        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                DataCache.getInstance().getSettings().setLifeStoryLines(
                        lifeStoryLines.isChecked()
                );
            }
        });

        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                DataCache.getInstance().getSettings().setFamilyTreeLines(
                        familyTreeLines.isChecked()
                );
            }
        });

        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                DataCache.getInstance().getSettings().setSpouseLines(
                        spouseLines.isChecked()
                );
            }
        });

        fatherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            DataCache.getInstance().getSettings().setFatherSide(
                    fatherSide.isChecked()
            );
        }
    });

        motherSide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            DataCache.getInstance().getSettings().setMotherSide(
                    motherSide.isChecked()
            );
        }
    });

        maleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            DataCache.getInstance().getSettings().setShowMale(
                    maleEvents.isChecked()
            );
        }
    });

        femaleEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b)
        {
            DataCache.getInstance().getSettings().setShowFemale(
                    femaleEvents.isChecked()
            );
        }
    });

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.putExtra("Restart", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }

    private void initializeSwitches()
    {
        ;
        //TODO Use settings from datacache to initialize switches
        lifeStoryLines.setChecked(DataCache.getInstance().getSettings().isLifeStoryLines());
        familyTreeLines.setChecked(DataCache.getInstance().getSettings().isFamilyTreeLines());
        spouseLines.setChecked(DataCache.getInstance().getSettings().isSpouseLines());
        fatherSide.setChecked(DataCache.getInstance().getSettings().isFatherSide());
        motherSide.setChecked(DataCache.getInstance().getSettings().isMotherSide());
        maleEvents.setChecked(DataCache.getInstance().getSettings().isShowMale());
        femaleEvents.setChecked(DataCache.getInstance().getSettings().isShowFemale());
    }
}