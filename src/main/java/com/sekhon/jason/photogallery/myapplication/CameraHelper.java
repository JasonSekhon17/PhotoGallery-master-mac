package com.sekhon.jason.photogallery.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CameraHelper  extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;

    public DateFormat sDateFormat;
    public DateFormat sDateParse;
    public Date boolDate;

    public CameraHelper() {
        sDateParse = new SimpleDateFormat("yyyyMMdd");
        sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date boolDate = new Date();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getCurrentPhotoIndex() {
        return currentPhotoIndex;
    }

    public void setCurrentPhotoIndex(int currentPhotoIndex) {
        this.currentPhotoIndex = currentPhotoIndex;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    public ArrayList<String> populateGallery(Date minDate, Date maxDate){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.sekhon.jason.photogallery/files/Pictures");
        ArrayList<String> photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            if(minDate != boolDate || maxDate != boolDate) {
                for (File f : file.listFiles()) {
                    Date temp = parseDate(f.getPath());
                    Log.d("COMPARE DATE", minDate.toString() + ":::" + maxDate.toString() + ":::" + temp.toString());
                    if (minDate.compareTo(temp) * temp.compareTo(maxDate) >= 0)
                        photoGallery.add(f.getPath());
                }
            } else{
                for (File f : file.listFiles()) {
                    photoGallery.add(f.getPath());
                }
            }
        }
        return photoGallery;
    }

    public Date parseDate(String path){
        if (path == null)
            return null;
        try {
            if (path.split("_").length > 1)
                return sDateParse.parse(path.split("_")[1]);
            else return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void displayPhoto(String path, ImageView iv, TextView imageDate) {
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        Date temp = parseDate(path);
        if (temp != null)
            imageDate.setText(sDateFormat.format(temp));
    }

    public void takePicture(View v, Activity a) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(a);
            } catch (IOException ex) {
                Log.d("FileCreation", "Failed");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sekhon.jason.photogallery.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                a.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public File createImageFile(Activity a) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;
    }
}
