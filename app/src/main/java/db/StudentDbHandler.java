package db;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import controler.AppController;
import model.Student;
import model.StudentListAsyncResponse;
import utils.Constant;

public class StudentDbHandler  {
    Context context;
    private String Url = Constant.BASE_URL+"studentsApi.php";
    private String urlStudent = Constant.BASE_URL+"studentApi.php?username=";
    ArrayList<Student> studentArrayList ;
    private Student studentObj;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public StudentDbHandler(Context context) {
        this.context = context;
        studentArrayList = new ArrayList<>();
    }

    public Student getStudent(String username) {
        return findObject(username);
    }

    private Student findObject(String username) {

        for(int i = 0 ; i<=studentArrayList.size();i++ ){
            if (username == studentArrayList.get(i).getUsername()){
                return studentArrayList.get(i);
            }
        }
        return null;
    }

    public List<Student> getStudents(final StudentListAsyncResponse callBack){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONArray teachers= response  ;
                            for (int i=0 ; i<teachers.length() ;i++) {
                                JSONObject teacher = teachers.getJSONObject(i).getJSONObject("student");
                                studentArrayList.add(setObject(teacher));
                            }
                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(null != callBack) callBack.processFinished(studentArrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e "+error);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return studentArrayList;
    }
    public Student getStudent(String username,final StudentListAsyncResponse callBack){
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, urlStudent+username, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject student= response;
                                JSONObject teacher = student.getJSONObject("student");
                                studentObj = setObject(teacher);

                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(null != callBack) callBack.processFinishedStudent(studentObj);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e "+error);
            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return studentObj;
    }


    private void loadIntoArrayList(String json) throws JSONException {

        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);

        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {
            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i).getJSONObject("student");

            studentArrayList.add(setObject(obj));
            Log.d("test123", "loadIntoArrayList: ye s "+studentArrayList.get(i).getDateOfBirth());

        }
    }
    public ArrayList<Student> getStudentList(){
        return studentArrayList;
    }

    private Student setObject(JSONObject obj) {
        String studentName = null;
        String contactNo = null;
        String[] subjects = {"math","biology"};
        String imageURI = null;
        String email = null;
        String gender = null;
        String dateOfBirth = null;
        String username = null;
        String password = null;
        String degree = null;
        try {
            studentName = obj.getString("stu_name");
            int studentId = obj.getInt("stu_id");
            contactNo = obj.getString("stu_phone");
            imageURI = obj.getString("stu_image");
            email = obj.getString("stu_email");
            gender = obj.getString("stu_gender");
            dateOfBirth = obj.getString("stu_DOB");
            username = obj.getString("stu_username");
            password = obj.getString("stu_password");
            degree = obj.getString("stu_degree");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new Student(studentName, subjects, contactNo, imageURI, email, gender, dateOfBirth, username, password, degree);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    private void getJSON(final String urlWebService) {


        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {


                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoArrayList(s);

                    Log.d("test123", "onPostExecute: "+studentArrayList.get(0).getDateOfBirth());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {


                try {
                    //creating a URL
                    URL url = new URL(urlWebService);
                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");

                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


}
