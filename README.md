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

<img width="324" height="652" alt="image" src="https://github.com/user-attachments/assets/ebcfaf5e-2ec0-48a7-99d1-36c5dd10613c" />
<img width="354" height="656" alt="image" src="https://github.com/user-attachments/assets/2b495551-88b6-4914-807a-233c9eba0db1" />


           
1.Main Page (Splash Screen/Launcher Activity):
This is the initial screen displayed when the app is launched. It likely shows the game's title ("MemoKids"), a logo, and might briefly display before automatically navigating to the LevelSelectionActivity
2.LevelSelectionActivity: This page serves as the main menu where players select which memory puzzle level (e.g., Level 1, Level 2, Level 3) they want to play, showing which levels are unlocked.
                      
<img width="356" height="608" alt="image" src="https://github.com/user-attachments/assets/4a327a75-b3e5-4261-8d05-1e3f06673741" />

<img width="347" height="602" alt="image" src="https://github.com/user-attachments/assets/7f07b51b-1f22-4f95-9b43-f74dcd32be86" />

3.MainActivity (Level 1): This is the first game level, presenting an 8-pair memory matching puzzle with a 60-second timer and 30 moves limit. Players match pairs to complete the level.

4.Moves Out Of / Run Out Of Page (Game Over Dialog): This isn't a separate Activity, but rather a dialog that appears within a level (e.g., MainActivity, Level2, Level3) when the player exceeds the allowed number of moves or the timer runs out. It informs the player of the game over condition and offers options to "Try Again" or "Back to Levels".

<img width="341" height="694" alt="image" src="https://github.com/user-attachments/assets/68494467-b85f-4fdc-89ea-059e2d96f772" />

<img width="375" height="698" alt="image" src="https://github.com/user-attachments/assets/3cd30809-8f4e-43c9-ae2d-3c2a67ded1e8" />

<img width="375" height="698" alt="image" src="https://github.com/user-attachments/assets/4210a57b-e3c1-40f9-bcd7-67b4f055b34c" />

5.Level 2 : This page offers the second game level, featuring an 8-pair memory puzzle with different images, a shorter 40-second timer, and a stricter 20-move limit.

<img width="364" height="577" alt="image" src="https://github.com/user-attachments/assets/01f6c772-544c-4025-9650-6107a586ab29" />

<img width="368" height="582" alt="image" src="https://github.com/user-attachments/assets/ae6f3d9c-5ac7-466f-aec8-fb5f62a10d46" />
6.Level 3: (Implied from flow) This page would host the third game level, continuing the memory puzzle challenge with its own set of images and potentially even tougher time/move constraints.
          
<img width="370" height="723" alt="image" src="https://github.com/user-attachments/assets/4dfeb239-3898-4196-b29d-e350746f5fce" />
<img width="321" height="722" alt="image" src="https://github.com/user-attachments/assets/beaa222d-1698-4de2-995c-0ed30a15e883" />

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
