package com.woofyapp.sms.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.woofyapp.sms.R;
import com.woofyapp.sms.application.Constants;
import com.woofyapp.sms.application.SmsApplication;
import com.woofyapp.sms.service.NetworkService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private EditText phoneNumber;
    private Button okButton;
    private  boolean okEnabled = false;
    private SmsApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        okButton = (Button) findViewById(R.id.verifyButton);
        app =  SmsApplication.getInstance();

        NetworkService  networkService = new NetworkService();

        if(!networkService.isConnected(MainActivity.this)){
            Toast.makeText(MainActivity.this,R.string.no_connection,Toast.LENGTH_LONG).show();
            phoneNumber.setFocusable(false);
            phoneNumber.setEnabled(false);
        }else{
            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after){}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){}

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length()==10 && !okEnabled){
                        okButton.setEnabled(true);
                        okButton.setClickable(true);
                        okEnabled = true;
                    }else if(okEnabled){
                        okButton.setEnabled(false);
                        okButton.setClickable(false);
                        okEnabled=false;
                    }
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.addToRequestQueue(createUser(),Constants.CREATE_USER_TAG);
                }
            });
        }



    }
    private JsonObjectRequest createUser(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        Map<String, String> params = new HashMap<String, String>();
        params.put("number", phoneNumber.getText().toString());
        params.put("token", Constants.TOKEN);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.CREATE_USER_URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(Constants.CREATE_USER_TAG, response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        return jsonObjReq;

    }
}
