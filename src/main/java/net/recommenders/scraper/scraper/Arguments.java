package net.recommenders.scraper.scraper;

import com.github.jankroken.commandline.annotations.*;


public class Arguments  {
    private int year;
    private int id;
    private boolean resultlist = false;

    @Option
    @LongSwitch("year")
    @ShortSwitch("y")
    @SingleArgument
    public void setYear(String year) {
        this.year = Integer.parseInt(year);
    }

    @Option
    @LongSwitch("resultlist")
    @ShortSwitch("r")
    @Toggle(true)
    public void setResultlist(boolean resultlist) {
        this.resultlist = resultlist;
    }


    @Option
    @LongSwitch("id")
    @ShortSwitch("i")
    @SingleArgument
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public int getYear() {
        return year;
    }

    public int getId() {
        return id;
    }

    public boolean getResultlist() {
        return resultlist;
    }
}