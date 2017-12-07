package com.sourcey.myappartment.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sourcey.myappartment.R;
import com.sourcey.myappartment.database.DBCategorie;
import com.sourcey.myappartment.database.DBHelper;
import com.sourcey.myappartment.database.DBProject;
import com.sourcey.myappartment.util.Utils;

import java.io.InputStream;

public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "PhotoTest";

    CoordinatorLayout coordinatorLayout;
    Button btnSelectImage;
    Button btnAddProject;
    AppCompatImageView imgView;

    DBHelper dbHelper;
    DBProject dbProject;
    DBCategorie dbCategorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);


        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        btnAddProject = (Button) findViewById(R.id.btnAddProject);
        imgView = (AppCompatImageView) findViewById(R.id.imgView);

        btnSelectImage.setOnClickListener(this);
        btnAddProject.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbProject = new DBProject(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSelectImage:
                openImageChooser();
                break;

            case R.id.btnLoadImage:
                loadImageFromDB();
                break;
        }
    }


    // Show simple message using SnackBar
    void showMessage(String message) {
//        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
//        snackbar.show();
//
        Snackbar.make(findViewById(R.id.add_project_activity),message, Snackbar.LENGTH_LONG).show();
    }

    // Choose an image from Gallery
    void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_PICTURE) {
                Uri selectImageUri = data.getData();
                if(null != selectImageUri) {
                    //Saving to database
                    if(saveImageInDB(selectImageUri)) {
                        showMessage("Image saved in database");
                        imgView.setImageURI(selectImageUri);
                    }
                    // Reading from Database after 3 seconds just to show the message
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(loadImageFromDB()){
                                showMessage("Image loaded from database...");
                            }
                        }
                    }, 3000);
                }

            }
        }
    }

    Boolean saveImageInDB(Uri selectedImageUri){
        try {
            dbHelper.open();
            InputStream iStream = getContentResolver().openInputStream(selectedImageUri);
            byte[] inputData = Utils.getBytes(iStream);
            dbHelper.insertImage(inputData);
            dbHelper.close();
            return true;
        } catch (Exception e) {
            //Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            System.out.print("Error: "+e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }
    }

    Boolean loadImageFromDB() {
        try {
            dbHelper.open();
            byte[] bytes = dbHelper.retreiveImageFromDB();
            dbHelper.close();
            // Show Image from DB in ImageView
            imgView.setImageBitmap(Utils.getImage(bytes));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "<loadImageFromDB> Error : " + e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }
    }
}
