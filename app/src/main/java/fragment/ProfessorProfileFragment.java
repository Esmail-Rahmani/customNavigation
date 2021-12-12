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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import controler.AppController;
import db.DatabaseHandler;
import model.StudentParentDetail;
import model.Teacher;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;

public class ProfessorProfileFragment extends Fragment {
    private AlertDialog.Builder builder;
    private Button uploadButton;
    private Button openButton;
    private AlertDialog alertDialog;
    private ScrollView scrol;
    int id;
    private ImageView profImage;
    private TextView profName, profSERNo, profEmail, profPhone, profDegree,department, profAbout, profGender, profDOB,bloodGroup;
    private String url = Constant.BASE_URL+"ProfessorDetailsApi.php?id=";
    String sPhone,sEmail;
    String ImageUri = Constant.BASE_URL+"uploadImageApi.php";
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private DatabaseHandler db;
    private Bitmap bitmap;

    public ProfessorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setListeners();
        id = Integer.parseInt(mPreferences.getString("SignedInUserID","null"));
        setDate(id);
    }

    private void setListeners() {
        profPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+sPhone));
                startActivity(callIntent);
            }
        });
        profImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
        profEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+sEmail));
                intent.putExtra(Intent.EXTRA_EMAIL, sEmail);
                intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }
    private void getDataFromSqLite() {
        Teacher teacher = db.getProfessorProfile(id);

        Picasso.get()
                .load(Constant.IMAGE_URL + teacher.getImageURI())
                .placeholder(R.drawable.profile_error)
                .into(profImage);

        sPhone =teacher.getPhone();
        sEmail=teacher.getEmail();
        profName.setText(teacher.getTeacherName() +" "+teacher.getTeacherLName());
        profSERNo.setText("SER No: "+teacher.getSerNo());
        profPhone.setText("Phone: "+teacher.getPhone());
        profEmail.setText("Email: "+teacher.getEmail());
        profAbout.setText("About: "+teacher.getAbout());
        profDegree.setText("Degree: "+teacher.getDegree());
        profGender.setText("Gender: "+teacher.getGender());
        profDOB.setText("Date of birth: "+teacher.getDateOfBirth());
        bloodGroup.setText("Blood Group: "+teacher.getBloodGroup());
        department.setText("Department: "+teacher.getDepartment());

    }
    private void setDate(int id) {
        String urlTest = url + id;
        Log.d("test123", "professor  " + urlTest);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlTest, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONObject prfessor = response;

                            Picasso.get()
                                    .load(Constant.IMAGE_URL + prfessor.getString("prof_image"))
                                    .placeholder(R.drawable.profile_error)
                                    .into(profImage);
                        
                            sPhone =prfessor.getString("prof_phone");
                            sEmail=prfessor.getString("prof_email");
                            profName.setText(prfessor.getString("prof_name") +" "+prfessor.getString("prof_lname"));
                            profSERNo.setText("SER No: "+prfessor.getString("ser_no"));
                            profPhone.setText("Phone: "+prfessor.getString("prof_phone"));
                            profEmail.setText("Email: "+prfessor.getString("prof_email"));
                            profAbout.setText("About: "+prfessor.getString("about"));
                            profDegree.setText("Degree: "+prfessor.getString("prof_degree"));
                            profGender.setText("Gender: "+prfessor.getString("prof_gender"));
                            profDOB.setText("Date of birth: "+prfessor.getString("prof_DOB"));
                            bloodGroup.setText("Blood Group: "+prfessor.getString("blood_group"));
                            department.setText("Department: "+prfessor.getString("prof_department"));

                            JSONObject stu = prfessor;
                            String stuName = stu.getString("prof_name");
                            String SERNo = stu.getString("ser_no");
                            String stuImage = stu.getString("prof_image");
                            String stuLName = stu.getString("prof_lname");
                            int stuId = Integer.parseInt(stu.getString("prof_id"));
                            String email = stu.getString("prof_email");
                            String phone = stu.getString("prof_phone");
                            String DOB = stu.getString("prof_DOB");
                            String bloodGroup = stu.getString("blood_group");
                            String degree = stu.getString("prof_degree");
                            String department = stu.getString("prof_department");
                            String about  = stu.getString("about");
                            String gender = stu.getString("prof_gender");

                            Teacher teacher = new Teacher(stuId,stuName, stuLName, phone ,stuImage,
                                    email,gender,bloodGroup,SERNo, about, DOB,  degree,
                                    department);

                            db.addProfessorProfile(teacher);

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
                startActivityForResult(intent,1001);

                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1001){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    profImage.setImageBitmap(bitmap);
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
                Log.d("test1234", "onResponse: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test1234", "onResponse: "+error);

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                String imgStr = imageToString(bitmap);
                params.put("image",imgStr);
                params.put("id",id+"");
                params.put("userType",mPreferences.getInt("userType",0)+"");

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        String decodedImage = Base64.encodeToString(imageByte,Base64.DEFAULT);
        return decodedImage;
    }

    private void initWidget() {
        profImage = getActivity().findViewById(R.id.prof_profile_img);
        profName = getActivity().findViewById(R.id.prof_name_profile);
        profSERNo = getActivity().findViewById(R.id.prof_ser_no_profile);
        profEmail = getActivity().findViewById(R.id.prof_email_profile);
        profPhone = getActivity().findViewById(R.id.prof_phone_profile);
        profDegree = getActivity().findViewById(R.id.prof_degree_profile);
        department = getActivity().findViewById(R.id.prof_dep_profile);
        profAbout = getActivity().findViewById(R.id.prof_about_profile);
        profGender = getActivity().findViewById(R.id.prof_gender_profile);
        profDOB = getActivity().findViewById(R.id.prof_dob_profile);
        bloodGroup = getActivity().findViewById(R.id.prof_blood_group_profile);
       mPreferences=getActivity().getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        scrol = getActivity().findViewById(R.id.profile_profile_fragment);
        scrol.setBackgroundColor(Color.WHITE);
        db = new DatabaseHandler(getContext());

    }

    public static ProfessorProfileFragment newInstance(String param1, String param2) {
        ProfessorProfileFragment fragment = new ProfessorProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_professor_profile, container, false);
    }
}