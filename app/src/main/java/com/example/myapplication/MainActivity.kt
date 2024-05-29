package com.example.myapplication
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var gridLayout: GridLayout
    private val gridSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)

        setupGridLayout()
    }

    private fun setupGridLayout() {
        gridLayout.removeAllViews()
        gridLayout.columnCount = gridSize
        gridLayout.rowCount = gridSize

        var cellSize = resources.getDimensionPixelSize(R.dimen.cell_size)
        var borderSize = resources.getDimensionPixelSize(R.dimen.border_size)

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cell = View(this)
                val params = GridLayout.LayoutParams()
                params.width = cellSize
                params.height = cellSize
                params.rowSpec = GridLayout.spec(row)
                params.columnSpec = GridLayout.spec(col)
                cell.layoutParams = params

                val shape = GradientDrawable()
                shape.shape = GradientDrawable.RECTANGLE
                shape.setColor(Color.WHITE)
                shape.setStroke(borderSize, Color.GRAY)
                cell.background = shape

                gridLayout.addView(cell)
            }
        }

        placeShips()
    }

    private fun placeShips() {
        val ships = listOf(4, 3, 3, 2, 2, 2, 1, 1, 1, 1)
        val shipPositions = mutableListOf<Pair<Int, Int>>()

        var direction: Int

        for (shipSize in ships) {
            var isValidPosition = false
            var position: Pair<Int, Int> = Pair(0, 0)
            direction = Random.nextInt(2)
            while (!isValidPosition) {
                position = Pair(Random.nextInt(gridSize), Random.nextInt(gridSize))
                direction = Random.nextInt(2)

                isValidPosition = isPositionValid(shipPositions, position, shipSize, direction)
            }

            val shipPositionsForThisShip = getShipPositions(position, shipSize, direction)
            shipPositions.addAll(shipPositionsForThisShip)
        }

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cell = gridLayout.getChildAt(row * gridSize + col) as View
                if (shipPositions.contains(Pair(row, col))) {
                    val shape = cell.background as GradientDrawable
                    shape.setColor(Color.BLACK)
                }
            }
        }
    }

    private fun isPositionValid(
        shipPositions: List<Pair<Int, Int>>,
        position: Pair<Int, Int>,
        shipSize: Int,
        direction: Int
    ): Boolean {
        val (row, col) = position
        val endRow = row + if (direction == 0) shipSize - 1 else 0
        val endCol = col + if (direction == 1) shipSize - 1 else 0

        if (endRow >= gridSize || endCol >= gridSize) return false

        for (i in row-1..endRow+1) {
            for (j in col-1..endCol+1) {
                if (i in 0 until gridSize && j in 0 until gridSize) {
                    if (shipPositions.contains(Pair(i, j))) return false
                }
            }
        }

        return true
    }

    fun resetShips(view: View) {
        setupGridLayout()
    }

    private fun getShipPositions(position: Pair<Int, Int>, shipSize: Int, direction: Int): List<Pair<Int, Int>> {
        val (row, col) = position
        val positions = mutableListOf<Pair<Int, Int>>()

        for (i in 0 until shipSize) {
            val newRow = row + if (direction == 0) i else 0
            val newCol = col + if (direction == 1) i else 0
            positions.add(Pair(newRow, newCol))
        }
        return positions
    }

}
