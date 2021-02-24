package adapter;

import android.content.Context;
import android.database.DataSetObserver;
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
            TextView title = convertView.findViewById(R.id.subject_name_row);
            title.setText(subjectData.getsName());
            TextView classes=convertView.findViewById(R.id.goal_attendance);
            TextView attended = convertView.findViewById(R.id.attended_class);
            donutProgress = convertView.findViewById(R.id.progress_bar_total_subjects);
            double sum = (100*subjectData.getAttendedClass())/(subjectData.getTotalClass());
            donutProgress.setText(sum+"%");
            donutProgress.setMax(100);
            donutProgress.setProgress((float) sum);
            classes.setText("Classes: "+subjectData.getTotalClass());
            attended.setText("Attendance: "+subjectData.getAttendedClass());


        }
        return convertView;
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
