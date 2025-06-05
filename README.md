# android-homework-maze

# Maze Game

An Android mobile application written in Kotlin that allows users to generate and play through mazes using device motion sensors. Designed for educational purposes and implemented without external libraries.

## Features

- Generate square mazes of custom sizes (10–50).
- Save mazes with custom names using `SharedPreferences`.
- Navigate the maze by tilting the device (accelerometer).
- Path tracking with visual indication of visited tiles.
- Full state preservation on device rotation.

## 📂 Project Structure

```plaintext
com.example.maze/
├── MainActivity.kt          # Main menu with navigation
├── GeneratorActivity.kt     # Maze generation interface
├── ParametrsActivity.kt    # Maze configuration and selection
├── GameActivity.kt          # Motion-based gameplay
├── MazeGenerator.kt         # Maze generation logic
├── SharedPreferencesUtils.kt# Saving/loading mazes
└── res/layout/              # UI XML layouts
```

## Getting Started

### 1. Requirements
- Android Studio
- Kotlin plugin
- Android SDK 21+ (Lollipop)

### 2. Installation
1. Clone the repository:
   ```bash
   git clone ...
   cd android-homework-maze
   ```

2. Open with **Android Studio**:
   - File > Open > Select the project folder.

3. Build and run:
   - Select a physical device or emulator.
   - Click ▶️ to run the app.

## How to Use

### Home Screen
- Tap **Maze Generator** to go to generation screen.
- Tap **Parameters** to select size and maze name.
- Tap **Start Game** to start navigating a maze.

### Generator Screen
- Tap **Generate** to create a maze.
- Enter a name and tap **Save Maze** to store it.
- Tap **Parameters** to adjust settings.

### Parameters Screen
- Adjust maze size (10 to 50) using `SeekBar`.
- Select saved mazes using the dropdown `Spinner`.

### Game Screen
- Use device tilt to move.
- Reach the blue finish tile to win.
- Path is marked with cyan, start with green, goal is blue.

## Configuration

- Maze size range: 10–50 (default: 20)
- Saved in `SharedPreferences` under `"maze_<name>"`
- Uses built-in accelerometer (`Sensor.TYPE_ACCELEROMETER`)

## Constraints

- No external libraries used.
- Only `SharedPreferences` for data storage.

---

> This app is intended for academic demonstration only.
