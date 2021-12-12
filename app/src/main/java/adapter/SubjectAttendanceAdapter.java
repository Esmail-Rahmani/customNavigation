package adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.example.customnavigation.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

import model.SubjectAttendance;

public class SubjectAttendanceAdapter implements ListAdapter {
    private DonutProgress donutProgress;

    ArrayList<SubjectAttendance> arrayList;
    Context context;

    public SubjectAttendanceAdapter(ArrayList<SubjectAttendance> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SubjectAttendance subjectData=arrayList.get(position);
        if(convertView==null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView=layoutInflater.inflate(R.layout.subject_attendance_row, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            int cls;
            TextView title = convertView.findViewById(R.id.subject_name_row);
            title.setText(subjectData.getsName());
            TextView classes=convertView.findViewById(R.id.goal_attendance);
            TextView attended = convertView.findViewById(R.id.attended_class);
            TextView status = convertView.findViewById(R.id.attended_status);
            donutProgress = convertView.findViewById(R.id.progress_bar_total_subjects);
            double sum = (100*subjectData.getAttendedClass())/(subjectData.getTotalClass());
            donutProgress.setText(sum+"%");
            donutProgress.setMax(100);
            donutProgress.setProgress((float) sum);
            if (sum>=75){
                donutProgress.setFinishedStrokeColor(Color.rgb(0,165,0));
            }
            else{
                donutProgress.setFinishedStrokeColor(Color.rgb(165,0,0));
            }
            classes.setText("Classes: "+subjectData.getTotalClass());
            attended.setText("Attendance: "+subjectData.getAttendedClass());

            if (sum>75){
                cls = getDiffrence(subjectData.getTotalClass(),subjectData.getAttendedClass());
                if (cls==1){
                    status.setText("status : you can have one more leave.");
                }else if(cls==0){
                    status.setText("status : you can't have more leave.");
                }
                else{
                    status.setText("status : you can have "+cls+ " more leave.");
                }
            }else if(sum<75){
                cls = getDiffrence(subjectData.getTotalClass(),subjectData.getAttendedClass());
                if (cls==1){
                    status.setText("status : you have to be present one more session");
                }else if(cls==0){
                    status.setText("status : you reached the goal");
                }else{
                    status.setText("status : you have to be present "+cls+ " more sessions");
                }
            }else {
                status.setText("status : you reach the goal");
            }



        }
        return convertView;
    }

    private int getDiffrence(int totalClass, int attendedClass) {
        int i=0;
        if (((double)attendedClass/totalClass)>0.75){
            while (((double)attendedClass/totalClass)>=0.75){
                totalClass++;
                if (((double)attendedClass/totalClass)>0.75){
                    i++;
                }
            }
        }else
        {
            while (((double)attendedClass/totalClass)<=0.75){
                totalClass++;
                attendedClass++;
                i++;
            }
        }
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
