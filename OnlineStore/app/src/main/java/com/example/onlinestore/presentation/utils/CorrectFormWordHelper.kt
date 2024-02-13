package com.example.onlinestore.presentation.utils

object CorrectFormWordHelper {

    fun identifyCase(number: Int): Case {
        val remainder10 = number % 10
        val remainder100 = number % 100
        return when {
            remainder100 in 11..19 -> Case.THIRD
            remainder10 == 1 -> Case.FIRST
            remainder10 in 2..4 -> Case.SECOND
            else -> Case.THIRD
        }
    }

    enum class Case {
        FIRST,
        SECOND,
        THIRD,
    }
}