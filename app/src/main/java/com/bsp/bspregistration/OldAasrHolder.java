package com.bsp.bspregistration;

import java.util.List;

public class OldAasrHolder {
    private String token;
    private String oldaasrnumber;
    private String oldnameofinstitution;
    private String oldaasrlocalcouncil;
    private String unit;
    private String unitno;
    private String nature;
    private String referencenumber;
    private List<OldAddFieldHolder> oldaddfield;
    private String unitleaderid;
    private  String disID;
    private String unitgroupID;

    public OldAasrHolder(String token, String oldaasrnumber, String oldnameofinstitution, String oldaasrlocalcouncil, String unit, String unitno, String nature, String referencenumber, List<OldAddFieldHolder> oldaddfield, String unitleaderid, String disID, String unitgroupID) {
        this.token = token;
        this.oldaasrnumber = oldaasrnumber;
        this.oldnameofinstitution = oldnameofinstitution;
        this.oldaasrlocalcouncil = oldaasrlocalcouncil;
        this.unit = unit;
        this.unitno = unitno;
        this.nature = nature;
        this.referencenumber = referencenumber;
        this.oldaddfield = oldaddfield;
        this.unitleaderid = unitleaderid;
        this.disID = disID;
        this.unitgroupID = unitgroupID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOldaasrnumber() {
        return oldaasrnumber;
    }

    public void setOldaasrnumber(String oldaasrnumber) {
        this.oldaasrnumber = oldaasrnumber;
    }

    public String getOldnameofinstitution() {
        return oldnameofinstitution;
    }

    public void setOldnameofinstitution(String oldnameofinstitution) {
        this.oldnameofinstitution = oldnameofinstitution;
    }

    public String getOldaasrlocalcouncil() {
        return oldaasrlocalcouncil;
    }

    public void setOldaasrlocalcouncil(String oldaasrlocalcouncil) {
        this.oldaasrlocalcouncil = oldaasrlocalcouncil;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getReferencenumber() {
        return referencenumber;
    }

    public void setReferencenumber(String referencenumber) {
        this.referencenumber = referencenumber;
    }

    public List<OldAddFieldHolder> getOldaddfield() {
        return oldaddfield;
    }

    public void setOldaddfield(List<OldAddFieldHolder> oldaddfield) {
        this.oldaddfield = oldaddfield;
    }

    public String getUnitleaderid() {
        return unitleaderid;
    }

    public void setUnitleaderid(String unitleaderid) {
        this.unitleaderid = unitleaderid;
    }

    public String getDisID() {
        return disID;
    }

    public void setDisID(String disID) {
        this.disID = disID;
    }

    public String getUnitgroupID() {
        return unitgroupID;
    }

    public void setUnitgroupID(String unitgroupID) {
        this.unitgroupID = unitgroupID;
    }
}
