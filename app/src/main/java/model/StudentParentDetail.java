package model;

public class StudentParentDetail {
    private int stuId;

    public String getParentName() {
        return parentName;
    }

    public StudentParentDetail() {
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    private String stuName;
    private String stuLName;
    private String image;
    private String rollNo;
    private String email;
    private String phone;
    private String DOB;
    private String bloodGroup;
    private String degree;
    private String department;
    private String about;
    private String gender;
    private String parentImage;
    private String parentEmail;
    private String parentPhone;
    private String parentName;

    public StudentParentDetail(int stuId, String stuName, String stuLName, String image, String rollNo, String email, String phone, String DOB, String bloodGroup, String degree, String department, String about, String gender,String parentName, String parentImage, String parentEmail, String parentPhone) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.stuLName = stuLName;
        this.image = image;
        this.rollNo = rollNo;
        this.email = email;
        this.phone = phone;
        this.DOB = DOB;
        this.bloodGroup = bloodGroup;
        this.degree = degree;
        this.department = department;
        this.about = about;
        this.gender = gender;
        this.parentName = parentName;
        this.parentImage = parentImage;
        this.parentEmail = parentEmail;
        this.parentPhone = parentPhone;
    }

    public String getStuLName() {
        return stuLName;
    }

    public void setStuLName(String stuLName) {
        this.stuLName = stuLName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getParentImage() {
        return parentImage;
    }

    public void setParentImage(String parentImage) {
        this.parentImage = parentImage;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public StudentParentDetail(int stuId, String stuName, String rollNo, String image, boolean attendance) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.rollNo = rollNo;
        this.image = image;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
