package com.example.emojimemory

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var board: GridLayout
    private lateinit var status: TextView
    private lateinit var restartButton: Button

    private val cards = mutableListOf(
        "😍", "😍", "😶", "😶", "🎶", "🎶", "😡", "😡", "🥶", "🥶","😒", "😒",
        "😊", "😊", "😂", "😂", "😎", "😎", "❤️", "❤️", "👍", "👍", "😁", "😁"
    )

    private val flippedCards = mutableListOf<Button>()
    private val matchedCards = mutableListOf<Button>()
    private var attempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        board = findViewById(R.id.game_board)
        status = findViewById(R.id.status)
        restartButton = findViewById(R.id.restart_button)

        createBoard()

        restartButton.setOnClickListener {
            attempts = 0
            matchedCards.clear()
            flippedCards.clear()
            status.text = ""
            createBoard()
        }
    }

    private fun shuffle(array: MutableList<String>) {
        array.shuffle()
    }

    private fun createBoard() {
        board.removeAllViews()
        shuffle(cards)
        cards.forEachIndexed { index, cardValue ->
            val cardButton = Button(this).apply {
                text = cardValue
                tag = cardValue
                textSize = 24f // Ajuste o tamanho do texto conforme necessário
                val params = GridLayout.LayoutParams().apply {
                    width = 200
                    height = 200 // Ajuste a altura conforme necessário
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(8, 8, 8, 8) // Margens ao redor das cartas
                }
                layoutParams = params
                setBackgroundResource(R.drawable.button_background)
                setOnClickListener { handleCardClick(this) }
            }
            board.addView(cardButton)
        }
        Handler().postDelayed({ hideCards() }, 3000)
    }


    private fun hideCards() {
        for (i in 0 until board.childCount) {
            val cardButton = board.getChildAt(i) as Button
            cardButton.text = ""
        }
    }

    private fun handleCardClick(card: Button) {
        if (flippedCards.size < 2 && card !in flippedCards) {
            flipCard(card)
            flippedCards.add(card)

            if (flippedCards.size == 2) {
                checkForMatch()
            }
        }
    }

    private fun flipCard(card: Button) {
        card.text = card.tag as String
    }

    private fun checkForMatch() {
        attempts++
        status.text = "Tentativas: $attempts"
        val (card1, card2) = flippedCards
        if (card1.tag == card2.tag) {
            matchedCards.addAll(flippedCards)
            flippedCards.clear()
            if (matchedCards.size == cards.size) {
                status.text = "Parabéns! Você encontrou todas as combinações em $attempts tentativas."
            }
        } else {
             Handler().postDelayed({
                unflipCards(card1, card2)
            }, 1000)
        }
    }

    private fun unflipCards(card1: Button, card2: Button) {
        card1.text = ""
        card2.text = ""
        flippedCards.clear()
    }
}
