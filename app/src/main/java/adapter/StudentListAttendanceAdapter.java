package adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.customnavigation.ProfessorActivity;
import com.example.customnavigation.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DatabaseHandler;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Attendance;
import model.StudentParentDetail;
import utils.Constant;


public class StudentListAttendanceAdapter extends RecyclerView.Adapter<StudentListAttendanceAdapter.MyViewHolder> {
    Context context;
    public static final int ATTENDANCE_SYNCED_WITH_SERVER = 1;
    public static final int ATTENDANCE_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "net.heratUniAp.dataSaved";
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    List<StudentParentDetail> studentParentDetails;

    ArrayList<Attendance> attendanceArrayList;
    private String Url = Constant.BASE_URL + "setAttendance.php";
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    DatabaseHandler db ;


    public StudentListAttendanceAdapter(Context context, List<StudentParentDetail> studentParentDetailList) {
        this.context = context;
        db = new DatabaseHandler(context);
        this.studentParentDetails = studentParentDetailList;
    }

    public StudentListAttendanceAdapter(Context applicationContext, ArrayList<StudentParentDetail> studentParentDetails, ArrayList<Attendance> attendanceArrayList) {
        this.context = applicationContext;
        this.studentParentDetails = studentParentDetails;
        db = new DatabaseHandler(applicationContext);
        this.attendanceArrayList = attendanceArrayList;
    }

    @NonNull
    @Override
    public StudentListAttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == R.layout.student_list_attendance_row) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_attendance_row, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.button, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentListAttendanceAdapter.MyViewHolder holder, int position) {
        if (position == studentParentDetails.size()) {
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createPopDialog(holder);
                }
            });
        } else {
            Picasso.get()
                    .load(Constant.IMAGE_URL + studentParentDetails.get(position).getImage())
                    .placeholder(R.drawable.profile_error)
                    .into(holder.profileImage);
            holder.studentName.setText(studentParentDetails.get(position).getStuName());
            holder.rollNo.setText(studentParentDetails.get(position).getRollNo());
            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton radioButton = holder.itemView.findViewById(holder.radioGroup.getCheckedRadioButtonId());
                    if (radioButton.getText().equals("P")) {
                        holder.radioButtonP.setChecked(true);

                    } else {
                        holder.radioButtonA.setChecked(true);
                       attendanceArrayList.get(position).setAttendance(false);
                    }
                }
            });
        }
    }

    private void createPopDialog(MyViewHolder holder) {
        builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.attendance_total_popup, null);

        Button submitBtn, cancelBtn;
        TextView totalPresent, totalAbsent;
        submitBtn = view.findViewById(R.id.submit_btn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        totalAbsent = view.findViewById(R.id.total_absent);
        totalPresent = view.findViewById(R.id.total_present);
        int present, absent;
        present = presentCount(attendanceArrayList);
        absent = attendanceArrayList.size() - present;
        totalPresent.setText("Present: "+present);
        totalAbsent.setText("Absent: "+absent);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setTitle("Attendance");
        alertDialog.show();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test(attendanceArrayList);
                ProfessorActivity.professorActivity.finish();
                Intent intent = new Intent(v.getContext(), ProfessorActivity.class);
                intent.putExtra("viewpager_position", 1);

                v.getContext().startActivity(intent);
                ((Activity)context).finish();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }



    private int presentCount(ArrayList<Attendance> attendanceArrayList) {
        int sum = 0;
        for (int i = 0; i < attendanceArrayList.size(); i++) {
            if (attendanceArrayList.get(i).isAttendance()) {
                sum++;
            }
        }
        return sum;
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

        Log.d("test123", "test: " + json_string);
        sendData(attendanceArrayList,json_string);
    }

    private void sendData(ArrayList<Attendance> attendanceArrayList, String json_string) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    if (obj.getString("success").equals("1")) {
                        //if there is a success
                        //storing the name to sqlite with status synced
                        Log.d("test123", "onResponse: "+attendanceArrayList.get(0).getCourseId());
                        db.deleteAttendance();
                    } else {
                        //if there is some error
                        //saving the name to sqlite with status unsynced
                        saveAttendanceToLocalStorage(attendanceArrayList, ATTENDANCE_NOT_SYNCED_WITH_SERVER);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, "Your Attendance successfully submitted ", Toast.LENGTH_SHORT).show();
                Log.d("test123", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Your Attendance successfully saved  ", Toast.LENGTH_SHORT).show();
                Log.d("test123", "onResponse: " + error.toString());
                saveAttendanceToLocalStorage(attendanceArrayList, ATTENDANCE_NOT_SYNCED_WITH_SERVER);

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

    private void saveAttendanceToLocalStorage(ArrayList<Attendance> attendanceArrayList, int attendanceSyncedWithServer) {
        for (int i=0;i<attendanceArrayList.size();i++) {
            db.addAttendanceItem(attendanceArrayList.get(i),attendanceSyncedWithServer);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == studentParentDetails.size()) ? R.layout.button : R.layout.student_list_attendance_row;
    }

    @Override
    public int getItemCount() {
        return studentParentDetails.size() + 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView studentName, rollNo;
        RadioGroup radioGroup;
        RadioButton radioButtonP;
        RadioButton radioButtonA;
        Button button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image_student_list);
            studentName = itemView.findViewById(R.id.student_name_list);
            rollNo = itemView.findViewById(R.id.student_roll_no_list);
            radioGroup = itemView.findViewById(R.id.radio_group_student_list);
            radioButtonP = itemView.findViewById(R.id.present_button);
            radioButtonA = itemView.findViewById(R.id.absent_button);
            button = itemView.findViewById(R.id.button_submit);


        }
    }
}



