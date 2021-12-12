package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.CourseAttendanceAdapter;
import adapter.CoursesForStudentListAdapter;
import adapter.TeachersListAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.Subject;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class CourseAttendanceFragment extends Fragment {
    private RecyclerView recyclerView;
    public ArrayList<Subject> subjectArrayList;
    private RequestQueue queue;
    private String teacherUrl = Constant.BASE_URL+"professorCoursesApi.php";
    private String id;
    private SharedPreferences mPreferences;
    DatabaseHandler db;
    String sharedProfFile ="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    SwipeRefreshLayout swipeRefreshLayout;

    public CourseAttendanceFragment() {
        // Required empty public constructor
    }

    public static CourseAttendanceFragment newInstance(String param1, String param2) {
        CourseAttendanceFragment fragment = new CourseAttendanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void initWidget() {

        mPreferences=getActivity().getSharedPreferences(sharedProfFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID","null");
        subjectArrayList = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.course_attendance_recycler_view);
        db = new DatabaseHandler(getContext());
        swipeRefreshLayout = getActivity().findViewById(R.id.refresh_course_view);
    }
    private void getDataFromSqLite() {
        subjectArrayList.clear();
        subjectArrayList= db.getCourseList();

        CourseAttendanceAdapter courseAttendanceAdapter = new CourseAttendanceAdapter(getContext(), subjectArrayList);
        GridLayoutManager layoutManager= new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(courseAttendanceAdapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_attendance, container, false);
    }

    private ArrayList<Subject> setAttendanceData() {

        String urlTest = teacherUrl + "?id="+ id;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject professor = response;
                            Log.d("test123", "onResponse:ej " + professor.toString());
                            subjectArrayList.clear();
                            JSONObject att = professor.getJSONObject(id);

//                           db.deleteCourseTable(db.getWritableDatabase());
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

                                Log.d("test123", "onResponse:ej " + subName+" "+prof_name+" "+subName+" "+prof_phone+" "+prof_photo+" "+prof_email+" "+prof_degree+" "+prof_dep);

                                Subject subject = new Subject(subName,courseId);
                                Log.d("test123", "onResponse:ej " + subject.getSubName());



                                db.addCourse(subject);
                                subjectArrayList.add(subject);
                            }

                            CourseAttendanceAdapter courseAttendanceAdapter = new CourseAttendanceAdapter(getContext(), subjectArrayList);
                            GridLayoutManager layoutManager= new GridLayoutManager(getContext(),2);

                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(courseAttendanceAdapter);
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

        return subjectArrayList;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setAttendanceData();
        setListeners();
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
}