package com.example.familyclient.Model;

public class Settings
{
    private boolean lifeStoryLines;
    private boolean familyTreeLines;
    private boolean spouseLines;
    private boolean fatherSide;
    private boolean motherSide;
    private boolean showMale;
    private boolean showFemale;

    public Settings(boolean lifeStoryLines, boolean familyTreeLines, boolean spouseLines, boolean fatherSide, boolean motherSide, boolean showMale, boolean showFemale)
    {
        this.lifeStoryLines = lifeStoryLines;
        this.familyTreeLines = familyTreeLines;
        this.spouseLines = spouseLines;
        this.fatherSide = fatherSide;
        this.motherSide = motherSide;
        this.showMale = showMale;
        this.showFemale = showFemale;
    }

    //Default is true
    public Settings()
    {
        this.lifeStoryLines = true;
        this.familyTreeLines = true;
        this.spouseLines = true;
        this.fatherSide = true;
        this.motherSide = true;
        this.showMale = true;
        this.showFemale = true;
    }

    public boolean isLifeStoryLines()
    {
        return lifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines)
    {
        this.lifeStoryLines = lifeStoryLines;
    }

    public boolean isFamilyTreeLines()
    {
        return familyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines)
    {
        this.familyTreeLines = familyTreeLines;
    }

    public boolean isSpouseLines()
    {
        return spouseLines;
    }

    public void setSpouseLines(boolean spouseLines)
    {
        this.spouseLines = spouseLines;
    }

    public boolean isFatherSide()
    {
        return fatherSide;
    }

    public void setFatherSide(boolean fatherSide)
    {
        this.fatherSide = fatherSide;
    }

    public boolean isMotherSide()
    {
        return motherSide;
    }

    public void setMotherSide(boolean motherSide)
    {
        this.motherSide = motherSide;
    }

    public boolean isShowMale()
    {
        return showMale;
    }

    public void setShowMale(boolean showMale)
    {
        this.showMale = showMale;
    }

    public boolean isShowFemale()
    {
        return showFemale;
    }

    public void setShowFemale(boolean showFemale)
    {
        this.showFemale = showFemale;
    }
}
