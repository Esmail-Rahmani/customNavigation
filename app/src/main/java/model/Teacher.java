package model;

public class Teacher {

    private String teacherName;
    private String teacherLName;
    private String subjects;
    private int teacherId;
    private String phone;
    private String imageURI;
    private String email;
    private String gender;
    private String bloodGroup;
    private String serNo;
    private String about;
    private int age;
    private String dateOfBirth;
    private int salary;
    private String degree;
    private String department;


    public Teacher(int teacherId,String teacherName, String teacherLName,  String phone, String imageURI, String email, String gender, String bloodGroup, String serNo, String about, String dateOfBirth, String degree, String department) {
        this.teacherName = teacherName;
        this.teacherLName = teacherLName;
        this.teacherId = teacherId;
        this.phone = phone;
        this.imageURI = imageURI;
        this.email = email;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.serNo = serNo;
        this.about = about;
        this.dateOfBirth = dateOfBirth;
        this.degree = degree;
        this.department = department;
    }

    public Teacher(String teacherName, String subjects, String phone, String imageURI, String email, String degree, String department) {
        this.teacherName = teacherName;
        this.subjects = subjects;
        this.phone = phone;
        this.imageURI = imageURI;
        this.email = email;
        this.degree = degree;
        this.department = department;
    }

    public String getTeacherLName() {
        return teacherLName;
    }

    public void setTeacherLName(String teacherLName) {
        this.teacherLName = teacherLName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getSerNo() {
        return serNo;
    }

    public void setSerNo(String serNo) {
        this.serNo = serNo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public Teacher() {
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getContactNo() {
        return phone;
    }

    public void setContactNo(String contactNo) {
        this.phone = contactNo;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Teacher(String teacherName, String subjects, String phone, String imageURI, String email, String gender, String dateOfBirth, int salary) {
        this.teacherName = teacherName;
        this.subjects = subjects;
        this.phone = phone;
        this.imageURI = imageURI;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
    }
    public Teacher(int id, String teacherName, String subjects, String phone, String imageURI, String email, String gender, String dateOfBirth) {
       this.teacherId = id;
        this.teacherName = teacherName;
        this.subjects = subjects;
        this.phone = phone;
        this.imageURI = imageURI;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;

    }
}
