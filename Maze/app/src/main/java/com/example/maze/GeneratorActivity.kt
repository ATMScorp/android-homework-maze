package com.example.maze

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GeneratorActivity : AppCompatActivity() {
    private lateinit var mazeImage: ImageView
    private lateinit var mazeNameEditText: EditText
    private lateinit var prefs: SharedPreferences
    private var lastMaze: Array<IntArray>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generator)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mazeImage = findViewById(R.id.image_maze)
        mazeNameEditText = findViewById(R.id.edit_maze_name)
        prefs = getSharedPreferences("MazePrefs", MODE_PRIVATE)

        findViewById<Button>(R.id.btn_generate).setOnClickListener {
            val mazeSize = prefs.getInt("maze_size", 20)
            lastMaze = MazeGenerator.generateMazeArray(mazeSize)
            lastMaze?.let { maze ->
                val screenWidth = resources.displayMetrics.widthPixels
                val screenHeight = resources.displayMetrics.heightPixels
                val cellSize = minOf(
                    (screenWidth * 0.95f / mazeSize).toInt(),
                    (screenHeight * 0.75f / mazeSize).toInt()
                ).coerceAtLeast(4)
                mazeImage.setImageBitmap(MazeGenerator.generateScaledMazeBitmap(maze, cellSize))
            }
        }

        findViewById<Button>(R.id.btn_parameters).setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
        }

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val name = mazeNameEditText.text.toString()
            if (name.isNotEmpty() && lastMaze != null) {
                SharedPreferencesUtils.saveMaze(prefs, name, lastMaze!!)
                prefs.edit().putString("selected_maze", name).apply()
                Toast.makeText(this, "Maze saved as '$name'", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please generate maze and enter a name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
