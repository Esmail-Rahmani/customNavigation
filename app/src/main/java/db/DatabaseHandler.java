package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.customnavigation.R;

import java.util.ArrayList;

import model.Attendance;
import model.StudentParentDetail;
import model.Subject;
import model.SubjectAttendance;
import model.Teacher;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "HeratUniApp";
    private static final String TABLE_PROF_LIST = "professorList";
    private static final String TABLE_PROFESSOR = "professor";
    private static final String TABLE_COURSE_LIST = "courseList";
    private static final String TABLE_STUDENT_PARENT_LIST = "studentParent";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SER_NO = "serNo";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHOTO = "photo";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_COURSE = "course";
    private static final String KEY_DEGREE = "degree";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_ROLL_NO = "rollNo";
    private static final String KEY_DOB = "dob";
    private static final String KEY_BLOOD_GROUP = "bloodGroup";
    private static final String KEY_ABOUT = "about";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PARENT_IMAGE = "parentImage";
    private static final String KEY_PARENT_NAME = "parentName";
    private static final String KEY_PARENT_EMAIL = "parentEmil";
    private static final String KEY_PARENT_PHONE = "parentPhone";
    private static final String KEY_STU_ID = "stu_id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_ATTENDED = "attended";
    private static final String TABLE_ATTENDANCE = "attendance";
    private static final String TABLE_ATTENDANCE_LIST = "attendanceList";
    private static final String COURSE_STUDENT_TABLE = "course_student";
    private static final String KEY_PROF_ID = "prof_id";
    private static final String KEY_STATUS = "status";


    String CREATE_ATTENDANCE_LIST_TABLE = " CREATE TABLE " + TABLE_ATTENDANCE_LIST + "(" +
            KEY_ID + " INTEGER PRIMARY KEY ," + KEY_COURSE_ID + " INTEGER,"
            + KEY_STU_ID + " INTEGER," + KEY_PROF_ID + " INTEGER,"
            + KEY_ATTENDED + " TINYINT," + KEY_STATUS + " TINYINT" + ")";

    String CREATE_PROFESSOR_TABLE = " CREATE TABLE " + TABLE_PROFESSOR + "(" +
            KEY_ID + " INTEGER PRIMARY KEY ," + KEY_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT," + KEY_PHOTO + " BLOB,"
            + KEY_SER_NO + " TEXT," + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT," + KEY_DOB + " TEXT,"
            + KEY_BLOOD_GROUP + " TEXT," + KEY_DEGREE + " TEXT,"
            + KEY_DEPARTMENT + " TEXT," + KEY_ABOUT + " TEXT,"
            + KEY_GENDER + " TEXT" + ")";

    String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE + "(" +
            KEY_ID + " INTEGER PRIMARY KEY ," + KEY_NAME + " TEXT,"
            + KEY_TOTAL + " INTEGER," + KEY_ATTENDED + " INTEGER" + ")";

    String CREATE_STUDENT_PARENT_LIST_TABLE = "CREATE TABLE " + TABLE_STUDENT_PARENT_LIST + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT," + KEY_PHOTO + " BLOB,"
            + KEY_ROLL_NO + " TEXT," + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT," + KEY_DOB + " TEXT,"
            + KEY_BLOOD_GROUP + " TEXT," + KEY_DEGREE + " TEXT,"
            + KEY_DEPARTMENT + " TEXT," + KEY_ABOUT + " TEXT,"
            + KEY_GENDER + " TEXT," + KEY_PARENT_NAME + " TEXT," + KEY_PARENT_IMAGE + " BLOB,"
            + KEY_PARENT_EMAIL + " TEXT," + KEY_PARENT_PHONE + " TEXT" + ")";

    String CREATE_COURSE_LIST_TABLE = "CREATE TABLE " + TABLE_COURSE_LIST + "(" +
            KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

    String CREATE_PROF_LIST_TABLE = "CREATE TABLE " + TABLE_PROF_LIST + "(" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT," + KEY_PHOTO + " BLOB,"
            + KEY_PHONE + " TEXT," + KEY_DEPARTMENT + " TEXT,"
            + KEY_COURSE + " TEXT," + KEY_DEGREE + " TEXT" + ")";

    String CREATE_TABLE_STUDENT_COURSE = "CREATE TABLE " + COURSE_STUDENT_TABLE + "(" +
            KEY_STU_ID + " INTEGER," + KEY_COURSE_ID + " INTEGER," +
            "PRIMARY KEY (" + KEY_STU_ID + " ," + KEY_COURSE_ID + "))";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROF_LIST_TABLE);
        db.execSQL(CREATE_COURSE_LIST_TABLE);
        db.execSQL(CREATE_STUDENT_PARENT_LIST_TABLE);
        db.execSQL(CREATE_TABLE_STUDENT_COURSE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_PROFESSOR_TABLE);
        db.execSQL(CREATE_ATTENDANCE_LIST_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROF_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT_PARENT_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_STUDENT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_LIST);
        onCreate(db);
    }

    public boolean deleteProfTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_PROF_LIST);
        db.close();
        return true;
    }

    public boolean deleteStudentParentTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_STUDENT_PARENT_LIST);
        db.close();
        return true;
    }

    public boolean deleteCourseTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE_COURSE_LIST);
        db.close();
        return true;
    }

    public boolean addAttendanceItem(Attendance attendance, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STU_ID, attendance.getStuId());
        values.put(KEY_COURSE_ID, attendance.getCourseId());
        values.put(KEY_PROF_ID, attendance.getProfId());
        if (attendance.isAttendance())
            values.put(KEY_ATTENDED, 1);
        else
            values.put(KEY_ATTENDED, 0);

        values.put(KEY_STATUS, status);
        db.insert(TABLE_ATTENDANCE_LIST, null, values);
        db.close();
        return true;
    }

    public boolean updateAttendanceStatus(ArrayList<Attendance> id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < id.size(); i++) {

            contentValues.put(KEY_STATUS, status);
            db.update(TABLE_ATTENDANCE_LIST, contentValues, KEY_ID + "=" + id.get(i).getId(), null);
        }
        db.close();
        return true;
    }

    public ArrayList<Attendance> getUnSyncAttendanceListList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Attendance> attendanceArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE_LIST + " WHERE " + KEY_STATUS + " = 0;";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Attendance attendance = new Attendance();
            attendance.setStuId(cursor.getInt(cursor.getColumnIndex(KEY_STU_ID)));
            attendance.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            attendance.setProfId(cursor.getInt(cursor.getColumnIndex(KEY_PROF_ID)));
            attendance.setCourseId(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_ID)));
            boolean i = false;
            if (cursor.getInt(cursor.getColumnIndex(KEY_ATTENDED)) == 1) {
                i = true;
            }
            attendance.setAttendance(i);
            attendanceArrayList.add(attendance);
        }
        return attendanceArrayList;
    }

    public ArrayList<Attendance> getAttendanceListList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Attendance> attendanceArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE_LIST;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Attendance attendance = new Attendance();
            attendance.setStuId(cursor.getInt(cursor.getColumnIndex(KEY_STU_ID)));
            attendance.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            attendance.setProfId(cursor.getInt(cursor.getColumnIndex(KEY_PROF_ID)));
            attendance.setCourseId(cursor.getInt(cursor.getColumnIndex(KEY_COURSE_ID)));
            boolean i = false;
            if (cursor.getInt(cursor.getColumnIndex(KEY_ATTENDED)) == 1) {
                i = true;
            }
            attendance.setAttendance(i);
            attendanceArrayList.add(attendance);
        }
        return attendanceArrayList;
    }

    public void addProfessorProfile(Teacher professor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, professor.getTeacherId());
        values.put(KEY_NAME, professor.getTeacherName());
        values.put(KEY_DEPARTMENT, professor.getDepartment());
        values.put(KEY_DEGREE, professor.getDegree());
        values.put(KEY_PHONE, professor.getPhone());
        values.put(KEY_SER_NO, professor.getSerNo());
        values.put(KEY_BLOOD_GROUP, professor.getBloodGroup());
        values.put(KEY_GENDER, professor.getGender());
        values.put(KEY_LAST_NAME, professor.getTeacherLName());
        values.put(KEY_ABOUT, professor.getAbout());
        values.put(KEY_DOB, professor.getDateOfBirth());
        values.put(KEY_PHOTO, R.drawable.profile_error);
        values.put(KEY_EMAIL, professor.getEmail());
        db.insertWithOnConflict(TABLE_PROFESSOR, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Teacher getProfessorProfile(int stuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PROFESSOR + " WHERE " + KEY_ID + " LIKE " + stuId;
        Teacher detail = new Teacher();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            detail.setBloodGroup(cursor.getString(cursor.getColumnIndex(KEY_BLOOD_GROUP)));
            detail.setTeacherId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detail.setDateOfBirth(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            detail.setAbout(cursor.getString(cursor.getColumnIndex(KEY_ABOUT)));
            detail.setTeacherName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            detail.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            detail.setImageURI(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
            detail.setDegree(cursor.getString(cursor.getColumnIndex(KEY_DEGREE)));
            detail.setDepartment(cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)));
            detail.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            detail.setTeacherLName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            detail.setSerNo(cursor.getString(cursor.getColumnIndex(KEY_SER_NO)));
            detail.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));

        }
        return detail;
    }

    public void addStudentParent(StudentParentDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, detail.getStuId());
        values.put(KEY_NAME, detail.getStuName());
        values.put(KEY_PARENT_IMAGE, detail.getParentImage());
        values.put(KEY_PARENT_PHONE, detail.getParentPhone());
        values.put(KEY_PARENT_NAME, detail.getParentName());
        values.put(KEY_PARENT_EMAIL, detail.getParentEmail());
        values.put(KEY_DEPARTMENT, detail.getDepartment());
        values.put(KEY_DEGREE, detail.getDegree());
        values.put(KEY_PHONE, detail.getPhone());
        values.put(KEY_ROLL_NO, detail.getRollNo());
        values.put(KEY_BLOOD_GROUP, detail.getBloodGroup());
        values.put(KEY_GENDER, detail.getGender());
        values.put(KEY_LAST_NAME, detail.getStuLName());
        values.put(KEY_ABOUT, detail.getAbout());
        values.put(KEY_DOB, detail.getDOB());
        values.put(KEY_PHOTO, R.drawable.profile_error);
        values.put(KEY_EMAIL, detail.getEmail());
        db.insertWithOnConflict(TABLE_STUDENT_PARENT_LIST, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void addProfessor(Teacher teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, teacher.getTeacherId());
        values.put(KEY_NAME, teacher.getTeacherName());
        values.put(KEY_COURSE, teacher.getSubjects());
        values.put(KEY_DEPARTMENT, teacher.getDepartment());
        values.put(KEY_DEGREE, teacher.getDegree());
        values.put(KEY_PHONE, teacher.getContactNo());
        values.put(KEY_PHOTO, R.drawable.profile_error);
        values.put(KEY_EMAIL, teacher.getEmail());
        db.insertWithOnConflict(TABLE_PROF_LIST, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public void addAttendance(SubjectAttendance attendance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, attendance.getSubId());
        values.put(KEY_NAME, attendance.getsName());
        values.put(KEY_TOTAL, attendance.getTotalClass());
        values.put(KEY_ATTENDED, attendance.getAttendedClass());
        db.insertWithOnConflict(TABLE_ATTENDANCE, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<SubjectAttendance> getAttendanceList(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<SubjectAttendance> subjectAttendances = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_ATTENDANCE;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            SubjectAttendance attendance = new SubjectAttendance();
            attendance.setSubId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            attendance.setsName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            attendance.setTotalClass(cursor.getInt(cursor.getColumnIndex(KEY_TOTAL)));
            attendance.setAttendedClass(cursor.getInt(cursor.getColumnIndex(KEY_ATTENDED)));
            subjectAttendances.add(attendance);
        }
        return subjectAttendances;
    }

    public void addStudentCourse(int stu_id, int course_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_STU_ID, stu_id);
        values.put(KEY_COURSE_ID, course_id);
        db.insertWithOnConflict(COURSE_STUDENT_TABLE, KEY_COURSE_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addCourse(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, subject.getSubId());
        values.put(KEY_NAME, subject.getSubName());
        db.insertWithOnConflict(TABLE_COURSE_LIST, KEY_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public String studentsInCourse(int courseId) {

        SQLiteDatabase db = this.getWritableDatabase();
        String list = "( ";
        String query = "SELECT * FROM " + COURSE_STUDENT_TABLE + " WHERE " + KEY_COURSE_ID + " = " + courseId;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(KEY_STU_ID));
            list += id + ",";
        }
        list = list.substring(0, list.length() - 1) + ")";
        return list;
    }

    public ArrayList<Subject> getCourseList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Subject> subjectArrayList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_COURSE_LIST;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Subject subject = new Subject();
            subject.setSubName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            subject.setSubId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            subjectArrayList.add(subject);
        }
        return subjectArrayList;
    }

    public ArrayList<Teacher> getProfessorList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Teacher> teacherList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PROF_LIST;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Teacher teacher = new Teacher();
            teacher.setTeacherName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            teacher.setContactNo(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            teacher.setImageURI(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
            teacher.setDegree(cursor.getString(cursor.getColumnIndex(KEY_DEGREE)));
            teacher.setDepartment(cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)));
            teacher.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            teacher.setSubjects(cursor.getString(cursor.getColumnIndex(KEY_COURSE)));
            teacherList.add(teacher);
        }
        return teacherList;
    }

    public ArrayList<StudentParentDetail> getStudentParentList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<StudentParentDetail> details = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_STUDENT_PARENT_LIST;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            StudentParentDetail detail = new StudentParentDetail();
            detail.setParentImage(cursor.getString(cursor.getColumnIndex(KEY_PARENT_IMAGE)));
            detail.setParentEmail(cursor.getString(cursor.getColumnIndex(KEY_PARENT_EMAIL)));
            detail.setParentName(cursor.getString(cursor.getColumnIndex(KEY_PARENT_NAME)));
            detail.setParentPhone(cursor.getString(cursor.getColumnIndex(KEY_PARENT_PHONE)));
            detail.setBloodGroup(cursor.getString(cursor.getColumnIndex(KEY_BLOOD_GROUP)));
            detail.setStuId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detail.setDOB(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            detail.setAbout(cursor.getString(cursor.getColumnIndex(KEY_ABOUT)));
            detail.setStuName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            detail.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            detail.setImage(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
            detail.setDegree(cursor.getString(cursor.getColumnIndex(KEY_DEGREE)));
            detail.setDepartment(cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)));
            detail.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            detail.setStuLName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            detail.setRollNo(cursor.getString(cursor.getColumnIndex(KEY_ROLL_NO)));
            detail.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));

            details.add(detail);
        }
        return details;
    }

    public ArrayList<StudentParentDetail> getStudentParentList(int courseId) {
        String str = studentsInCourse(courseId);
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<StudentParentDetail> details = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_STUDENT_PARENT_LIST + " WHERE " + KEY_ID + " IN" + str;

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            StudentParentDetail detail = new StudentParentDetail();
            detail.setParentImage(cursor.getString(cursor.getColumnIndex(KEY_PARENT_IMAGE)));
            detail.setParentEmail(cursor.getString(cursor.getColumnIndex(KEY_PARENT_EMAIL)));
            detail.setParentName(cursor.getString(cursor.getColumnIndex(KEY_PARENT_NAME)));
            detail.setParentPhone(cursor.getString(cursor.getColumnIndex(KEY_PARENT_PHONE)));
            detail.setBloodGroup(cursor.getString(cursor.getColumnIndex(KEY_BLOOD_GROUP)));
            detail.setStuId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detail.setDOB(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            detail.setAbout(cursor.getString(cursor.getColumnIndex(KEY_ABOUT)));
            detail.setStuName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            detail.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            detail.setImage(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
            detail.setDegree(cursor.getString(cursor.getColumnIndex(KEY_DEGREE)));
            detail.setDepartment(cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)));
            detail.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            detail.setStuLName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            detail.setRollNo(cursor.getString(cursor.getColumnIndex(KEY_ROLL_NO)));
            detail.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));

            details.add(detail);
        }
        return details;
    }

    public StudentParentDetail getStudentParent(int stuId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_STUDENT_PARENT_LIST + " WHERE " + KEY_ID + " LIKE " + stuId;
        StudentParentDetail detail = new StudentParentDetail();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            detail.setParentImage(cursor.getString(cursor.getColumnIndex(KEY_PARENT_IMAGE)));
            detail.setParentEmail(cursor.getString(cursor.getColumnIndex(KEY_PARENT_EMAIL)));
            detail.setParentName(cursor.getString(cursor.getColumnIndex(KEY_PARENT_NAME)));
            detail.setParentPhone(cursor.getString(cursor.getColumnIndex(KEY_PARENT_PHONE)));
            detail.setBloodGroup(cursor.getString(cursor.getColumnIndex(KEY_BLOOD_GROUP)));
            detail.setStuId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            detail.setDOB(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
            detail.setAbout(cursor.getString(cursor.getColumnIndex(KEY_ABOUT)));
            detail.setStuName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            detail.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            detail.setImage(cursor.getString(cursor.getColumnIndex(KEY_PHOTO)));
            detail.setDegree(cursor.getString(cursor.getColumnIndex(KEY_DEGREE)));
            detail.setDepartment(cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)));
            detail.setEmail(cursor.getString(cursor.getColumnIndex(KEY_EMAIL)));
            detail.setStuLName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
            detail.setRollNo(cursor.getString(cursor.getColumnIndex(KEY_ROLL_NO)));
            detail.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));

        }
        return detail;
    }

    public void deleteAttendance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE_LIST, null, null);
        db.execSQL("delete from " + TABLE_ATTENDANCE_LIST);
        db.close();

    }
}
