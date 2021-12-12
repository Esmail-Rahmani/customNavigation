package adapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.customnavigation.R;
import java.util.ArrayList;
import java.util.List;
import db.DatabaseHandler;
import model.Marks;
import model.StudentParentDetail;


public class ResultAndMarksAdapter extends RecyclerView.Adapter<ResultAndMarksAdapter.MyViewHolder> {
    Context context;
    public static final int ATTENDANCE_SYNCED_WITH_SERVER = 1;
    public static final int ATTENDANCE_NOT_SYNCED_WITH_SERVER = 0;
    public static final String DATA_SAVED_BROADCAST = "net.heratUniAp.dataSaved";
    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;


    ArrayList<Marks> marksArrayList;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    DatabaseHandler db ;


    public ResultAndMarksAdapter(Context context, ArrayList<Marks> marksList) {
        this.context = context;
        db = new DatabaseHandler(context);
        this.marksArrayList = marksList;
    }



    @NonNull
    @Override
    public ResultAndMarksAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == R.layout.marks_list_item_row) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.marks_list_item_row, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.semester_marks_header, parent, false);
        }
        return new ResultAndMarksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAndMarksAdapter.MyViewHolder holder, int position) {
        if (marksArrayList.get(position).getCourseName()==null) {
            holder.semester.setText("Result of Semester "+marksArrayList.get(position).getSemester());
        } else {
            holder.obtainMark.setText(" "+marksArrayList.get(position).getTotalObtain());
            holder.maxMark.setText(" "+marksArrayList.get(position).getTotalMax());
            holder.credit.setText(" "+marksArrayList.get(position).getCredit());
            holder.courseName.setText(" "+marksArrayList.get(position).getCourseName());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPopDialog(holder,position);
                }
            });
        }
    }

    @SuppressLint("ResourceAsColor")
    private void createPopDialog(MyViewHolder holder, int position) {
        builder = new AlertDialog.Builder(context);

        View view = LayoutInflater.from(context).inflate(R.layout.mark_details_popup, null);
        LinearLayout linearLayout;
        linearLayout = view.findViewById(R.id.lin_mark_popup);

        for (int i=0;i<marksArrayList.get(position).getMarkDetails().size();i++){

            LinearLayout layout2 = new LinearLayout(context);
            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            layout2.setOrientation(LinearLayout.HORIZONTAL);
            layout2.setPadding(15,15,15,15);
            layout2.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(layout2);

            //children of layout2 LinearLayout
            TextView tv1 = new TextView(context);
            TextView tv3 = new TextView(context);

            tv1.setPadding(10,10,10,10);
            tv3.setPadding(10,10,10,10);
            tv1.setTextColor(R.color.textMainColor);
            tv3.setTextColor(R.color.textMainColor);
            tv1.setTextSize(20);
            tv3.setTextSize(20);

            tv1.setText(marksArrayList.get(position).getMarkDetails().get(i).getName());
            tv3.setText(" "+marksArrayList.get(position).getMarkDetails().get(i).getObtain());

            layout2.addView(tv1);
            layout2.addView(tv3);
        }


        TextView title = new TextView(context);
        // You Can Customise your Title here
        title.setText("Marks Detail");
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        builder.setCustomTitle(title);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();


    }

    @Override
    public int getItemViewType(int position) {
        return (marksArrayList.get(position).getCourseName()==null) ? R.layout.semester_marks_header : R.layout.marks_list_item_row;
    }

    @Override
    public int getItemCount() {
        return marksArrayList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView courseName, credit,maxMark,obtainMark,semester;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.course_name_marks_list);
            credit = itemView.findViewById(R.id.course_credit_marks_list);
            maxMark = itemView.findViewById(R.id.max_marks_list);
            obtainMark = itemView.findViewById(R.id.obtain_marks_list);
            cardView = itemView.findViewById(R.id.cardView_mark_list);
            semester = itemView.findViewById(R.id.semester_text);


        }
    }
}