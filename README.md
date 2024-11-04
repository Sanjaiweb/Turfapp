# 🌱 TurfApp - Your Turf Booking Companion

Welcome to **TurfApp** - a seamless, user-friendly app for discovering and booking sports turfs! Whether you're looking to reserve a turf for football, cricket, or any other sport, TurfApp makes it easy to view available options, check details, and make bookings in a few taps.

---

## 📱 Features

- 🖼️ **Browse Turfs** - See a list of sports turfs, complete with images, descriptions, prices, and availability status.
- 📍 **Turf Details** - Access detailed information about each turf, including location, amenities, and rates.
- 📅 **Booking** - Easily book a turf if it's available, with a smooth transition to the booking confirmation page.
- 🔐 **User Authentication** - Users can register and log in securely to book turfs and access personalized features.
- 🔗 **Firebase Integration** - Utilize Firebase for data storage and authentication, allowing for real-time updates to turf availability and user booking data.

---

## 🛠️ Tech Stack

- **Android Studio** - Java for the frontend, with a responsive and modern UI.
- **Firebase** - For backend functionality, including real-time database updates and authentication.
- **Glide** - For efficient image loading and caching.

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/TurfApp.git
cd TurfApp
```

### 2. Set Up Firebase

To connect your app with Firebase, follow these steps:

1. Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
2. **Add an Android app** in the Firebase project settings. Use your app's package name (`com.example.turfapp`).
3. Download the `google-services.json` file and place it in the `app` directory.
4. Enable **Firebase Realtime Database** and **Firebase Authentication** in your Firebase Console to store and retrieve turf details, booking statuses, and user accounts.

### 3. Add Firebase Dependencies

Add the following dependencies to your `build.gradle` files:

- **Project-level `build.gradle`**:
  ```gradle
  classpath 'com.google.gms:google-services:4.3.8'
  ```

- **App-level `build.gradle`**:
  ```gradle
  implementation 'com.google.firebase:firebase-database:20.0.3'
  implementation 'com.google.firebase:firebase-auth:21.0.1'
  apply plugin: 'com.google.gms.google-services'
  ```

Sync the project to ensure Firebase is integrated correctly.

### 4. Add Dependencies for Glide

To load images smoothly, add Glide:

```gradle
implementation 'com.github.bumptech.glide:glide:4.12.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
```

---

## 📂 Project Structure

Here's a quick overview of the key files in the project:

```plaintext
TurfApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/turfapp/
│   │   │   │   ├── MainActivity.java            # Entry point - lists turfs
│   │   │   │   ├── TurfDetailsActivity.java     # Displays detailed turf info
│   │   │   │   ├── TurfBookingActivity.java     # Handles the booking process
│   │   │   │   ├── AuthActivity.java            # Handles user login and registration
│   │   │   │   ├── models/
│   │   │   │   │   └── Turf.java                # Turf model class
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_main.xml        # Main screen layout
│   │   │   │   │   ├── activity_turf_details.xml # Turf details layout
│   │   │   │   │   ├── activity_auth.xml        # Authentication layout
│   │   │   │   │   ├── activity_turf_booking.xml # Booking confirmation layout
│   │   │   │   ├── drawable/
│   │   │   │   │   └── rounded_corner.xml       # Rounded corner drawable for UI
├── build.gradle
└── google-services.json                         # Firebase configuration file
```

---

## 🛠️ How It Works

1. **MainActivity**: Displays a list of available turfs using RecyclerView. Each item shows a thumbnail, name, location, and price.
2. **TurfDetailsActivity**: Shows detailed information about a selected turf, including an image (loaded with Glide), price, description, and availability status.
3. **AuthActivity**: Manages user registration and login, allowing users to securely access booking features.
4. **TurfBookingActivity**: Allows users to confirm the booking. If the turf is unavailable, they’ll see a message saying "Currently Unavailable."

### Firebase Integration 🌐

- **Realtime Database**: Stores turf data, including name, location, description, price, availability, and image URL.
- **Authentication**: Firebase Authentication is used to manage user login and registration, providing a secure and personalized booking experience.

---

## 📸 Screenshots
- **User Authentication** - Login and registration screens
- ![Register](https://github.com/user-attachments/assets/1d3e8ad8-1634-436d-95e5-f57a934bed1e)
- ![Login](https://github.com/user-attachments/assets/5fde70d9-4bf8-4b5a-9aa7-680da6aefaad)
  
- **Home Screen** - List of available turfs
- ![Home Page ](https://github.com/user-attachments/assets/59dbb257-f6ba-4052-a056-e0c054c4c3b1)

 
- **Adding Turf** - Add Turf with Images.
- ![Adding Turf](https://github.com/user-attachments/assets/91b0fd7f-a3ce-47c3-bd39-1e25257b5a9c)


- **Turf Details** - Description, price, and image of the turf
- ![Turf Details](https://github.com/user-attachments/assets/5331b5d4-1f76-4c9d-94b9-035d8c0b7046)


- **Booking Page** - Confirmation of booking details
- 


---

## 📝 Future Enhancements

- **Booking History** 📅: Track user bookings and add a history feature.
- **Push Notifications** 🔔: Notify users of booking confirmations or changes in availability.
- **Payment Integration** 💳: Integrate with a payment gateway for booking payments.

---

## 🌈 UI & UX Highlights

- **Rounded Corners** 🟦: Designed for a modern look using rounded corner shapes.
- **Custom Button Styles** 🔘: Styled the booking button with green background and white text.
- **Glide Image Loading** 📷: Glide is used for smooth image loading, with a fallback image if the URL is missing.

---

## 🐛 Troubleshooting

- **Firebase Connection Issues**: Ensure `google-services.json` is placed in the `app` folder and synced.
- **Image Loading**: Check internet permissions in `AndroidManifest.xml` for loading images via Glide.
- **Database Rules**: Update Firebase Database rules to allow read/write access for testing:
  ```json
  {
    "rules": {
      ".read": "auth != null",
      ".write": "auth != null"
    }
  }
  ```

> Remember to secure your rules before deploying to production.

---

## 💻 Contributing

Contributions are welcome! Feel free to fork the project, make improvements, and submit a pull request. Let’s make TurfApp even better together! 🙌

---

## 📜 License

This project is licensed under the MIT License.

---

Enjoy using TurfApp! 🏆 Book your turf with ease and stay active!
