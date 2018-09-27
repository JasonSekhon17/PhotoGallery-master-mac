package com.sekhon.jason.photogallery.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sekhon.jason.photogallery.R;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    //
    private ArrayList<String> photoGallery;
    //
    private TextView imageDate;
    private TextView filterDate;
    private ImageView iv;
    private CameraHelper cameraHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CameraHelper
        cameraHelper = new CameraHelper();
        //UI elements
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        Button btnFilter = (Button)findViewById(R.id.btnFilter);
        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        imageDate = (TextView)findViewById(R.id.imageDate);
        filterDate = (TextView)findViewById(R.id.filterDate);
        iv = (ImageView) findViewById(R.id.ivMain);
        //Listeners
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnFilter.setOnClickListener(filterListener);
        btnCamera.setOnClickListener(cameraListener);
        //
        photoGallery = cameraHelper.populateGallery(cameraHelper.boolDate, cameraHelper.boolDate);
        if (photoGallery.size() > 0)
            cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
        cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate);
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        public void onClick(View v) {
            cameraHelper.takePicture(v, MainActivity.this);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClick( View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                cameraHelper.setCurrentPhotoIndex(cameraHelper.getCurrentPhotoIndex()-1);
                break;
            case R.id.btnRight:
                cameraHelper.setCurrentPhotoIndex(cameraHelper.getCurrentPhotoIndex()+1);
                break;
            default:
                break;
        }
        if (photoGallery.size() > 0) {
            if (cameraHelper.getCurrentPhotoIndex() < 0)
                cameraHelper.setCurrentPhotoIndex(0);
            if (cameraHelper.getCurrentPhotoIndex() >= photoGallery.size())
                cameraHelper.setCurrentPhotoIndex(photoGallery.size() - 1);

            cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
            cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Date minDate = cameraHelper.sDateFormat.parse(data.getStringExtra("STARTDATE"));
                    Date endDate = cameraHelper.sDateFormat.parse(data.getStringExtra("ENDDATE"));
                    filterDate.setText(cameraHelper.sDateFormat.format(minDate) + '\n' + cameraHelper.sDateFormat.format(endDate));
                    photoGallery = cameraHelper.populateGallery(minDate, endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cameraHelper.setCurrentPhotoIndex(0);
                if (photoGallery.size() > 0) {
                    cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
                    cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate);
                } else {
                    iv.setImageBitmap(null);
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                photoGallery = cameraHelper.populateGallery(cameraHelper.boolDate, cameraHelper.boolDate);
                cameraHelper.setCurrentPhotoIndex(0);
                cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
                cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate);
            }
        }
    }
}
