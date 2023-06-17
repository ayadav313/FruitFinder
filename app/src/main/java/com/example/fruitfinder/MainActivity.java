package com.example.fruitfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraButton = findViewById(R.id.cameraButton);
        Button basketButton = findViewById(R.id.basketButton);
        Button logoutButton = findViewById(R.id.logoutButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Debug", "onClick!");
                //TODO: Test openCamera()
                //TODO: Make analyzePicture()
//                openCamera();
//                analyzePicture();
                //TODO: set fruitName from image
                String fruitName = "apple"; // Replace "banana" with the actual fruit name
                Intent intent = new Intent(MainActivity.this, FruitInformationActivity.class);
                intent.putExtra("fruitName", fruitName);
                startActivity(intent);

            }
        });

        basketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Logout of firebase

                finish(); // Optional: finish the current activity to prevent going back to it via the back button
            }
        });
    }

    private void openCamera() {
        Log.d("Debug", "openCamera!");

        // Check if there is a camera app available
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } else {
                Log.d("Debug", "No camera app found.");
            }
        } else {
            Log.d("Debug", "No camera available.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Log.d("Camera", "Result OK!");
        }
    }
}