package model;

public class Subject {

    private String subName;
    private int subId;

    public Subject(String subName) {
        this.subName = subName;
    }

    public Subject() {
    }

    public Subject(String subName, int subId) {
        this.subName = subName;
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }
}
