package com.bsp.bspregistration;

public class RegCollection {

    private String token;
    private String lastname;
    private String firstname;
    private String midname;
    private String age;
    private String email;
    private String school;
    private String birthdate;
    private String gender;

    public RegCollection(String token, String lastname, String firstname, String midname, String age, String email, String school, String birthdate, String gender) {
        this.token = token;
        this.lastname = lastname;
        this.firstname = firstname;
        this.midname = midname;
        this.age = age;
        this.email = email;
        this.school = school;
        this.birthdate = birthdate;
        this.gender = gender;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
