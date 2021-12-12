package model;

public class Student {
    private String studentName;
    private String[] subjects;
    private int studentId;
    private String contactNo;
    private String imageURI;
    private String email;
    private String gender;
    private int age;
    private String dateOfBirth;
    private String username;
    private String password;
    private String degree;

    public Student() {
    }

    public Student(String studentName, String[] subjects, String contactNo, String imageURI, String email, String gender, String dateOfBirth, String username, String password, String degree) {
        this.studentName = studentName;
        this.subjects = subjects;
        this.contactNo = contactNo;
        this.imageURI = imageURI;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
        this.degree = degree;
    }

    public Student(int studentId, String studentName, String[] subjects, String contactNo, String imageURI, String email, String gender, String dateOfBirth, String username, String password, String degree) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.subjects = subjects;
        this.contactNo = contactNo;
        this.imageURI = imageURI;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
        this.degree = degree;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String[] getSubjects() {
        return subjects;
    }

    public void setSubjects(String[] subjects) {
        this.subjects = subjects;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
