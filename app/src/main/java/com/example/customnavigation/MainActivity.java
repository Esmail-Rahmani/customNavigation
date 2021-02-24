package com.example.customnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import fragment.AttendanceFragment;
import fragment.DashboardFragment;
import fragment.StudentProfileFragment;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);// - to hide it,
//        getSupportActionBar().setTitle("new title"); - if you want to change it
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawer);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this ,
                drawerLayout ,
                toolbar ,
                R.string.open ,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_subject:
                        Fragment fragment = new AttendanceFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.lin1, fragment).commit();
                        toolbar.setTitle("Attendance");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_dashboard:

                        Fragment fragment2 = new DashboardFragment();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.lin1, fragment2).commit();
                        toolbar.setTitle("Dashboard");
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_facebook:
                        Toast.makeText(getApplicationContext(),"Clicked done",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_student_profile:
                        Fragment fragment1 = new StudentProfileFragment();
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        fragmentManager1.beginTransaction().replace(R.id.lin1, fragment1).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        toolbar.setTitle("Student Profile");

                        break;
                }

                return  true;

            }
        });

    }
}