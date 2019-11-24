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
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class  MainActivity extends AppCompatActivity {

    ImageView mImageView;
    TextView mResultTv;
    Button mChooseBtn;
    DatabaseReference reff;
    Model model;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultTv = findViewById(R.id.resultTv);
        mImageView = findViewById(R.id.imageView);
        mChooseBtn = findViewById(R.id.choose_image_btn);
        reff = FirebaseDatabase.getInstance().getReference().child("Model");
        model= new Model();

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
                        model.setX((int)event.getX());
                        model.setY((int)event.getY());
                        model.setPixel(bitmap.getPixel(model.getX(),model.getY()));
                        model.setRed(Color.red(model.getPixel()));
                        model.setGreen(Color.green(model.getPixel()));
                        model.setBlue(Color.blue(model.getPixel()));
                        Model color = new Model();
                        model.setColor(color.getColorNameFromRgb(model.getRed(),model.getGreen(),model.getBlue()));
                        reff.push().setValue(model);
                        Toast.makeText(MainActivity.this,"Data Inserted Successfully",Toast.LENGTH_LONG).show();

                        mResultTv.setText("RGB: " + model.getRed() + "," + model.getGreen() + "," + model.getBlue() + "\nColor: " + model.getColor());

                    }
                }catch (Exception e) {
                    System.out.println("Error " + e.getMessage());
                }

                return true;
            }

        });


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