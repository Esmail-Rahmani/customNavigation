package com.example.customnavigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import fragment.TeacherListFragment;
import utils.Constant;

public class TeacherViewActivity extends AppCompatActivity {
    CircleImageView imageView;
    TextView teacherName, courseName, department, degree;
    ImageButton phone, email;
    String dep, name, course, contact, emailString, image, degreee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        initWidget();
        Intent intent = getIntent();
        setData(intent);
        setListeners();

    }

    private void setData(Intent intent) {
        dep = intent.getStringExtra("dep");
        name = intent.getStringExtra("teacherName");
        course = intent.getStringExtra("subject");
        contact = intent.getStringExtra("phone");
        emailString = intent.getStringExtra("email");
        image = intent.getStringExtra("image");
        degreee = intent.getStringExtra("degree");
        Picasso.get()
                .load(Constant.IMAGE_URL + image)
                .placeholder(R.drawable.profile_error)
                .into(imageView);
        //set above data
        teacherName.setText("Prof: "+name);
        courseName.setText("Sub Name :"+course);
        department.setText("Department: "+dep);
        degree.setText("Degree: "+degreee);



    }

    private void setListeners() {
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+contact));
                startActivity(callIntent);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+emailString));
                intent.putExtra(Intent.EXTRA_EMAIL, emailString);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void initWidget() {

        imageView = findViewById(R.id.profileImage);
        teacherName = findViewById(R.id.teacher_name);
        courseName = findViewById(R.id.course_name);
        degree = findViewById(R.id.degree);
        department = findViewById(R.id.department_teacher_view);
        phone = findViewById(R.id.phone_teacher_view);
        email = findViewById(R.id.email_teacher_view);
    }
}