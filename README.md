# To-Do App with Firebase

A simple To-Do list application for Android, built with Jetpack Compose and Firebase. This project is a learning exercise to demonstrate and master modern Android development concepts, including MVVM architecture, Firebase integration for backend services, and a reactive UI.

**Current Status:** The application currently features a complete user authentication flow (Sign Up and Sign In). The next phase of development will implement the core To-Do list functionality.

## Features

### Current Features
* **User Authentication:** Secure user sign-up and sign-in using Firebase Authentication (Email & Password).
* **Clean Architecture:** Follows the MVVM (Model-View-ViewModel) pattern to separate UI, business logic, and data layers.
* **Reactive UI:** Built entirely with Jetpack Compose, with UI state managed by ViewModels using StateFlow.

### Planned Features
* **Cloud-Synced To-Do Lists:** Create, Read, Update, and Delete (CRUD) To-Do items that are saved to Cloud Firestore.
* **User-Specific Data:** Users will only be able to see and manage their own To-Do items, enforced by Firestore Security Rules.
* **Real-time Updates:** The list will update in real-time across devices.

## Screenshots

*(Here you should add screenshots of your application. This is very important for a public repository!)*

| Login Screen                                 | To-Do List Screen (Planned)                                  |
|----------------------------------------------|--------------------------------------------------------------|
| *<img width="240" alt="Image" src="https://github.com/user-attachments/assets/c1dd3663-dbb3-443f-9a19-f58670d1bfb2" />* | *<img width="240" alt="Image" src="https://github.com/user-attachments/assets/c6a1f346-cddd-42b9-b966-b63b6a0f572b" />* |

## Tech Stack & Architecture

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Architecture:** MVVM (Model-View-ViewModel)
* **Asynchronous Programming:** Kotlin Coroutines & Flow
* **Backend:** [Firebase](https://firebase.google.com/)
    * **Authentication:** Firebase Authentication
    * **Database:** Cloud Firestore
* **Dependency Injection:** (Planned: Hilt)

## Setup and Configuration

To run this project yourself, you will need to set up your own Firebase backend.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/your-username/your-repository-name.git](https://github.com/your-username/your-repository-name.git)
    ```

2.  **Create a Firebase Project:**
    * Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.

3.  **Enable Services:**
    * In the Firebase Console, go to **Build > Authentication** and enable the **Email/Password** sign-in provider.
    * Go to **Build > Firestore Database** and create a new database in **Production mode**.

4.  **Register Your App:**
    * In your Firebase project settings, add a new Android app.
    * Use the package name: `com.romit.post`.
    * You can skip the SHA-1 key for now.

5.  **Add Configuration File:**
    * Download the `google-services.json` file provided by Firebase.
    * Place this file in the `app/` directory of the project. **This file is not included in the repository for security reasons.**

6.  **Build and Run:**
    * Open the project in Android Studio, let Gradle sync, and run the app on an emulator or a physical device.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
