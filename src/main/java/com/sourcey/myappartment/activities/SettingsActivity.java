package com.sourcey.myappartment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.sourcey.myappartment.R;
import com.sourcey.myappartment.util.MyContextWrapper;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private String LANG_CURRENT = "en";
    private ViewHolder viewHolder = new ViewHolder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.viewHolder.rbEnglish = (RadioButton) findViewById(R.id.rb_english);
        this.viewHolder.rbPortuguese = (RadioButton) findViewById(R.id.rb_portuguese);
        this.viewHolder.rbGerman = (RadioButton) findViewById(R.id.rb_german);
        this.viewHolder.rbFranch = (RadioButton) findViewById(R.id.rb_french);
        this.viewHolder.btConfirm = (Button) findViewById(R.id.bt_confirm_settings);

        this.viewHolder.btConfirm.setOnClickListener(this);
    }



    public void changeLang(Context context, String lang) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Language", lang);
        editor.apply();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        LANG_CURRENT = preferences.getString("Language", "en");

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_confirm_settings:
                if (this.viewHolder.rbEnglish.isChecked() == true) {
                    changeLang(SettingsActivity.this, "en");
                } else if (this.viewHolder.rbPortuguese.isChecked() == true) {
                    changeLang(SettingsActivity.this, "pt");
                } else if (this.viewHolder.rbFranch.isChecked() == true) {
                    changeLang(SettingsActivity.this, "fr");
                } else if (this.viewHolder.rbGerman.isChecked() == true) {
                    changeLang(SettingsActivity.this, "de");
                }

                Intent intent = getIntent();
                finish();
                startActivity(intent);
                //startActivity(new Intent(MainActivity.this, MainActivity.class));
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                break;
        }
    }

    private class ViewHolder {
        RadioButton rbEnglish;
        RadioButton rbPortuguese;
        RadioButton rbGerman;
        RadioButton rbFranch;
        Button btConfirm;
    }
}
