package com.sourcey.myappartment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sourcey.myappartment.R;
import com.sourcey.myappartment.util.Language;
import com.sourcey.myappartment.util.MyContextWrapper;


public class ProjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //APP LANGUAGE
    public void changeLang(Context context, String lang) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Language", lang);
        editor.apply();
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        Language.LANG_CURRENT = preferences.getString("Language", Language.LANG_CURRENT);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, Language.LANG_CURRENT));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProjectsActivity.this, LoginActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(ProjectsActivity.this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_logout) {
            startActivity(new Intent(ProjectsActivity.this, LoginActivity.class));
            return true;
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(ProjectsActivity.this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
