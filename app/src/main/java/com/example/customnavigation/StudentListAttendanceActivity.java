package com.example.customnavigation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.CoursesForStudentListAdapter;
import adapter.StudentListAttendanceAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.Attendance;
import model.StudentParentDetail;
import utils.Constant;

public class StudentListAttendanceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    public ArrayList<StudentParentDetail> studentParentDetails;
    public ArrayList<Attendance> attendanceArrayList;
    DatabaseHandler db;
    public ArrayList<StudentParentDetail> arrayList;
    private String Url = Constant.BASE_URL + "studentAttendanceApi.php?id=";
    private String id;
    int courseId;
    private SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_attendance);


        //set color for action bar and status abr
        if (Build.VERSION.SDK_INT>=21){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Student List For Attendance");
            actionBar.setHomeButtonEnabled(true);
            ColorDrawable colorDrawable
                    = new ColorDrawable(this.getResources().getColor(R.color.colorToolbar));
            actionBar.setBackgroundDrawable(colorDrawable);
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        courseId = getIntent().getIntExtra("id", 1);

        initWidget();
        setListeners();
        arrayList = setAttendanceData();

    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAttendanceData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void initWidget() {

        mPreferences = getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID", "null");
        studentParentDetails = new ArrayList<>();
        attendanceArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView_student_list_attendance);
        db = new DatabaseHandler(getApplicationContext());
        swipeRefreshLayout = findViewById(R.id.refresh_student_list_attendance);
    }
    private void getDataFromSqLite() {
        studentParentDetails.clear();
        attendanceArrayList.clear();
        studentParentDetails= db.getStudentParentList(courseId);

        final Calendar cal = Calendar.getInstance();

        Date today = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(today);
        for (int i=0;i<studentParentDetails.size();i++)
            attendanceArrayList.add(new Attendance(studentParentDetails.get(i).getStuId(),Integer.parseInt(id),courseId,dateString,true));

        StudentListAttendanceAdapter studentListAttendanceAdapter = new StudentListAttendanceAdapter(StudentListAttendanceActivity.this, studentParentDetails, attendanceArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(studentListAttendanceAdapter);
    }

    private ArrayList<StudentParentDetail> setAttendanceData() {

        String urlTest = Url + courseId;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject professor = response;
//                            db.deleteStudentParentTable(db.getWritableDatabase());
//                            db.onUpgrade(db.getWritableDatabase(),1,1);
                                attendanceArrayList.clear();
                                studentParentDetails.clear();
                            JSONArray jsonArray = professor.getJSONArray("student");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject stu = jsonArray.getJSONObject(i);
                                String stuName = stu.getString("stu_name");
                                String stuRollNo = stu.getString("stu_roll_no");
                                String stuImage = stu.getString("stu_image");
                                String stuLName = stu.getString("stu_lname");
                                int stuId = Integer.parseInt(stu.getString("stu_id"));
                                String email = stu.getString("stu_email");
                                String phone = stu.getString("stu_phone");
                                String DOB = stu.getString("stu_dob");
                                String bloodGroup = stu.getString("stu_blood");
                                String degree = stu.getString("stu_degree");
                                String department = stu.getString("stu_dep");
                                String about  = stu.getString("stu_about");
                                String gender = stu.getString("stu_gender");
                                String parentName = stu.getString("parent_name");
                                String parentImage = stu.getString("parent_image");
                                String parentPhone = stu.getString("parent_phone");
                                String parentEmail = stu.getString("parent_email");

                                final Calendar cal = Calendar.getInstance();

                                Date today = cal.getTime();
                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String dateString = dateFormat.format(today);

                                Attendance a = new Attendance( stuId, Integer.parseInt(id), courseId, dateString, true);
                                StudentParentDetail parentDetail = new StudentParentDetail(stuId, stuName, stuLName, stuImage,
                                        stuRollNo, email, phone, DOB, bloodGroup, degree, department, about,
                                        gender,parentName, parentImage, parentEmail, parentPhone);

                                attendanceArrayList.add(a);
                                db.addStudentParent(parentDetail);
                                db.addStudentCourse(stuId,courseId);
                                studentParentDetails.add(parentDetail);
                            }

                            StudentListAttendanceAdapter studentListAttendanceAdapter = new StudentListAttendanceAdapter(StudentListAttendanceActivity.this, studentParentDetails, attendanceArrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(studentListAttendanceAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("test123", "onResponse:try " + e);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e6656 " + error);
                getDataFromSqLite();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return studentParentDetails;
    }
}