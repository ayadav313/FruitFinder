package com.example.fruitfinder;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class BasketActivity extends AppCompatActivity {
    private ListView savedFruitsListView;
    private SavedFruitAdapter savedFruitAdapter;
    private ArrayList<String> savedFruits;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        savedFruitsListView = findViewById(R.id.savedFruitsListView);

        // Initialize the data for the saved fruits list
        savedFruits = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        db.collection("users")
                .document(userId)
                .collection("fruits")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                savedFruits.add((String)document.getData().get("name"));
                            }
                            // Create and set the custom adapter
                            savedFruitAdapter = new SavedFruitAdapter(BasketActivity.this, savedFruits);
                            savedFruitsListView.setAdapter(savedFruitAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Button backButton = findViewById(R.id.basketBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class SavedFruitAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final ArrayList<String> savedFruits;

        public SavedFruitAdapter(Context context, ArrayList<String> savedFruits) {
            super(context, R.layout.saved_fruit_item, savedFruits);
            this.context = context;
            this.savedFruits = savedFruits;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View listItemView = convertView;

            if (listItemView == null) {
                listItemView = inflater.inflate(R.layout.saved_fruit_item, parent, false);
            }

            // Get the views from the saved fruit item layout
            TextView fruitNameTextView = listItemView.findViewById(R.id.fruitNameTextView);
            Button infoButton = listItemView.findViewById(R.id.basketInfoButton);
            Button deleteButton = listItemView.findViewById(R.id.basketDeleteButton);

            // Set data to the views
            String currentFruit = savedFruits.get(position);
            fruitNameTextView.setText(currentFruit);

            // Set click listeners for buttons
            infoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Redirect user to FruitInformationActivity with the corresponding fruit name
                    Intent intent = new Intent(context, FruitInformationActivity.class);
                    intent.putExtra("fruitName", currentFruit);
                    context.startActivity(intent);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove fruit from DB
                    // TODO: Implement the code to remove the fruit from the database
                    remove(currentFruit);
                    notifyDataSetChanged();
                }
            });

            return listItemView;
        }
    }

}