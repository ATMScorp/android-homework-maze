package com.example.maze

import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var prefs: SharedPreferences
    private lateinit var sensorManager: SensorManager
    private lateinit var maze: Array<IntArray>
    private var posX = 1
    private var posY = 1
    private var path: MutableList<Pair<Int, Int>> = mutableListOf(Pair(1, 1))
    private var cellSize = 0
    private lateinit var view: View
    private var finishX = 0
    private var finishY = 0
    private var offsetX = 0
    private var offsetY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = getSharedPreferences("MazePrefs", MODE_PRIVATE)
        val name = prefs.getString("selected_maze", null)
        if (name == null) {
            Toast.makeText(this, "No maze selected in Parameters.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

//        val json = prefs.getString("maze_$name", null)
//        if (json == null) {
//            Toast.makeText(this, "Maze '$name' not found.", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }

        maze = SharedPreferencesUtils.loadMaze(prefs, name) ?: run {
            Toast.makeText(this, "Maze '$name' not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

//        maze = MazeGenerator.fromJson(json)
        finishX = MazeGenerator.getExitX()
        finishY = MazeGenerator.getExitY()

        savedInstanceState?.let {
            posX = it.getInt("posX", 1)
            posY = it.getInt("posY", 1)
            path = it.getSerializable("path") as? MutableList<Pair<Int, Int>> ?: mutableListOf(Pair(1, 1))
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        cellSize = minOf(
            (screenWidth * 0.95f / maze.size).toInt(),
            (screenHeight * 0.75f / maze.size).toInt()
        ).coerceAtLeast(4)

        val mazePixelSize = maze.size * cellSize
        offsetX = (screenWidth - mazePixelSize) / 2
        offsetY = (screenHeight - mazePixelSize) / 2

        view = object : View(this) {
            private val paint = Paint()

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)

                for (y in maze.indices) {
                    for (x in maze[y].indices) {
                        paint.color = when {
                            y == 1 && x == 0 -> Color.GREEN
                            y == finishY && x == finishX -> Color.BLUE
                            maze[y][x] == 1 -> Color.BLACK
                            else -> Color.WHITE
                        }
                        canvas.drawRect(
                            (x * cellSize + offsetX).toFloat(),
                            (y * cellSize + offsetY).toFloat(),
                            ((x + 1) * cellSize + offsetX).toFloat(),
                            ((y + 1) * cellSize + offsetY).toFloat(),
                            paint
                        )
                    }
                }

                paint.color = Color.CYAN
                path.forEach { (x, y) ->
                    canvas.drawCircle(
                        (x * cellSize + cellSize / 2 + offsetX).toFloat(),
                        (y * cellSize + cellSize / 2 + offsetY).toFloat(),
                        (cellSize / 6).toFloat(),
                        paint
                    )
                }

                paint.color = Color.RED
                canvas.drawCircle(
                    (posX * cellSize + cellSize / 2 + offsetX).toFloat(),
                    (posY * cellSize + cellSize / 2 + offsetY).toFloat(),
                    (cellSize / 3).toFloat(),
                    paint
                )
            }
        }

        setContentView(view)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("posX", posX)
        outState.putInt("posY", posY)
        outState.putSerializable("path", ArrayList(path))
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val dx = -event.values[0].toInt().coerceIn(-1, 1)
        val dy = event.values[1].toInt().coerceIn(-1, 1)
        val newX = posX + dx
        val newY = posY + dy
        if (newX in maze.indices && newY in maze.indices && maze[newY][newX] == 0) {
            posX = newX
            posY = newY
            path.add(Pair(posX, posY))
            view.invalidate()
            if (posX == finishX && posY == finishY) {
                Toast.makeText(this, "🎉 You Win!", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
