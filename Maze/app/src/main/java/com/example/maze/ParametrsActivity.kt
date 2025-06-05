package com.example.maze

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ParametersActivity : AppCompatActivity() {
    private lateinit var sizeEditText: EditText
    private lateinit var mazeListSpinner: Spinner
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parameters)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sizeEditText = findViewById(R.id.edit_size)
        mazeListSpinner = findViewById(R.id.spinner_mazes)
        prefs = getSharedPreferences("MazePrefs", MODE_PRIVATE)

        val names = SharedPreferencesUtils.getMazeNames(prefs)
        mazeListSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, names)

        sizeEditText.setText(prefs.getInt("maze_size", 20).toString())
        prefs.getString("selected_maze", null)?.let { selected ->
            val index = names.indexOf(selected)
            if (index >= 0) mazeListSpinner.setSelection(index)
        }

        findViewById<Button>(R.id.btn_save_params).setOnClickListener {
            val size = sizeEditText.text.toString().toIntOrNull()
            if (size != null && size in 10..50) {
                prefs.edit().putInt("maze_size", size).apply()
            } else {
                Toast.makeText(this, "Size must be between 10 and 50", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            mazeListSpinner.selectedItem?.toString()?.let { selected ->
                prefs.edit().putString("selected_maze", selected).apply()
                Toast.makeText(this, "Parameters saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}