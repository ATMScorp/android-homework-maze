package com.example.maze

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_generator).setOnClickListener {
            startActivity(Intent(this, GeneratorActivity::class.java))
        }
        findViewById<Button>(R.id.btn_game).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }
        findViewById<Button>(R.id.btn_parameters).setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}