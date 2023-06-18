package com.example.fruitfinder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FruitInformationActivity extends AppCompatActivity {

    private TextView fruitNameTextView;
    private TextView familyTextView;
    private TextView orderTextView;
    private TextView genusTextView;
    private TextView caloriesTextView;
    private TextView fatTextView;
    private TextView sugarTextView;
    private TextView carbohydratesTextView;
    private TextView proteinTextView;
    private TextView errorTextView;
    private Button saveButton;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_information);

        // Retrieve the fruit name from the intent extras
        String fruitName = getIntent().getStringExtra("fruitName");

        // Construct the API_URL using the fruit name
        String apiUrl = "https://www.fruityvice.com/api/fruit/" + fruitName;

        // Find the TextViews by their IDs
        fruitNameTextView = findViewById(R.id.fruitNameTextView);
        familyTextView = findViewById(R.id.familyTextView);
        orderTextView = findViewById(R.id.orderTextView);
        genusTextView = findViewById(R.id.genusTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        fatTextView = findViewById(R.id.fatTextView);
        sugarTextView = findViewById(R.id.sugarTextView);
        carbohydratesTextView = findViewById(R.id.carbohydratesTextView);
        proteinTextView = findViewById(R.id.proteinTextView);

        errorTextView = findViewById(R.id.errorTextView);
        errorTextView.setVisibility(View.GONE);

        // Find buttons by IDs
        Button backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveButton.setVisibility(View.GONE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

                Map<String, Object> fruitData = new HashMap<>();
                fruitData.put("name", fruitNameTextView.getText().toString());
                fruitData.put("family", familyTextView.getText().toString());
                fruitData.put("order", orderTextView.getText().toString());
                fruitData.put("genus", genusTextView.getText().toString());
                fruitData.put("calories", caloriesTextView.getText().toString());
                fruitData.put("fat", fatTextView.getText().toString());
                fruitData.put("sugar", sugarTextView.getText().toString());
                fruitData.put("carbohydrates", carbohydratesTextView.getText().toString());
                fruitData.put("protein", proteinTextView.getText().toString());

                // Create a new document reference for the fruit information in the user's collection
                DocumentReference documentRef = db.collection("users").document(userId)
                        .collection("fruits").document();

                // Save the fruit data to Firestore
                documentRef.set(fruitData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Show a success message or perform any additional actions
                                Log.d(TAG, "Fruit information saved successfully");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Show an error message or handle the failure
                                Log.e(TAG, "Error saving fruit information", e);
                            }
                        });
            }
        });

        // Call the API with the constructed URL
        new FetchFruitInformationTask().execute(apiUrl);

    }

    private class FetchFruitInformationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String API_URL = urls[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String fruitInformationJson = null;

            try {
                URL url = new URL(API_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream == null) {
                    return null;
                }

                StringBuilder stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                if (stringBuilder.length() == 0) {
                    return null;
                }

                fruitInformationJson = stringBuilder.toString();
            } catch (IOException e) {
                Log.e("FruitInformation", "Error fetching fruit information", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("FruitInformation", "Error closing reader", e);
                    }
                }
            }

            return fruitInformationJson;
        }

        @Override
        protected void onPostExecute(String fruitInformationJson) {
            super.onPostExecute(fruitInformationJson);

            if (fruitInformationJson != null) {
                try {
                    // Parse the JSON response
                    JSONObject infoJson = new JSONObject(fruitInformationJson);

                    // Extract the fruit information
                    String name = infoJson.getString("name");
                    String family = infoJson.getString("family");
                    String order = infoJson.getString("order");
                    String genus = infoJson.getString("genus");
                    int calories = infoJson.getJSONObject("nutritions").getInt("calories");
                    double fat = infoJson.getJSONObject("nutritions").getDouble("fat");
                    double sugar = infoJson.getJSONObject("nutritions").getDouble("sugar");
                    double carbohydrates = infoJson.getJSONObject("nutritions").getDouble("carbohydrates");
                    double protein = infoJson.getJSONObject("nutritions").getDouble("protein");

                    // Set the fruit information in the TextViews
                    fruitNameTextView.setText(name);
                    familyTextView.setText("Family: " + family);
                    orderTextView.setText("Order: " + order);
                    genusTextView.setText("Genus: " + genus);
                    caloriesTextView.setText("Calories: " + calories);
                    fatTextView.setText("Fat: " + fat);
                    sugarTextView.setText("Sugar: " + sugar);
                    carbohydratesTextView.setText("Carbohydrates: " + carbohydrates);
                    proteinTextView.setText("Protein: " + protein);

                    saveButton.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    Log.e("FruitInformation", "Error parsing JSON", e);
                }
            } else {
                // Show the error message and hide the save button
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText("Error fetching fruit information");
            }
        }
    }
}
