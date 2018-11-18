package pl.dawidkulpa.knj;

import android.view.View;

public class User {
    private String name;
    private String sname;
    private String email;
    private String phoneNo;
    private String aboutMe;
    private String password;
    private String loginToken;


    public String getName() {
        return name;
    }

    public String getSname() {
        return sname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getAboutMe() {
        return aboutMe;
    }
    public String getPassword(){return password;}
    public String getLoginToken(){return loginToken;}

    public void setName(String name) {
        this.name = name;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginToken(String loginToken){
        this.loginToken= loginToken;
    }
}
