package com.example.customnavigation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import adapter.StudentListAttendanceAdapter;
import adapter.StudentParentDetailsAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.Attendance;
import model.StudentParentDetail;
import utils.Constant;

public class CourseStudentListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public ArrayList<StudentParentDetail> studentParentDetails;

    public ArrayList<StudentParentDetail> arrayList;
    private String Url = Constant.BASE_URL+"studentAttendanceApi.php?id=";
    private String id;
    int courseId;
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private DatabaseHandler db;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_student_list);


        if (Build.VERSION.SDK_INT>=21){
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("Student Details List");
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
        courseId = getIntent().getIntExtra("id",1);

        initWidget();
        setListeners();
        arrayList =  setStudentData();

    }
    private void getDataFromSqLite() {
        studentParentDetails.clear();
        studentParentDetails= db.getStudentParentList(courseId);
        StudentParentDetailsAdapter studentListAttendanceAdapter = new StudentParentDetailsAdapter(getApplicationContext(), studentParentDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(studentListAttendanceAdapter);
    }
    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setStudentData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initWidget() {

        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID","null");
        studentParentDetails = new ArrayList<>();
        recyclerView =findViewById(R.id.recyclerView_student_list);
        db = new DatabaseHandler(getApplicationContext());
        swipeRefreshLayout = findViewById(R.id.refresh_student_list);
    }

    private ArrayList<StudentParentDetail> setStudentData() {

        String urlTest = Url + courseId;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject professor = response;
                            Log.d("test123", "onResponse:ej " + professor.toString());



                            JSONArray jsonArray = professor.getJSONArray("student");
                            Log.d("test123", "onResponse:ej " + jsonArray.toString());

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

                                 StudentParentDetail parentDetail = new StudentParentDetail(stuId, stuName, stuLName, stuImage,
                                        stuRollNo, email, phone, DOB, bloodGroup, degree, department, about,
                                        gender,parentName, parentImage, parentEmail, parentPhone);

                                db.addStudentParent(parentDetail);
                                db.addStudentCourse(stuId,courseId);
                                studentParentDetails.add(parentDetail);
                            }

                            StudentParentDetailsAdapter studentListAttendanceAdapter = new StudentParentDetailsAdapter(getApplicationContext(), studentParentDetails);
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
                Log.d("test123", "onResponse:e " + error);
                getDataFromSqLite();

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return studentParentDetails;
    }
}