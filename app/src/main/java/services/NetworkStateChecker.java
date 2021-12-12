package services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.customnavigation.ProfessorActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import adapter.StudentListAttendanceAdapter;
import db.DatabaseHandler;
import model.Attendance;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DatabaseHandler db;
    private String Url = Constant.BASE_URL + "setAttendance.php";
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private boolean flag;

    @Override
    public void onReceive(Context context, Intent intent) {

        mPreferences=context.getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        flag = mPreferences.getBoolean("net",true);
        Log.d("test1234", "test: " );
        this.context = context;
        db = new DatabaseHandler(context);

        if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false);
            if (noConnectivity){
                Log.d("test123", "onReceive: No connectivity");
            }else {
                Log.d("test123", "onReceive:  connected");
                ArrayList<Attendance> arrayList = db.getUnSyncAttendanceListList();

                if (arrayList.size() > 0) {
                    test(arrayList);
                    Log.d("test1234", "test: " + "test o " + arrayList.size());
                }
            }
        }


//        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//
//        //if there is a network
//        if (activeNetwork != null && flag) {
//            preferencesEditor.putBoolean("net",false);
//            Log.d("test1234", "test: " + "test network is there");
//
//            //if connected to wifi or mobile data plan
//            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
//                Log.d("test1234", "test: " + "test network is there");
//
//                //getting all the unsynced names
//                ArrayList<Attendance> arrayList = db.getUnSyncAttendanceListList();
//
//                if (arrayList.size() > 0) {
//                    test(arrayList);
//                    Log.d("test1234", "test: " + "test o " + arrayList.size());
//                }
//            }else {
//                preferencesEditor.putBoolean("net",true);
//            }
//        }else {
//            preferencesEditor.putBoolean("net",true);
//        }
    }

    private void test(ArrayList<Attendance> attendanceArrayList) {
        //Create JSON string start
        String json_string = "{\"upload_attendance\":[";

        for (int i = 0; i < attendanceArrayList.size(); i++) {
            JSONObject obj_new = new JSONObject();
            try {
                obj_new.put("course_id", attendanceArrayList.get(i).getCourseId());
                obj_new.put("stu_id", attendanceArrayList.get(i).getStuId());
                obj_new.put("prof_id", attendanceArrayList.get(i).getProfId());
                final Calendar cal = Calendar.getInstance();
                Date today = cal.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateString = dateFormat.format(today);
                obj_new.put("date", dateString);
                obj_new.put("attendance", attendanceArrayList.get(i).isAttendance());


                json_string = json_string + obj_new.toString() + ",";
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

//Close JSON string
        json_string = json_string.substring(0, json_string.length() - 1);
        json_string += "]}";

        Log.d("test1234", "test: " + json_string);
        sendData(json_string,attendanceArrayList);
    }

    private void sendData(String json_string, ArrayList<Attendance> attendanceArrayList) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.getString("success").equals("1")) {

                        //updating the status in sqlite
                        db.updateAttendanceStatus(attendanceArrayList,1);
                        db.deleteAttendance();
                        Log.d("test1234", "onResponse: this is done");
                        //sending the broadcast to refresh the list
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Your Attendance successfully submitted ", Toast.LENGTH_SHORT).show();
                Log.d("test1234", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("LIST", json_string);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
