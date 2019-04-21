package com.scanmyfood.imamf.scanmyfood;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.ibm.watson.developer_cloud.android.library.camera.CameraHelper;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IntentIntegrator qrScan;
    private ImageButton imgBt_qr, imgBt_camera;
    private TextView tv_productName, tv_expired;
    private String productName = "", imgSrc = "";
    private static int REQUEST_CAMERA = 98;

    private CameraHelper camHelper;

    private ConstraintLayout layout_pBar;

    public MainActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //intializing image button
        imgBt_camera = findViewById(R.id.imgBt_camera);
        imgBt_qr = findViewById(R.id.imgBt_qr);

        //initializing textView
        tv_productName = findViewById(R.id.tv_productName);
        tv_expired = findViewById(R.id.tv_expired);

        //initializing layout for progress bar
        layout_pBar = findViewById(R.id.layout_progressBar);
        layout_pBar.setVisibility(View.GONE);


        //intializing qr scan object
        qrScan = new IntentIntegrator(this);


        //initializin IBM Visual Recognition camera helper
        camHelper = new CameraHelper(this);

        //on click scan qr
        imgBt_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();

            }
        });

        //on click scan organic
        imgBt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camHelper.dispatchTakePictureIntent();
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //automatically open camera after permission granted
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        camHelper.dispatchTakePictureIntent();

                    }

                }
                return;

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK){
            //organic scan result
            if(requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE) {
                //get image path after captured
                final File photoFile = camHelper.getFile(resultCode);
                imgRecogntion(photoFile);
            }
            //qr scan result
            else{
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

                if (result != null) {
                    //if qrcode has nothing in it
                    if (result.getContents() == null) {
                        Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
                    } else {
                        //if qr contains data
                        try {
                            //converting the data to json
                            JSONObject obj = new JSONObject(result.getContents());
                            productName = obj.getString("productName");
                            imgSrc = obj.getString("imgSrc");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                        }

                        Intent i = new Intent(MainActivity.this, FoodFact.class);
                        i.putExtra("productName", productName);
                        Log.d("tes", productName+" "+imgSrc);
                        i.putExtra("imgSrc", imgSrc);
                        startActivity(i);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }


    }

    //Object detection using IBM Watson Visual Recognition
    public void imgRecogntion(final File file){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout_pBar.setVisibility(View.VISIBLE);
                    }
                });
                IamOptions options = new IamOptions.Builder()
                        .apiKey(getResources().getString(R.string.api_key))
                        .build();

                VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);

                ClassifyOptions classifyOptions = null;
                try {
                    classifyOptions = new ClassifyOptions.Builder()
                            .imagesFile(file)
                            .classifierIds(Arrays.asList("DefaultCustomModel_1553789228"))
                            .build();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ClassifiedImages result = visualRecognition.classify(classifyOptions).execute();


                Log.d("smf", result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).toString()+"\n"
                +result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getClassName());

                productName = result.getImages().get(0).getClassifiers().get(0).getClasses().get(0).getClassName();
                if (productName.equalsIgnoreCase("Apel")){
                    imgSrc = "https://doktersehat.com/wp-content/uploads/2014/05/apel.jpg";
                }
                else if (productName.equalsIgnoreCase("Kol")){
                    imgSrc = "https://blue.kumparan.com/kumpar/image/upload/fl_progressive,fl_lossy,c_fill,q_auto:best,w_640/v1521360707/raxkgzgrt44iiebd0krw.jpg";
                }



                if (productName != ""){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            layout_pBar.setVisibility(View.GONE);
                        }
                    });
                    Intent i = new Intent(MainActivity.this, FoodFact.class);
                    i.putExtra("productName", productName);
                    i.putExtra("imgSrc", imgSrc);
                    Log.d("tes", productName+" img: "+imgSrc);
                    //i.putExtra("imgSrc", imgSrc);
                    startActivity(i);
                }
            }
        });

    }
}
