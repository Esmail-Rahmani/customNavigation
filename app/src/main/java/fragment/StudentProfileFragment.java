package fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.customnavigation.ProfileViewActivity;
import com.example.customnavigation.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import db.StudentDbHandler;
import model.Student;
import model.StudentListAsyncResponse;


public class StudentProfileFragment extends Fragment {
    private ImageView profile;
    private TextView name, curriculum, birthCountry, nationality, gender, DOB, email, enrollNo, cls, classRank;
    private AlertDialog.Builder builder;
    private Button uploadButton;
    private Button openButton;
    private AlertDialog alertDialog;
    private List<Student> studentList;
    private Student student;

    public StudentProfileFragment() {
        // Required empty public constructor
    }

    public static StudentProfileFragment newInstance(String param1, String param2) {
        StudentProfileFragment fragment = new StudentProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initWidget();
        listener();
        getStudentObject();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getStudentObject() {
        Intent intent = getActivity().getIntent();
        String username = intent.getStringExtra("username");
        username = "omid";
        student = new StudentDbHandler(getContext()).getStudent(username,new StudentListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Student> studentArrayList) {

            }

            @Override
            public void processFinishedStudent(Student student) {
                setData(student);
            }
        });


    }

    private void setData(Student student) {
        if (student.getImageURI() != null) {
            profile.setImageURI(Uri.parse(student.getImageURI()));
        }
        name.setText(student.getStudentName());
        curriculum.setText(student.getDegree() + "");
        birthCountry.setText("Afghanistan");
        nationality.setText("Afghanistan");
        gender.setText(student.getGender());
        DOB.setText(student.getDateOfBirth() + "");
        email.setText(student.getEmail());
        enrollNo.setText(student.getStudentId() + "");
        cls.setText("TY BCA");
        classRank.setText("second");
    }

    private void listener() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.profile_popup, null);

        openButton = view.findViewById(R.id.open_image);
        uploadButton = view.findViewById(R.id.upload_image);


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileViewActivity.class);
                intent.putExtra("URl", R.drawable.omid);
                startActivity(intent);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initWidget() {
        profile = getActivity().findViewById(R.id.student_profile_pic);
        name = getActivity().findViewById(R.id.name_of_student_profile);
        curriculum = getActivity().findViewById(R.id.curriculum_profile);
        birthCountry = getActivity().findViewById(R.id.birth_country_profile);
        nationality = getActivity().findViewById(R.id.nationality_profile);
        gender = getActivity().findViewById(R.id.gender_profile);
        DOB = getActivity().findViewById(R.id.date_of_birth_profile);
        email = getActivity().findViewById(R.id.email_profile);
        enrollNo = getActivity().findViewById(R.id.enroll_no_student_profile);
        cls = getActivity().findViewById(R.id.class_student_profile);
        classRank = getActivity().findViewById(R.id.class_rank_student_profile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_profile, container, false);
    }
}