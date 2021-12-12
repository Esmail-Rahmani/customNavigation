package model;

public class Attendance {
    private  int id;
    private int stuId,profId,courseId;
    private String date;
    private  boolean attendance;

    public Attendance(int id, int stuId, int profId, int courseId, String date, boolean attendance) {
        this.id = id;
        this.stuId = stuId;
        this.profId = profId;
        this.courseId = courseId;
        this.date = date;
        this.attendance = attendance;
    }
    public Attendance( int stuId, int profId, int courseId, String date, boolean attendance) {
        this.stuId = stuId;
        this.profId = profId;
        this.courseId = courseId;
        this.date = date;
        this.attendance = attendance;
    }
    public Attendance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public int getProfId() {
        return profId;
    }

    public void setProfId(int profId) {
        this.profId = profId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }
}
