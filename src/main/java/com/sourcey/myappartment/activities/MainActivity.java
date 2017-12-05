package com.sourcey.myappartment.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sourcey.myappartment.R;
import com.sourcey.myappartment.database.DBUser;
import com.sourcey.myappartment.model.UserSessionData;
import com.sourcey.myappartment.util.Language;
import com.sourcey.myappartment.util.MyContextWrapper;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";

    DBUser dbUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbUser = new DBUser(this);

        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvAddress = (TextView) findViewById(R.id.tv_address);
        TextView tvEmail = (TextView) findViewById(R.id.tv_email);
        TextView tvMobileNumber= (TextView) findViewById(R.id.tv_mobile_number);
        Button btLogout = (Button) findViewById(R.id.btn_logout);
        Button btLanguage = (Button) findViewById(R.id.btn_language);

        btLogout.setOnClickListener(this);
        btLanguage.setOnClickListener(this);

        tvName.setText(UserSessionData.NAME);
        tvEmail.setText(UserSessionData.EMAIL);
        tvAddress.setText(UserSessionData.ADDRESS);
        tvMobileNumber.setText(Integer.toString(UserSessionData.MOBILE_NUMBER));

    }

    //APP LANGUAGE
    public void changeLang(Context context, String lang) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Language", lang);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_logout:
                startActivity(new Intent(MainActivity.this, ProjectsActivity.class));
                break;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        Language.LANG_CURRENT = preferences.getString("Language", Language.LANG_CURRENT);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, Language.LANG_CURRENT));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, ProjectsActivity.class));
    }

}
