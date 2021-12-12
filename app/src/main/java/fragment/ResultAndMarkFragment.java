package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;
import com.example.customnavigation.StudentListAttendanceActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.ResultAndMarksAdapter;
import adapter.StudentListAttendanceAdapter;
import controler.AppController;
import model.Attendance;
import model.MarkDetail;
import model.Marks;
import model.StudentParentDetail;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class ResultAndMarkFragment extends Fragment {

    private String url = Constant.BASE_URL+"exam_resultApi.php?id=";

    private SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private String id;
    ArrayList<Marks> marksArrayList;
    private RecyclerView recyclerView;

    public ResultAndMarkFragment() {
        // Required empty public constructor
    }


    public static ResultAndMarkFragment newInstance(String param1, String param2) {
        ResultAndMarkFragment fragment = new ResultAndMarkFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void getDataFromServer() {

        String urlTest = url + id;
        Log.d("thisTest", "onResponse: "+urlTest);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray courses = response.getJSONArray("courses");
                            Log.d("thisTest", "onResponse: "+courses.toString());
                            int sem=0;
                            // getting data from json
                            for (int i = 0; i < courses.length(); i++) {
                                JSONObject marks = courses.getJSONObject(i);
                                String courseName = marks.getString("course_name");
                                int semester = marks.getInt("semester");
                                int maxTotal = marks.getInt("max_total");
                                int obtainTotal = marks.getInt("obtain_total");
                                int courseCredit = marks.getInt("course_credit");


                                Log.d("thisTest", "onResponse: "+courseName +" "+ semester +" "+maxTotal+" " +" "+obtainTotal);


                                if (semester == sem){
                                    ArrayList<MarkDetail> markDetailArrayList = new ArrayList<>();

                                    JSONArray marksDetails = marks.getJSONArray("marks");
                                    for (int j = 0; j < marksDetails.length(); j++) {
                                        int markId = marksDetails.getJSONObject(j).getInt("mark_id");
                                        String markTitle = marksDetails.getJSONObject(j).getString("mark_title");
                                        int maxMark = marksDetails.getJSONObject(j).getInt("max_mark");
                                        int obtainMark = marksDetails.getJSONObject(j).getInt("obtain_mark");
                                        MarkDetail markDetail = new MarkDetail(markId,maxMark,obtainMark,markTitle);
                                        markDetailArrayList.add(markDetail);
                                        Log.d("thisTest", "onResponse: "+markId+" "+markTitle+" "+maxMark+" "+obtainMark);
                                    }

                                    Marks marks1 = new Marks(courseName,semester,maxTotal,obtainTotal,courseCredit,markDetailArrayList);
                                    marksArrayList.add(marks1);

                                }else {
                                    sem = semester;

                                    Marks marks1 = new Marks(null,semester,0,0,0,null);
                                    marksArrayList.add(marks1);

                                    ArrayList<MarkDetail> markDetailArrayList = new ArrayList<>();

                                    JSONArray marksDetails = marks.getJSONArray("marks");
                                    for (int j = 0; j < marksDetails.length(); j++) {
                                        int markId = marksDetails.getJSONObject(j).getInt("mark_id");
                                        String markTitle = marksDetails.getJSONObject(j).getString("mark_title");
                                        int maxMark = marksDetails.getJSONObject(j).getInt("max_mark");
                                        int obtainMark = marksDetails.getJSONObject(j).getInt("obtain_mark");
                                        MarkDetail markDetail = new MarkDetail(markId,maxMark,obtainMark,markTitle);
                                        markDetailArrayList.add(markDetail);
                                        Log.d("thisTest", "onResponse: "+markId+" "+markTitle+" "+maxMark+" "+obtainMark);
                                    }

                                    Marks marks2 = new Marks(courseName,semester,maxTotal,obtainTotal,courseCredit,markDetailArrayList);
                                    marksArrayList.add(marks2);
                                }


//                                db.addStudentParent(parentDetail);
//                                db.addStudentCourse(stuId,courseId);
                            }

                            ResultAndMarksAdapter resultAndMarksAdapter = new ResultAndMarksAdapter(getContext(),marksArrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(resultAndMarksAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("test123", "onResponse:try " + e);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e6656 " + error);
//                getDataFromSqLite();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        getDataFromServer();
        setListeners();
    }



    private void setListeners() {
    }

    private void initWidget() {
        recyclerView = getActivity().findViewById(R.id.mark_list_recycler_view);
        mPreferences = getContext().getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID", "null");
        marksArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_and_mark, container, false);
    }
}