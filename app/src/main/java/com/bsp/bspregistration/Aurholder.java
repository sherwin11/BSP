package com.bsp.bspregistration;

import java.util.List;

public class Aurholder {
    private String token;
    private String aurnumber;
    private String aurinstitution;
    private String aurdistrict;
    private String aurcounsil;
    private String aurunit;
    private String aurunitno;
    private String aurnature;
    private String referencenumber;
    private String unitleaderid;
    private List<RosterScoutmember> rosterScoutmembers;
    private List<AdultLeaders> adultLeaders;
    private String overwrite;

    public Aurholder(String token, String aurnumber, String aurinstitution, String aurdistrict, String aurcounsil, String aurunit, String aurunitno, String aurnature, String referencenumber, String unitleaderid, List<RosterScoutmember> rosterScoutmembers, List<AdultLeaders> adultLeaders, String overwrite) {
        this.token = token;
        this.aurnumber = aurnumber;
        this.aurinstitution = aurinstitution;
        this.aurdistrict = aurdistrict;
        this.aurcounsil = aurcounsil;
        this.aurunit = aurunit;
        this.aurunitno = aurunitno;
        this.aurnature = aurnature;
        this.referencenumber = referencenumber;
        this.unitleaderid = unitleaderid;
        this.rosterScoutmembers = rosterScoutmembers;
        this.adultLeaders = adultLeaders;
        this.overwrite = overwrite;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAurnumber() {
        return aurnumber;
    }

    public void setAurnumber(String aurnumber) {
        this.aurnumber = aurnumber;
    }

    public String getAurinstitution() {
        return aurinstitution;
    }

    public void setAurinstitution(String aurinstitution) {
        this.aurinstitution = aurinstitution;
    }

    public String getAurdistrict() {
        return aurdistrict;
    }

    public void setAurdistrict(String aurdistrict) {
        this.aurdistrict = aurdistrict;
    }

    public String getAurcounsil() {
        return aurcounsil;
    }

    public void setAurcounsil(String aurcounsil) {
        this.aurcounsil = aurcounsil;
    }

    public String getAurunit() {
        return aurunit;
    }

    public void setAurunit(String aurunit) {
        this.aurunit = aurunit;
    }

    public String getAurunitno() {
        return aurunitno;
    }

    public void setAurunitno(String aurunitno) {
        this.aurunitno = aurunitno;
    }

    public String getAurnature() {
        return aurnature;
    }

    public void setAurnature(String aurnature) {
        this.aurnature = aurnature;
    }

    public String getReferencenumber() {
        return referencenumber;
    }

    public void setReferencenumber(String referencenumber) {
        this.referencenumber = referencenumber;
    }

    public String getUnitleaderid() {
        return unitleaderid;
    }

    public void setUnitleaderid(String unitleaderid) {
        this.unitleaderid = unitleaderid;
    }

    public List<RosterScoutmember> getRosterScoutmembers() {
        return rosterScoutmembers;
    }

    public void setRosterScoutmembers(List<RosterScoutmember> rosterScoutmembers) {
        this.rosterScoutmembers = rosterScoutmembers;
    }

    public List<AdultLeaders> getAdultLeaders() {
        return adultLeaders;
    }

    public void setAdultLeaders(List<AdultLeaders> adultLeaders) {
        this.adultLeaders = adultLeaders;
    }

    public String getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(String overwrite) {
        this.overwrite = overwrite;
    }
}
