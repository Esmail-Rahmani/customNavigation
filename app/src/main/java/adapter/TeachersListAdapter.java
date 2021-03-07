package adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.customnavigation.R;
import com.example.customnavigation.TeacherViewActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Teacher;

// for searcing
public class TeachersListAdapter  extends RecyclerView.Adapter<TeachersListAdapter.MyViewHolder> implements Filterable {
    Context context;
    List<Teacher> teachers;
    // for searching
    List<Teacher> teachersfull;

    public TeachersListAdapter(Context context, List<Teacher> teachers) {
        this.context = context;
        this.teachers = teachers;
        teachersfull = new ArrayList<>(teachers);
    }

    @NonNull
    @Override
    public TeachersListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_list_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersListAdapter.MyViewHolder holder, int position) {
        holder.teacherName.setText(teachers.get(position).getTeacherName());
        holder.bookName.setText(teachers.get(position).getContactNo());
        holder.profileImage.setImageResource(Integer.parseInt(teachers.get(position).getImageURI()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherViewActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    @Override
    public Filter getFilter() {
        return FilterUser;
    }
    private Filter FilterUser= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String searchText = constraint.toString().toLowerCase();
            List<Teacher>tempList = new ArrayList<>();
            if(searchText.length()==0||searchText.isEmpty()){
                tempList.addAll(teachersfull);
            }
            else {
                for (Teacher item:teachersfull){
                    if(item.getTeacherName().toLowerCase().contains(searchText)||item.getTeacherName().toLowerCase().contains(searchText)){
                        tempList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = tempList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            teachers.clear();
            teachers.addAll((Collection<?extends Teacher>)results.values);
            notifyDataSetChanged();

        }
    };
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView teacherName,bookName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            teacherName = itemView.findViewById(R.id.user_name);
            bookName = itemView.findViewById(R.id.book_description);

        }
    }
}



