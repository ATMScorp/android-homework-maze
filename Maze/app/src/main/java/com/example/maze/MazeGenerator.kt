package com.example.maze

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.json.JSONArray
import org.json.JSONObject

object MazeGenerator {
    private var exitXInternal = 0
    private var exitYInternal = 0

    fun getExitX(): Int = exitXInternal
    fun getExitY(): Int = exitYInternal
    fun setExit(x: Int, y: Int) {
        exitXInternal = x
        exitYInternal = y
    }

    fun generateMazeArray(size: Int): Array<IntArray> {
        val maze = Array(size) { IntArray(size) { 1 } }
        val directions = listOf(Pair(0, -1), Pair(1, 0), Pair(0, 1), Pair(-1, 0))

        fun isInBounds(x: Int, y: Int) = x in 1 until size - 1 && y in 1 until size - 1

        fun dfs(x: Int, y: Int) {
            maze[y][x] = 0
            directions.shuffled().forEach { (dx, dy) ->
                val nx = x + dx * 2
                val ny = y + dy * 2
                if (isInBounds(nx, ny) && maze[ny][nx] == 1) {
                    maze[y + dy][x + dx] = 0
                    dfs(nx, ny)
                }
            }
        }

        dfs(1, 1)
        maze[1][0] = 0 // вход

        val openCells = mutableListOf<Pair<Int, Int>>()
        for (y in 1 until size - 1) {
            for (x in 1 until size - 1) {
                if (maze[y][x] == 0 && (x != 1 || y != 1)) openCells.add(Pair(x, y))
            }
        }
        openCells.sortBy { (x, y) ->
            val center = size / 2
            (x - center).let { dx -> dx * dx } + (y - center).let { dy -> dy * dy }
        }
        val (ex, ey) = openCells.first()
        exitXInternal = ex
        exitYInternal = ey

        return maze
    }

    fun generateScaledMazeBitmap(maze: Array<IntArray>, cellSize: Int): Bitmap {
        val size = maze.size
        val mazePixelSize = size * cellSize
        val bitmapSize = mazePixelSize + 100
        val bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { style = Paint.Style.FILL }

        val offsetX = (bitmapSize - mazePixelSize) / 2
        val offsetY = (bitmapSize - mazePixelSize) / 2

        for (y in maze.indices) {
            for (x in maze[y].indices) {
                paint.color = when {
                    y == 1 && x == 0 -> Color.GREEN
                    y == exitYInternal && x == exitXInternal -> Color.BLUE
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
        return bitmap
    }

    fun toJson(maze: Array<IntArray>): String {
        val json = JSONObject()
        val outer = JSONArray()
        for (row in maze) {
            val inner = JSONArray()
            for (cell in row) {
                inner.put(cell)
            }
            outer.put(inner)
        }
        json.put("maze", outer)
        json.put("exitX", exitXInternal)
        json.put("exitY", exitYInternal)
        return json.toString()
    }

    fun fromJson(jsonString: String): Array<IntArray> {
        val obj = JSONObject(jsonString)
        val outer = obj.getJSONArray("maze")
        val maze = Array(outer.length()) { y ->
            val inner = outer.getJSONArray(y)
            IntArray(inner.length()) { x -> inner.getInt(x) }
        }
        exitXInternal = obj.getInt("exitX")
        exitYInternal = obj.getInt("exitY")
        return maze
    }
}
