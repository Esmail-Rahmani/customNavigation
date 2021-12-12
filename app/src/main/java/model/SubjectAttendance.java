package model;

public class SubjectAttendance {

    private int subId;
    private String sName;
    private int totalClass;
    private int attendedClass;

    public SubjectAttendance(int subId, String subName, int total, int attended) {
        this.subId = subId;
        this.sName = subName;
        this.totalClass = total;
        this.attendedClass = attended;
    }

    public SubjectAttendance() {
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public SubjectAttendance(String sName, int totalClass, int attendedClass) {
        this.sName = sName;
        this.totalClass = totalClass;
        this.attendedClass = attendedClass;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getTotalClass() {
        return totalClass;
    }

    public void setTotalClass(int totalClass) {
        this.totalClass = totalClass;
    }

    public int getAttendedClass() {
        return attendedClass;
    }

    public void setAttendedClass(int attendedClass) {
        this.attendedClass = attendedClass;
    }
}
