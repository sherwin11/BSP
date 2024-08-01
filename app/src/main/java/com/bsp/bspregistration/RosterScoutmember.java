package com.bsp.bspregistration;

public class RosterScoutmember {
    private String rostsurname;
    private String rostfirstname;
    private String rostmidname;
    private String rostreligion;
    private String rostage;
    private String rostregstat;
    private String rostmemcardNo;
    private String rosthighestrank;
    private String rosttennureinscounting;
    private String rostposition;
    private String gender;

    public RosterScoutmember(String rostsurname, String rostfirstname, String rostmidname, String rostreligion, String rostage, String rostregstat, String rostmemcardNo, String rosthighestrank, String rosttennureinscounting, String rostposition, String gender) {
        this.rostsurname = rostsurname;
        this.rostfirstname = rostfirstname;
        this.rostmidname = rostmidname;
        this.rostreligion = rostreligion;
        this.rostage = rostage;
        this.rostregstat = rostregstat;
        this.rostmemcardNo = rostmemcardNo;
        this.rosthighestrank = rosthighestrank;
        this.rosttennureinscounting = rosttennureinscounting;
        this.rostposition = rostposition;
        this.gender = gender;
    }

    public String getRostsurname() {
        return rostsurname;
    }

    public void setRostsurname(String rostsurname) {
        this.rostsurname = rostsurname;
    }

    public String getRostfirstname() {
        return rostfirstname;
    }

    public void setRostfirstname(String rostfirstname) {
        this.rostfirstname = rostfirstname;
    }

    public String getRostmidname() {
        return rostmidname;
    }

    public void setRostmidname(String rostmidname) {
        this.rostmidname = rostmidname;
    }

    public String getRostreligion() {
        return rostreligion;
    }

    public void setRostreligion(String rostreligion) {
        this.rostreligion = rostreligion;
    }

    public String getRostage() {
        return rostage;
    }

    public void setRostage(String rostage) {
        this.rostage = rostage;
    }

    public String getRostregstat() {
        return rostregstat;
    }

    public void setRostregstat(String rostregstat) {
        this.rostregstat = rostregstat;
    }

    public String getRostmemcardNo() {
        return rostmemcardNo;
    }

    public void setRostmemcardNo(String rostmemcardNo) {
        this.rostmemcardNo = rostmemcardNo;
    }

    public String getRosthighestrank() {
        return rosthighestrank;
    }

    public void setRosthighestrank(String rosthighestrank) {
        this.rosthighestrank = rosthighestrank;
    }

    public String getRosttennureinscounting() {
        return rosttennureinscounting;
    }

    public void setRosttennureinscounting(String rosttennureinscounting) {
        this.rosttennureinscounting = rosttennureinscounting;
    }

    public String getRostposition() {
        return rostposition;
    }

    public void setRostposition(String rostposition) {
        this.rostposition = rostposition;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
