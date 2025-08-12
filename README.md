# MediMom

**MediMom** is an Android application built with Kotlin, designed to help parents track their children's health. It allows you to monitor body temperature, manage medication intake, and keep a clear and organized health history for each child.

## 📱 Screenshots

<img src="screenshots/screenshot_01.png" width="160" alt="Children screen" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="screenshots/screenshot_02.png" width="160" alt="Sickness screen" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="screenshots/screenshot_03.png" width="160" alt="Daily routine screen" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="screenshots/screenshot_04.png" width="160" alt="Statistic screen" />

## ✨ Features

- 👶 Manage profiles for multiple children
- 🌡️ Log body temperature measurements
- 💊 Track medication usage
- 📆 Group data by date and child
- 📷 Attach photos to children and medicines
- 🔍 Filter and browse history easily
- 📊 View structured and categorized health data

## 🛠 Tech Stack

- **Kotlin**
- **Jetpack Components**: ViewModel, LiveData, Room, Navigation
- **Room Database** for local persistence
- **Coroutines** for async operations
- **Material Design UI**
- **ActivityResult API** for image selection

## 📁 Project Structure

app/  
  ├── data/ &nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # Database, repositories<br>
  ├── entity/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # Room entities<br>
  ├── fragment/ &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # UI fragments<br>
  ├── adapter/ &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # RecyclerView adapters<br>
  ├── viewmodel/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # ViewModel classes<br>
  ├── utils/ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # Utility classes and helpers<br>
  └── info/ &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; # Display models (DTOs)<br>

## Privacy Policy

This application respects your privacy. You can read the full privacy policy here:

👉 [Privacy Policy](https://maksimtest.github.io/KotlinMediMom/privacy-policy.html)

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/maksimtest/MediMom.git
   ```
2. Open the project in Android Studio Arctic Fox or newer.
3. Let Gradle sync and build the project.
4. Run the app on an emulator or Android device.
