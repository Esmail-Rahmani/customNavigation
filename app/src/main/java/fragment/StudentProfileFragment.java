package fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.customnavigation.ProfileViewActivity;
import com.example.customnavigation.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controler.AppController;
import db.DatabaseHandler;
import db.StudentDbHandler;
import model.Student;
import model.StudentListAsyncResponse;
import model.StudentParentDetail;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class StudentProfileFragment extends Fragment {
    private AlertDialog.Builder builder;
    private Button uploadButton;
    private Button openButton;
    private AlertDialog alertDialog;
    private ScrollView scrol;
    String ImageUri = Constant.BASE_URL + "uploadImageApi.php";
    int id, user_type;
    private ImageView pImage, stuImage;
    private TextView stuName, stuRollNo, stuEmail, stuPhone, stuDegree, department, stuAbout, stuGender, stuDOB, bloodGroup;
    private TextView parentName, parentEmail, parentPhone;
    private String url = Constant.BASE_URL + "studentParentApi.php?id=";
    String pPhone, sPhone, pEmail, sEmail;
    private SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private DatabaseHandler db;
    Bitmap bitmap;

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
        setListeners();
        id = Integer.parseInt(mPreferences.getString("SignedInUserID", "null"));
        user_type = mPreferences.getInt("userType", 0);
        setDate(id);
    }

    private void setListeners() {
        parentPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pPhone));
                startActivity(callIntent);
            }
        });
        parentEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + pEmail));
                intent.putExtra(Intent.EXTRA_EMAIL, pEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        stuPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + sPhone));
                startActivity(callIntent);
            }
        });
        stuEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + sEmail));
                intent.putExtra(Intent.EXTRA_EMAIL, sEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        stuImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (user_type == 3) {
                    createPopDialog();
                }
            }
        });


        pImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (user_type == 4) {
                    createPopDialog();
                }
            }
        });

    }

    private void getDataFromSqLite() {
        StudentParentDetail student = db.getStudentParent(id);

        Picasso.get()
                .load(Constant.IMAGE_URL + student.getImage())
                .placeholder(R.drawable.profile_error)
                .into(stuImage);
        Picasso.get()
                .load(Constant.IMAGE_URL + student.getParentImage())
                .placeholder(R.drawable.profile_error)
                .into(pImage);
        pPhone = student.getParentPhone();
        sPhone = student.getPhone();
        pEmail = student.getParentEmail();
        sEmail = student.getEmail();
        stuName.setText(student.getStuName() + " " + student.getStuLName());
        stuRollNo.setText("Roll No: " + student.getRollNo());
        stuPhone.setText("Phone: " + student.getPhone());
        stuEmail.setText("Email: " + student.getEmail());
        stuAbout.setText("About: " + student.getAbout());
        stuDegree.setText("Degree: " + student.getDegree());
        stuGender.setText("Gender: " + student.getGender());
        stuDOB.setText("Date of birth: " + student.getDOB());
        bloodGroup.setText("Blood Group: " + student.getBloodGroup());
        department.setText("Department: " + student.getDepartment());
        parentEmail.setText("Email: " + student.getParentEmail());
        parentName.setText(student.getParentName());
        parentPhone.setText("Phone: " + student.getParentPhone());

    }

    private void setDate(int id) {
        String urlTest = url + id;
        Log.d("test123", "setAttendanceData: " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject student = response;

                            Picasso.get()
                                    .load(Constant.IMAGE_URL + student.getString("stu_image"))
                                    .placeholder(R.drawable.profile_error)
                                    .into(stuImage);
                            Picasso.get()
                                    .load(Constant.IMAGE_URL + student.getString("parent_image"))
                                    .placeholder(R.drawable.profile_error)
                                    .into(pImage);
                            pPhone = student.getString("parent_phone");
                            sPhone = student.getString("stu_phone");
                            pEmail = student.getString("parent_email");
                            sEmail = student.getString("stu_email");
                            stuName.setText(student.getString("stu_name") + " " + student.getString("stu_lname"));
                            stuRollNo.setText("Roll No: " + student.getString("stu_roll_no"));
                            stuPhone.setText("Phone: " + student.getString("stu_phone"));
                            stuEmail.setText("Email: " + student.getString("stu_email"));
                            stuAbout.setText("About: " + student.getString("stu_about"));
                            stuDegree.setText("Degree: " + student.getString("stu_degree"));
                            stuGender.setText("Gender: " + student.getString("stu_gender"));
                            stuDOB.setText("Date of birth: " + student.getString("stu_dob"));
                            bloodGroup.setText("Blood Group: " + student.getString("stu_blood"));
                            department.setText("Department: " + student.getString("stu_dep"));
                            parentEmail.setText("Email: " + student.getString("parent_email"));
                            parentName.setText(student.getString("parent_name"));
                            parentPhone.setText("Phone: " + student.getString("parent_phone"));

                            JSONObject stu = student;
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


    }

    @RequiresApi(api = Build.VERSION_CODES.O)


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
                alertDialog.dismiss();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);
                alertDialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    if (user_type == 3)
                        stuImage.setImageBitmap(bitmap);
                    else
                        pImage.setImageBitmap(bitmap);
                    uploadImageToServer(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImageToServer(Bitmap bitmap) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ImageUri, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("test1234", "onResponse: " + response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test1234", "onResponse: " + error);

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String imgStr = imageToString(bitmap);
                params.put("image", imgStr);
                params.put("id", id + "");
                params.put("userType", mPreferences.getInt("userType", 0) + "");

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String decodedImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return decodedImage;
    }

    private void initWidget() {
        pImage = getActivity().findViewById(R.id.parent_img_details);
        stuImage = getActivity().findViewById(R.id.student_img_details);
        stuName = getActivity().findViewById(R.id.stu_name_details);
        stuRollNo = getActivity().findViewById(R.id.stu_rollno_details);
        stuEmail = getActivity().findViewById(R.id.stu_email_details);
        stuPhone = getActivity().findViewById(R.id.stu_phone_details);
        stuDegree = getActivity().findViewById(R.id.stu_degree_details);
        department = getActivity().findViewById(R.id.stu_dep_details);
        stuAbout = getActivity().findViewById(R.id.stu_about_details);
        stuGender = getActivity().findViewById(R.id.stu_gendar_details);
        stuDOB = getActivity().findViewById(R.id.stu_dob_details);
        bloodGroup = getActivity().findViewById(R.id.stu_blood_group_details);
        parentName = getActivity().findViewById(R.id.parent_name_details);
        parentEmail = getActivity().findViewById(R.id.parent_email_details);
        parentPhone = getActivity().findViewById(R.id.parent_phone_details);
        mPreferences = getActivity().getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        scrol = getActivity().findViewById(R.id.profile_lin);
        scrol.setBackgroundColor(Color.WHITE);
        db = new DatabaseHandler(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_student_parent_details, container, false);
    }
}