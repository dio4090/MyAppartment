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
import android.widget.CheckBox;
import android.widget.EditText;

import com.sourcey.myappartment.R;
import com.sourcey.myappartment.database.DBCategorie;
import com.sourcey.myappartment.database.DBHelper;
import com.sourcey.myappartment.database.DBProject;
import com.sourcey.myappartment.model.Categorie;
import com.sourcey.myappartment.model.Project;
import com.sourcey.myappartment.util.Utils;

import java.io.InputStream;
import java.util.ArrayList;

public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "PhotoTest";

    CoordinatorLayout coordinatorLayout;
    Button btnSelectImage;
    Button btnAddProject;
    AppCompatImageView imgView;

    EditText etProjectName;
    EditText etProjectDescription;

    CheckBox checkBoxSuggestion;
    CheckBox checkBoxReform;
    CheckBox checkBoxImprovement;

    DBHelper dbHelper;
    DBProject dbProject;
    DBCategorie dbCategorie;

    Project project;
    Categorie categorie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        etProjectName = (EditText) findViewById(R.id.etProjectName);
        etProjectDescription = (EditText) findViewById(R.id.etProjectDescription);

        btnSelectImage = (Button) findViewById(R.id.btnSelectImage);
        btnAddProject = (Button) findViewById(R.id.btnAddProject);
        imgView = (AppCompatImageView) findViewById(R.id.imgView);

        checkBoxImprovement = (CheckBox) findViewById(R.id.checkbox_improvement);
        checkBoxReform = (CheckBox) findViewById(R.id.checkbox_reform);
        checkBoxSuggestion = (CheckBox) findViewById(R.id.checkbox_suggestion);

        btnSelectImage.setOnClickListener(this);
        btnAddProject.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        dbProject = new DBProject(this);
        dbCategorie = new DBCategorie(this);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSelectImage:
                openImageChooser();
                break;

            case R.id.btnAddProject:

                Categorie cat = new Categorie();
                Project project = new Project();

                cat.setName(getProjectCategory());

                if(dbCategorie.insertCategorie(cat)){
                    project = setProject(project);
                    dbProject.insertProject(project);
                }

                startActivity(new Intent(AddProjectActivity.this, ProjectsActivity.class));
                break;
        }
    }

    public Project setProject(Project p) {
        p.setCategorie_id(dbCategorie.getLastCategorieId());
        p.setName(etProjectName.getText().toString());
        p.setDescription(etProjectDescription.getText().toString());
        p.setImage_id(dbHelper.retreiveLastImageFromDB());
        p.setIs_enabled(true);
        return p;
    }

    public String getProjectCategory() {
        if(checkBoxImprovement.isChecked())
            return "Melhoria";

        if(checkBoxReform.isChecked())
            return "Reforma";

        if(checkBoxSuggestion.isChecked())
            return "Sugestão";

        return "Sem Categoria";
    }


    // Show simple message using SnackBar
    void showMessage(String message) {
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
