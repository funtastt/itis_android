package ru.kpfu.itis.android.asadullin.fragments

interface AnswerChangedListener {
    fun onAnswerChanged(questionPosition: Int, answerPosition: Int)
}