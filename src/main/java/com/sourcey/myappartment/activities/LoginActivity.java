package com.sourcey.myappartment.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.sourcey.myappartment.R;
import com.sourcey.myappartment.database.DBCategorie;
import com.sourcey.myappartment.database.DBHelper;
import com.sourcey.myappartment.database.DBProject;
import com.sourcey.myappartment.database.DBUser;
import com.sourcey.myappartment.model.User;
import com.sourcey.myappartment.model.UserSessionData;
import com.sourcey.myappartment.util.Language;
import com.sourcey.myappartment.util.LoginRequest;
import com.sourcey.myappartment.util.MyContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    boolean save_user = false;

    CheckBox ckLogin;
    DBUser dbUser;

    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        User user = new User();

        //for problems with database
        resetDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dbUser = new DBUser(this);

        this.ckLogin = (CheckBox) findViewById(R.id.ckLogin);

        //ADD LAST USER
        user = dbUser.getLastLoggedUser();
        _emailText.setText(user.getEmail());
        _passwordText.setText(user.getPassword());

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    public void resetDatabase(){
        DBProject dbProject = new DBProject(this);
        DBCategorie dbCategorie = new DBCategorie(this);
        DBHelper dbHelper = new DBHelper(this);

        dbProject.open();
        dbProject.dropTableProject();
        dbProject.createProjectTable();
        dbProject.open();

        dbCategorie.openDatabase();
        dbCategorie.dropTableCategorie();
        dbCategorie.createCategorieTable();
        dbCategorie.closeDatabase();

        dbHelper.open();
        dbHelper.dropTableProject();
        dbHelper.createProjectTable();
        dbHelper.close();
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


    //LOGIN
    public void login() {
        Log.d(TAG, "Login");

        //Check if save user in DB
        if(this.ckLogin.isChecked()){
            this.save_user = true;
        }


        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String strResponse) {
                try {
                    String name="", address="", email="";
                    int mobile_number=0;

                    boolean success = true;

                    System.out.print("RESPOSTA: "+strResponse);
                    JSONArray jsonResponse = new JSONArray(strResponse);

                    if (success) {
                        User u = new User();

                        for(int i=0; i < jsonResponse.length(); i++) {
                            JSONObject jsonobject = jsonResponse.getJSONObject(i);
                            u.setName(jsonobject.getString("name"));
                            u.setEmail(jsonobject.getString("email"));
                            u.setPassword(jsonobject.getString("password"));
                            u.setAddress(jsonobject.getString("address"));
                            u.setMobile_number(jsonobject.getInt("mobile_number"));
                            u.setLogin_updated_at((int) System.currentTimeMillis());
                        }

                        //Save user session
                        UserSessionData.NAME = u.getName();
                        UserSessionData.EMAIL = u.getEmail();
                        UserSessionData.ADDRESS = u.getAddess();
                        UserSessionData.MOBILE_NUMBER = u.getMobile_number();

                        if(UserSessionData.MOBILE_NUMBER != 0){
                            if(save_user)
                                saveUserInDB(u);
                            startActivity(new Intent(LoginActivity.this, ProjectsActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                            userNotExists();
                        }

                    } else {
                        //AlertDialog
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        LoginRequest loginRequest = new LoginRequest(email, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    Boolean saveUserInDB(User user){
        try {
            dbUser.open();
            dbUser.insertUser(user);
            dbUser.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            dbUser.close();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public void userNotExists() {
        Toast.makeText(getBaseContext(), "Usuário não cadastrado!", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}