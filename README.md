# 🧠 Memo Kids – Educational Learning App

Memo Kids is an engaging and interactive educational Android app designed for kids to learn fundamental concepts like alphabets, numbers, colors, and animals through fun visuals and activities.

This app was created during my Android Development Internship to help kids develop cognitive skills through a playful and intuitive experience.

## 🎯 Features

- 🎨 Learn Cartoons
- 🔢 Learn Imagination power
- 🐾 Recognize Animals, Fruits, and Colors
- 🗣️ Pronunciation support
- 👶 Simple and colorful UI for kids
- 🎮 Fun quizzes and learning games (if implemented)
- 📱 Works on all Android screen sizes

## 🛠️ Tech Stack

- **Platform:** Android (Java / Kotlin)
- **IDE:** Android Studio
- **Design:** XML layouts, vector graphics


## 📱 Screenshots



           
1.Main Page (Splash Screen/Launcher Activity):
This is the initial screen displayed when the app is launched. It likely shows the game's title ("MemoKids"), a logo, and might briefly display before automatically navigating to the LevelSelectionActivity
2.LevelSelectionActivity: This page serves as the main menu where players select which memory puzzle level (e.g., Level 1, Level 2, Level 3) they want to play, showing which levels are unlocked.
                      

3.MainActivity (Level 1): This is the first game level, presenting an 8-pair memory matching puzzle with a 60-second timer and 30 moves limit. Players match pairs to complete the level.

4.Moves Out Of / Run Out Of Page (Game Over Dialog): This isn't a separate Activity, but rather a dialog that appears within a level (e.g., MainActivity, Level2, Level3) when the player exceeds the allowed number of moves or the timer runs out. It informs the player of the game over condition and offers options to "Try Again" or "Back to Levels".





          

7.Congratulations Page (Level Completion Dialog): Similar to the game over dialog, this is a dialog displayed within a level when the player successfully completes it. It congratulates the player and provides options to proceed to the "Next Level" or "Exit App".

## 🚀 Getting Started

### Prerequisites

- Android Studio
- Android SDK
- A physical device or emulator

### Installation

1. Clone the repository:
 
   git clone (https://github.com/srushti2135/Memo-Kids.git)

2. Open the project in Android Studio.

3. Build and run the app on a device or emulator.

📂 Folder Structure

MemoKids/
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── kotlin+java/
│   │   └── com.example.memokids/
│   │       ├── CongratulationsActivity
│   │       ├── ImageAdapter
│   │       ├── Level2
│   │       ├── Level3
│   │       ├── LevelSelectionActivity
│   │       ├── MainActivity
│   │       └── splash_activity
│   ├── res/
│   │   ├── drawable/                # All images and assets
│   │   ├── layout/                  # UI XML files for each screen
│   │   ├── mipmap/                  # App launcher icons
│   │   ├── raw/                     # JSON animation file
│   │   ├── values/                  # Colors, strings, themes
│   │   └── xml/                     # Backup and extraction rules
├── build.gradle (Project level)
├── build.gradle (Module :app)
├── gradle.properties
├── gradle-wrapper.properties
├── local.properties
├── proguard-rules.pro
├── settings.gradle
└── README.md


👩‍💻 Developer
Developed by Srushti More

📫 Email: moresrushti0307@gmail.com
🔗 GitHub: srushti2135
