package com.sourcey.myappartment;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


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

//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_logout:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;

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
