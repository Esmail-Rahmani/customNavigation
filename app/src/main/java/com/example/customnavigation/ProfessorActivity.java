package com.example.customnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.customnavigation.ui.login.StudentLoginActivity;
import com.google.android.material.navigation.NavigationView;

import db.DatabaseHandler;
import fragment.AttendanceFragment;
import fragment.CourseAttendanceFragment;
import fragment.CoursesForStudentListFragment;
import fragment.DashboardFragment;
import fragment.FeedbackFragment;
import fragment.NewFeedFragment;
import fragment.ProfessorProfileFragment;
import fragment.StudentProfileFragment;
import fragment.TeacherListFragment;
import services.NetworkStateChecker;


public class ProfessorActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    public static ProfessorActivity professorActivity;
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    String id, name, username;
    boolean flag;
    DatabaseHandler db;
    NetworkStateChecker networkStateChecker = new NetworkStateChecker();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_professor);
         professorActivity = this;
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // - to hide it,


        if (Build.VERSION.SDK_INT>=21){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorStatusBar));
        }
        initWidget();
        actionBarDrawerToggle = new ActionBarDrawerToggle(this ,
                drawerLayout ,
                toolbar,
                R.string.open ,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        int position = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
        }


        navigationView.getMenu().getItem(position).setChecked(true);
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new CourseAttendanceFragment();
            fragmentManager.beginTransaction().replace(R.id.lin1_prof, fragment).commit();
            toolbar.setTitle("Attendance");
            setListener();

//            setSupportActionBar(toolbar);
//            actionBar = getSupportActionBar();
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_humberger);
    }

    private void setListener() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (item.getItemId()){
                    case R.id.menu_subject_prof:
                        navigationView.setCheckedItem(R.id.menu_subject_prof);
                        Fragment fragment = new CourseAttendanceFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1_prof, fragment).commit();
                        toolbar.setTitle("Attendance");
                        break;

                    case R.id.menu_news_prof:
                        navigationView.setCheckedItem(R.id.menu_news_prof);
                        Fragment f = new NewFeedFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1_prof, f).commit();
                        toolbar.setTitle("News And Post");
                        break;
                    case R.id.menu_students_prof:
                        navigationView.setCheckedItem(R.id.menu_students_prof);
                        Fragment frag = new CoursesForStudentListFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1_prof, frag).commit();
                        toolbar.setTitle("Students");

                        break;
                    case R.id.menu_logout_prof:
                        logout();
                        break;
                    case R.id.menu_prof_profile:
                        navigationView.setCheckedItem(R.id.menu_prof_profile);
                        Fragment fragment1 = new ProfessorProfileFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1_prof, fragment1).commit();
                        toolbar.setTitle("Professor Profile");
                        break;
                    case R.id.menu_feedback_prof:
                        navigationView.setCheckedItem(R.id.menu_feedback_prof);
                        Fragment fFrag = new FeedbackFragment();
                        fragmentManager.beginTransaction().replace(R.id.lin1_prof, fFrag).commit();
                        toolbar.setTitle("Feedback");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return  true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkStateChecker, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkStateChecker);
    }

    void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfessorActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Are your sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        preferencesEditor.putString("issignedin","false");
                        preferencesEditor.apply();
                        Intent loginScreen = new Intent(ProfessorActivity.this, StudentLoginActivity.class);
                        startActivity(loginScreen);
                        db.onUpgrade(db.getWritableDatabase(),1,1);
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
        toolbar = findViewById(R.id.toolbar_prof);
        navigationView = findViewById(R.id.nav_menu_prof);
        drawerLayout = findViewById(R.id.drawer_prof);
        mPreferences=getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id=mPreferences.getString("SignedInUserID","null");
        name=mPreferences.getString("SignedInName","null");
        username = mPreferences.getString("SignedInusername","null");
        flag = mPreferences.getBoolean("net",true);
        db = new DatabaseHandler(this);
    }
}