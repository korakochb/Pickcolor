package com.example.pickcolor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    ImageView mImageView;
    ImageView mImageView2;
    TextView mResultTv;
    Button mChooseBtn;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTv = findViewById(R.id.resultTv);
        mImageView = findViewById(R.id.imageView);
        mImageView2 = findViewById(R.id.imageView);
        mChooseBtn = findViewById(R.id.choose_image_btn);

        mImageView.setDrawingCacheEnabled(true);
        mImageView.buildDrawingCache(true);

        mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        requestPermissions(permissions, PERMISSION_CODE);

                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });


        mImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch (View v, MotionEvent event) {

                try {
                    if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        int pixel = bitmap.getPixel((int) event.getX(), (int) event.getY());

                        int r = Color.red(pixel);
                        int g = Color.green(pixel);
                        int b = Color.blue(pixel);

                        String hex = "#" + Integer.toHexString(pixel);

                        String color = getComplementaryColor(pixel);

                        mResultTv.setText("RGB: " + r + "," + g + "," + b + "\nHEX: " + hex + "\nColor: " + color);

                    }
                }catch (Exception e) {
                    System.out.println("Error " + e.getMessage());
                }

                return true;
            }

            public String getComplementaryColor(int colorToInvert) {
                String name = "";
                float hsv[] = new float[3];
                int r = (colorToInvert >> 16) & 0xFF;
                int g = (colorToInvert >> 8) & 0xFF;
                int b = (colorToInvert) & 0xFF;
                Color.RGBToHSV(r, g, b, hsv);

                if (hsv[1] < 0.1 && hsv[2] > 0.9) {
                    name = "White";
                } else if (hsv[2] < 0.1) {
                    name = "Black";
                } else {
                    float deg = hsv[0];

                    if (deg >= 0 && deg < 15) {
                        name = "Red";
                    } else if (deg >= 15 && deg < 45) {
                        name = "Orange";
                    } else if (deg >= 45 && deg < 65) {
                        name = "Yellow";
                    } else if (deg >= 65 && deg < 180) {
                        name = "Green";
                    } else if (deg >= 180 && deg < 210) {
                        name = "Cyan";
                    } else if (deg >= 210 && deg < 270) {
                        name = "Blue";
                    } else if (deg >= 270 && deg < 300) {
                        name = "Purple";
                    } else if (deg >= 300 && deg < 330) {
                        name = "Pink";
                    } else {
                        name = "Red";
                    }
                }

                return name;
            }

        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

    }

    private void pickImageFromGallery() {

        Intent photoPickerintent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();

        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerintent.setDataAndType(data, "image/*");
        startActivityForResult(photoPickerintent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){

            mImageView.setImageURI(data.getData());

        }
    }


}