package com.example.fruitfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        //TODO: Create Custom ListView to display saved fruits
        // Listview will include:
        // 1. Fruit name
        // 2. Information button
        // 3. Delete button

        Button backButton = findViewById(R.id.basketBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}