package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.customnavigation.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapter.NewsFeedAdapter;
import adapter.TeachersListAdapter;
import controler.AppController;
import model.NewsFeed;
import model.Teacher;
import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class NewFeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsFeedAdapter adapter;
    public ArrayList<NewsFeed> newsFeedArrayList;
    private RequestQueue queue;
    private String newFeedUrl = Constant.BASE_URL+"postApi.php";
    private String username;
    private SharedPreferences mPreferences;
    String sharedprofFile="com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    public NewFeedFragment() {
        // Required empty public constructor
    }


    public static NewFeedFragment newInstance(String param1, String param2) {
        NewFeedFragment fragment = new NewFeedFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWidget();
        setPostData();
    }

    private void initWidget() {

        mPreferences=getActivity().getSharedPreferences(sharedprofFile,MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        username = mPreferences.getString("SignedInusername","null");
        newsFeedArrayList = new ArrayList<>();
        recyclerView = getActivity().findViewById(R.id.news_feed_recycler_view);
    }

    private ArrayList<NewsFeed> setPostData() {

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, newFeedUrl, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("test123", "onResponse:ej " + response.toString());

                        try {
                            JSONArray news = response;

                            for (int i = 0; i < news.length(); i++) {
                                JSONObject object = news.getJSONObject(i).getJSONObject("post");
                                int id = Integer.parseInt(object.getString("post_id"));
                                String title = object.getString("post_title");
                                String post_text = object.getString("post_text");
                                String image = object.getString("post_image");

                                int postType = Integer.parseInt(object.getString("post_type"));
                                String postDate = object.getString("post_date");


                                NewsFeed post = new NewsFeed(title,postDate,post_text,"8.jpg",image,id,postType);

                               newsFeedArrayList.add(post);
                            }

                            NewsFeedAdapter feedsListAdapter = new NewsFeedAdapter(getContext(),newsFeedArrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(feedsListAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("test123", "onResponse:try " + e);

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("test123", "onResponse:e " + error);

            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

        return newsFeedArrayList ;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_feed, container, false);
    }
}