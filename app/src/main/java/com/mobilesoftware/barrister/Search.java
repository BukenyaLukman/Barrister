package com.mobilesoftware.barrister;

public class Search {

    private String FullName,AreaOfPractice,image,pid,Phone;

    public Search(String fullName, String areaOfPractice, String image, String pid, String phone) {
        FullName = fullName;
        AreaOfPractice = areaOfPractice;
        this.image = image;
        this.pid = pid;
        Phone = phone;
    }

    public Search() {
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getAreaOfPractice() {
        return AreaOfPractice;
    }

    public void setAreaOfPractice(String areaOfPractice) {
        AreaOfPractice = areaOfPractice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
