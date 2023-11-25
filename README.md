# FruitFinder Android App

The FruitFinder app is an Android application designed to identify and analyze fruits using Firebase ML Kit's image processing capabilities. Users can capture images via the device's camera and receive information about the identified fruits. Additionally, the app allows users to view saved fruits in their "basket" and fetch detailed information about specific fruits from an external API.

### Technologies & Skills Showcased

- **Android Development:** Utilizing Java for Android app development.
- **Firebase Services:** Incorporating Firebase Authentication and Firestore for user authentication and database management.
- **Firebase ML Kit:** Integrating ML Kit for image processing and object recognition in real-time.
- **Google Sign-In:** Implementing Google Sign-In API for user authentication.
- **REST API Integration:** Fetching external data from RESTful APIs for fruit information retrieval.
- **Asynchronous Tasks:** Employing AsyncTask for handling background tasks and API calls.
- **Firebase UI Components:** Utilizing Firebase UI components for streamlined user authentication.
- **Firebase Database Queries:** Making queries to Firebase Firestore for data retrieval and storage.
- **User Interface Design:** Designing and implementing user interfaces using Android XML layouts and views.
- **ListViews & Adapters:** Implementing custom adapters for ListViews to display fetched data.


### Getting Started
To run this application locally, follow these steps:

1. **Clone Repository:**
   ```bash
   git clone https://github.com/your_username/FruitFinder.git
   ```

2. **Set Up Firebase:**
   - Create a Firebase project on the [Firebase Console](https://console.firebase.google.com/).
   - Add your `google-services.json` file to the `app/` directory.

3. **Run the App:**
   - Open the project in Android Studio.
   - Connect your Android device or use an emulator.
   - Build and run the app.

### Functionalities Overview

- **Camera Functionality:** Capture images of fruits through the app's camera feature, initiating Firebase ML Kit's image analysis for fruit recognition.
- **BasketActivity:** View saved fruits in a list and perform actions like viewing detailed information or deleting items from the list. The list is fetched from Firebase Firestore based on user data.
- **FruitInformationActivity:** Fetch detailed information about specific fruits from an external API using REST calls. Users can also save this information to their Firestore database.
- **LoginActivity:** Handles user authentication via Google accounts using Firebase authentication services.

### Contributing
Contributions to enhance the app's functionalities or fix issues are welcome. Please submit a pull request.

### License
This project is licensed under the [MIT License](LICENSE).

README.md file created with assistance from ChatGPT by [ayadav313](https://github.com/ayadav313)
