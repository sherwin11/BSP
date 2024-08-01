package com.bsp.bspregistration;

public class AddFieldHolder  {
    private String surname;
    private String firstname;
    private String midname;
    private String registrationStat;
    private String age;
    private String gender;
    private String memcard;
    private String highestrank;
    private String numofyear;


    public AddFieldHolder(String surname, String firstname, String midname, String registrationStat,
                          String age, String gender, String memcard, String highestrank, String numofyear) {
        this.surname = surname;
        this.firstname = firstname;
        this.midname = midname;
        this.registrationStat = registrationStat;
        this.age = age;
        this.gender = gender;
        this.memcard = memcard;
        this.highestrank = highestrank;
        this.numofyear = numofyear;


    }



    public String getAasrregexpdate() {
        return aasrregexpdate;
    }

    public void setAasrregexpdate(String aasrregexpdate) {
        this.aasrregexpdate = aasrregexpdate;
    }

    private String aasrregexpdate;


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        this.midname = midname;
    }

    public String getRegistrationStat() {
        return registrationStat;
    }

    public void setRegistrationStat(String registrationStat) {
        this.registrationStat = registrationStat;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMemcard() {
        return memcard;
    }

    public void setMemcard(String memcard) {
        this.memcard = memcard;
    }

    public String getHighestrank() {
        return highestrank;
    }

    public void setHighestrank(String highestrank) {
        this.highestrank = highestrank;
    }

    public String getNumofyear() {
        return numofyear;
    }

    public void setNumofyear(String numofyear) {
        this.numofyear = numofyear;
    }


}
