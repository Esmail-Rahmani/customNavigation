package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.StudentParentDetailsAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.StudentParentDetail;
import model.Subject;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class CoursesForStudentListFragment extends Fragment {
    private RecyclerView recyclerView;
    public ArrayList<Subject> subjectArrayList;
    private String teacherUrl = Constant.BASE_URL + "professorCoursesApi.php";
    private String Url = Constant.BASE_URL + "studentAttendanceApi.php?id=";
    private String id;
    private SharedPreferences mPreferences;
    DatabaseHandler db;
    String sharedProfFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<StudentParentDetail> studentParentDetails;
    StudentParentDetailsAdapter studentListAttendanceAdapter;
    int flag = 0;
    Toolbar toolbar;

    public CoursesForStudentListFragment() {
        // Required empty public constructor
    }

    public static CoursesForStudentListFragment newInstance(String param1, String param2) {
        CoursesForStudentListFragment fragment = new CoursesForStudentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initWidget() {
        mPreferences = getActivity().getSharedPreferences(sharedProfFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID", "null");
        subjectArrayList = new ArrayList<>();
        studentParentDetails = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.recyclerView_student_list);
        db = new DatabaseHandler(getContext());
        swipeRefreshLayout = getActivity().findViewById(R.id.refresh_course_student_list);
        toolbar = getActivity().findViewById(R.id.toolbar_prof);
        toolbar.inflateMenu(R.menu.search_menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_courses_for_student_list, container, false);
    }

    private ArrayList<Subject> setAttendanceData() {
        String urlTest = teacherUrl + "?id=" + id;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());
                        subjectArrayList.clear();
                        try {
                            JSONObject professor = response;
                            Log.d("test123", "onResponse:ej " + professor.toString());
                            JSONObject att = professor.getJSONObject(id);

//                            db.deleteCourseTable(db.getWritableDatabase());
                            JSONArray jsonArray = att.getJSONArray("subjects");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String subName = jsonArray.getJSONObject(i).getString("subjectName");
                                int courseId = Integer.parseInt(jsonArray.getJSONObject(i).getString("course_id"));
                                String prof_name = jsonArray.getJSONObject(i).getString("prof_name");
                                String prof_email = jsonArray.getJSONObject(i).getString("prof_email");
                                String prof_photo = jsonArray.getJSONObject(i).getString("prof_image");
                                String prof_phone = jsonArray.getJSONObject(i).getString("prof_phone");
                                String prof_dep = jsonArray.getJSONObject(i).getString("prof_department");
                                String prof_degree = jsonArray.getJSONObject(i).getString("prof_degree");
                                Log.d("test123", "onResponse:ej " + subName + " " + prof_name + " " + subName + " " + prof_phone + " " + prof_photo + " " + prof_email + " " + prof_degree + " " + prof_dep);
                                Subject subject = new Subject(subName, courseId);
                                Log.d("test123", "onResponse:ej " + subject.getSubName());
                                db.addCourse(subject);
                                subjectArrayList.add(subject);
                            }
                            flag = 0;
                            addAllStudents(subjectArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("test123", "onResponse:try " + e);
                            getCourseDataFromSqLite();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e " + error);
                getCourseDataFromSqLite();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
        return subjectArrayList;
    }

    private void getCourseDataFromSqLite() {
        subjectArrayList.clear();
        subjectArrayList = db.getCourseList();
        flag = 1;
        addAllStudents(subjectArrayList);
    }

    private void addAllStudents(ArrayList<Subject> subjectArrayList) {
        if (flag==0){
            db.deleteStudentParentTable(db.getWritableDatabase());
            for (Subject s : subjectArrayList) {
                setStudentData(s.getSubId());
            }
        }
        Log.d("test12345", "getDataFromSqLite: " + subjectArrayList.size());


        int secs = 2; // Delay in seconds
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataFromSqLite();
            }
        }, 2000);
    }

    private void getDataFromSqLite() {

        if (db.getStudentParentList().size() != 0) {
            studentParentDetails.clear();
            studentParentDetails = db.getStudentParentList();
        }
        Log.d("test12345", "getDataFromSqLite: " + studentParentDetails.size());
        studentListAttendanceAdapter = new StudentParentDetailsAdapter(getContext(), studentParentDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(studentListAttendanceAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setVisible(true);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                studentListAttendanceAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setAttendanceData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.action_search) {
                    item.setVisible(true);
                    SearchView searchView = (SearchView) item.getActionView();
                    searchView.setIconifiedByDefault(true);
                    searchView.setFocusable(true);
                    searchView.setIconified(false);
                    searchView.requestFocusFromTouch();

                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            studentListAttendanceAdapter.getFilter().filter(newText);
                            return false;
                        }
                    });
                } else {
                    // do something
                }

                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar.getMenu().clear();
    }

    private ArrayList<StudentParentDetail> setStudentData(int courseId) {

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
                                String about = stu.getString("stu_about");
                                String gender = stu.getString("stu_gender");
                                String parentName = stu.getString("parent_name");
                                String parentImage = stu.getString("parent_image");
                                String parentPhone = stu.getString("parent_phone");
                                String parentEmail = stu.getString("parent_email");

                                StudentParentDetail parentDetail = new StudentParentDetail(stuId, stuName, stuLName, stuImage,
                                        stuRollNo, email, phone, DOB, bloodGroup, degree, department, about,
                                        gender, parentName, parentImage, parentEmail, parentPhone);

                                db.addStudentParent(parentDetail);
                                studentParentDetails.add(parentDetail);
                                db.addStudentCourse(stuId, courseId);
                            }
                            Log.d("test12345", "getDataFromSqLite: 9 " + db.getStudentParentList().size());

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setAttendanceData();
        setListeners();
    }

}