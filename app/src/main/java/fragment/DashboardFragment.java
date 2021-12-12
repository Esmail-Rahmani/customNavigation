package fragment;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.customnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.Constant;

public class DashboardFragment extends Fragment {

    private String Url = Constant.BASE_URL+"teachersApi.php";
    private ListView listView;
    private RequestQueue queue;


    public DashboardFragment() {
        // Required empty public constructor
    }

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) getView().findViewById(R.id.listView);
//        getJSON(Url);
        queue = Volley.newRequestQueue(getContext());

        getJSON(Url);

    }

    private void getJSON(String url) {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            JSONArray teachers= response  ;
                            Log.d("test123", "onResponse:ts "+teachers);

                            String heroes[] = new String[teachers.length()];
                            for (int i=0 ; i<teachers.length() ;i++) {
                                JSONObject teacher = teachers.getJSONObject(i).getJSONObject("teacher");
                                Log.d("test123", "onResponse:t "+teacher);


                                heroes[i] = teacher.getString("teacher_name");
                                Log.d("test123", "onResponse:i "+heroes[i]);


                            }
                            Log.d("test123", "onResponse:a "+heroes);

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, heroes);

                            //attaching adapter to listview
                            listView.setAdapter(arrayAdapter);
                        }  catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e "+error);

            }
        });

        queue.add(jsonObjectRequest);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

}