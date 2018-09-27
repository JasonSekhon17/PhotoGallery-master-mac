package com.sekhon.jason.photogallery.myapplication;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sekhon.jason.photogallery.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private ArrayList<String> photoGallery;
    private Date boolDate = new Date();
    private TextView imageDate;
    private TextView filterDate;
    private DateFormat sDateFormat;
    private DateFormat sDateParse;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        Button btnFilter = (Button)findViewById(R.id.btnFilter);
        imageDate = (TextView)findViewById(R.id.imageDate);
        filterDate = (TextView)findViewById(R.id.filterDate);
        iv = (ImageView) findViewById(R.id.ivMain);
        sDateParse = new SimpleDateFormat("yyyyMMdd");
        sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnFilter.setOnClickListener(filterListener);

        photoGallery = populateGallery(boolDate, boolDate);
        Log.d("onCreate, size", Integer.toString(photoGallery.size()));
        if (photoGallery.size() > 0)
            currentPhotoPath = photoGallery.get(currentPhotoIndex);
        displayPhoto(currentPhotoPath);
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    private ArrayList<String> populateGallery(Date minDate, Date maxDate){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.sekhon.jason.photogallery/files/Pictures");
        photoGallery = new ArrayList<String>();
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
    private Date parseDate(String path){
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
    private void displayPhoto(String path) {
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        Date temp = parseDate(path);
        if (temp != null)
            imageDate.setText(sDateFormat.format(temp));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onClick( View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                --currentPhotoIndex;
                break;
            case R.id.btnRight:
                ++currentPhotoIndex;
                break;
            default:
                break;
        }
        if (photoGallery.size() > 0) {
            if (currentPhotoIndex < 0)
                currentPhotoIndex = 0;
            if (currentPhotoIndex >= photoGallery.size())
                currentPhotoIndex = photoGallery.size() - 1;

            currentPhotoPath = photoGallery.get(currentPhotoIndex);
            Log.d("phpotoleft, size", Integer.toString(photoGallery.size()));
            Log.d("photoleft, index", Integer.toString(currentPhotoIndex));
            Log.d("photoPath, name", currentPhotoPath);
            displayPhoto(currentPhotoPath);
        }
    }

    /*
    public void goToSettings(View v) {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void goToDisplay(String x) {
        Intent i = new Intent(this, DisplayActivity.class);
        i.putExtra("DISPLAY_TEXT", x);
        startActivity(i);
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", data.getStringExtra("STARTDATE"));
                Log.d("createImageFile", data.getStringExtra("ENDDATE"));
                try {
                    Date minDate = sDateFormat.parse(data.getStringExtra("STARTDATE"));
                    Date endDate = sDateFormat.parse(data.getStringExtra("ENDDATE"));
                    filterDate.setText(sDateFormat.format(minDate) + '\n' + sDateFormat.format(endDate));
                    photoGallery = populateGallery(minDate, endDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("onCreate, size", Integer.toString(photoGallery.size()));
                currentPhotoIndex = 0;
                if (photoGallery.size() > 0) {
                    currentPhotoPath = photoGallery.get(currentPhotoIndex);
                    displayPhoto(currentPhotoPath);
                } else {
                    iv.setImageBitmap(null);
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("createImageFile", "Picture Taken");
                photoGallery = populateGallery(boolDate, boolDate);
                currentPhotoIndex = 0;
                currentPhotoPath = photoGallery.get(currentPhotoIndex);
                displayPhoto(currentPhotoPath);
            }
        }
    }

    public void takePicture(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d("FileCreation", "Failed");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sekhon.jason.photogallery.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        Log.d("createImageFile", currentPhotoPath);
        return image;
    }
}
