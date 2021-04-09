package com.example.pianoman

class Score {
    var score: Int = 0
    var multiplier: Int = 1
    var consecutiveNotes: Int = 0

    fun increaseScore(n: Int) {
        score += n*multiplier
    }

    fun decreaseScore(n: Int) {
        score -= n
    }

    fun increaseMultiplier() {
        consecutiveNotes += 1
        if(consecutiveNotes == 5) {
            multiplier *= 2
            consecutiveNotes = 0
        }
    }

    fun resetMultiplier() {
        consecutiveNotes = 0
        multiplier = 1
    }
}