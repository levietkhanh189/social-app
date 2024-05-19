# How to Run Source Code on Android Studio

## System Requirements

- **Android Studio**: Latest version (recommended)
- **Java Development Kit (JDK)**: Version 8 or higher
- **Gradle**: Pre-installed with Android Studio
- **Android Device or Emulator**: To run the application

## Step 1: Install Android Studio

1. Download and install [Android Studio](https://developer.android.com/studio).
2. Install JDK if not already installed (Android Studio typically handles this).

## Step 2: Clone the Repository

Open a terminal and run the following command to clone the repository:

```bash
git clone https://github.com/username/repository.git
```

## Step 3: Open the Project in Android Studio

1. Open Android Studio.
2. Select **File** -> **Open**.
3. Navigate to the directory containing the cloned source code and select **Open**.

## Step 4: Set Up Run Configuration

1. Select **Run** -> **Edit Configurations**.
2. Click the plus icon (+) and select **Android App**.
3. Name the configuration and select the main module of your application.
4. Click **OK** to save the configuration.

## Step 5: Connect Device or Start Emulator

1. **Physical Device**: Connect your Android device to your computer via USB and ensure **Developer Mode** and **USB Debugging** are enabled.
2. **Emulator**: Open **AVD Manager** in Android Studio and create/start a new emulator.

## Step 6: Run the Application

1. Select the run configuration from the dropdown menu on the toolbar.
2. Click the **Run** button (or use the shortcut Shift + F10).
3. Wait for the build and installation process to complete; the application will launch on the connected device or emulator.

## Common Issues

- **Gradle sync failed**: Check your internet connection and try again.
- **Cannot resolve symbol**: Verify the library versions in the `build.gradle` file.

---

We hope this guide helps you run the source code on Android Studio easily. If you encounter any issues, please refer to the documentation on [developer.android.com](https://developer.android.com).
