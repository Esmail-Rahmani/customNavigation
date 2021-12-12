package fragment;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.TeachersListAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.Teacher;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;

public class TeacherListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TeachersListAdapter adapter;
    public ArrayList<Teacher> teacherArrayList;
    private RequestQueue queue;
    private String teacherUrl = Constant.BASE_URL+"professorApi.php";
    private String id;
    DatabaseHandler db;
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;


    public TeacherListFragment() {
        // Required empty public constructor
    }

    public static TeacherListFragment newInstance(String param1, String param2) {
        TeacherListFragment fragment = new TeacherListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setProfessorListData();
    }

    private void getDataFromSqLite() {
        teacherArrayList.clear();
        teacherArrayList= db.getProfessorList();
        TeachersListAdapter teachersListAdapter = new TeachersListAdapter(getContext(),teacherArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(teachersListAdapter);
    }

    private void initWidget() {

        mPreferences=getActivity().getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        id = mPreferences.getString("SignedInUserID","null");
        teacherArrayList = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.recyclerView_teacher);
        db = new DatabaseHandler(getContext());

    }

    private ArrayList<Teacher> setProfessorListData() {

        String urlTest = teacherUrl + "?id="+ id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject professor = response;
                            Log.d("test123", "onResponse:ej " + professor.toString());

                            JSONObject att = professor.getJSONObject(id);


                            JSONArray jsonArray = att.getJSONArray("subjects");
//                            db.deleteProfTable(db.getWritableDatabase());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String subName = jsonArray.getJSONObject(i).getString("subjectName");
                                String prof_name = jsonArray.getJSONObject(i).getString("prof_name");
                                String prof_email = jsonArray.getJSONObject(i).getString("prof_email");
                                String prof_photo = jsonArray.getJSONObject(i).getString("prof_image");

                                String prof_phone = jsonArray.getJSONObject(i).getString("prof_phone");
                                String prof_dep = jsonArray.getJSONObject(i).getString("prof_department");
                                String prof_degree = jsonArray.getJSONObject(i).getString("prof_degree");

                                Log.d("test123", "onResponse:ej " + subName+" "+prof_name+" "+subName+" "+prof_phone+" "+prof_photo+" "+prof_email+" "+prof_degree+" "+prof_dep);

                                Teacher teacher = new Teacher(i+1,prof_name,subName,prof_phone,prof_photo,prof_email,prof_degree,prof_dep);
                                teacher.setDegree(prof_degree);
                                teacher.setDepartment(prof_dep);
                                Log.d("test123", "onResponse:ej " + teacher.getDepartment());
                                db.addProfessor(teacher);
                                teacherArrayList.add(teacher);
                            }

                            TeachersListAdapter teachersListAdapter = new TeachersListAdapter(getContext(),teacherArrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(teachersListAdapter);
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

        return teacherArrayList ;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_list, container, false);
    }
}