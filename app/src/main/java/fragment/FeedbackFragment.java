package fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.customnavigation.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Checked;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class FeedbackFragment extends Fragment implements Validator.ValidationListener {

    @Checked
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    @NotEmpty
    private EditText title;
    @NotEmpty
    private EditText description;
    private Validator validator;
    private TextView fed_type;
    private Button sendBtn;
    private String titleString, descriptionString, type;
    public static final String URL = Constant.BASE_URL + "feedback.php";
    private SharedPreferences mPreferences;
    String sharedprofFile = "com.protocoderspoint.registration_login";
    SharedPreferences.Editor preferencesEditor;
    private String username;
    private SwipeRefreshLayout swipeToRefresh;


    public FeedbackFragment() {

    }

    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        initWidget();
        setListeners();
    }



    private void setListeners() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.setValidationListener(FeedbackFragment.this);
                validator.validate();
                Log.d("test123", "onClick: "+validator.isValidating() +" ");
                if (validator.isValidating()) {
                    setData();
                    sendData();

                }
            }
        });
    }

    // clear data like refreshing
    private void clearData() {
        title.getText().clear();
       description.getText().clear();
    }
    private void sendData() {
        Log.d("test123", "onResponse: " + URL);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getContext(), "Your Feedback successfully submitted", Toast.LENGTH_LONG).show();
                Log.d("test123", "onResponse: " + response);
                clearData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("test123", "onResponse: " + error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("title", titleString);
                parms.put("description", descriptionString);
                parms.put("type", type);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void setData() {
        titleString = title.getText().toString();
        descriptionString = description.getText().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = getActivity().findViewById(selectedId);
        type = radioButton.getText().toString();
    }
    private void initWidget() {
        radioGroup = getActivity().findViewById(R.id.radio_group);
        sendBtn = getActivity().findViewById(R.id.feedback_button);
        title = getActivity().findViewById(R.id.feedback_title);
        description = getActivity().findViewById(R.id.feedback_description);
        fed_type = getActivity().findViewById(R.id.feedback_type);
        mPreferences = getActivity().getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        username = mPreferences.getString("SignedInusername", "null");
        validator = new Validator(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false);
    }
    @Override
    public void onValidationSucceeded() {
        validator.validate(true);
    }
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }  else {
                Toast.makeText(getContext(), "Feedback type must be select", Toast.LENGTH_LONG).show();
            }
        }
    }
}
