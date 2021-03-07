package com.example.customnavigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ProfileViewActivity extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);


        Intent intent = getIntent();
        int id = intent.getIntExtra("URl",R.drawable.ic_curriculum);
        imageView = findViewById(R.id.image_profile_view);
        imageView.setImageResource(id);
    }
}