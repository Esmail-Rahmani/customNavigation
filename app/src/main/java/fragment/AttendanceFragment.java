package fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.customnavigation.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.SubjectAttendanceAdapter;
import model.SubjectAttendance;

public class AttendanceFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    String[] sem = {"sem I", "Sem II", "Sem III"};
    private Spinner spinner;
    private EditText startDate;
    private EditText endDate;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private ArrayList<SubjectAttendance> attendanceArrayList;
    private ListView listViewSubject;
    private DonutProgress donutProgress;


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

        ArrayAdapter spinnerAdapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, sem);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        SubjectAttendance subjectAttendance = new SubjectAttendance("Math",12,7);
        attendanceArrayList.add(subjectAttendance);
        attendanceArrayList.add(new SubjectAttendance("Biology",22,7));
        attendanceArrayList.add(new SubjectAttendance("Software Testing",21,17));

        SubjectAttendanceAdapter customAdapter = new SubjectAttendanceAdapter( attendanceArrayList,getContext());
//        listViewSubject.setAdapter(customAdapter);
        double sum = (100*(7+7+17))/(12+21+22);
        donutProgress.setText(sum+"%");
        donutProgress.setMax(100);
        donutProgress.setProgress((float) sum);

        LinearLayout listViewReplacement =  getView().findViewById(R.id.line11);
        for (int i = 0; i < customAdapter.getCount(); i++) {
            View view = customAdapter.getView(i, null, listViewReplacement);
            listViewReplacement.addView(view);
        }
        setListeners();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    private void setListeners() {
        spinner.setOnItemSelectedListener(this);
        startDate.setOnClickListener(this);
        endDate.setOnClickListener(this);
    }

    private void initWidget() {
        spinner = getView().findViewById(R.id.spinner_semester_selection);
        startDate = getView().findViewById(R.id.start_date_attendance);
        endDate = getView().findViewById(R.id.end_date_attendance);
//        listViewSubject = findViewById(R.id.attendance_list);
        attendanceArrayList = new ArrayList<>();
        donutProgress = getView().findViewById(R.id.progress_bar_total_subjects);
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
}