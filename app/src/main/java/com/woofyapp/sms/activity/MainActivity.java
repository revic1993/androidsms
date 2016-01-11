package com.woofyapp.sms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.woofyapp.sms.R;
import com.woofyapp.sms.service.NetworkService;

public class MainActivity extends AppCompatActivity {


    private EditText phoneNumber;
    private Button okButton;
    private  boolean okEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        okButton = (Button) findViewById(R.id.verifyButton);


        NetworkService  networkService = new NetworkService();
        if(!networkService.isConnected(MainActivity.this)){
            Toast.makeText(MainActivity.this,R.string.no_connection,Toast.LENGTH_LONG).show();
            phoneNumber.setFocusable(false);
            phoneNumber.setEnabled(false);
        }else{
            phoneNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

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
                    Intent intent = new Intent(MainActivity.this,ContactActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
