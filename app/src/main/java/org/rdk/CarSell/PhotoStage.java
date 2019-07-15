package org.rdk.CarSell;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

public class PhotoStage extends AppCompatActivity implements View.OnClickListener {

    ImageButton carPhoto1;
    ImageButton carPhoto2;
    ImageButton carPhoto3;
    ImageButton carPhoto4;

    File finalFile1 = null;
    File finalFile2 = null;
    File finalFile3 = null;
    File finalFile4 = null;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_stage);
        // Making that our app have only portrait mode
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar = (ProgressBar) findViewById(R.id.progressLoader);
        progressBar.setVisibility(View.GONE);

        String phoneNumber = null;
        String userName    = null;
        String carMark     = null;
        String carModel    = null;
        String carYear     = null;
        String describeCar = null;
        Boolean isDamaged  = null;
        String damaging    = "Nie";

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userName    = extras.getString("userName");
            phoneNumber = extras.getString("phoneNumber");
            carMark     = extras.getString("carMark");
            carModel    = extras.getString("carModel");
            carYear     = extras.getString("carYear");
            describeCar = extras.getString("describeCar");
            isDamaged   = extras.getBoolean("isDamaged");
        }
        if(isDamaged) {
            damaging = "Tak";
        }
        final String number      = phoneNumber;
        final String name        = userName;
        final String mark        = carMark;
        final String model       = carModel;
        final String year        = carYear;
        final String description = describeCar;
        final String damaged     = damaging;

        carPhoto1 = findViewById(R.id.carPhoto1);
        carPhoto2 = findViewById(R.id.carPhoto2);
        carPhoto3 = findViewById(R.id.carPhoto3);
        carPhoto4 = findViewById(R.id.carPhoto4);
        Button lastBtn = findViewById(R.id.lastBtn);
        final TextView textView = findViewById(R.id.textView);

        carPhoto1.setOnClickListener(this);
        carPhoto2.setOnClickListener(this);
        carPhoto3.setOnClickListener(this);
        carPhoto4.setOnClickListener(this);

        lastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isReadStoragePermissionGranted();
                isWriteStoragePermissionGranted();

                if(finalFile1==null || finalFile2==null || finalFile3==null ||finalFile4==null
                        || sameFiles(finalFile1.toString(), finalFile2.toString(), finalFile3.toString(), finalFile4.toString())==true){

                    textView.setTextColor(Color.RED);
                    textView.setText("Proszę dodać cztery zdjęcia \nPamiętaj że zdjecia muszą być różne! ");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {

                    public void run() {
                        try {
                            GMailSender sender = new GMailSender(
                                    "xx@gmail.com",
                                    "xx");
                            sender.sendMail(
                                    "Samochód na sprzedaż",
                                    "Imię: "+ name +"\nNumer telefonu: "+number+
                                    "\nMarka samochodu: "+mark + "\nModel samochodu: "+model+
                                    "\nRok: " + year + "\nUszkodzony?: " + damaged+
                                    "\nOpis: "+description,
                                    "konradskupaut@gmail.com",
                                    "konradskupaut@gmail.com",
                                    finalFile1.toString(), finalFile2.toString(), finalFile3.toString(), finalFile4.toString());
                        } catch (Exception e) {
                        }
                    }
                }).start();
                Intent lastStage = new Intent(getApplicationContext(), LastStage.class);
                startActivity(lastStage);
            }
        });
    }
    public static final int PICK_IMAGE1 = 1;
    public static final int PICK_IMAGE2 = 2;
    public static final int PICK_IMAGE3 = 3;
    public static final int PICK_IMAGE4 = 4;

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.carPhoto1:
                Intent galleryIntent1=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent1.setType("image/*");
                startActivityForResult(galleryIntent1, PICK_IMAGE1);
                break;
            case R.id.carPhoto2:
                Intent galleryIntent2=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent2.setType("image/*");
                startActivityForResult(galleryIntent2, PICK_IMAGE2);
                break;
            case R.id.carPhoto3:
                Intent galleryIntent3=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent3.setType("image/*");
                startActivityForResult(galleryIntent3, PICK_IMAGE3);
                break;
            case R.id.carPhoto4:
                Intent galleryIntent4=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent4.setType("image/*");
                startActivityForResult(galleryIntent4, PICK_IMAGE4);
                break;
        }
    }
        @Override
        protected void onActivityResult(int reqCode, int resultCode, Intent data) {
            super.onActivityResult(reqCode, resultCode, data);
            if(resultCode==RESULT_OK && data!=null){
                if(reqCode==PICK_IMAGE1){
                    Uri firstImageUri = data.getData();
                    carPhoto1.setImageURI(firstImageUri);

                    finalFile1 = new File(getRealPathFromURI(firstImageUri));
                }
                if(reqCode==PICK_IMAGE2){
                    Uri secImageUri = data.getData();
                    carPhoto2.setImageURI(secImageUri);

                    finalFile2 = new File(getRealPathFromURI(secImageUri));
                }
                if(reqCode==PICK_IMAGE3){
                    Uri thrdImageUri = data.getData();
                    carPhoto3.setImageURI(thrdImageUri);

                    finalFile3 = new File(getRealPathFromURI(thrdImageUri));
                }
                if(reqCode==PICK_IMAGE4){
                    Uri fourImageUri = data.getData();
                    carPhoto4.setImageURI(fourImageUri);

                    finalFile4 = new File(getRealPathFromURI(fourImageUri));
                }
            }
        }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    public boolean sameFiles(String val1, String val2, String val3, String val4) {
        String[] values = {val1, val2, val3, val4};

        Boolean x=false;
        for(int i=0; i<values.length; i++) {
            for (int j=i+1; j < values.length - 1; j++) {
                if (values[i] == values[j]) {
                    x = true;
                }
            }
        }
        return x;
    }

}
