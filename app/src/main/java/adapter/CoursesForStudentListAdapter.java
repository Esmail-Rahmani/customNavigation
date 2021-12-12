package adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customnavigation.CourseStudentListActivity;
import com.example.customnavigation.R;
import com.example.customnavigation.StudentListAttendanceActivity;

import java.util.List;

import model.Subject;

public class CoursesForStudentListAdapter extends RecyclerView.Adapter<CoursesForStudentListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Subject> mData;


    public CoursesForStudentListAdapter(Context mContext, List<Subject> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.subject_attendance_grid_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.courseName.setText(mData.get(position).getSubName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CourseStudentListActivity.class);
                intent.putExtra("id", mData.get(position).getSubId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.course_title_id);
            cardView = (CardView) itemView.findViewById(R.id.card_view_subject_attendance);
        }
    }
}
