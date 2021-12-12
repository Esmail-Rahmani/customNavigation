package fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import adapter.CourseAttendanceAdapter;
import adapter.SubjectAttendanceAdapter;
import controler.AppController;
import db.DatabaseHandler;
import model.SubjectAttendance;
import utils.Constant;
import static android.content.Context.MODE_PRIVATE;


public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnFocusChangeListener {
    String[] sem = {"sem I", "Sem II", "Sem III"};
    private TextView startDate;
    private TextView endDate;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private ArrayList<SubjectAttendance> attendanceArrayList;
    private ListView listViewSubject;
    private DonutProgress donutProgress;
    private RequestQueue queue;
    private String url = Constant.BASE_URL + "attendanceApi.php?id=";
    private String id;
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private DatabaseHandler db;

    public AttendanceFragment() {
    }

    public static AttendanceFragment newInstance(String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setAttendanceData();
        setListeners();
    }
    private void getDataFromSqLite() {
        attendanceArrayList.clear();
        attendanceArrayList= db.getAttendanceList(id);
        SubjectAttendance sub = new SubjectAttendance();
        for (SubjectAttendance a : attendanceArrayList) {
                if (a.getsName().equals("total")){
                    sub = a;
                    break;
                }
        }

        Log.d("test123", "onResponse:wow " + sub.getAttendedClass()+ " "+sub.getTotalClass());


        double sum = 100*sub.getAttendedClass()/sub.getTotalClass();
        donutProgress.setText(sum + "%");
        donutProgress.setMax(100);
        donutProgress.setStartingDegree(-90);
        donutProgress.setProgress((float) sum);
        if (sum>=75){
            donutProgress.setFinishedStrokeColor(Color.GREEN);
        }
        else{
            donutProgress.setFinishedStrokeColor(Color.RED);
        }

        SubjectAttendanceAdapter customAdapter = new SubjectAttendanceAdapter(attendanceArrayList, getContext());

        LinearLayout listViewReplacement = getView().findViewById(R.id.line11);
        for (int i = 1; i < customAdapter.getCount(); i++) {
            View view = customAdapter.getView(i, null, listViewReplacement);
            listViewReplacement.addView(view);
        }

    }

    private ArrayList<SubjectAttendance> setAttendanceData() {
        attendanceArrayList.clear();
        String start, end;
        start = startDate.getText().toString() + "";
        end = endDate.getText().toString() + "";

        String urlTest = url + id + "&from_date=" + start + "&to_date=" + end;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject attendance = response;
                            Log.d("test123", "onResponse:ej " + attendance.toString());

                            JSONObject att = attendance.getJSONObject(id);
                            double sum = att.getInt("precentege");
                            int total1= att.getInt("totalClasses");
                            int attended1 = att.getInt("attendedClasses");
                            donutProgress.setText(sum + "%");
                            donutProgress.setMax(100);
                            donutProgress.setStartingDegree(-90);
                            donutProgress.setProgress((float) sum);
                            if (sum>=75){
                                donutProgress.setFinishedStrokeColor(Color.rgb(0,165,0));
                            }
                            else{
                                donutProgress.setFinishedStrokeColor(Color.rgb(165,0,0));
                            }
                            SubjectAttendance subjectAttendance1 = new SubjectAttendance(0,"total", total1, attended1);

                            db.addAttendance(subjectAttendance1);
                            JSONArray jsonArray = att.getJSONArray("subjects");
                            for (int i = 0; i < jsonArray.length(); i++) {
                               String subName = jsonArray.getJSONObject(i).getString("subjectName");
                                int subId = jsonArray.getJSONObject(i).getInt("course_id");

                                Log.d("test123", "onResponse:j " + subName);

                                int total = jsonArray.getJSONObject(i).getInt("totalClasses");
                                int attended = jsonArray.getJSONObject(i).getInt("attendedClasses");
                                Log.d("test123", "onResponse:j 1 " +total);

                                SubjectAttendance subjectAttendance = new SubjectAttendance(subId,subName, total, attended);

                               db.addAttendance(subjectAttendance);
                                Log.d("test123", "onResponse:j 2 " + subjectAttendance.getsName());

                                attendanceArrayList.add(subjectAttendance);
                                Log.d("test123", "onResponse:ej " + attendanceArrayList.get(i).getTotalClass());

                            }

                            SubjectAttendanceAdapter customAdapter = new SubjectAttendanceAdapter(attendanceArrayList, getContext());

                            LinearLayout listViewReplacement = getActivity().findViewById(R.id.line11);
                            for (int i = 0; i < customAdapter.getCount(); i++) {
                                View view = customAdapter.getView(i, null, listViewReplacement);
                                listViewReplacement.addView(view);
                            }

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

        return attendanceArrayList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private void setListeners() {
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);

    }

    private void initWidget() {
        startDate = getView().findViewById(R.id.start_date_attendance);
        endDate = getView().findViewById(R.id.end_date_attendance);
        attendanceArrayList = new ArrayList<>();
        donutProgress = getView().findViewById(R.id.progress_bar_total_subjects);
        mPreferences=getActivity().getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        db = new DatabaseHandler(getContext());

        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tomorrowAsString = dateFormat.format(tomorrow);
        String end = tomorrowAsString;

        endDate.setText(end);
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_YEAR, -140);
        Date t = c.getTime();
        String tomorrowString = dateFormat.format(t);
        String start = tomorrowString;
        startDate.setText(start);
        id = mPreferences.getString("SignedInUserID","null");


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getContext(), sem[position], Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_date_attendance:

                calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDate.setText(dayOfMonth + "/" + (month + 1) + "/ " + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.end_date_attendance:

                calendar = Calendar.getInstance();
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                int month1 = calendar.get(Calendar.MONTH);
                int year1 = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDate.setText(dayOfMonth + "/" + (month + 1) + "/ " + year);
                            }
                        }, year1, month1, day1);

                datePickerDialog.show();
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attendence, container, false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
    }
}