package com.sourcey.myappartment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sourcey.myappartment.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        TextView tvMobileNumber= (TextView) findViewById(R.id.tv_mobile_number);
        Button btLogout = (Button) findViewById(R.id.btn_logout);

        btLogout.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();

        String name = extras.getString("name");
        String address = extras.getString("address");
        String email = extras.getString("email");
        int mobile_number = extras.getInt("mobile_number", -1);

        tvName.setText(name);
        tvAddress.setText(address);
        tvEmail.setText(email);
        tvMobileNumber.setText(Integer.toString(mobile_number));

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_logout:
                startActivity(new Intent(MainActivity.this, ProjectsActivity.class));
                break;

        }
    }


}
