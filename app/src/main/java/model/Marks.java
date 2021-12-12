package model;

import java.util.ArrayList;

public class Marks {
    String courseName;
    int semester;
    int totalMax;
    int totalObtain;

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Marks(String courseName, int semester, int totalMax, int totalObtain, int credit, ArrayList<MarkDetail> markDetails) {
        this.courseName = courseName;
        this.semester = semester;
        this.totalMax = totalMax;
        this.totalObtain = totalObtain;
        this.credit = credit;
        this.markDetails = markDetails;
    }

    int credit;
    ArrayList<MarkDetail> markDetails;

    public Marks() {
    }

    public Marks(String courseName, int semester, int totalMax, int totalObtain, ArrayList<MarkDetail> markDetails) {
        this.courseName = courseName;
        this.semester = semester;
        this.totalMax = totalMax;
        this.totalObtain = totalObtain;
        this.markDetails = markDetails;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTotalMax() {
        return totalMax;
    }

    public void setTotalMax(int totalMax) {
        this.totalMax = totalMax;
    }

    public int getTotalObtain() {
        return totalObtain;
    }

    public void setTotalObtain(int totalObtain) {
        this.totalObtain = totalObtain;
    }

    public ArrayList<MarkDetail> getMarkDetails() {
        return markDetails;
    }

    public void setMarkDetails(ArrayList<MarkDetail> markDetails) {
        this.markDetails = markDetails;
    }
}
