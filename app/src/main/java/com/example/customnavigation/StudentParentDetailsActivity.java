package com.example.customnavigation;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import adapter.CoursesForStudentListAdapter;
import adapter.StudentParentDetailsAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.StudentParentDetail;
import model.Subject;
import utils.Constant;

public class StudentParentDetailsActivity extends AppCompatActivity {
    private ImageView pImage,stuImage;
    private TextView stuName,stuRollNo,stuEmail,stuPhone,stuDegree,department,stuAbout,stuGender,stuDOB,bloodGroup;
    private TextView parentName,parentEmail,parentPhone;
    private String url = Constant.BASE_URL+"studentParentApi.php?id=";
    String pPhone,sPhone,pEmail,sEmail;
    private DatabaseHandler db;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_parent_details);
         id =getIntent().getIntExtra("stu_id",0);
        Log.d("test123", "onClick: "+id);

        if (Build.VERSION.SDK_INT>=21){

            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }
        initWidget();
        setListeners();
        setDate(id);
    }
    private void setListeners() {
        parentPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+pPhone));
                startActivity(callIntent);
            }
        });
        parentEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+pEmail));
                intent.putExtra(Intent.EXTRA_EMAIL, pEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        stuPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+sPhone));
                startActivity(callIntent);
            }
        });
        stuEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+sEmail));
                intent.putExtra(Intent.EXTRA_EMAIL, sEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
    private void getDataFromSqLite() {
        StudentParentDetail student = db.getStudentParent(id);

        Picasso.get()
                .load(Constant.IMAGE_URL + student.getImage())
                .placeholder(R.drawable.profile_error)
                .into(stuImage);
        Picasso.get()
                .load(Constant.IMAGE_URL + student.getParentImage())
                .placeholder(R.drawable.profile_error)
                .into(pImage);
        pPhone = student.getParentPhone();
        sPhone =student.getPhone();
        pEmail =student.getParentEmail();
        sEmail=student.getEmail();
        stuName.setText(student.getStuName() +" "+student.getStuLName());
        stuRollNo.setText("Roll No: "+student.getRollNo());
        stuPhone.setText("Phone: "+student.getPhone());
        stuEmail.setText("Email: "+student.getEmail());
        stuAbout.setText("About: "+student.getAbout());
        stuDegree.setText("Degree: "+student.getDegree());
        stuGender.setText("Gender: "+student.getGender());
        stuDOB.setText("Date of birth: "+student.getDOB());
        bloodGroup.setText("Blood Group: "+student.getBloodGroup());
        department.setText("Department: "+student.getDepartment());
        parentEmail.setText("Email: "+student.getParentEmail());
        parentName.setText(student.getParentName());
        parentPhone.setText("Phone: "+student.getParentPhone());

    }
    private void setDate(int id) {
        String urlTest = url + id;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject student = response;

                            Picasso.get()
                                    .load(Constant.IMAGE_URL + student.getString("stu_image"))
                                    .placeholder(R.drawable.profile_error)
                                    .into(stuImage);
                            Picasso.get()
                                    .load(Constant.IMAGE_URL + student.getString("parent_image"))
                                    .placeholder(R.drawable.profile_error)
                                    .into(pImage);
                                    pPhone = student.getString("parent_phone");
                                    sPhone =student.getString("stu_phone");
                                    pEmail =student.getString("parent_email");
                                    sEmail=student.getString("stu_email");

                            stuName.setText(student.getString("stu_name") +" "+student.getString("stu_lname"));
                            stuRollNo.setText("Roll No: "+student.getString("stu_roll_no"));
                            stuPhone.setText("Phone: "+student.getString("stu_phone"));
                            stuEmail.setText("Email: "+student.getString("stu_email"));
                            stuAbout.setText("About: "+student.getString("stu_about"));
                            stuDegree.setText("Degree: "+student.getString("stu_degree"));
                            stuGender.setText("Gender: "+student.getString("stu_gender"));
                            stuDOB.setText("Date of birth: "+student.getString("stu_dob"));
                            bloodGroup.setText("Blood Group: "+student.getString("stu_blood"));
                            department.setText("Department: "+student.getString("stu_dep"));
                            parentEmail.setText("Email: "+student.getString("parent_email"));
                            parentName.setText(student.getString("parent_name"));
                            parentPhone.setText("Phone: "+student.getString("parent_phone"));


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


    }

    private void initWidget() {
        pImage = findViewById(R.id.parent_img_details);
        stuImage = findViewById(R.id.student_img_details);
        stuName = findViewById(R.id.stu_name_details);
        stuRollNo = findViewById(R.id.stu_rollno_details);
        stuEmail = findViewById(R.id.stu_email_details);
        stuPhone = findViewById(R.id.stu_phone_details);
        stuDegree = findViewById(R.id.stu_degree_details);
        department = findViewById(R.id.stu_dep_details);
        stuAbout = findViewById(R.id.stu_about_details);
        stuGender = findViewById(R.id.stu_gendar_details);
        stuDOB = findViewById(R.id.stu_dob_details);
        bloodGroup = findViewById(R.id.stu_blood_group_details);
        parentName = findViewById(R.id.parent_name_details);
        parentEmail = findViewById(R.id.parent_email_details);
        parentPhone = findViewById(R.id.parent_phone_details);
        db = new DatabaseHandler(getApplicationContext());

    }
}