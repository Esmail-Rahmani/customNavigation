package adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.customnavigation.R;
import com.example.customnavigation.StudentParentDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.StudentParentDetail;
import utils.Constant;


public class StudentParentDetailsAdapter extends RecyclerView.Adapter<adapter.StudentParentDetailsAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<StudentParentDetail> studentParentDetails;
    List<StudentParentDetail> studentParentDetailsFull;



    public StudentParentDetailsAdapter(Context context, List<StudentParentDetail> studentParentDetailList) {
        this.context = context;
        this.studentParentDetails = studentParentDetailList;
        studentParentDetailsFull = new ArrayList<>(studentParentDetails);
    }


    @NonNull
    @Override
    public adapter.StudentParentDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_list_row, parent, false);
        return new adapter.StudentParentDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.StudentParentDetailsAdapter.MyViewHolder holder, int position) {

        Picasso.get()
                .load(Constant.IMAGE_URL + studentParentDetails.get(position).getImage())
                .placeholder(R.drawable.profile_error)
                .into(holder.profileImage);
        holder.studentName.setText(studentParentDetails.get(position).getStuName());
        holder.rollNo.setText(studentParentDetails.get(position).getRollNo());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentParentDetailsActivity.class);
                intent.putExtra("stu_id", studentParentDetails.get(position).getStuId());
                Log.d("test123", "onClick: "+ studentParentDetails.get(position).getStuId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return (position == studentParentDetails.size()) ? R.layout.button : R.layout.student_list_attendance_row;
    }

    @Override
    public int getItemCount() {
        return studentParentDetails.size();
    }

    @Override
    public Filter getFilter() {
        return studentFilter;
    }
    private Filter studentFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StudentParentDetail> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length()==0){
                filteredList.addAll(studentParentDetailsFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (StudentParentDetail p : studentParentDetailsFull ) {
                    if (p.getStuName().toLowerCase().contains(filterPattern) || p.getRollNo().toLowerCase().contains(filterPattern)
                            ||p.getStuLName().toLowerCase().contains(filterPattern)){
                        filteredList.add(p);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            studentParentDetails.clear();
            studentParentDetails.addAll((Collection<? extends StudentParentDetail>) results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView studentName, rollNo;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image_student_list_row);
            studentName = itemView.findViewById(R.id.student_name_list_row);
            rollNo = itemView.findViewById(R.id.student_roll_no_list_row);
            cardView = itemView.findViewById(R.id.cardView_student_list_row);
        }
    }
}



