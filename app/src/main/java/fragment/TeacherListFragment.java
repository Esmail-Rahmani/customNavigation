package fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.customnavigation.R;

import java.util.List;

import adapter.TeachersListAdapter;
import model.Teacher;

public class TeacherListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TeachersListAdapter adapter;
    public List<Teacher> list;

    public TeacherListFragment() {
        // Required empty public constructor
    }

    public static TeacherListFragment newInstance(String param1, String param2) {
        TeacherListFragment fragment = new TeacherListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.recyclerView_teacher);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String[] userName = getResources().getStringArray(R.array.teacherName);
        int[] profileImages ={R.drawable.n1,R.drawable.n2,
                R.drawable.n3,R.drawable.n4,
                R.drawable.n5,R.drawable.n6,
                R.drawable.n7};
//        String bookName = getResources().getString(R.string.BookName);

//
//        for (int i =0;i<userName.length;i++){
//            Teacher teacher = new Teacher(userName[i],bookName,profileImages[i]);
//            list.add(teacher);
//        }
//        adapter = new TeachersListAdapter(this,list);
//        recyclerView.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_list, container, false);
    }
}