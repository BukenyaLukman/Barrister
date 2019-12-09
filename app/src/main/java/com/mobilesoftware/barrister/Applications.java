package com.mobilesoftware.barrister;

public class Applications {
    private String AreaOfLaw,AreaOfPractice,TitleOfLegal,LegalNeed,NameOfClient;

    public Applications(String areaOfLaw, String areaOfPractice, String titleOfLegal, String legalNeed, String nameOfClient) {
        AreaOfLaw = areaOfLaw;
        AreaOfPractice = areaOfPractice;
        TitleOfLegal = titleOfLegal;
        LegalNeed = legalNeed;
        NameOfClient = nameOfClient;
    }

    public Applications() {
    }


    public String getAreaOfLaw() {
        return AreaOfLaw;
    }

    public void setAreaOfLaw(String areaOfLaw) {
        AreaOfLaw = areaOfLaw;
    }

    public String getAreaOfPractice() {
        return AreaOfPractice;
    }

    public void setAreaOfPractice(String areaOfPractice) {
        AreaOfPractice = areaOfPractice;
    }

    public String getTitleOfLegal() {
        return TitleOfLegal;
    }

    public void setTitleOfLegal(String titleOfLegal) {
        TitleOfLegal = titleOfLegal;
    }

    public String getLegalNeed() {
        return LegalNeed;
    }

    public void setLegalNeed(String legalNeed) {
        LegalNeed = legalNeed;
    }

    public String getNameOfClient() {
        return NameOfClient;
    }

    public void setNameOfClient(String nameOfClient) {
        NameOfClient = nameOfClient;
    }
}
