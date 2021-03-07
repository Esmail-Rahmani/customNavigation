package com.example.customnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import fragment.TeacherListFragment;

public class TeacherViewActivity  extends AppCompatActivity {
    CircleImageView imageView;
    TextView teacherName,bookDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        imageView = findViewById(R.id.profileImage);
        teacherName = findViewById(R.id.teacher_name);
        bookDescription = findViewById(R.id.book_descriptione);

//
//        int position = getIntent().getIntExtra("position",1);
//        imageView.setImageResource(TeacherListFragment.list.get(position).getImageProfile());
//        teacherName.setText(MainActivity.list.get(position).getTeacherName());
//        bookDescription.setText(MainActivity.list.get(position).getBookName());



    }
}