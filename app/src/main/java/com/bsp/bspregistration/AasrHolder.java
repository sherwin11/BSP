package com.bsp.bspregistration;

import java.util.List;

public class AasrHolder {
    private String token;
    private String sponsoring_int;
    private String date_applied;
    private String unitno;
    private String localcouncil;
    private String unit;
    private String nature;
    private String femalecnt;
    private String malecnt;
    private List<AddFieldHolder> addfield;

    private String aasramout1;
    private String totalfeeremitted;
    private String aasrtotalcountofscount;
    private String aarpaidunderor;
    private String aasrdatefees;
    private String aasrregexpdate;
    private String aasrnumber;
    public AasrHolder(String token, String sponsoring_int, String date_applied, String unitno, String localcouncil, String unit, String nature, String femalecnt, String malecnt, List<AddFieldHolder> addfield,
                      String aasramout1, String totalfeeremitted, String aasrtotalcountofscount, String aarpaidunderor,
                      String aasrdatefees, String aasrregexpdate,String aasrnumber) {
        this.token = token;
        this.sponsoring_int = sponsoring_int;
        this.date_applied = date_applied;
        this.unitno = unitno;
        this.localcouncil = localcouncil;
        this.unit = unit;
        this.nature = nature;
        this.femalecnt = femalecnt;
        this.malecnt = malecnt;
        this.addfield = addfield;
        this.aasramout1 = aasramout1;
        this.totalfeeremitted = totalfeeremitted;
        this.aasrtotalcountofscount = aasrtotalcountofscount;
        this.aarpaidunderor = aarpaidunderor;
        this.aasrdatefees = aasrdatefees;
        this.aasrregexpdate = aasrregexpdate;
        this.aasrnumber = aasrnumber;
    }
    public String getAasramout1() {
        return aasramout1;
    }

    public void setAasramout1(String aasramout1) {
        this.aasramout1 = aasramout1;
    }

    public String getAasramount2() {
        return totalfeeremitted;
    }

    public void setAasramount2(String aasramount2) {
        this.totalfeeremitted = aasramount2;
    }

    public String getAasrtotalcountofscount() {
        return aasrtotalcountofscount;
    }

    public void setAasrtotalcountofscount(String aasrtotalcountofscount) {
        this.aasrtotalcountofscount = aasrtotalcountofscount;
    }

    public String getAarpaidunderor() {
        return aarpaidunderor;
    }

    public void setAarpaidunderor(String aarpaidunderor) {
        this.aarpaidunderor = aarpaidunderor;
    }

    public String getAasrdatefees() {
        return aasrdatefees;
    }

    public void setAasrdatefees(String aasrdatefees) {
        this.aasrdatefees = aasrdatefees;
    }

    public String getAasrregexpdate() {
        return aasrregexpdate;
    }

    public void setAasrregexpdate(String aasrregexpdate) {
        this.aasrregexpdate = aasrregexpdate;
    }


    public String getSponsoring_int() {
        return sponsoring_int;
    }

    public void setSponsoring_int(String sponsoring_int) {
        this.sponsoring_int = sponsoring_int;
    }

    public String getDate_applied() {
        return date_applied;
    }

    public void setDate_applied(String date_applied) {
        this.date_applied = date_applied;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getLocalcouncil() {
        return localcouncil;
    }

    public void setLocalcouncil(String localcouncil) {
        this.localcouncil = localcouncil;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getFemalecnt() {
        return femalecnt;
    }

    public void setFemalecnt(String femalecnt) {
        this.femalecnt = femalecnt;
    }

    public String getMalecnt() {
        return malecnt;
    }

    public void setMalecnt(String malecnt) {
        this.malecnt = malecnt;
    }

    public List<AddFieldHolder> getAddfield() {
        return addfield;
    }

    public void setAddfield(List<AddFieldHolder> addfield) {
        this.addfield = addfield;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAasrnumber() {
        return aasrnumber;
    }

    public void setAasrnumber(String aasrnumber) {
        this.aasrnumber = aasrnumber;
    }
}
