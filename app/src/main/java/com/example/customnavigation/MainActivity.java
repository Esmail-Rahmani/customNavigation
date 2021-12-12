package com.example.customnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.customnavigation.ui.login.StudentLoginActivity;
import com.google.android.material.navigation.NavigationView;

import db.DatabaseHandler;
import fragment.AttendanceFragment;
import fragment.CourseAttendanceFragment;
import fragment.DashboardFragment;
import fragment.FeedbackFragment;
import fragment.NewFeedFragment;
import fragment.ResultAndMarkFragment;
import fragment.StudentProfileFragment;
import fragment.TeacherListFragment;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    String id, name, username;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);// - to hide it,

        // change status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }
        initWidget();

        int position = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
        }
        navigationView.getMenu().getItem(position).setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new AttendanceFragment();
        fragmentManager.beginTransaction().replace(R.id.lin1, fragment).commit();
        toolbar.setTitle("Attendance");

        setListeners();

//        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (item.getItemId()) {

                    case R.id.menu_subject:
                        Fragment fragment = new AttendanceFragment();

                        fragmentManager.beginTransaction().replace(R.id.lin1, fragment).commit();
                        toolbar.setTitle("Attendance");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_news:
                        Fragment f = new NewFeedFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1, f).commit();
                        toolbar.setTitle("News And Post");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_teacher:

                        Fragment frag = new TeacherListFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1, frag).commit();
                        toolbar.setTitle("Professors");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_logout:
                        logout();
                        break;

                    case R.id.menu_student_profile:
                        Fragment fragment1 = new StudentProfileFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1, fragment1).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        toolbar.setTitle("Student Profile");
                        break;
                    case R.id.menu_marks:
                        Fragment fragment3 = new ResultAndMarkFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1, fragment3).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        toolbar.setTitle("Marks And Result");
                        break;
                    case R.id.menu_feedback:
                        Fragment fFrag = new FeedbackFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1, fFrag).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        toolbar.setTitle("Feedback");
                        break;
                }

                return true;

            }
        });
    }

    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are your sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        preferencesEditor.putString("issignedin", "false");
                        preferencesEditor.apply();
                        Intent loginscreen = new Intent(MainActivity.this, StudentLoginActivity.class);
                        startActivity(loginscreen);
                        db.onUpgrade(db.getWritableDatabase(), 1, 1);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initWidget() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer);
        mPreferences = getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        id = mPreferences.getString("SignedInUserID", "null");
        name = mPreferences.getString("SignedInName", "null");
        username = mPreferences.getString("SignedInUserID", "null");
        db = new DatabaseHandler(this);


    }
}